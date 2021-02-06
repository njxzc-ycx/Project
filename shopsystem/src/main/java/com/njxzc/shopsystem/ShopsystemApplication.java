package com.njxzc.shopsystem;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("com.njxzc.shopsystem.mapper")
@SpringBootApplication
public class ShopsystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShopsystemApplication.class, args);
    }

}
