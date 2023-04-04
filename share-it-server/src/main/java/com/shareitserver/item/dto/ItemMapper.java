package com.shareitserver.item.dto;

import com.shareitserver.item.model.Item;
import org.mapstruct.factory.Mappers;


public interface ItemMapper {

    ItemMapper ITEM_MAPPER = Mappers.getMapper(ItemMapper.class);

    ItemOutputDto toDto(Item item);

    Item toItem(ItemInputDto dto);
}
