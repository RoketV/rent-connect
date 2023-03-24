package ru.practicum.user;

import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.client.BaseClient;
import ru.practicum.user.dto.UserDto;

import java.util.Objects;
import java.util.Set;

@Service
public class UserClient extends BaseClient {
    private static final String API_PREFIX = "/users";
    Gson gson = new Gson();

    @Autowired
    public UserClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(SimpleClientHttpRequestFactory::new)
                        .build()
        );
    }

    public ResponseEntity<UserDto> addUser(UserDto dto) {
        ResponseEntity<Object> response = post("", dto);
        UserDto responseDto = gson.fromJson(Objects.requireNonNull(response.getBody()).toString(), UserDto.class);
        return ResponseEntity.status(response.getStatusCode()).body(responseDto);
    }

    public ResponseEntity<UserDto> updateUser(UserDto dto, Long userId) {
        ResponseEntity<Object> response = patch("/" + userId.toString(), dto);
        UserDto responseDto = gson.fromJson(Objects.requireNonNull(response.getBody()).toString(), UserDto.class);
        return ResponseEntity.status(response.getStatusCode()).body(responseDto);
    }

    public ResponseEntity<UserDto> getUser(Long userId) {
        ResponseEntity<Object> response = get("/" + userId.toString());
        UserDto responseDto = gson.fromJson(Objects.requireNonNull(response.getBody()).toString(), UserDto.class);
        return ResponseEntity.status(response.getStatusCode()).body(responseDto);
    }

    protected ResponseEntity<Set<UserDto>> getUsers() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<?> entity = new HttpEntity<>(headers);
        ParameterizedTypeReference<Set<UserDto>> resultSet = new ParameterizedTypeReference<>() {
        };
        return rest.exchange(
                "/users",
                HttpMethod.GET,
                entity,
                resultSet
        );
    }
}
