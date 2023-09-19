package ru.practicum.shareit.request.model;

import lombok.*;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import java.time.LocalDateTime;
import java.util.Set;

@AllArgsConstructor
@Data
@NoArgsConstructor
@Entity
@Table(name = "requests")
public class ItemRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    Long id;

    @NotBlank
    @NotNull
    @Column(name = "description", length = 1024)
    String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "requester_id")
    User requester;

    @PastOrPresent
    @Column(name = "created")
    LocalDateTime created;

    @OneToMany(mappedBy = "request")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Set<Item> items;

}
