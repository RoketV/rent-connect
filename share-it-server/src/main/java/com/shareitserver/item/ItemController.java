package com.shareitserver.item;

import com.shareitserver.comments.dto.CommentInputDto;
import com.shareitserver.comments.dto.CommentOutputDto;
import com.shareitserver.item.dto.ItemInputDto;
import com.shareitserver.item.dto.ItemOutputDto;
import com.shareitserver.util.PaginationParams;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {

    private final ItemService itemService;

    @PostMapping
    public ResponseEntity<ItemOutputDto> addItem(@Valid @RequestBody ItemInputDto dto,
                                                 @RequestHeader("X-Sharer-User-Id") @NotNull Long userId) {
        return ResponseEntity.ok(itemService.addItem(dto, userId));
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<ItemOutputDto> updateItem(@RequestBody ItemInputDto dto,
                                                    @RequestHeader("X-Sharer-User-Id") @NotNull Long userId,
                                                    @PathVariable Long itemId) {
        return ResponseEntity.ok(itemService.updateItem(dto, userId, itemId));
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<ItemOutputDto> getItem(@RequestHeader("X-Sharer-User-Id") @NotNull Long ownerId,
                                                 @PathVariable Long itemId) {
        return ResponseEntity.ok(itemService.getItem(ownerId, itemId));
    }

    @GetMapping
    public ResponseEntity<List<ItemOutputDto>> getItems(@RequestHeader("X-Sharer-User-Id") @NotNull Long userId,
                                                        @Valid PaginationParams params) {
        return ResponseEntity.ok(itemService.getItems(userId, params));
    }

    @GetMapping("/search")
    public ResponseEntity<List<ItemOutputDto>> searchItem(@RequestParam(required = false) String text,
                                                          @Valid PaginationParams params) {
        return ResponseEntity.ok(itemService.searchItem(text, params));
    }

    @DeleteMapping("/{itemId}")
    public ResponseEntity<ItemOutputDto> deleteItem(@PathVariable Long itemId) {
        return ResponseEntity.ok(itemService.deleteItem(itemId));
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<CommentOutputDto> postComment(@Valid @RequestBody CommentInputDto dto,
                                                        @PathVariable Long itemId,
                                                        @RequestHeader("X-Sharer-User-Id") @NotNull Long userId) {
        return ResponseEntity.ok(itemService.addComment(dto, itemId, userId));
    }
}
