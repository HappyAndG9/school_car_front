package happy.schoolcarfront.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import happy.schoolcarfront.Result.Result;
import happy.schoolcarfront.entity.ChargingPile;
import happy.schoolcarfront.mapper.ChargingPileMapper;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 木月丶
 * @since 2023-03-16
 */
@RestController
@RequestMapping("/chargingPile")
public class ChargingPileController {

    @Resource
    ChargingPileMapper mapper;


    @GetMapping("/thisChargingPileInfo")
    @ApiOperation("当前所用充电桩的具体信息")
    public Result thisChargingPileInfo(String serialNumber){
        QueryWrapper<ChargingPile> wrapper = new QueryWrapper<>();
        wrapper.eq("serial_number", serialNumber);
        ChargingPile chargingPile = mapper.selectOne(wrapper);
        if (chargingPile == null){
            return Result.error("二维码无效！");
        }
        if (chargingPile.getCurrentState() == 1){
            return Result.error("当前充电桩正在被人使用，请换一个试试吧！");
        }
        return Result.successWithData(chargingPile);
    }



}
