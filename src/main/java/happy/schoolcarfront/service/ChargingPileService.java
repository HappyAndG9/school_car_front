package happy.schoolcarfront.service;

import happy.schoolcarfront.entity.ChargingPile;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 木月丶
 * @since 2023-03-16
 */
public interface ChargingPileService extends IService<ChargingPile> {
    //开启充电桩
    void turnOnChargingPile(String serialNumber);

}
