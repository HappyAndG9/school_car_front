package happy.schoolcarfront.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import happy.schoolcarfront.Result.Result;
import happy.schoolcarfront.entity.ChargingPile;
import happy.schoolcarfront.entity.Orders;
import happy.schoolcarfront.entity.User;
import happy.schoolcarfront.mapper.ChargingPileMapper;
import happy.schoolcarfront.mapper.OrdersMapper;
import happy.schoolcarfront.mapper.UserMapper;
import happy.schoolcarfront.mqtt.MQTTConnect;
import happy.schoolcarfront.mqtt.MyCallback;
import happy.schoolcarfront.service.ChargingPileService;
import happy.schoolcarfront.service.OrdersService;
import happy.schoolcarfront.vo.OrdersVo;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 木月丶
 * @since 2023-03-16
 */
@RestController
@Slf4j
@RequestMapping("/order")
public class OrdersController {
    @Resource
    OrdersMapper ordersMapper;

    @Resource
    OrdersService ordersService;

    @Resource
    UserMapper userMapper;

    @Resource
    ChargingPileService chargingPileService;

    @Resource
    ChargingPileMapper chargingPileMapper;

    @Value("${mqtt.username}")
    private String username;

    @Value("${mqtt.password}")
    private String password;

    @PostMapping("/creatOrder")
    @ApiOperation("创建订单")
    public Result creatOrder(String openId,String serialNumber) throws MqttException {
        //获取用户信息
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("open_id", openId);
        User user = userMapper.selectOne(userQueryWrapper);

        //查找用户是否有正在进行的订单
        if (ordersService.queryOngoingOrders(user)){
            return Result.error("你有正在进行的订单");
        }

        //查看充电桩是否空闲
        if (!ordersService.checkChargingPileAvailable(serialNumber)){
            return Result.error("当前充电桩暂时不可用，换一个试试吧");
        }

        //创建订单
        Orders order = ordersService.createOrder(user, serialNumber);

        //通知硬件通电
        publish(msgMaker(serialNumber, 1));

        //开启充电桩
        chargingPileService.turnOnChargingPile(serialNumber);

        //将开始时间转成毫秒数时间戳响应给前端
        long startTime = order.getStartTime().toInstant(ZoneOffset.of("+8")).toEpochMilli();

        //一开始的金额都是0.00
        BigDecimal amount = order.getAmount();

        long state = order.getState();

        HashMap<String, Object> map = new HashMap<>();
        map.put("serialNumber", serialNumber);
        map.put("orderNumber", order.getOrderNumber());
        map.put("startTime", startTime);
        map.put("amount", amount);
        map.put("state", state);

        return Result.successWithMap(map);

    }


    @GetMapping("queryOrderInProgress")
    @ApiOperation("查看正在进行的订单")
    public Result queryOrderInProgress(String orderNumber) throws ParseException {

        //获取订单信息
        Orders orderInfo = ordersService.getOngoingOrderInfo(orderNumber);
        if (orderInfo == null){
            return Result.error("出错啦，请稍候重试！");
        }

        //获取时间戳响应给前端
        long timeDifference = new SimpleDateFormat("HH:mm:ss").parse(orderInfo.getDuration()).getTime();

        //封装返回结果
        HashMap<String, Object> map = new HashMap<>();
        map.put("serialNumber", orderInfo.getSerialNumber());
        map.put("startTime", orderInfo.getStartTime());
        map.put("amount", orderInfo.getAmount());
        map.put("timeDifference", timeDifference);

        return Result.successWithMap(map);
    }



