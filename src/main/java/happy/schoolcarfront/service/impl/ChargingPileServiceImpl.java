package happy.schoolcarfront.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import happy.schoolcarfront.entity.ChargingPile;
import happy.schoolcarfront.mapper.ChargingPileMapper;
import happy.schoolcarfront.service.ChargingPileService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 木月丶
 * @since 2023-03-16
 */
@Service
public class ChargingPileServiceImpl extends ServiceImpl<ChargingPileMapper, ChargingPile> implements ChargingPileService {

    @Resource
    ChargingPileMapper chargingPileMapper;

    //开启充电桩
    @Override
    public void turnOnChargingPile(String serialNumber) {
        QueryWrapper<ChargingPile> wrapper = new QueryWrapper<>();
        wrapper.eq("serial_number",serialNumber);
        ChargingPile chargingPile = chargingPileMapper.selectOne(wrapper);
        chargingPile.setCurrentState(1);
        chargingPileMapper.updateById(chargingPile);
    }

}
