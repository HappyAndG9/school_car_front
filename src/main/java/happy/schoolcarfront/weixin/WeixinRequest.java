package happy.schoolcarfront.weixin;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @Author 木月丶
 * @Description
 */
@Component
public class WeixinRequest {
    public static final String WxAccessToken="WxAccessToken";

    @Value("${APPID}")
    public static String APPID;

    @Value("${SECRET}")
    public static String SECRET;


    @Value("${APPID}")
    public void setAPPID(String APPID) {
        WeixinRequest.APPID = APPID;
    }

    @Value("${SECRET}")
    public void setSECRET(String SECRET) {
        WeixinRequest.SECRET = SECRET;
    }
}
