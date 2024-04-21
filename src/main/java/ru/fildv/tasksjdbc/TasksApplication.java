package ru.fildv.tasksjdbc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
@EnableCaching
@ConfigurationPropertiesScan
public class TasksApplication {
    public static void main(final String[] args) {
        SpringApplication.run(TasksApplication.class, args);
    }
}
