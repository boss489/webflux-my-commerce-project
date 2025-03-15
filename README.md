# WebFlux 기반 마이크로서비스 커머스 프로젝트

이 프로젝트는 Spring WebFlux를 활용한 반응형 마이크로서비스 아키텍처로 구현된 커머스 시스템입니다. 비동기-논블로킹 방식으로 설계되어 높은 확장성과 성능을 제공합니다.

## 프로젝트 구조

프로젝트는 다음과 같은 마이크로서비스로 구성되어 있습니다:

### Customer Service
고객 정보 관리 및 거래 처리를 담당하는 서비스입니다.
- 고객 정보 조회 API 제공
- 주식 거래 처리 API 제공
- R2DBC를 활용한 반응형 데이터 액세스

### Aggregator Service
여러 서비스의 데이터를 집계하고 클라이언트에게 제공하는 서비스입니다.
- 고객 포트폴리오 정보 집계 및 제공
- 주식 거래 요청 처리 및 검증
- 주식 가격 스트림 제공 (Server-Sent Events)

## 기술 스택

- **Java 21**: 최신 Java 버전 활용
- **Spring Boot 3.4.x**: 최신 Spring Boot 프레임워크
- **Spring WebFlux**: 반응형 웹 애플리케이션 개발
- **Project Reactor**: 반응형 프로그래밍 라이브러리
- **R2DBC**: 반응형 관계형 데이터베이스 연결
- **H2 Database**: 개발 및 테스트용 인메모리 데이터베이스
- **Lombok**: 반복 코드 감소
- **JUnit 5**: 테스트 프레임워크
- **MockServer**: 테스트를 위한 HTTP 서버 모킹

## 주요 기능

### 고객 정보 관리
- 고객 정보 조회 API
- 반응형 데이터 액세스 패턴 적용

### 주식 거래 처리
- 비동기 주식 거래 요청 처리
- 거래 요청 검증
- 거래 결과 응답

### 실시간 주식 가격 스트림
- Server-Sent Events를 활용한 실시간 주식 가격 정보 제공
- 반응형 스트림 처리

## 실행 방법

### 사전 요구사항
- Java 21 이상
- Maven 3.8 이상

### 빌드 및 실행

각 서비스 디렉토리에서 다음 명령어를 실행합니다:

```bash
# 빌드
mvn clean package

# 실행
mvn spring-boot:run
```

또는 JAR 파일을 직접 실행할 수 있습니다:

```bash
java -jar target/customer-service-0.0.1-SNAPSHOT.jar
java -jar target/aggregator-service-0.0.1-SNAPSHOT.jar
```

## API 문서

### Customer Service API

- `GET /customers/{customerId}`: 고객 정보 조회
- `POST /customers/{customerId}/trade`: 주식 거래 요청 처리

### Aggregator Service API

- `GET /customers/{customerId}`: 고객 포트폴리오 정보 조회
- `POST /customers/{customerId}/trade`: 주식 거래 요청 처리
- `GET /stock/price-stream`: 실시간 주식 가격 스트림 제공 (SSE)

## 아키텍처

이 프로젝트는 마이크로서비스 아키텍처를 기반으로 설계되었으며, 각 서비스는 독립적으로 배포 및 확장 가능합니다. 서비스 간 통신은 WebClient를 통한 비동기 HTTP 통신으로 이루어집니다.

## 개발 및 테스트

- JUnit 5와 WebTestClient를 활용한 통합 테스트
- MockServer를 활용한 외부 서비스 모킹
- Reactor Test를 활용한 반응형 코드 테스트

## 라이센스

이 프로젝트는 MIT 라이센스 하에 배포됩니다. 