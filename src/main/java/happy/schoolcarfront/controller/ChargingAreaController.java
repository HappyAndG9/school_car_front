package happy.schoolcarfront.controller;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import happy.schoolcarfront.Result.Result;
import happy.schoolcarfront.entity.ChargingArea;
import happy.schoolcarfront.mapper.ChargingAreaMapper;
import io.swagger.annotations.ApiOperation;
import lombok.Data;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.lang.reflect.Field;
import java.util.*;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 木月丶
 * @since 2023-03-25
 */
@RestController
@RequestMapping("/chargingArea")
public class ChargingAreaController {
    @Resource
    ChargingAreaMapper mapper;


    @GetMapping("/chargingAreaInfo")
    @ApiOperation("获取全部充电区域坐标信息以及默认样式")
    public Result getAllChargingAreaInfo() throws IllegalAccessException {
        QueryWrapper<ChargingArea> wrapper = new QueryWrapper<>();

        //返回结果集
        HashMap<String, List<Map<String, Object>>> resultMap = new HashMap<>();

        //points数组
        ArrayList<Map<String, Object>> pointsList = new ArrayList<>();

        //makers数组
        ArrayList<Map<String, Object>> makersList = new ArrayList<>();

//        List<ChargingArea> chargingAreaList = mapper.getAllChargingArea();
        List<ChargingArea> chargingAreaList = mapper.selectList(wrapper);

        for (ChargingArea chargingArea : chargingAreaList) {
            //单点坐标样式集
            HashMap<String, Object> pointsMap = new HashMap<>();

            Point[] allPoints = JSON.parseObject(chargingArea.getCoordinate(), Point[].class);
            Point[] point = Arrays.copyOfRange(allPoints, 0, 4);

            pointsMap.put("points", point);
            pointsMap.put("areaId", chargingArea.getAreaId());
            pointsMap.put("description", chargingArea.getDescription());
            pointsMap.put("fillColor", "#C6CFF8");
            pointsMap.put("strikeWidth", 2);
            pointsMap.put("strokeColor", "#93A6F9");
            pointsMap.put("zIndex", 1);

            //添加
            pointsList.add(pointsMap);



            //单maker样式集
            HashMap<String, Object> makersMap = new HashMap<>();

            makersMap.put("iconPath","../../image/chargingPile.png");
            makersMap.put("areaId", chargingArea.getAreaId());
            //makersMap.put("makers", allPoints[4]);
            for (Field declaredField : allPoints[4].getClass().getDeclaredFields()) {
                declaredField.setAccessible(true);
                makersMap.put(declaredField.getName(),declaredField.get(allPoints[4]));
            }
            makersMap.put("width", 28);
            makersMap.put("height", 32);

            HashMap<String, Object> label = new HashMap<>();
            label.put("content", chargingArea.getDescription());
            label.put("color", "#333");
            label.put("borderRadius",3);
            label.put("borderWidth",1);
            label.put("borderColor","#FFF");
            label.put("bgColor","#ffffff");
            label.put("padding",5);
            label.put("textAlign", "center");
            makersMap.put("label", label);

            //添加
            makersList.add(makersMap);
        }
        resultMap.put("makers", makersList);
        resultMap.put("points", pointsList);
        return Result.successWithData(resultMap);
    }


    @PostMapping("/vacancy")
    @ApiOperation("获取一片区域的空闲充电桩数量信息")
    public Result getTheNumberOfVacancy(Long locationId){
        return Result.successWithData(mapper.getVacancyChargingPileCount(locationId));
    }

    @GetMapping("/getVacancyAndTotal")
    @ApiOperation("获取所有充电区域的空闲充电桩和充电桩总数")
    public Result getVacancyAndTotal(){
        List<HashMap<String, Integer>> vacancyAndTotal = mapper.getVacancyAndTotal();
        return vacancyAndTotal != null ? Result.successWithData(vacancyAndTotal) : Result.error("暂无数据");
    }

    //内部类
    @Data
    private static class Point{
        private Double longitude;   //经度
        private Double latitude;   //纬度
    }
}
