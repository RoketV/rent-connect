package com.shareitserver.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.web.bind.annotation.RequestParam;


public record ItemRequestPaginationParams(@Min(0) Integer from, @Min(1) @Max(20) Integer size) {
    public ItemRequestPaginationParams(@RequestParam(value = "from", defaultValue = "0")
                                       Integer from,
                                       @RequestParam(value = "size", defaultValue = "20")
                                       Integer size) {
        if (from == null) {
            from = 0;
        }
        if (size == null) {
            size = 20;
        }
        this.from = from;
        this.size = size;
    }
}

