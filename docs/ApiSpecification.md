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

## 2. 무등산 날씨 조회 🚧 예정 (계약 초안)

### `GET /api/v1/weather`

홈 화면 날씨 카드용. 데이터 출처: OpenWeatherMap (기준 지점: 무등산 주소지 좌표)

**응답 200 OK (초안 — 구현 시 확정)**

```json
{
  "temperature": 22.1,
  "feelsLike": 25.4,
  "precipitationProbability": 20,
  "precipitationAmount": null,
  "humidity": 100,
  "sunrise": "05:30",
  "sunset": "19:46",
  "source": "OpenWeather",
  "updatedAt": "2026-08-25T09:40:00"
}
```

- 숫자 필드에 단위 문자열 없음 — °C, %, mm 표기는 화면에서
- `precipitationAmount`가 `null`이면 강수 없음 → 화면 "-" 표시
- 코스별 날씨는 추후 `GET /api/v1/courses/{courseId}/weather` (같은 응답 형태) 예정
