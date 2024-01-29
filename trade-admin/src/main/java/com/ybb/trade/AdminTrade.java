package com.ybb.trade;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
@MapperScan("com.ybb.trade.mapper")
public class AdminTrade {
    public static void main(String[] args)
    {
        SpringApplication.run(AdminTrade.class,args);
    }
}
