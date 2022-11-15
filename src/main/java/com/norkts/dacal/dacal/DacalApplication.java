package com.norkts.dacal.dacal;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.norkts.dacal")
public class DacalApplication {

    public static void main(String[] args) {
        SpringApplication.run(DacalApplication.class, args);
    }

}
