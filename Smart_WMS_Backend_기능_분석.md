# Smart WMS Backend 기능 분석 보고서

## 🏗 프로젝트 개요

**Smart WMS Backend**는 창고 관리 시스템(Warehouse Management System)의 백엔드 서비스로, Spring Boot 3.5.3을 기반으로 구축되었습니다.

### 기술 스택
- **프레임워크**: Spring Boot 3.5.3, Java 17
- **데이터베이스**: MySQL 8.x (AWS RDS)
- **보안**: Spring Security (세션 기반 인증)
- **ORM**: Spring Data JPA + Hibernate
- **문서화**: Swagger/OpenAPI 3.0
- **통신**: RESTful API, WebSocket (STOMP)
- **빌드 도구**: Gradle
- **배포**: Docker, Render.yaml

## 📁 프로젝트 구조

```
src/main/java/com/example/smart_wms_be/
├── SmartWmsBeApplication.java          # 메인 애플리케이션 클래스
├── config/                             # 설정 클래스들
│   ├── SecurityConfig.java            # Spring Security 설정
│   ├── WebConfig.java                 # CORS, Web MVC 설정
│   ├── SwaggerConfig.java             # API 문서화 설정
│   └── DbConnectionTest.java          # DB 연결 테스트
├── controller/                         # REST API 컨트롤러
├── domain/                            # JPA 엔티티 도메인 모델
├── dto/                               # 데이터 전송 객체
├── repository/                        # 데이터 액세스 레이어
└── service/                           # 비즈니스 로직 레이어
```

## 🚀 핵심 기능 모듈

### 1. 사용자 관리 (User Management)
**관련 클래스**: `UserController`, `UserService`, `UserRepository`, `User`, `AuthController`, `AuthService`

#### 주요 기능:
- **사용자 CRUD**: 사용자 생성, 조회, 수정, 삭제
- **인증/인가**: 세션 기반 로그인/로그아웃
- **역할 관리**: ADMIN, USER 역할 기반 접근 제어
- **비밀번호 보안**: BCrypt 암호화

#### API 엔드포인트:
```
POST /api/auth/login          # 로그인
POST /api/auth/logout         # 로그아웃
GET  /api/auth/me            # 현재 사용자 정보 조회
GET  /api/users              # 사용자 목록 조회
POST /api/users              # 사용자 생성
PUT  /api/users/{userId}     # 사용자 정보 수정
DELETE /api/users/{userId}   # 사용자 삭제
```

### 2. 거래처 관리 (Company Management)
**관련 클래스**: `CompanyController`, `CompanyService`, `CompanyRepository`, `Company`

#### 주요 기능:
- **거래처 CRUD**: 고객사 및 공급업체 관리
- **타입별 분류**: CLIENT, SUPPLIER 등 다중 타입 지원
- **연락처 관리**: 담당자 정보, 이메일, 전화번호

#### API 엔드포인트:
```
GET  /api/companies?type={type}  # 거래처 목록 조회 (타입 필터링)
POST /api/companies              # 거래처 등록
PUT  /api/companies/{id}         # 거래처 정보 수정
DELETE /api/companies/{id}       # 거래처 삭제
```

### 3. 품목 관리 (Item Management)
**관련 클래스**: `ItemController`, `ItemService`, `ItemRepository`, `Item`

#### 주요 기능:
- **품목 마스터**: 품목코드(SKU), 품목명, 규격, 단위 관리
- **가격 정보**: 입고 단가, 출고 단가
- **분류 체계**: 품목 그룹별 분류

#### API 엔드포인트:
```
GET  /api/items              # 전체 품목 목록 조회
POST /api/items              # 품목 등록
PUT  /api/items/{id}         # 품목 정보 수정
DELETE /api/items/{id}       # 품목 삭제
```

### 4. 입출고 주문 관리 (In/Out Order Management)
**관련 클래스**: `InOutOrderController`, `InOutOrderService`, `InOutOrderRepository`, `InOutOrder`, `OrderItem`

