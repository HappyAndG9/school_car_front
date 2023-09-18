package happy.schoolcarfront.service;

import happy.schoolcarfront.entity.Orders;
import com.baomidou.mybatisplus.extension.service.IService;
import happy.schoolcarfront.entity.User;


/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 木月丶
 * @since 2023-03-25
 */
public interface OrdersService extends IService<Orders> {
    //查询是否有正在进行的订单
    boolean queryOngoingOrders(User user);

    //查询充电桩的状态
    boolean checkChargingPileAvailable(String serialNumber);

    //创建订单
    Orders createOrder(User user,String serialNumber);

    //获取正在进行的订单的信息
    Orders getOngoingOrderInfo(String orderNumber);


}
