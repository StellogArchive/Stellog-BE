# --- 1단계: 빌드(Build) 환경 ---
# 빌드를 위해 JDK 버전 사용
FROM amazoncorretto:17 AS builder

# 작업 디렉토리 설정
WORKDIR /workspace

# Gradle 래퍼와 소스 코드 복사
COPY gradlew .
COPY build.gradle .
COPY settings.gradle .
COPY src ./src

# Gradle로 프로젝트 빌드
RUN ./gradlew build -x test

# --- 2단계: 실행(Runtime) 환경 ---
# 실행을 위해 더 가벼운 JRE 버전 사용
FROM amazoncorretto:17-alpine

# 작업 디렉토리 생성
WORKDIR /app

# 빌드 단계(builder)에서 생성된 JAR 파일만 복사
COPY --from=builder /workspace/build/libs/*.jar app.jar

# 포트 노출
EXPOSE 8080

# 시간대 설정 (Alpine Linux 방식)
RUN apk add --no-cache tzdata \
    && ln -snf /usr/share/zoneinfo/Asia/Seoul /etc/localtime \
    && echo 'Asia/Seoul' > /etc/timezone

# non-root 사용자 생성 (Alpine Linux 방식)
RUN addgroup -S appgroup && adduser -S -G appgroup appuser
RUN chown -R appuser:appgroup /app
USER appuser

# 애플리케이션 실행
ENTRYPOINT ["java", "-jar", "/app/app.jar"]