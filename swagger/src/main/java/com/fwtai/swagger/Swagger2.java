package com.fwtai.swagger;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Parameter;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

/**
 * swagger2配置,注意扫描的包名!!!
 * @作者 田应平
 * @版本 v1.0
 * @创建时间 2020/3/23 18:00
 * @QQ号码 444141300
 * @Email service@yinlz.com
 * @官网 <url>http://www.yinlz.com</url>
*/
@Configuration
@EnableSwagger2
public class Swagger2{

    //swagger2的配置文件,打开 http://127.0.0.1:801/swagger-ui.html
    @Bean
    public Docket docketV1(){// 创建API基本信息
        return docket("v1.0.0");
    }

    @Bean
    public Docket docketV2(){// 创建API基本信息
        return docket("v1.0.1");
    }

    private Docket docket(final String groupName){
        final ParameterBuilder ticketPar = new ParameterBuilder();
        final List<Parameter> pars = new ArrayList<Parameter>();
        ticketPar.name("access_token").description("access_token").modelRef(new ModelRef("string")).parameterType("header").required(false).build();
        ticketPar.name("refresh_token").description("refresh_token").modelRef(new ModelRef("string")).parameterType("header").required(false).build();
        pars.add(ticketPar.build());
        return new Docket(DocumentationType.SWAGGER_2).groupName(groupName)//多版本时指定名称
          .apiInfo(apiInfo()).enable(true)//是否开启swagger
          .select()
          //扫描该包下的所有需要在Swagger中展示的API，@ApiIgnore注解标注的除外(若不想在swagger上面显示接口文档直接把扫描的包名改下即可)
          .apis(RequestHandlerSelectors.basePackage("com.fwtai.api.controller"))
          .paths(PathSelectors.any())
          .build()
          .securitySchemes(securitySchemes())
          .securityContexts(securityContexts());
    }

    private List<ApiKey> securitySchemes() {
        final List<ApiKey> apiKeyList= new ArrayList();
        apiKeyList.add(new ApiKey("access_token", "access_token","header"));
        apiKeyList.add(new ApiKey("refresh_token", "refresh_token","header"));
        return apiKeyList;
    }

    private List<SecurityContext> securityContexts() {
        final List<SecurityContext> securityContexts=new ArrayList<>();
        securityContexts.add(
          SecurityContext.builder()
            .securityReferences(defaultAuth())
            .forPaths(PathSelectors.regex("^(?!auth).*$"))
            .build());
        return securityContexts;
    }

    private List<SecurityReference> defaultAuth() {
        final AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
        final AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        final List<SecurityReference> securityReferences = new ArrayList<>();
        securityReferences.add(new SecurityReference("Authorization", authorizationScopes));
        return securityReferences;
    }

    private ApiInfo apiInfo(){
        return new ApiInfoBuilder()
          .title("项目接口文档")
          .description("swagger-接口文档<br />若是报错java.lang.NumberFormatException异常,如请求参数里有int类型的添加 example = \"1\"<br />另外，可以使用@ApiIgnore注解标注的除外，不显示,该注解可以在类或方法上使用<br />如果不显示请检查扫描的包名是否正确")//API描述
          .version("v1.0")//版本号
          .contact(new Contact("引路者","http://www.yinlz.com","444141300@qq.com"))
          .termsOfServiceUrl("http://www.yinlz.com")
          .license("保密版本")
          .licenseUrl("http://www.yinlz.com")
          .build();
    }
}