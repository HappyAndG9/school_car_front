package happy.schoolcarfront.controller;

import happy.schoolcarfront.Result.Result;
import happy.schoolcarfront.mapper.AnnouncementMapper;
import happy.schoolcarfront.vo.AnnouncementVo;
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
@RequestMapping("/announcement")
public class AnnouncementController {

    @Resource
    AnnouncementMapper mapper;


    @GetMapping("/getTheLatestAnnouncement")
    @ApiOperation("获取最新一条公告")
    public Result getTheLatestAnnouncement(){
        AnnouncementVo theLatestAnnouncement = mapper.getTheLatestAnnouncement();
        return Result.successWithData(theLatestAnnouncement);
    }
}
