# 설계 질문 모음

> 리뷰어/크루에게 질문하기 위한 정리. 각 질문은 배경 → 질문 → 현재 잠정 선택 → 남은 의문 순.

## Q1. 도메인 객체의 필수 필드 강제, requireNonNull이 자연스러운 선택인가?

### 배경

값 객체(CourseName, Distance 등)는 각자 생성자에서 자기 내용을 검증한다.
그런데 이 값 객체들을 조립하는 Course는 "필드가 존재하는지(참조가 null이 아닌지)"를 따로 확인해야 한다
— null은 "내용이 빈 객체"가 아니라 "객체 없음"이라서, 하위 객체의 검증이 실행될 기회조차 없기 때문.

```java
public Course(CourseName name, Distance distance, Duration duration, Difficulty difficulty) {
    this.name = Objects.requireNonNull(name, "Course 생성에 CourseName이 필요합니다");
    this.distance = Objects.requireNonNull(distance, "Course 생성에 Distance가 필요합니다");
    // ...
}
```

### 질문

객체의 생성 시점, 즉 생성자가 생성과 검증을 책임진다는 원칙은 이해했다.
**"해당 필드가 비어 있지 않다"를 코드 레벨에서 강제하는 수단으로 `Objects.requireNonNull`을 쓰는 것이
자연스러운(관례적인) 선택인가?**

### 현재 잠정 선택과 근거

- `requireNonNull` 채택. 근거: ① 대입과 검증이 한 표현식이라 검증 누락이 구조적으로 불가능
  ② JDK 표준 관용구 ③ null 인자에는 NPE가 관례(Effective Java 아이템 72)
- 대안으로 검토한 것: private validate 메서드 + IllegalArgumentException
  (검증이 한 곳에 모이고 예외 타입을 고를 수 있음. 필드 간 교차 검증이 생기면 이쪽으로 전환 예정)

### 남은 의문

- 값 객체(CourseName)는 IAE를 던지는데 조립 객체(Course)만 NPE를 던져서 도메인 검증 예외가
  두 종류로 갈라진다 — IAE로 통일하는 것이 낫지 않은가? (팀/리뷰어의 관례가 궁금함)

## Q2. 어댑터가 만들어주는 도메인 record에도 requireNonNull이 필요한가?

### 배경

외부 API 응답은 「외부 DTO(전부 래퍼 타입, 부재 허용) → 어댑터(검증·번역) → 도메인 record」로 흘러온다.
어댑터(파서)가 이미 null·미지 값을 걸러내는데, 최종 산출물인 도메인 record가 또 검증한다:

```java
public record ControlSnapshot(ControlZone zone, ControlStatus status,
                              LocalDateTime referenceTime, String source) {
    public ControlSnapshot {
        Objects.requireNonNull(zone, "ControlSnapshot 생성에 ControlZone이 필요합니다");
        // ...
    }
}
```

참고: 이전 프로젝트의 같은 역할 객체(PaymentResult — PG사 응답을 어댑터가 번역한 결과물)는
검증 없이 순수 record였다.

### 질문

**생산자(어댑터)가 단일하고 이미 검증을 수행하는 상황에서, 그 산출물인 도메인 record의
생성자 검증(requireNonNull)은 중복 아닌가? 과연 필요한가?**

### 현재 잠정 선택과 근거

- **검증 생략하고 진행 (2026-08-25, 질문 답변 후 재결정).** 어댑터가 단일 생산자로서 이미 검증하므로
  당장의 중복을 피하고, 아래 "검증 유지" 논거의 타당성은 리뷰어 답변을 듣고 판단하기로 함.
- 검증 유지 측 논거(보류 중): ① 생성자는 public이라 어댑터 외의 호출자(서비스, 테스트, 미래 코드)를
  막을 수 없고, record는 자기를 누가 만들었는지 모른다 ② "도메인 객체가 존재한다 = 완전하다"는 보장은
  생성자 검증이 있을 때만 참 ③ 검증을 빼면 스냅샷을 소비하는 모든 곳이 null 체크를 반복하게 됨

### 남은 의문

- "어댑터 산출물 record는 검증 생략"을 팀 규칙으로 삼는 스타일도 실무에 있는가?
  있다면 그 스타일에서는 도메인의 무결성을 무엇으로 보장하는가?
- 계층마다 검증이 반복되는 것(외부 DTO 검증 → 어댑터 검증 → 도메인 검증)은 과잉인가,
  아니면 각 층이 서로 다른 불변식을 지키는 정상 구조인가?
