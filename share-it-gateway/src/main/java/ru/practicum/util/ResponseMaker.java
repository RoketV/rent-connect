package ru.practicum.util;

import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class ResponseMaker {
    private final Gson gson;

    @Autowired
    public ResponseMaker(Gson gson) {
        this.gson = gson;
    }

    public <T> ResponseEntity<T> makeResponse(ResponseEntity<Object> response, Class<T> clazz) {
        if(response.getStatusCode().is2xxSuccessful()) {
                String json = gson.toJson(response.getBody());
                T t = gson.fromJson(json, clazz);
                return ResponseEntity.status(response.getStatusCode()).body(t);
            }
            return ResponseEntity.status(response.getStatusCode()).build();
    }

    public <T> ResponseEntity<List<T>> makeListResponse(ResponseEntity<Object[]> response, Class<T> clazz) {
        if (response.getStatusCode().is2xxSuccessful()) {
            List<T> dtos = Arrays.stream((Objects.requireNonNull(response.getBody())))
                    .map( o -> {
                     String json = gson.toJson(o);
                     return gson.fromJson(json, clazz);
                    }).collect(Collectors.toList());
            return ResponseEntity.status(response.getStatusCode()).body(dtos);
        }
        return ResponseEntity.status(response.getStatusCode()).build();
    }
}