#### 주요 기능:
- **주문 생성**: 입고(INBOUND), 출고(OUTBOUND) 주문 생성
- **상태 관리**: PENDING → PROCESSING → COMPLETED
- **자동 재고 연동**: 주문 완료 시 재고 자동 증감
- **거래 내역 기록**: 재고 변동 시 트랜잭션 자동 생성

#### API 엔드포인트:
```
GET  /api/inout/orders?type={type}&status={status}  # 주문 목록 조회
POST /api/inout/orders                              # 입출고 주문 생성
GET  /api/inout/orders/{orderId}                   # 주문 상세 조회
PUT  /api/inout/orders/{orderId}/status            # 주문 상태 업데이트
```

#### 복잡한 비즈니스 로직:
- **트랜잭션 관리**: 주문-재고-거래내역의 원자적 처리
- **N+1 문제 해결**: Repository에서 FETCH JOIN 최적화

### 5. 재고 관리 (Inventory Management)
**관련 클래스**: `InventoryController`, `InventoryService`, `InventoryRepository`, `Inventory`, `InventoryTransaction`

#### 주요 기능:
- **실시간 재고 조회**: 품목별, 위치별 재고 현황
- **재고 거래 내역**: 입고, 출고 거래 이력 추적
- **위치 관리**: 창고 내 위치 코드별 재고 관리

#### API 엔드포인트:
```
GET /api/inventory?itemCode={code}&locationCode={location}  # 재고 조회
GET /api/inventory/history                                  # 재고 이력 조회
```

### 6. AMR 관리 (Autonomous Mobile Robot Management)
**관련 클래스**: `AmrController`, `AmrService`, `AmrRepository`, `Amr`

#### 주요 기능:
- **AMR 상태 모니터링**: IDLE, MOVING, CHARGING, ERROR
- **배터리 레벨 관리**: 실시간 배터리 상태 확인
- **위치 추적**: 현재 위치 정보 관리

#### API 엔드포인트:
```
GET /api/amrs    # 전체 AMR 상태 조회
```

### 7. 대시보드 (Dashboard)
**관련 클래스**: `DashboardController`, `DashboardService`

#### 주요 기능:
- **요약 정보**: 재고, 주문, AMR 상태 요약
- **성능 최적화**: CompletableFuture를 활용한 병렬 데이터 로딩
- **비즈니스 지표**: 주문 완료율, 재고 회전율, AMR 활용률

#### API 엔드포인트:
```
GET /api/dashboard/summary    # 대시보드 요약 정보
GET /api/dashboard/all        # 통합 대시보드 데이터 (성능 최적화)
```

#### 성능 특화 기능:
- **병렬 처리**: 여러 데이터 소스를 동시에 조회
- **실행 시간 모니터링**: 성능 측정 및 로깅

### 8. 스케줄 관리 (Schedule Management)
**관련 클래스**: `ScheduleController`, `ScheduleService`, `ScheduleRepository`, `Schedule`

#### 주요 기능:
- **작업 스케줄**: 입고, 출고, 재고조사, 회의 등 일정 관리
- **달력 기능**: 날짜 범위별 스케줄 조회
- **스케줄 타입**: INBOUND, OUTBOUND, INVENTORY_CHECK, MEETING, ETC

#### API 엔드포인트:
```
GET  /api/schedules?start_date={date}&end_date={date}  # 기간별 스케줄 조회
POST /api/schedules                                    # 스케줄 생성
DELETE /api/schedules/{id}                            # 스케줄 삭제
```

### 9. 알림 시스템 (Notification System)
**관련 클래스**: `NotificationController`, `NotificationService`, `NotificationRepository`, `Notification`

#### 주요 기능:
- **알림 관리**: 시스템 알림 생성 및 조회
- **읽음 처리**: 알림 읽음 상태 관리

#### API 엔드포인트:
```
GET  /api/notifications        # 모든 알림 조회
POST /api/notifications/{id}/read  # 알림 읽음 처리
```

