package com.newland.jxh.store;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
/*@MapperScan(basePackages = {

})*/
@EnableTransactionManagement
public class JxhStoreApplication {

	public static void main(String[] args) {
		SpringApplication.run(JxhStoreApplication.class, args);
	}

}
