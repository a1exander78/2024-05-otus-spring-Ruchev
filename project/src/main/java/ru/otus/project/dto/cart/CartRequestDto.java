package ru.otus.project.dto.cart;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartRequestDto {
    private long id;

    private long statusId;

    private long userId;

    private long bagId;
}
