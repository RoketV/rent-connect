package ru.practicum.itemRequest;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.practicum.user.dto.UserDto;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.Objects;


@Getter
@Setter
@ToString
@JsonRootName("itemRequest")
public class ItemRequestDto {
    private Long id;

    @NotBlank
    private String description;

    private LocalDateTime created;

    @SerializedName("user")
    @JsonProperty("user")
    private UserDto user;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ItemRequestDto)) return false;
        ItemRequestDto request = (ItemRequestDto) o;
        return Objects.equals(getId(), request.getId()) && Objects.equals(getDescription(), request.getDescription()) && Objects.equals(getCreated(), request.getCreated()) && Objects.equals(getUser(), request.getUser());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getDescription(), getCreated(), getUser());
    }
}
