package com.shareitserver.item.model;

import com.shareitserver.request.ItemRequest;
import com.shareitserver.user.model.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import java.util.Objects;

@Entity
@Getter
@Setter
@ToString
@Table(name = "items", schema = "public")
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name")
    @NotBlank
    private String name;
    @Column(name = "description")
    @NotBlank
    private String description;
    @Column(name = "is_available")
    @NotNull
    private Boolean available;
    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User user;
    @ManyToOne
    @JoinColumn(name = "request_id")
    private ItemRequest request;

    public Item() {
    }

    public Item(String name, String description, Boolean available, User user) {
        this.name = name;
        this.description = description;
        this.available = available;
        this.user = user;
    }

    public Item(Long id, String name, String description, Boolean available, User user, ItemRequest request) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.available = available;
        this.user = user;
        this.request = request;
    }

    public Item(String name, String description, Boolean available, User user, ItemRequest request) {
        this.name = name;
        this.description = description;
        this.available = available;
        this.user = user;
        this.request = request;
    }

    public Item(Long id, String name, String description, Boolean available, User user) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.available = available;
        this.user = user;
    }

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
