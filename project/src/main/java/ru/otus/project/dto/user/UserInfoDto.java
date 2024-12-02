package ru.otus.project.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.otus.project.model.Address;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserInfoDto {
    private long id;

    private String login;

    private String userName;

    private Address address;
}
