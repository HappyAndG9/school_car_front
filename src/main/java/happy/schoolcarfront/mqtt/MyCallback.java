package happy.schoolcarfront.mqtt;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import happy.schoolcarfront.entity.ParkingArea;
import happy.schoolcarfront.service.ParkingAreaService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Author 木月丶
 * @Description 自定义的回调函数
 */
@Slf4j
@Component
public class MyCallback implements MqttCallback {
    @Resource
    ParkingAreaService service;


    /**
     * MQTT 断开连接会执行此方法
     */
    @Override
    public void connectionLost(Throwable cause) {
    }

    /**
     * publish发布成功后会执行到这里
     */
    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
        System.out.println("我已经推送了一条消息");
    }

    /**
     * subscribe订阅后得到的消息会执行到这里
     */
    @Override
    public void messageArrived(String topic, MqttMessage message){

            String info = new String(message.getPayload());

            msg msg = JSON.parseObject(info, msg.class);

            QueryWrapper<ParkingArea> wrapper = new QueryWrapper<>();
            wrapper.eq("area_id", msg.getAreaId());
            ParkingArea parkingArea = service.getOne(wrapper);

            if (parkingArea.getRatedQuantity() < msg.getOccupy()){
                parkingArea.setVacancy(0);
            }else {
                parkingArea.setVacancy(parkingArea.getRatedQuantity() - msg.getOccupy());
            }

            service.updateById(parkingArea);


    }
    @Data
    private static class msg{
        private Long areaId;
        private Integer occupy;
    }
}
