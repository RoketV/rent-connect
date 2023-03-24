package com.shareitserver.request;

import com.shareitserver.request.dto.ItemRequestInputDto;
import com.shareitserver.request.dto.ItemRequestOutputDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/requests")
public class ItemRequestController {

    private final ItemRequestService itemRequestService;

    @PostMapping
    public ResponseEntity<ItemRequestOutputDto> postRequest(@Valid @RequestBody ItemRequestInputDto dto,
                                                            @RequestHeader("X-Sharer-User-Id") @NotNull Long userId) {
        return ResponseEntity.ok(itemRequestService.addRequest(dto, userId));
    }

    @GetMapping
    public ResponseEntity<List<ItemRequestOutputDto>> getRequestsByOwner(
            @RequestHeader("X-Sharer-User-Id") @NotNull Long userId) {
        return ResponseEntity.ok(itemRequestService.getItemRequestWithItemsResponse(userId));
    }

    @GetMapping("{requestId}")
    public ResponseEntity<ItemRequestOutputDto> getRequestsById(@PathVariable Long requestId,
                                                                @RequestHeader("X-Sharer-User-Id") @NotNull Long userId) {
        return ResponseEntity.ok(itemRequestService.getItemRequest(requestId, userId));
    }

    @GetMapping("/all")
    public ResponseEntity<List<ItemRequestOutputDto>> getRequestsWithPagination(
            @RequestHeader("X-Sharer-User-Id") @NotNull Long userId,
            ItemRequestPaginationParams paginationParams) {
        return ResponseEntity.ok(itemRequestService.getItemRequestWithPagination(userId, paginationParams));
    }
}
