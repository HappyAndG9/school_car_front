package happy.schoolcarfront.mqtt;

import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

/**
 * 由于业务场景需要，在项目启动时，监听MQTT主题Topic，故编写MQTT监听器
 * 即:项目启动 监听主题
 */
@Slf4j
@Component
public class MQTTListener implements ApplicationListener<ContextRefreshedEvent> {

  @Value("${mqtt.username}")
  private String username;

  @Value("${mqtt.password}")
  private String password;

  @Value("${mqtt.topic}")
  private String topic;

  private final MQTTConnect connect;

  private final MyCallback myCallback;


  @Autowired
  public MQTTListener(MQTTConnect connect, MyCallback myCallback) {
    this.connect = connect;
    this.myCallback = myCallback;
  }

  @Override
  public void onApplicationEvent(@NotNull ContextRefreshedEvent contextRefreshedEvent) {
    try {
      connect.setMqttClient(username, password, myCallback);
      connect.sub(topic,1);
    } catch (MqttException e) {
      log.error(e.getMessage(), e);
    }
  }
}


