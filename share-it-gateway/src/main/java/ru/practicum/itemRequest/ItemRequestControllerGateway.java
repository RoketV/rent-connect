package ru.practicum.itemRequest;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.itemRequest.dto.ItemRequestInputDto;
import ru.practicum.itemRequest.dto.ItemRequestOutputDto;
import ru.practicum.util.PaginationParams;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/requests")
public class ItemRequestControllerGateway {

    private final ItemRequestClient itemRequestClient;

    @PostMapping
    public ResponseEntity<ItemRequestOutputDto> postRequest(@Valid @RequestBody ItemRequestInputDto dto,
                                                            @RequestHeader("X-Sharer-User-Id") @NotNull Long userId) {
        return itemRequestClient.addRequest(dto, userId);
    }

    @GetMapping
    public ResponseEntity<List<ItemRequestOutputDto>> getRequestsByOwner(
            @RequestHeader("X-Sharer-User-Id") @NotNull Long userId) {
        return itemRequestClient.getItemRequestWithItemsResponse(userId);
    }

    @GetMapping("{requestId}")
    public ResponseEntity<ItemRequestOutputDto> getRequestsById(@PathVariable Long requestId,
                                                                @RequestHeader("X-Sharer-User-Id") @NotNull Long userId) {
        return itemRequestClient.getItemRequest(requestId, userId);
    }

    @GetMapping("/all")
    public ResponseEntity<List<ItemRequestOutputDto>> getRequestsWithPagination(
            @RequestHeader("X-Sharer-User-Id") @NotNull Long userId,
            PaginationParams paginationParams) {
        return itemRequestClient.getItemRequestWithPagination(userId, paginationParams);
    }
}
