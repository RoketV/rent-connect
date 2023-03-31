package ru.practicum.item;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.item.dto.ItemInputDto;
import ru.practicum.item.dto.ItemOutputDto;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemControllerGateway {

    private final ItemClient itemClient;

    @PostMapping
    public ResponseEntity<ItemOutputDto> addItem(@Valid @RequestBody ItemInputDto dto,
                                                 @RequestHeader("X-Sharer-User-Id") @NotNull Long userId) {
        return itemClient.addItem(dto, userId);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<ItemOutputDto> updateItem(@RequestBody ItemInputDto dto,
                                                    @RequestHeader("X-Sharer-User-Id") @NotNull Long userId,
                                                    @PathVariable Long itemId) {
        return itemClient.updateItem(dto, userId, itemId);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<ItemOutputDto> getItem(@RequestHeader("X-Sharer-User-Id") @NotNull Long ownerId,
                                                 @PathVariable Long itemId) {
        return itemClient.getItem(ownerId, itemId);
    }

    @GetMapping
    public ResponseEntity<List<ItemOutputDto>> getItems(@RequestHeader("X-Sharer-User-Id") @NotNull Long userId,
                                                        @Valid ItemPaginationParams params) {
        return itemClient.getItems(userId, params);
    }

    @GetMapping("/search")
    public ResponseEntity<List<ItemOutputDto>> searchItem(@RequestParam(required = false) String text,
                                                          @Valid ItemPaginationParams params) {
        return itemClient.searchItem(text, params);
    }

    @DeleteMapping("/{itemId}")
    public ResponseEntity<ItemOutputDto> deleteItem(@PathVariable Long itemId) {
        return itemClient.deleteItem(itemId);
    }

//    @PostMapping("/{itemId}/comment")
//    public ResponseEntity<CommentOutputDto> postComment(@Valid @RequestBody CommentInputDto dto,
//                                                        @PathVariable Long itemId,
//                                                        @RequestHeader("X-Sharer-User-Id") @NotNull Long userId) {
//        return itemClient.addComment(dto, itemId, userId);
//    }
}