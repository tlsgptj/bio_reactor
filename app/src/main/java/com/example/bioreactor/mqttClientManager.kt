import android.content.Context
import org.eclipse.paho.android.service.MqttAndroidClient
import org.eclipse.paho.client.mqttv3.IMqttActionListener
import org.eclipse.paho.client.mqttv3.IMqttToken
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended
import org.eclipse.paho.client.mqttv3.MqttConnectOptions
import org.eclipse.paho.client.mqttv3.MqttMessage

class MqttClientManager(context: Context, brokerUrl: String, clientId: String) {

    private val mqttAndroidClient: MqttAndroidClient = MqttAndroidClient(context, brokerUrl, clientId)

    init {
        mqttAndroidClient.setCallback(object : MqttCallbackExtended {
            override fun connectComplete(reconnect: Boolean, serverURI: String) {
                // 연결 완료 시 처리
            }

            override fun connectionLost(cause: Throwable) {
                // 연결 끊김 시 처리
            }

            @Throws(Exception::class)
            override fun messageArrived(topic: String, message: MqttMessage) {
                // 메시지 수신 시 처리
            }

            override fun deliveryComplete(token: IMqttToken) {
                // 메시지 전달 완료 시 처리
            }
        })
    }

    fun connect(listener: IMqttActionListener) {
        val options = MqttConnectOptions()
        options.isAutomaticReconnect = true

        try {
            mqttAndroidClient.connect(options, null, listener)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun subscribe(topic: String, qos: Int) {
        try {
            mqttAndroidClient.subscribe(topic, qos)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun publish(topic: String, payload: String, qos: Int) {
        try {
            val message = MqttMessage(payload.toByteArray())
            message.qos = qos
            mqttAndroidClient.publish(topic, message)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun disconnect() {
        try {
            mqttAndroidClient.disconnect()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
