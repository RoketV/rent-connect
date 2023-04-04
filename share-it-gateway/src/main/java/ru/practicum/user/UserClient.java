package ru.practicum.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.MainClient;
import ru.practicum.user.dto.UserDto;
import ru.practicum.util.ResponseMaker;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserClient extends MainClient {
    private static final String API_PREFIX = "/users";

    private final ResponseMaker responseMaker;

    @Autowired
    public UserClient(@Value("${shareit-server.url}") String serverUrl,
                      RestTemplateBuilder builder, ResponseMaker responseMaker) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
        this.responseMaker = responseMaker;
    }

    public ResponseEntity<UserDto> addUser(UserDto dto) {
        ResponseEntity<Object> response = post("", dto);
        return responseMaker.makeResponse(response, UserDto.class);
    }

    public ResponseEntity<UserDto> updateUser(UserDto dto, Long userId) {
        ResponseEntity<Object> response = patch("/" + userId.toString(), dto);
        return responseMaker.makeResponse(response, UserDto.class);
    }

    public ResponseEntity<UserDto> getUser(Long userId) {
        ResponseEntity<Object> response = get("/" + userId.toString());
        return responseMaker.makeResponse(response, UserDto.class);
    }

    public ResponseEntity<Set<UserDto>> getUsers() {
        ResponseEntity<Object[]> response = getUsersClient();
        if (response.getStatusCode().is2xxSuccessful()) {
            Set<UserDto> userDtos = Arrays.stream(Objects.requireNonNull(response.getBody()))
                    .map(o -> gson.fromJson(o.toString(), UserDto.class))
                    .collect(Collectors.toCollection(LinkedHashSet::new));
            log.info("users retrieved");
            return ResponseEntity.status(response.getStatusCode()).body(userDtos);
        }
        log.info("no users found in database or other server issues");
        return ResponseEntity.status(response.getStatusCode()).build();
    }

    public ResponseEntity<UserDto> deleteUser(Long id) {
        ResponseEntity<Object> response = delete("/" + id.toString());
        return responseMaker.makeResponse(response, UserDto.class);
    }

    protected ResponseEntity<Object[]> getUsersClient() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<?> entity = new HttpEntity<>(headers);
        return rest.exchange(
                "",
                HttpMethod.GET,
                entity,
                Object[].class
        );
    }
}
