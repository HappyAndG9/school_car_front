package happy.schoolcarfront.mqtt;

import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.*;
import org.springframework.stereotype.Component;

/**
 * MQTT工具类操作
 */
@Slf4j
@Component
public class MQTTConnect {

  private MqttClient mqttClient;

  /**
   * 客户端connect连接mqtt服务器
   *
   * @param username 用户名
   * @param password 密码
   * @param mqttCallback 回调函数
   **/
  public void setMqttClient(String username, String password, MqttCallback mqttCallback)
      throws MqttException {

    String host = "";
    MqttConnectOptions options = mqttConnectOptions(host, username, password);

    mqttClient.setCallback(mqttCallback);

    mqttClient.connect(options);
  }

  /**
   * MQTT连接参数设置
   */
  private MqttConnectOptions mqttConnectOptions(String host, String userName, String passWord)
      throws MqttException {

    String clientId = "client";
    mqttClient = new MqttClient(host, clientId);

    MqttConnectOptions options = new MqttConnectOptions();

    //设置username
    options.setUserName(userName);

    //设置password
    options.setPassword(passWord.toCharArray());

    //设置连接超时时长
    options.setConnectionTimeout(10);///默认：30

    //是否自动重连？
    options.setAutomaticReconnect(true);//默认：false

    //是否清除会话？
    options.setCleanSession(false);//默认：true

    return options;
  }

  /**
   * 向某个主题发布消息
   *
   * @param topic: 发布的主题
   * @param msg: 发布的消息
   * @param qos: 消息质量    Qos：0、1、2
   */
  public void pub(String topic, String msg, int qos) throws MqttException {

    MqttMessage mqttMessage = new MqttMessage();

    mqttMessage.setQos(qos);

    mqttMessage.setPayload(msg.getBytes());

    MqttTopic mqttTopic = mqttClient.getTopic(topic);

    MqttDeliveryToken token = mqttTopic.publish(mqttMessage);

    token.waitForCompletion();
  }

  /**
   * 订阅某一个主题，可携带Qos
   *
   * @param topic 所要订阅的主题
   * @param qos 消息质量：0、1、2
   */
  public void sub(String topic, int qos) throws MqttException {
    mqttClient.subscribe(topic, qos);
  }
}