## 🔧 설정 및 인프라

### 보안 설정 (SecurityConfig)
- **인증 방식**: 세션 기반 (JWT 미사용)
- **접근 제어**: 현재 모든 API 허용 (.anyRequest().permitAll())
- **CSRF**: 비활성화 (REST API 특성)

### CORS 설정 (WebConfig)
- **허용 오리진**: 
  - http://localhost:3000 (웹 프론트엔드)
  - http://localhost:8081, https://localhost:8081 (Expo 앱)
- **세션 쿠키**: allowCredentials(true) 설정

### 데이터베이스 최적화
- **HikariCP 커넥션 풀**: 최대 20개, 최소 5개 연결
- **JPA 배치 처리**: batch_size=20, 순서 보장
- **N+1 문제 해결**: Repository에서 FETCH JOIN 적극 활용

### API 문서화 (SwaggerConfig)
- **Swagger UI**: /swagger-ui/index.html
- **OpenAPI 3.0**: API 명세서 자동 생성

## 🎯 아키텍처 특징

### 레이어드 아키텍처
1. **Controller Layer**: REST API 엔드포인트 제공
2. **Service Layer**: 비즈니스 로직 처리
3. **Repository Layer**: 데이터 액세스 담당
4. **Domain Layer**: JPA 엔티티 및 도메인 로직

### 설계 패턴
- **DTO 패턴**: Request/Response 객체 분리
- **Repository 패턴**: Spring Data JPA 활용
- **Service 패턴**: 트랜잭션 및 비즈니스 로직 캡슐화

### 성능 최적화 전략
- **병렬 처리**: CompletableFuture 활용
- **FETCH JOIN**: N+1 문제 해결
- **배치 처리**: JPA 배치 설정 최적화
- **커넥션 풀**: HikariCP 튜닝

## 📊 데이터베이스 스키마

### 핵심 테이블
- **users**: 사용자 정보 및 인증
- **companies**: 거래처 정보
- **items**: 품목 마스터
- **in_out_orders**: 입출고 주문
- **order_items**: 주문 상세 항목
- **inventory**: 재고 현황
- **inventory_transactions**: 재고 거래 내역
- **amrs**: AMR 로봇 정보
- **schedules**: 작업 스케줄
- **notifications**: 시스템 알림

### 주요 관계
- Company ← InOutOrder (다대일)
- InOutOrder → OrderItem (일대다)
- Item ← OrderItem (다대일)
- Item ← Inventory (다대일)
- Item ← InventoryTransaction (다대일)

## 🔍 특별한 기능들

### 1. 실시간 재고 연동
입출고 주문 완료 시 자동으로 재고를 증감하고 거래 내역을 생성하는 복잡한 트랜잭션 로직

### 2. 성능 모니터링 대시보드
CompletableFuture를 활용한 병렬 데이터 로딩으로 대시보드 응답 속도 최적화

### 3. 유연한 필터링 시스템
대부분의 조회 API에서 선택적 파라미터를 통한 유연한 데이터 필터링

### 4. 감사(Auditing) 기능
생성일시, 수정일시 자동 관리 및 사용자 활동 추적

## 🚀 확장 가능성

### 현재 구현된 확장 포인트
- **다중 회사 타입**: CLIENT, SUPPLIER 등 확장 가능
- **스케줄 타입**: 새로운 작업 유형 추가 용이
- **AMR 상태**: 새로운 로봇 상태 추가 가능
- **알림 시스템**: 다양한 알림 타입 확장 가능

### 향후 확장 가능 기능
- 실시간 WebSocket 알림
- 고급 재고 분석 및 예측
- AMR 경로 최적화
- 바코드/RFID 연동
---

이 Smart WMS Backend는 창고 관리의 핵심 기능들을 체계적으로 구현한 견고한 시스템으로
- 다국어 지원
- 보고서 생성 기능
, 확장성과 성능을 고려한 현대적인 아키텍처를 채택하고 있습니다.