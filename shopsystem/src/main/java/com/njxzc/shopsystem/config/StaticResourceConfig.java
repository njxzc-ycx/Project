package com.njxzc.shopsystem.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class StaticResourceConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        //当地址栏包含 / 那么就默认访问 项目中resource 下的static 文件夹
        registry.addResourceHandler("/**").addResourceLocations("classpath:/static/");
        //当地址栏包含 /resource时，能够访问到我们电脑的指定位置
        registry.addResourceHandler("/resources/**").addResourceLocations("file:D:/upload_2021/shopsystem/");
    }
}
