package ru.practicum.util;

import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Map;

public class UriPathBuilderWithParams {

    public static URI buildUri(String serverUrl, String apiPrefix, String path, Map<String, String> parameters) {
        String url = serverUrl + apiPrefix + path;
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url);
        parameters.forEach(builder::queryParam);
        return builder.build().encode().toUri();
    }

    public static URI buildUri(String serverUrl, String apiPrefix, String path,
                               Map<String, String> parameters, Map<String, String> pathVariable) {
        String url = serverUrl + apiPrefix + path;
        UriComponentsBuilder builder = UriComponentsBuilder
                .fromUriString(url);
        parameters.forEach(builder::queryParam);
        return builder
                .buildAndExpand(pathVariable)
                .encode()
                .toUri();
    }
}
