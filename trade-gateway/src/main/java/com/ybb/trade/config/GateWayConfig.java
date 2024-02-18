package com.ybb.trade.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.http.codec.support.DefaultServerCodecConfigurer;

@Configuration
public class GateWayConfig {
//    @Bean
    ServerCodecConfigurer serverCodecConfigurer() {
        ServerCodecConfigurer serverCodecConfigurer = new DefaultServerCodecConfigurer();
        return serverCodecConfigurer;
    }
}
