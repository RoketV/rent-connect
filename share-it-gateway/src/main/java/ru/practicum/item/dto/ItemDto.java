package ru.practicum.item.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.practicum.itemRequest.ItemRequestDto;
import ru.practicum.user.dto.UserDto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Getter
@Setter
@ToString
@JsonRootName("item")
public class ItemDto {

    private Long id;

    @NotBlank
    private String name;

    @NotBlank
    private String description;

    @NotNull
    private Boolean available;

    @JsonProperty("user")
    private UserDto user;

    private ItemRequestDto request;

    public ItemDto(Long id, String name, String description, Boolean available, UserDto user) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.available = available;
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ItemDto)) return false;
        ItemDto itemDto = (ItemDto) o;
        return Objects.equals(getId(), itemDto.getId()) && Objects.equals(getName(), itemDto.getName()) && Objects.equals(getDescription(), itemDto.getDescription()) && Objects.equals(getAvailable(), itemDto.getAvailable()) && Objects.equals(getUser(), itemDto.getUser());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getDescription(), getAvailable(), getUser());
    }
}