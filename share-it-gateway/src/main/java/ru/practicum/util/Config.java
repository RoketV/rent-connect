package ru.practicum.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;

@Configuration
public class Config {

    @Bean
    public Gson gson() {
        return new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeTypeAdapter())
                .create();
    }
}
