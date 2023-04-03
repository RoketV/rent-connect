package ru.practicum.itemRequest;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.practicum.user.User;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.Objects;


@Getter
@Setter
@ToString
public class ItemRequest {
    private Long id;

    @NotBlank
    private String description;

    private LocalDateTime created;

    private User user;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ItemRequest)) return false;
        ItemRequest request = (ItemRequest) o;
        return Objects.equals(getId(), request.getId()) && Objects.equals(getDescription(), request.getDescription()) && Objects.equals(getCreated(), request.getCreated()) && Objects.equals(getUser(), request.getUser());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getDescription(), getCreated(), getUser());
    }
}
