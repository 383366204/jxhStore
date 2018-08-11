package com.newland.jxh.store;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
/*@MapperScan(basePackages = {

})*/
@EnableTransactionManagement
@EnableSwagger2
public class JxhStoreApplication {

	public static void main(String[] args) {
		SpringApplication.run(JxhStoreApplication.class, args);
	}

}
