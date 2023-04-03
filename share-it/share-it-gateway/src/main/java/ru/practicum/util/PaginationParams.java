package ru.practicum.util;

import org.springframework.web.bind.annotation.RequestParam;

public class PaginationParams {

    private final Integer from;
    private final Integer size;

    public PaginationParams(@RequestParam(value = "from", defaultValue = "0")
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

    public Integer from() {
        return from;
    }

    public Integer size() {
        return size;
    }
}
