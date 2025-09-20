# Java 17 환경 제공
FROM amazoncorretto:17

# JAR 파일 경로를 ARG로 받음 (빌드 시 사용)
ARG JAR_FILE=build/libs/*.jar

# 작업 디렉토리 생성 (선택이지만 권장)
WORKDIR /app

# JAR 파일을 복사하면서 이름을 고정
COPY ${JAR_FILE} app.jar

# 포트 노출
EXPOSE 8080

# 시간대를 Asia/Seoul로 설정
RUN ln -snf /usr/share/zoneinfo/Asia/Seoul /etc/localtime && echo 'Asia/Seoul' > /etc/timezone

# 애플리케이션 실행
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
