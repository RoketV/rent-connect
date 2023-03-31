package ru.practicum.item;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.practicum.itemRequest.ItemRequest;
import ru.practicum.user.User;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Getter
@Setter
@ToString
public class Item {

    private Long id;

    @NotBlank
    private String name;

    @NotBlank
    private String description;

    @NotNull
    private Boolean available;

    private User user;

    private ItemRequest request;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Item)) return false;
        Item item = (Item) o;
        return Objects.equals(getId(), item.getId()) && Objects.equals(getName(), item.getName()) && Objects.equals(getDescription(), item.getDescription()) && Objects.equals(getAvailable(), item.getAvailable()) && Objects.equals(getUser(), item.getUser());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getDescription(), getAvailable(), getUser());
    }
}