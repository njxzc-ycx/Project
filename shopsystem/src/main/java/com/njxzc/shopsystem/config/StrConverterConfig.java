package com.njxzc.shopsystem.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
@Configuration
public class StrConverterConfig implements WebMvcConfigurer {
    //将格式正确的日期  由  String 转换为 Timestamp
    @Bean
    public Converter<String, Timestamp> StringToTimestamp(){
        return new Converter<String, Timestamp>() {
            @Override
            public Timestamp convert(String s) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Timestamp ts = null;
                try {
                    ts = Timestamp.valueOf(s);
                }catch (Exception e){
                    e.printStackTrace();
                }
                return ts;
            }
        };
    }
}
