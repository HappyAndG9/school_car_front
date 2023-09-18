package happy.schoolcarfront.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import happy.schoolcarfront.entity.ChargingPile;
import happy.schoolcarfront.entity.Orders;
import happy.schoolcarfront.entity.User;
import happy.schoolcarfront.mapper.ChargingPileMapper;
import happy.schoolcarfront.mapper.OrdersMapper;
import happy.schoolcarfront.mapper.UserMapper;
import happy.schoolcarfront.service.OrdersService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import happy.schoolcarfront.utils.OrderNumberGenerator;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.ZoneOffset;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 木月丶
 * @since 2023-03-25
 */
@Service
public class OrdersServiceImpl extends ServiceImpl<OrdersMapper, Orders> implements OrdersService {

    @Resource
    UserMapper userMapper;

    @Resource
    ChargingPileMapper chargingPileMapper;

    @Resource
    OrdersMapper ordersMapper;

    @Override
    public boolean queryOngoingOrders(User user) {
        // 1 为有在正在进行的订单， 0 为没有
        return user.getHasOngoingOrder() == 1;
    }

    @Override
    public boolean checkChargingPileAvailable(String serialNumber) {
        QueryWrapper<ChargingPile> chargingPileQueryWrapper = new QueryWrapper<>();
        chargingPileQueryWrapper.eq("serial_number", serialNumber);
        ChargingPile chargingPile = chargingPileMapper.selectOne(chargingPileQueryWrapper);
        //CurrentState == 1代表充电桩目前有人正在使用
        //ServiceState == 0代表充电桩目前处于维修状态
        return chargingPile.getCurrentState() != 1 && chargingPile.getServiceState() != 0;
    }

    @Override
    public Orders createOrder(User user,String serialNumber) {
        //生成订单号
        OrderNumberGenerator orderNumberGenerator = new OrderNumberGenerator(3, 3);
        long orderNumber = orderNumberGenerator.nextId();

        //创建订单对象
        Long userId = user.getUserId();
        String userNickName = user.getUserNickName();
        Orders orders = new Orders(userId, userNickName, serialNumber, String.valueOf(orderNumber));

        //更新数据库中的订单表和用户表
        ordersMapper.insert(orders);
        user.setUserOrderQuantity(user.getUserOrderQuantity() + 1);
        userMapper.updateById(user);

        return orders;
    }


    @Override
    public Orders getOngoingOrderInfo(String orderNumber) {
        QueryWrapper<Orders> wrapper = new QueryWrapper<>();
        wrapper.eq("order_number", orderNumber);
        Orders orders = ordersMapper.selectOne(wrapper);

        long startTime = orders.getStartTime().toInstant(ZoneOffset.of("+8")).toEpochMilli();

        long currentTime = System.currentTimeMillis();

        long timeDifference = currentTime - startTime;

        BigDecimal amount = taximeter(startTime,currentTime);

        orders.setAmount(amount);
        orders.setDuration(String.valueOf(timeDifference));

        ordersMapper.updateById(orders);

        return orders;

    }

    private BigDecimal taximeter(long startTime, long currentTime){
        //充电时长
        BigDecimal timeDifference = BigDecimal.valueOf((currentTime - startTime) / 1000.00 / 60.00);
        //定价1块钱=4小时=240分钟
        BigDecimal unitPrice = new BigDecimal("240");
        return timeDifference.divide(unitPrice,2, RoundingMode.HALF_UP);
    }
}
