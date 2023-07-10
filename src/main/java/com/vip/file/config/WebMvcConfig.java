package com.vip.file.config;

import com.vip.file.interceptor.LoginInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.List;

/**
 * 解决跨域访问
 *
 * @author wgb
 * @date 2019/12/29 22:01
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    /**
     * 定义排除swagger访问的路径配置
     *
     *
     */
    String[] swaggerExcludes=new String[]{"/swagger-ui.html","/swagger-resources/**","/webjars/**","/image/**"};
//    @Override
//    public void addCorsMappings(CorsRegistry registry) {
//        registry.addMapping("/**")
//              .allowedOrigins("*")
//              .allowedMethods("POST", "GET", "PUT", "OPTIONS", "DELETE")
//              .maxAge(3600)
//              .allowCredentials(true);
//    }

    @Bean
    public WebMvcConfigurer crossConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            //重写父类提供的跨域请求处理的接口
            public void addCorsMappings(CorsRegistry registry) {
                //添加映射路径
                registry.addMapping("/**")
                        //放行哪些原始域
                        .allowedOrigins("*")
                        //是否发送Cookie信息
                        .allowCredentials(true)
                        //放行哪些原始域(请求方式)
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                        //放行哪些原始域(头部信息)
                        .allowedHeaders("*");
            }
        };
    }
    @Bean
    public LoginInterceptor getLoginInterceptor(){
        return new LoginInterceptor();
    }


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        List<String> list =new ArrayList<>();
        list.add("/user/userLogin");
        list.add("/system/file/video");
        list.add("/system/file/saveUploadFile");
        list.add("/api/file/check-file");
        list.add("/api/file/checkFile");
        list.add("/api/file/breakpoint-upload");
        list.add("/api/file/breakPointUpload");
        list.add("/queryAllFileVerificationRules");
        list.add("/saveUploadFailedLog");
        list.add("/system/file/removeVideo");
        list.add("/api/file/videoCompose");
        list.add("/system/file/removeBank");
        //注册TestInterceptor拦截器
        InterceptorRegistration registration = registry.addInterceptor(getLoginInterceptor());
        //所有路径都被拦截
        registration.addPathPatterns("/**");
        //添加不拦截路径
        registration.
//                excludePathPatterns("/systemSetup/user/userLogin","/systemSetup/warningManage/simulationPush",
//                "/systemSetup/warningManage/warningSign"
//        ).
        excludePathPatterns(list).excludePathPatterns(swaggerExcludes);
    }
}

