# 🔧 Smart WMS Backend (Spring Boot)

다중 AMR 창고 운영을 위한 **WMS API + 실시간 관제(WebSocket/STOMP)** 백엔드.

---

## 🧰 TECH STACK
- Spring Boot · Spring Security(세션 기반) 
- MySQL(AWS RDS) 
- EST over HTTPS · Spring WebSocket(STOMP) 
- 배포: Docker on AWS EC2 · Nginx 리버스 프록시 · Certbot(SSL/TLS)

---

## ⚡ 성능 개선
- **DB 인덱스 최적화**: 자주 조회되는 컬럼에 단일/복합 인덱스 도입  
  예) `inout_orders(type, status, expected_date)`, `order_item(order_id)`, `inventory(item_id, location_code)`
- **N+1 제거**: JPA `fetch join`/`@EntityGraph` 적용, 목록은 **DTO Projection + 페이징**으로 전환
- **쿼리 슬림화**: 불필요 조인 제거, 조건/집계는 DB로 위임, 대량 작업은 **batch insert/update**
- **HikariCP 튜닝**: `maximumPoolSize`/`idleTimeout`/`maxLifetime` 조정으로 스파이크 대응
- **트랜잭션 최소화**: 읽기 기본 `@Transactional(readOnly = true)`, 쓰기 작업은 서비스 단위로 짧게
- **응답 경량화**: 목록 응답 **경량 DTO** 구성, 기본 페이지네이션 적용

---

## 🧱 도메인
Users · Companies · Items · InOutOrders · OrderItem · Inventory · Notifications · Schedules  
입·출고(`InOutOrders`)—항목(`OrderItem`)—재고(`Inventory`)로 연결, 회사/품목은 다대일 참조, 상태/유형은 **ENUM** 관리

---

## 🔌 아키텍처
<img width="602" height="392" alt="Smart_WMS 복사본 3" src="https://github.com/user-attachments/assets/7c4bcf73-e1a2-4074-83b6-acce5178a84a" />

FE(Next.js) ↔ **BE(Spring Boot)** ↔ **DB(RDS)** 3계층 · **WebSocket(STOMP)** 실시간 스트림 ·  
**Nginx(SSL/TLS)** 리버스 프록시 · **Docker on EC2** 배포

---

## 📁 폴더 구조
<pre>
src/main/java/com/example/smart_wms_be
|-- config/              # Security, CORS, WebSocket, Hikari 등 공통 설정
|   |-- SecurityConfig.java
|   |-- WebSocketConfig.java
|   \-- WebConfig.java
|-- common/
|   |-- error/           # GlobalExceptionHandler, ErrorCode
|   \-- util/            # 공통 유틸(로깅/AOP 등)
|-- domain/              # JPA 엔티티
|-- dto/                 # 요청/응답 DTO (리스트용 경량 DTO)
|-- repository/          # Spring Data JPA
|-- service/             # 비즈니스 로직 (@Transactional)
|-- controller/          # REST API
\-- websocket/           # STOMP 핸들러/채널
</pre>

