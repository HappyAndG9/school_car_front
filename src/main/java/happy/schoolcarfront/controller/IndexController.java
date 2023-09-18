package happy.schoolcarfront.controller;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import happy.schoolcarfront.Result.Result;
import happy.schoolcarfront.weixin.WeixinRequest;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;

import static happy.schoolcarfront.weixin.WeixinRequest.WxAccessToken;

/**
 * @Author 木月丶
 * @Description
 */
@RestController
@RequestMapping("/index")
public class IndexController {

    @GetMapping("/getRequestParameter")
    @ApiOperation("获取小程序参数")
    public Result getRequestParameter(){
        HashMap<String, Object> resultMap = new HashMap<>();
        resultMap.put("WxAccessToken", WxAccessToken);
        resultMap.put("APPID", WeixinRequest.APPID);
        resultMap.put("SECRET", WeixinRequest.SECRET);
        return Result.successWithMap(resultMap);
    }

    @GetMapping("/getOpenId")
    @ApiOperation("获取openId")
    public Result getUrl(String code) {
        String url = "https://api.weixin.qq.com/sns/jscode2session?appid="
                + WeixinRequest.APPID + "&secret=" + WeixinRequest.SECRET
                + "&js_code=" + code + "&grant_type=authorization_code";

        BufferedReader in = null;

        try {
            URL weChatUrl = new URL(url);
            // 打开和URL之间的连接
            URLConnection connection = weChatUrl.openConnection();

            // 设置通用的请求属性
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);

            // 建立实际的连接
            connection.connect();

            // 定义 BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = in.readLine()) != null) {
                sb.append(line);
            }

            HashMap<String, String> resultMap = new HashMap<>();

            JSONObject jsonObject = JSONUtil.parseObj(sb.toString());

            resultMap.put("session_key", jsonObject.getStr("session_key"));

            resultMap.put("openid", jsonObject.getStr("openid"));

            return Result.successWithMap(resultMap);

        } catch (Exception e) {

            throw new RuntimeException(e);

        } finally {

            try {
                if (in != null) {
                    in.close();
                }

            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }
}
