package ru.otus.project.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.otus.project.dto.authority.AuthorityDto;
import ru.otus.project.model.Address;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserNoPasswordDto {
    private long id;

    private String login;

    private String userName;

    private Address address;

    private List<AuthorityDto> authorities;
}
