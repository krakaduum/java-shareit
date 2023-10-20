package ru.practicum.shareit.request.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@AllArgsConstructor
@Getter
@NoArgsConstructor
public class ItemRequestDto {

    @NotBlank
    @Size(max = 1024)
    String description;

}
