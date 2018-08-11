package com.newland.jxh.store.demo.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

/**
 * @Author qyw
 * @Description TODO
 * @Date Created in 22:39 2018/8/11
 */
@Configuration
@MapperScan("com.newland.jxh.store.demo.dao")
public class MybatisPlusConfig {
}
