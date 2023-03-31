package ru.practicum.item;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.MainClient;
import ru.practicum.item.dto.ItemInputDto;
import ru.practicum.item.dto.ItemOutputDto;
import ru.practicum.util.ResponseMaker;
import ru.practicum.util.UriPathBuilderWithParams;

import java.net.URI;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class ItemClient extends MainClient {

    private static final String API_PREFIX = "/items";
    private final String serverUrl;

    private final ResponseMaker responseMaker;

    @Autowired
    public ItemClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder,
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

    public ResponseEntity<ItemOutputDto> addItem(ItemInputDto dto, Long userId) {
        ResponseEntity<Object> response = post("", userId, dto);
        return responseMaker.makeResponse(response, ItemOutputDto.class);
    }

    public ResponseEntity<ItemOutputDto> updateItem(ItemInputDto dto, Long userId, Long itemId) {
        ResponseEntity<Object> response = patch("/" + itemId.toString(), userId, dto);
        return responseMaker.makeResponse(response, ItemOutputDto.class);
    }

    public ResponseEntity<ItemOutputDto> getItem(Long ownerId, Long itemId) {
        ResponseEntity<Object> response = get("/" + itemId.toString(), ownerId);
        return responseMaker.makeResponse(response, ItemOutputDto.class);
    }

    public ResponseEntity<List<ItemOutputDto>> getItems(Long userId, ItemPaginationParams params) {
        ResponseEntity<Object[]> response = getItemsClient("", userId, params);
        return responseMaker.makeListResponse(response, ItemOutputDto.class);
    }

    public ResponseEntity<ItemOutputDto> deleteItem(Long itemId) {
        ResponseEntity<Object> response = delete("/" + itemId.toString());
        return responseMaker.makeResponse(response, ItemOutputDto.class);
    }

    protected ResponseEntity<List<ItemOutputDto>> searchItem(String text, ItemPaginationParams params) {
        ResponseEntity<Object[]> response = searchItemsClient("/search", text, params);
        return responseMaker.makeListResponse(response, ItemOutputDto.class);
    }

    protected ResponseEntity<Object[]> getItemsClient(String path, Long userId, ItemPaginationParams params) {
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

    protected ResponseEntity<Object[]> searchItemsClient(String path, String text, ItemPaginationParams params) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        HttpEntity<?> entity = new HttpEntity(headers);
        Map<String, String> parameters = Map.of(
                "text", text,
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
