package com.empayre.dominator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@ServletComponentScan
@SpringBootApplication(scanBasePackages = {"com.empayre.dominator"})
public class DominatorApplication {

    public static void main(String[] args) {
        SpringApplication.run(DominatorApplication.class, args);
    }

}
