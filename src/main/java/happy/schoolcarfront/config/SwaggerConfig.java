package happy.schoolcarfront.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

/**
 * @Author 木月丶
 * @Description Swagger配置
 */
@Configuration
public class SwaggerConfig {
    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()//下面是暴露的接口
                //哪些包下的内容可以被看到
                .apis(RequestHandlerSelectors.basePackage("happy.schoolcarfront.controller"))
                //暴露下的所有url都可以访问
                .paths(PathSelectors.any())
                .build().apiInfo(new ApiInfoBuilder()
                        //文档标题
                        .title("school-car前台系统")
                        //文档描述
                        .description("作者：木月丶")
                        //项目版本号
                        .version("1.0")
                        .build());
    }
}
