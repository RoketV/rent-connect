package ru.practicum.shareit.item;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {
    Page<Item> findAllByUser_IdOrderByIdAsc(Long ownerId, Pageable pageable);

    @Query("SELECT i FROM Item i WHERE i.request IS NOT NULL")
    List<Item> findItemsWithRequest();

    @Query("SELECT i FROM Item i WHERE i.request.id = ?1")
    List<Item> findItemsByRequestId(Long requestId);
}
