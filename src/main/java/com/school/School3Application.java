package com.school;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@SpringBootApplication
//@MapperScan("com.school.mapper")
public class School3Application {

	public static void main(String[] args) {
		//TimeZone.setDefault(TimeZone.getTimeZone("Asia/Tokyo")); 
		SpringApplication.run(School3Application.class, args);
		log.info("启动成功");
	}

}
