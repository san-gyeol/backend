# 산결 백엔드 API 명세서

> 프론트엔드 연동용. 에러 응답의 코드 목록은 [ErrorCodes.md](./ErrorCodes.md) 참고.

## 공통 사항

- Base URL
  - 운영: `https://sangyeol.duckdns.org`
  - 로컬: `http://localhost:8080`
  - ⚠️ 운영 도메인은 임시(DuckDNS)이며 정식 출시 전 팀 도메인으로 교체됩니다.
    프론트 코드에 URL을 하드코딩하지 말고 **환경변수/상수 한 곳으로 분리**해 두세요.
- CORS: 허용 오리진은 Vercel 프로덕션(`sangyeol-fe-web-three.vercel.app`)·브랜치 프리뷰(`sangyeol-fe-web-*.vercel.app`)·`localhost:3000`, 메서드는 GET.
  새 오리진(커스텀 도메인 등)이 필요하면 백엔드에 요청할 것
- 응답 인코딩: `application/json; charset=UTF-8`
- 값 필드는 코드(`zone`, `status`)와 한글 라벨(`zoneName`, `statusName`)을 **동시에 제공**한다.
  화면 표시는 라벨을 그대로 사용하고, 분기 로직(배지 색 등)은 코드로 판단할 것 (문자열 비교 분기 방지)
- 에러 응답은 모든 API가 동일한 형식을 사용한다:

```json
{
  "message": "사용자에게 보여줄 수 있는 문구",
  "errorCode": "CONTROL502_001"
}
```

---

## 1. 실시간 통제정보 조회 ✅ 구현됨

### `GET /api/v1/controls`

무등산·무등산동부 2개 구역의 실시간 통제 상태를 반환한다.
데이터 출처: 국립공원공단 (서버가 공단 페이지에서 수집)

**요청**: 파라미터 없음

**응답 200 OK**

```json
{
  "controls": [
    {
      "zone": "MUDEUNGSAN",
      "zoneName": "무등산",
      "status": "PARTIAL",
      "statusName": "부분통제",
      "referenceTime": "2026-08-16T12:00:00",
      "source": "국립공원공단"
    },
    {
      "zone": "MUDEUNGSAN_EAST",
      "zoneName": "무등산동부",
      "status": "PARTIAL",
      "statusName": "부분통제",
      "referenceTime": "2026-08-16T12:32:00",
      "source": "국립공원공단"
    }
  ]
}
```

| 필드 | 타입 | 설명 |
|---|---|---|
| `zone` | string | 구역 코드: `MUDEUNGSAN` \| `MUDEUNGSAN_EAST` |
| `zoneName` | string | 구역 한글명 — 그대로 표시 |
| `status` | string | 상태 코드: `NORMAL` \| `PARTIAL` \| `FULL` — 배지 색 분기용 |
| `statusName` | string | 상태 한글명("정상"/"부분통제"/"전면통제") — 배지 문구로 그대로 표시 |
| `referenceTime` | string | 공단이 밝힌 기준 시각 (ISO-8601, KST) |
| `source` | string | 출처 표기("국립공원공단") |

- `controls` 배열은 항상 2건, 순서는 무등산 → 무등산동부 고정

**에러**

| 상황 | HTTP | errorCode |
|---|---|---|
| 공단 데이터 수집 실패 (서버 장애·페이지 변경 등) | 502 | `CONTROL502_001` |

→ 에러 카드("통제정보를 불러오지 못했어요") + "다시 시도" 버튼 표시. 다시 시도 = 본 API 재호출

---

## 2. 무등산 날씨 조회 ✅ 구현됨

### `GET /api/v1/weather`

홈 화면 날씨 카드용. 데이터 출처: OpenWeather (서버가 무등산 주소지 좌표로 실황과 3시간 예보를 조회해 조합)

**요청**: 파라미터 없음

**응답 200 OK**

```json
{
  "temperature": 27.78,
  "feelsLike": 28.05,
  "precipitationProbability": 20,
  "precipitationAmount": null,
  "humidity": 48,
  "sunrise": "05:49",
  "sunset": "19:24",
  "source": "OpenWeather",
  "updatedAt": "2026-08-13T15:50:18"
}
```

