package ru.practicum.shareit.item.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.item.model.Item;

public interface ItemRepository extends JpaRepository<Item, Long> {

    Page<Item> findAllByOwnerIdOrderByIdAsc(Long ownerId, Pageable pageable);

    @Query("SELECT i from Item i " +
            "WHERE (upper(i.name) LIKE upper(concat('%', ?1, '%')) OR upper(i.description) LIKE upper(concat('%', ?1, '%'))) " +
            "AND i.available = true")
    Page<Item> findAllAvailableItemsByNameOrDescription(String text, Pageable pageable);

}
