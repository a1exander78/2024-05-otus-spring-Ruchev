package ru.otus.project.dto.cart;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.otus.project.dto.user.UserInfoDto;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartUserDto {
    private long id;

    private long cartStatusId;

    private UserInfoDto user;
}