| 필드 | 타입 | 설명 |
|---|---|---|
| `temperature` | number | 현재 기온(°C). 소수 둘째 자리까지 올 수 있으므로 반올림은 화면에서 |
| `feelsLike` | number | 체감온도(°C) |
| `precipitationProbability` | integer | 대표 강수확률(0~100, %). 기준 시각과 같은 날짜의 남은 3시간 예보 구간 중 최댓값 |
| `precipitationAmount` | number \| null | 최근 1시간 강수량(mm). `null`이면 강수 없음(0mm와 구분) |
| `humidity` | integer | 습도(%) |
| `sunrise` | string | 일출 시각 `HH:mm` (KST) |
| `sunset` | string | 일몰 시각 `HH:mm` (KST) |
| `source` | string | 출처 표기("OpenWeather"). 화면에 그대로 표시 (OpenWeather attribution 요건) |
| `updatedAt` | string | OpenWeather가 자료를 산출한 시각 (ISO-8601, KST) |

- 숫자 필드에 단위 문자열 없음. °C, %, mm 표기는 화면에서 붙일 것
- `precipitationAmount`가 `null`이면 화면 "-" 표시
- 코스별 날씨는 추후 `GET /api/v1/courses/{courseId}/weather` (같은 응답 형태) 예정

**에러**

| 상황 | HTTP | errorCode |
|---|---|---|
| OpenWeather 호출 실패 (키 오류, 쿼터 초과, 타임아웃) 또는 응답 구조 변경 | 502 | `WEATHER502_001` |

→ 에러 카드("날씨 정보를 불러오지 못했어요") + "다시 시도" 버튼 표시. 다시 시도 = 본 API 재호출

---

## 3. 코스 날씨 조회 ✅ 구현됨

### `GET /api/v1/courses/{courseId}/weather`

지도 화면 바텀시트의 날씨 카드용. 기획서대로 해당 코스 거점 중 가장 높은 고도의 위치를 기준으로 조회한다
(2026-08-23 디스코드에서 프론트 박건규와 합의). 데이터 출처와 응답 형태는 홈 날씨(2절)와 완전히 같고 기준 좌표만 다르다.

**요청**: 경로 변수 `courseId` (아래 표의 정수 id). 프론트 정적 JSON의 코스 항목에 같은 `id`를 넣어 두면 연결이 끝난다.

| courseId | 코스 | 기준 거점 (최고 고도) | 기준 좌표 |
|---|---|---|---|
| 1 | 당산나무 코스 | 중머리재 590m | 35.119077, 126.984481 |
| 2 | 새인봉-입석대 코스 | 입석대 965m | 35.117551, 127.002573 |
| 3 | 늦재-옛길 코스 | 장불재 894m | 35.116271, 126.998612 |
| 4 | 시무지기폭포 코스 | 장불재 894m | 35.116271, 126.998612 |

**응답 200 OK**: 2절 홈 날씨와 같은 JSON (`temperature`, `feelsLike`, `precipitationProbability`, `precipitationAmount`,
`humidity`, `sunrise`, `sunset`, `source`, `updatedAt`). 바텀시트 카드는 `temperature`와 `feelsLike`만 쓴다.

**에러**

| 상황 | HTTP | errorCode |
|---|---|---|
| 없는 courseId (예: 9) | 404 | `COURSE404_001` |
| 숫자가 아닌 courseId (예: abc) | 400 | `COMMON400_001` |
| OpenWeather 호출 실패 또는 응답 구조 변경 | 502 | `WEATHER502_001` |

- OpenWeather는 관측소 기반이라 거점 간 거리가 2~3km인 무등산 안에서는 홈 날씨와 코스 날씨 값이 같게 나오는 경우가 많다.
  산악 관측 데이터가 필요해지면 기상청 조합으로 어댑터를 교체한다 (7절 소싱 맵).

---

## 4. 화면 매핑 (산결 리디자인 기준, 2026-09-20 점검)

### 홈 화면, 무등산 날씨 카드 → `GET /api/v1/weather`

