package happy.schoolcarfront;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.VelocityTemplateEngine;
import com.baomidou.mybatisplus.generator.fill.Column;

import java.util.Collections;

/**
 * @Author 木月丶
 * @Description 自动生成代码
 */
public class auto {
    public static void main(String[] args) {
        //数据源
        FastAutoGenerator.create("jdbc:mysql://localhost:3306/school_car", "root", "password")
                //全局配置
                .globalConfig(builder -> {
                    builder.author("木月丶") // 设置作者
                            .enableSwagger() // 开启 swagger 模式
                            .outputDir(System.getProperty("user.dir") + "/src/main/java"); // 指定输出目录
                })

                //包配置
                .packageConfig(builder -> {
                    builder.parent("happy.schoolcarfront") // 设置父包名
                            .pathInfo(Collections.singletonMap(OutputFile.xml, System.getProperty("user.dir")
                                    + "/src/main/resources/mapper")); // 设置mapperXml生成路径
                })

                //策略配置
                .strategyConfig(builder -> {
                    builder.addInclude(
//                                    "announcement",
//                                    "charging_pile",
                                    "feedback",
                                    "order",
                                    "suggest"
//                                    "user",
//                                    "charging_pile_area","parking_area"
                            ) 		//要生成entity的表都有哪些？

                            //entity 策略配置
                            .entityBuilder()
                            .enableLombok() //开启lombok
                            .naming(NamingStrategy.underline_to_camel)  //开启表的驼峰命名
                            .columnNaming(NamingStrategy.underline_to_camel)    //开启字段的驼峰命名
                            .addTableFills(
                                    new Column("creat_time", FieldFill.INSERT), //插入时自动填充时间
                                    new Column("start_time",FieldFill.INSERT),
                                    new Column("end_time",FieldFill.INSERT),
                                    new Column("feedback_time",FieldFill.INSERT),
                                    new Column("suggest_time",FieldFill.INSERT)
                            )
                            .enableTableFieldAnnotation()   //开启生成实体时生成字段注解

                            //mapper策略配置
                            .mapperBuilder()
                            .superClass(BaseMapper.class)   //所有mapper继承BaseMapper.class
                            .formatMapperFileName("%sMapper")   //格式化mapper文件名称 如：UserMapper
                            .enableMapperAnnotation()   //所有mapper上标注@Mapper
                            .formatXmlFileName("%sMapper") //格式化xml文件名称    如：UserMapper

                            //service策略配置
                            .serviceBuilder()
                            .formatServiceFileName("%sService") //格式化service文件名称    如：UserService
                            .formatServiceImplFileName("%sServiceImpl") //格式化所有实现service接口文件名称  如“UserServiceImpl

                            //controller策略配置
                            .controllerBuilder()
                            .formatFileName("%sController") //格式化所有controller文件名称   如：UserController
                            .enableRestStyle();     //所有controller上标注@RestController
                })
                .templateEngine(new VelocityTemplateEngine()) // 使用Velocity引擎模板
                .execute(); //执行
    }
}