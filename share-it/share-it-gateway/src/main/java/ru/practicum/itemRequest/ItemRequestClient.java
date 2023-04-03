package ru.practicum.itemRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.MainClient;
import ru.practicum.itemRequest.dto.ItemRequestInputDto;
import ru.practicum.itemRequest.dto.ItemRequestOutputDto;
import ru.practicum.util.PaginationParams;
import ru.practicum.util.ResponseMaker;
import ru.practicum.util.UriPathBuilderWithParams;

import java.net.URI;
import java.util.List;
import java.util.Map;

@Service
public class ItemRequestClient extends MainClient {

    private static final String API_PREFIX = "/requests";
    private final String serverUrl;

    private final ResponseMaker responseMaker;

    @Autowired
    public ItemRequestClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder,
                      ResponseMaker responseMaker) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
        this.serverUrl = serverUrl;
        this.responseMaker = responseMaker;
    }

    public ResponseEntity<ItemRequestOutputDto> addRequest(ItemRequestInputDto dto, Long userId) {
        ResponseEntity<Object> response = post("", userId, dto);
        return responseMaker.makeResponse(response, ItemRequestOutputDto.class);
    }

    public ResponseEntity<List<ItemRequestOutputDto>> getItemRequestWithItemsResponse(Long userId) {
        ResponseEntity<Object[]> response = getItemsClient("", userId);
        return responseMaker.makeListResponse(response, ItemRequestOutputDto.class);
    }

    public ResponseEntity<ItemRequestOutputDto> getItemRequest(Long requestId, Long userId) {
        ResponseEntity<Object> response = get("/" + requestId, userId);
        return responseMaker.makeResponse(response, ItemRequestOutputDto.class);
    }

    public ResponseEntity<List<ItemRequestOutputDto>> getItemRequestWithPagination(Long userId,
                                                                             PaginationParams params) {
        ResponseEntity<Object[]> response = getItemsClientWithPagination("/all", userId, params);
        return responseMaker.makeListResponse(response, ItemRequestOutputDto.class);
    }

    protected ResponseEntity<Object[]> getItemsClient(String path, Long userId) {
        HttpEntity<?> entity = new HttpEntity<>(defaultHeaders(userId));
        URI uri = UriPathBuilderWithParams.buildUri(serverUrl, API_PREFIX, path);
        return rest.exchange(
                uri,
                HttpMethod.GET,
                entity,
                Object[].class
        );
    }
    protected ResponseEntity<Object[]> getItemsClientWithPagination(String path, Long userId,
                                                                    PaginationParams params) {
        HttpEntity<?> entity = new HttpEntity<>(defaultHeaders(userId));
        Map<String, String> parameters = Map.of(
               "from", params.from().toString(),
               "size", params.size().toString()
        );
        URI uri = UriPathBuilderWithParams.buildUri(serverUrl, API_PREFIX, path, parameters);
        return rest.exchange(
                uri,
                HttpMethod.GET,
                entity,
                Object[].class
        );
    }

}