| 화면 요소 | 응답 필드 | 화면 처리 |
|---|---|---|
| "22.1°C" | `temperature` | 소수 첫째 자리로 반올림 (27.78 → 27.8) |
| "체감 (25.4°C)" | `feelsLike` | 같은 반올림 |
| 강수확률 "20%" | `precipitationProbability` | 정수 그대로 + "%" |
| 강수량(mm) "-" | `precipitationAmount` | `null`이면 "-", 값이 있으면 소수 첫째 자리 + "mm" |
| 습도 "100%" | `humidity` | 정수 그대로 + "%" |
| 일출 05:30 / 일몰 19:46 | `sunrise`, `sunset` | 문자열 그대로 |
| 우측 상단 출처 라벨 | `source` | Pencil 시안은 성공 상태 "OpenWeatherMap", 로딩과 실패 상태 "기상청"으로 엇갈림. 성공 상태는 `source`("OpenWeather")를 그대로 그리고, 응답이 없는 로딩과 실패 상태는 같은 문구로 고정할 것. "실시간" 자리는 `updatedAt`의 시각(HH:mm 기준)으로 대체 가능 |
| 카드 좌측 해 아이콘 | 해당 필드 없음 | 고정 아이콘이면 그대로. 날씨 상태에 따라 바꾸려면 백엔드에 `condition` 필드 추가 필요 (아래 5절) |
| 로딩 상태 (스켈레톤) | 없음 | 프론트 처리. 시안 프레임 "홈 - 정보 로드 중" |
| 실패 상태 (에러 카드 + 다시 시도) | 에러 응답 `message` | 시안 프레임 "홈 - 정보 로드 실패"의 두 줄 문구가 `message`와 글자까지 같음. 마침표 기준으로 두 줄로 나눠 그리면 됨. 다시 시도는 실패한 API만 재호출 |

### 홈 화면, 실시간 통제정보 카드 → `GET /api/v1/controls`

| 화면 요소 | 응답 필드 | 화면 처리 |
|---|---|---|
| 행 이름 "무등산", "무등산동부" | `zoneName` | 문자열 그대로. 배열 순서가 화면 순서 |
| 배지 문구 "부분통제" | `statusName` | 문자열 그대로 |
| 배지 색 | `status` | 디자인 시스템 "상태와 피드백"의 3단계(정상 초록, 주의 노랑, 통제 빨강)를 `NORMAL`, `PARTIAL`, `FULL`에 대응시키는 것이 자연스러움. 홈 시안은 부분통제를 빨강으로 그려 디자인 확인 필요 |
| 우측 상단 "국립공원공단 · 실시간" | `source` | 문자열 그대로. 필요하면 `referenceTime`을 기준 시각으로 표시 |

### 지도 화면, 코스 바텀시트 날씨 카드 → `GET /api/v1/courses/{courseId}/weather`

| 화면 요소 | 응답 필드 | 화면 처리 |
|---|---|---|
| "22.1°C" | `temperature` | 소수 첫째 자리로 반올림 |
| "체감 25.4°C" | `feelsLike` | 같은 반올림 |
| 해 아이콘 | 해당 필드 없음 | 홈 카드와 같은 확인 사항 (아래 5절) |

### 백엔드가 관여하지 않는 화면

코스 목록, 지도의 거점과 시설 칩, 코스 상세(코스 여정, 코스 TIP), 준비물과 식당 카드, 더보기 화면은 프론트 정적 데이터로 처리한다.

---

## 5. 확인이 필요한 것

1. **날씨 상태 아이콘**: 해/구름/비 아이콘을 상태에 따라 바꾸는 기획이면 `condition`(예: CLEAR, CLOUDS, RAIN, SNOW) 필드를 추가한다. 고정 아이콘이면 불필요.
2. **홈 날씨 출처 표기**: 시안의 "기상청"은 "OpenWeather"로 바뀌어야 한다 (OpenWeather 이용 약관의 출처 표기 요건).
3. **통제 배지 색**: 디자인 시스템의 3단계(정상 초록, 주의 노랑, 통제 빨강)와 홈 시안(부분통제 빨강)이 다르다. `FULL` 색 포함 디자인 확정 필요.
