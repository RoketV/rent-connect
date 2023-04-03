package com.shareitserver.request.dto;

import com.shareitserver.request.ItemRequest;
import org.mapstruct.factory.Mappers;

public interface ItemRequestMapper {

    ItemRequestMapper ITEM_REQUEST_MAPPER = Mappers.getMapper(ItemRequestMapper.class);

    ItemRequestOutputDto toDto(ItemRequest request);

    ItemRequest toItemRequest(ItemRequestInputDto dto);
}
