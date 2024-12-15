package ru.otus.project.dto.cart;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.otus.project.dto.status.StatusDto;
import ru.otus.project.dto.bag.BagDto;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartStatusBagsDto {
    private long id;

    private StatusDto cartStatus;

    private long userId;

    private List<BagDto> bags;
}