    @PostMapping("/closeOrder")
    @ApiOperation("结束订单")
    public Result closeOrder(String orderNumber) throws MqttException {

        //查出订单
        QueryWrapper<Orders> wrapper = new QueryWrapper<>();
        wrapper.eq("order_number", orderNumber);
        Orders orders = ordersMapper.selectOne(wrapper);

        //通知硬件断电
        publish(msgMaker(orders.getSerialNumber(), 0));


        //算出时间
        long startTime = orders.getStartTime().toInstant(ZoneOffset.of("+8")).toEpochMilli();

        long endTime = System.currentTimeMillis();

        long timeDifference = endTime - startTime;

        //算出费用
        BigDecimal amount = taximeter(startTime,endTime);

        //更新表单
        orders.setAmount(amount);
        orders.setEndTime(new Date(endTime).toInstant().atOffset(ZoneOffset.of("+8")).toLocalDateTime());
        //订单已完成
        orders.setState(2L);
        orders.setDuration(clock(timeDifference));
        ordersMapper.updateById(orders);

        //充电桩已空闲
        QueryWrapper<ChargingPile> chargingPileQueryWrapper = new QueryWrapper<>();
        chargingPileQueryWrapper.eq("serial_number", orders.getSerialNumber());
        ChargingPile chargingPile = chargingPileMapper.selectOne(chargingPileQueryWrapper);

        chargingPile.setCurrentState(0);
        chargingPileMapper.updateById(chargingPile);

        //响应
        HashMap<String, Object> resultMap = new HashMap<>();
        resultMap.put("orderNumber", orderNumber);
        resultMap.put("startTime", startTime);
        resultMap.put("endTime", endTime);
        resultMap.put("timeDifference", timeDifference);
        resultMap.put("amount", amount);

        return Result.successWithData(resultMap);

    }

    @PostMapping("/giveComment")
    @ApiOperation("订单评价")
    public Result giveComment(String orderNumber,String comment){
        QueryWrapper<Orders> wrapper = new QueryWrapper<>();
        wrapper.eq("order_number", orderNumber);
        Orders orders = ordersMapper.selectOne(wrapper);

        if (orders == null || orders.getComment() != null){
            return Result.error("你已对此订单做出评价，不能重复评价！");
        }

        orders.setComment(comment);
        int i = ordersMapper.updateById(orders);

        return i != 0 ? Result.success("操作成功！") : Result.error("操作失败，请稍候重试！");

    }

    @GetMapping("/myOrderList")
    @ApiOperation("查看我全部未删除的订单")
    public Result myOrderList(String openId){
        List<OrdersVo> ordersVoList = ordersMapper.getOrdersList(openId);
        if (ordersVoList == null || ordersVoList.size() == 0){
            return Result.error("暂无订单记录");
        }
        return Result.successWithData(ordersVoList);
    }

    @DeleteMapping("/deleteOrder")
    @ApiOperation("删除订单（逻辑删除）")
    public Result deleteOrder(String orderNumber){
        QueryWrapper<Orders> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("order_number", orderNumber);
        int delete = ordersMapper.delete(queryWrapper);
        return delete != 0 ? Result.success("删除成功！") : Result.error("删除失败，请重试！");
    }

    /**
     * @author 木月丶
     * @description 计价器
     * @return BigDecimal
     */
    private BigDecimal taximeter(long startTime,long currentTime){
        //充电时长
        BigDecimal timeDifference = BigDecimal.valueOf((currentTime - startTime) / 1000.00 / 60.00);
        //定价1块钱=4小时=240分钟
        BigDecimal unitPrice = new BigDecimal("240");
        return timeDifference.divide(unitPrice,2, RoundingMode.HALF_UP);
    }

    /**
     * @author 木月丶
     * @description 时间戳转时分秒格式（非24小时）
     * @return String
     */
    private String clock(long time){
        long hours = TimeUnit.MILLISECONDS.toHours(time);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(time) - TimeUnit.HOURS.toMinutes(hours);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(time) - TimeUnit.MINUTES.toSeconds(minutes) - TimeUnit.HOURS.toSeconds(hours);
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

    /**
     * @author 木月丶
     * @description 消息发布
     */
    private void publish(String msg) throws MqttException {
        MQTTConnect connect = new MQTTConnect();
        connect.setMqttClient(username, password, new MyCallback());
        connect.pub("powerOn", msg,0);
    }

    /**
     * @author 木月丶
     * @description 消息格式化
     * @return String
     */
    private String msgMaker(String serialNumber,Integer state){
        String topic = "powerOn";
        return "/" + topic + serialNumber + ":" + state;
    }
}
