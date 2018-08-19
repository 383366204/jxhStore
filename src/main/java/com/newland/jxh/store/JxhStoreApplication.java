package com.newland.jxh.store;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
/*@MapperScan(basePackages = {

})*/
@EnableTransactionManagement
@EnableSwagger2 // 开启Swagger2注解
@EnableCaching // 开启注解缓存
@EnableWebSocket
@EnableRedisHttpSession
public class JxhStoreApplication {

	public static void main(String[] args) {
		SpringApplication.run(JxhStoreApplication.class, args);
	}

}
