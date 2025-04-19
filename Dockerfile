#FROM ubuntu:latest
#LABEL authors="simjuheun"
#
#ENTRYPOINT ["top", "-b"]


# 1. Python 기반의 경량 이미지 선택
FROM python:3.11-slim

# 2. 작업 디렉토리 생성 및 설정
WORKDIR /app

# 3. requirements.txt 복사 & 의존성 설치
COPY requirements.txt .
RUN pip install --no-cache-dir -r requirements.txt

# 4. 앱 코드 복사
COPY . .

# 5. 포트 오픈 (정보용)
EXPOSE 5000

# 6. 컨테이너가 시작되면 실행할 명령
CMD ["python", "app.py"]