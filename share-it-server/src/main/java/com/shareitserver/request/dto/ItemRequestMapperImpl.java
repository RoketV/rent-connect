package com.shareitserver.request.dto;

import com.shareitserver.request.ItemRequest;
import org.springframework.stereotype.Component;

@Component
public class ItemRequestMapperImpl implements ItemRequestMapper {
    @Override
    public ItemRequestOutputDto toDto(ItemRequest request) {
        if (request == null) {
            return null;
        }
        ItemRequestOutputDto dto = new ItemRequestOutputDto();
        dto.setId(request.getId());
        dto.setDescription(request.getDescription());
        dto.setCreated(request.getCreated());
        return dto;
    }

    @Override
    public ItemRequest toItemRequest(ItemRequestInputDto dto) {
        if (dto == null) {
            return null;
        }
        ItemRequest request = new ItemRequest();
        request.setDescription(dto.getDescription());
        request.setUser(dto.getOwner());
        return request;
    }
}
