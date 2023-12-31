package com.example.testfinal.config;

import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.Clock;
import java.time.ZoneId;
import java.util.Set;
import java.util.concurrent.Executor;

@Configuration
public class ApplicationConfig {

    @Bean
    public ModelMapper modelMapper(Set<Converter> converters){
        ModelMapper modelMapper = new ModelMapper();
        converters.forEach(modelMapper::addConverter);
        return modelMapper;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean(name = "importPeopleExecutor")
    public Executor importPeopleExecutor() {
        final ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(1);
        executor.setMaxPoolSize(1);
        executor.setQueueCapacity(100);
        executor.setThreadNamePrefix("import-people-thread-");
        executor.initialize();
        return executor;
    }

    @Bean
    public ThreadPoolTaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5); // Set the core pool size
        executor.setMaxPoolSize(10); // Set the maximum pool size
        executor.setQueueCapacity(25); // Set the queue capacity
        executor.setThreadNamePrefix("MyTaskExecutor-");
        executor.initialize();
        return executor;
    }

    @Bean
    Clock clock(){
        ZoneId zoneId = ZoneId.of("UTC+2");
        return Clock.system(zoneId);
    }
}
