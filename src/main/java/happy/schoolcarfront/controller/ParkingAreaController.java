package happy.schoolcarfront.controller;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import happy.schoolcarfront.Result.Result;
import happy.schoolcarfront.entity.ParkingArea;
import happy.schoolcarfront.mapper.ParkingAreaMapper;
import io.swagger.annotations.ApiOperation;
import lombok.Data;
import org.springframework.web.bind.annotation.GetMapping;
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
@RequestMapping("/parkingArea")
public class ParkingAreaController {
    @Resource
    ParkingAreaMapper mapper;

    //获取停车区域坐标信息以及样式
    @GetMapping("/ParkingAreaInfo")
    @ApiOperation("获取全部停车区域坐标信息以及默认样式")
    public Result getAllChargingAreaInfo() throws IllegalAccessException {


        QueryWrapper<ParkingArea> wrapper = new QueryWrapper<>();
        //返回结果集
        HashMap<String, List<Map<String, Object>>> resultMap = new HashMap<>();

        //points数组
        ArrayList<Map<String, Object>> pointsList = new ArrayList<>();

        //makers数组
        ArrayList<Map<String, Object>> makersList = new ArrayList<>();

        List<ParkingArea> parkingAreaList = mapper.selectList(wrapper);

        for (ParkingArea parkingArea : parkingAreaList) {
            //单点坐标样式集
            HashMap<String, Object> pointsMap = new HashMap<>();

            Point[] allPoints = JSON.parseObject(parkingArea.getCoordinate(), Point[].class);
            Point[] point = Arrays.copyOfRange(allPoints, 0, 4);

            pointsMap.put("points", point);
            pointsMap.put("areaId", parkingArea.getAreaId());
            pointsMap.put("description", parkingArea.getDescription());
            pointsMap.put("fillColor", "#C6CFF8");
            pointsMap.put("strikeWidth", 2);
            pointsMap.put("strokeColor", "#93A6F9");
            pointsMap.put("zIndex", 1);

            //添加
            pointsList.add(pointsMap);



            //单maker样式集
            HashMap<String, Object> makersMap = new HashMap<>();

            makersMap.put("iconPath","../../image/parking.png");
            //makersMap.put("makers", allPoints[4]);
            for (Field declaredField : allPoints[4].getClass().getDeclaredFields()) {
                declaredField.setAccessible(true);
                makersMap.put(declaredField.getName(),declaredField.get(allPoints[4]));
            }

            makersMap.put("width", 28);
            makersMap.put("height", 32);
            makersMap.put("title", parkingArea.getDescription());

            //添加
            makersList.add(makersMap);
        }
        resultMap.put("makers", makersList);
        resultMap.put("points", pointsList);
        return Result.successWithData(resultMap);
    }

    //内部类
    @Data
    private static class Point{
        private Double longitude;   //经度
        private Double latitude;   //纬度
    }


    @GetMapping("/vacancy")
    @ApiOperation("获取该区域的空位数量")
    public Result getTheNumberOfVacancy(Long areaId){
        QueryWrapper<ParkingArea> wrapper = new QueryWrapper<>();
        wrapper.eq("area_id", areaId);
        ParkingArea parkingArea = mapper.selectOne(wrapper);
        if (parkingArea == null){
            return Result.error("出错啦，请重试！");
        }
        return Result.successWithData(parkingArea.getVacancy());
    }


}
