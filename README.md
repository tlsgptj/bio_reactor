### 데이터 흐름:

- 하드웨어 기기는 MQTT 클라이언트를 사용하여 Mosquitto 브로커에 데이터를 발행
- Mosquitto 브로커는 구독자(클라이언트)에게 메시지를 전달하거나 Firebase로 메시지를 전송
- Firebase는 실시간 데이터베이스 또는 Firestore에 데이터를 저장
- Android Studio에서는 Firebase SDK를 사용하여 이 데이터를 읽어오거나 Firebase Cloud Messaging (FCM)을 통해 실시간 업데이트

# (하드웨어 기기) → (Mosquitto) → (Firebase) → (Android Studio)
# Todo
- Firebase android studio 연결
- MQTT Mosquitto 연결
- 하드웨어 기기 Mosquitto 서버 연결
