package com.shareitserver.request;

import com.shareitserver.exceptions.EntityNotFoundException;
import com.shareitserver.item.ItemRepository;
import com.shareitserver.item.dto.ItemMapper;
import com.shareitserver.item.dto.ItemOutputDto;
import com.shareitserver.request.dto.ItemRequestInputDto;
import com.shareitserver.request.dto.ItemRequestMapper;
import com.shareitserver.request.dto.ItemRequestOutputDto;
import com.shareitserver.user.UserRepositoryServer;
import com.shareitserver.user.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ItemRequestService {

    private final ItemRequestRepository itemRequestRepository;
    private final UserRepositoryServer userRepositoryServer;
    private final ItemRepository itemRepository;

    public ItemRequestOutputDto addRequest(ItemRequestInputDto dto, Long userId) {
        if (!userExists(userId)) {
            throw new EntityNotFoundException(String.format("there is no user with id %d to make a itemRequest", userId));
        }
        User owner = userRepositoryServer.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format("there is no user with id %d to make a itemRequest", userId)));
        dto.setOwner(owner);
        ItemRequest request = ItemRequestMapper.ITEM_REQUEST_MAPPER.toItemRequest(dto);
        request.setCreated(LocalDateTime.now());
        return ItemRequestMapper.ITEM_REQUEST_MAPPER.toDto(itemRequestRepository.save(request));
    }

    public List<ItemRequestOutputDto> getItemRequestWithItemsResponse(Long userId) {
        if (userRepositoryServer.findById(userId).isEmpty()) {
            throw new EntityNotFoundException(String.format("there is no user with id %d", userId));
        }
        List<ItemRequestOutputDto> requests = itemRequestRepository.findAllByUser_Id(userId)
                .stream()
                .map(ItemRequestMapper.ITEM_REQUEST_MAPPER::toDto)
                .collect(Collectors.toList());
        setItemsForItemRequestsDto(requests);
        return requests;
    }

    public ItemRequestOutputDto getItemRequest(Long itemId, Long userId) {
        if (userRepositoryServer.findById(userId).isEmpty()) {
            throw new EntityNotFoundException(String.format("there is no user with id %d", userId));
        }
        ItemRequest request = itemRequestRepository.findById(itemId)
                .orElseThrow(() -> new EntityNotFoundException(String.format("there is no item with id %d", itemId)));
        ItemRequestOutputDto dto = ItemRequestMapper.ITEM_REQUEST_MAPPER.toDto(request);
        List<ItemOutputDto> items = itemRepository.findItemsByRequestId(itemId)
                .stream()
                .map(ItemMapper.ITEM_MAPPER::toDto)
                .collect(Collectors.toList());
        dto.setItems(items);
        return dto;
    }

    public List<ItemRequestOutputDto> getItemRequestWithPagination(Long userId, ItemRequestPaginationParams params) {
        if (userRepositoryServer.findById(userId).isEmpty()) {
            throw new EntityNotFoundException(String.format("there is no user with id %d", userId));
        }
        Page<ItemRequest> page = itemRequestRepository.findAllExceptUser(
                userId, PageRequest.of(params.from(), params.size()));
        List<ItemRequestOutputDto> requests = page.getContent().stream()
                .map(ItemRequestMapper.ITEM_REQUEST_MAPPER::toDto)
                .collect(Collectors.toList());
        setItemsForItemRequestsDto(requests);
        return requests;
    }

    private boolean userExists(Long userId) {
        return userRepositoryServer.findById(userId).isPresent();
    }

    private void setItemsForItemRequestsDto(List<ItemRequestOutputDto> requests) {
        List<ItemOutputDto> items = itemRepository.findItemsWithRequest()
                .stream()
                .map(ItemMapper.ITEM_MAPPER::toDto)
                .collect(Collectors.toList());
        for (ItemRequestOutputDto request : requests) {
            request.setItems(items
                    .stream()
                    .filter(item -> item.getRequestId().equals(request.getId()))
                    .collect(Collectors.toList()));
        }
    }
}
