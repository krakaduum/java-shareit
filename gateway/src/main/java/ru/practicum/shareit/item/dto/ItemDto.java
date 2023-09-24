package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@AllArgsConstructor
@Getter
@NoArgsConstructor
public class ItemDto {

    @NotBlank
    @Size(max = 255)
    String name;

    @NotBlank
    @Size(max = 1024)
    String description;

    @NotNull
    Boolean available;

    Long requestId;

}
