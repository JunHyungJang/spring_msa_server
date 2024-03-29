# MSA 기반 ecommerce server 구축 

## 목표
- MSA 방식으로 작동하는 ecommerce server 구축
- 대용량 Traffic Handling에 용이한 방법으로 서버 개선
- Grafana, Prometheus를 사용한 성능 모니터링
- Docker를 활용한 배포

## 주요기능
- JWT + 대칭키 기반의 로그인, 회원가입 기능 구현
- Order-service, User-service, Catalogservice 기능 구현 및 MySQL + H2 를 사용한 DB적용
- Discovery server(Eureka), APIgateway 구현
- Global, Custom filtering 적용한 Logging 기능 활성화 
- RestTemplate, FeignClient를 활용한 Microservice 간의 호출
- Circuit Breaker를 활용하여 Microservice간의 에러 핸들링 
- Kafka, RabbitMQ를 사용하여 분산서비스의 DB 동기화 및 Configuration 파일 실시간 호출
- Docker를 사용하여 배포 용이하게 만들기

## 사용기술
- 백엔드(Spring boot, Spring Cloud)
- DB(Mariadb,Mysql,H2)
- 메세징큐(Kafka, RabbitMQ)
- 배포(Docker)
- 모니터링(Prometheus, Grafana)

## 아키텍처
![image](https://github.com/JunHyungJang/spring_msa_server/assets/89409079/47b0d788-cc3c-40e7-b1fb-0c005fc0f2fe)

## DB 스키마

## 배포

|service|IP-address|port|
|------|---|---|
|rabbitMQ|172.18.0.11|5671|
|config-service|172.18.0.7|8888|
|discovery-service|127.18.0.6|8761|
|apigateway-service|172.18.0.5|8000|
|mariadb|172.18.0.10|3305|
|kafka-docker zookeeper|172.18.0.100|2181|
|kafka-docker kafka|172.18.0.101|9092|
|prometheus|172.18.0.8|9090|
|grafana|172.18.0.9|3000|
|user-service|172.18.0.11|Random|
|order-service|172.18.0.12|Random|
|catalog-service|172.18.0.12|Random|
