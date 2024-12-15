package ru.otus.project;

import ru.otus.project.dto.address.AddressRequestDto;
import ru.otus.project.dto.authority.AuthorityDto;
import ru.otus.project.dto.bag.BagDto;
import ru.otus.project.dto.cart.CartDto;
import ru.otus.project.dto.cart.CartRequestDto;
import ru.otus.project.dto.cart.CartStatusBagsDto;
import ru.otus.project.dto.cart.CartUserDto;
import ru.otus.project.dto.status.StatusDto;
import ru.otus.project.dto.user.UserAuthoritiesDto;
import ru.otus.project.dto.user.UserDto;
import ru.otus.project.dto.user.UserInfoDto;
import ru.otus.project.dto.user.UserNoPasswordDto;
import ru.otus.project.dto.user.UserRequestDto;
import ru.otus.project.model.Address;


import java.util.List;

public abstract class TestData {
    protected static final long ID_1 = 1L;
    protected static final long ID_2 = 2L;
    protected static final long ID_3 = 3L;
    protected static final long ID_4 = 4L;
    protected static final long ID_5 = 5L;
    protected static final long ID_6 = 6L;

    protected static final StatusDto STATUS_NEW = new StatusDto(ID_1, "NEW");
    protected static final StatusDto STATUS_IN_PROCESSING = new StatusDto(ID_2, "IN_PROCESSING");
    protected static final StatusDto STATUS_PROCESSED = new StatusDto(ID_3, "PROCESSED");

    protected static final BagDto BAG_GLASS = new BagDto(ID_1, "GLASS");
    protected static final BagDto BAG_PAPER = new BagDto(ID_2, "PAPER");
    protected static final BagDto BAG_PLASTIC = new BagDto(ID_3, "PLASTIC");
    protected static final BagDto BAG_MIXED = new BagDto(ID_4, "MIXED");

    protected static final AuthorityDto AUTHORITY_ADMIN = new AuthorityDto(ID_1, "ROLE_ADMIN");
    protected static final AuthorityDto AUTHORITY_USER = new AuthorityDto(ID_2, "ROLE_USER");
    protected static final AuthorityDto AUTHORITY_DRIVER = new AuthorityDto(ID_3, "ROLE_DRIVER");

    protected static final Address EMPTY = new Address("empty", "empty", "empty", "empty");
    protected static final Address LENINA_1_1 = new Address("central", "lenina", "1", "1");
    protected static final Address LENINA_1_2 = new Address("central", "lenina", "1", "2");
    protected static final Address PUSHKINA_2_11 = new Address("central", "pushkina", "2", "11");
    protected static final Address PUSHKINA_2_22 = new Address("central", "pushkina", "2", "22");

    protected static final AddressRequestDto LENINA_1 = new AddressRequestDto("lenina", "1");

    protected static final Address AYVAZOVSKOGO_13_13= new Address("south", "ayvazovskogo", "13", "13");

    protected static final UserDto ADMIN = new UserDto(ID_1, "admin", "$2a$12$/b.1Rppvlu3zearhr6qE0.e9dwPmSRzC6mirknKWlO1zQCDquKZXi", "admin",  EMPTY, List.of(AUTHORITY_ADMIN, AUTHORITY_USER));
    protected static final UserDto USER_SASHA = new UserDto(ID_2, "1_1", "$2a$12$TTmL9DtXP5EvS/6f9Omm4eRUShdv4bYYw6fk3qhJGj0kWsf.FKXEq", "sasha", LENINA_1_1, List.of(AUTHORITY_USER));
    protected static final UserDto USER_DASHA = new UserDto(ID_3, "1_2", "$2a$12$Ek2X2Uuw5n1xASEyObB7..7P/ykXRKIiKoX4cKLU0dwwxoAuUuYT.", "dasha", LENINA_1_2, List.of(AUTHORITY_USER));
    protected static final UserDto USER_MISHA = new UserDto(ID_4, "2_11", "$2a$12$kWhARHzNk3LZmktVaUpkZe5kbTF1V6FKivqNW7qvcdpVGBbZ1ul7m", "misha", PUSHKINA_2_11, List.of(AUTHORITY_USER));
    protected static final UserDto USER_MASHA = new UserDto(ID_5, "2_22", "$2a$12$M4thDAGuPNoCbwhhMWp58OlBt6ICpBYyYge749fZVF2sjExIFnmra", "masha", PUSHKINA_2_22, List.of(AUTHORITY_USER));

    protected static final UserNoPasswordDto ADMIN_NO_PASSWORD = new UserNoPasswordDto(ID_1, "admin", "admin",  EMPTY, List.of(AUTHORITY_ADMIN, AUTHORITY_USER));
    protected static final UserNoPasswordDto USER_SASHA_NO_PASSWORD = new UserNoPasswordDto(ID_2, "1_1", "sasha", LENINA_1_1, List.of(AUTHORITY_USER));
    protected static final UserNoPasswordDto USER_DASHA_NO_PASSWORD = new UserNoPasswordDto(ID_3, "1_2", "dasha", LENINA_1_2, List.of(AUTHORITY_USER));
    protected static final UserNoPasswordDto USER_MISHA_NO_PASSWORD = new UserNoPasswordDto(ID_4, "2_11", "misha", PUSHKINA_2_11, List.of(AUTHORITY_USER));
    protected static final UserNoPasswordDto USER_MASHA_NO_PASSWORD = new UserNoPasswordDto(ID_5, "2_22", "masha", PUSHKINA_2_22, List.of(AUTHORITY_USER));

    protected static final UserInfoDto ADMIN_INFO = new UserInfoDto(ID_1, "admin", "admin",  EMPTY);
    protected static final UserInfoDto USER_SASHA_INFO = new UserInfoDto(ID_2, "1_1", "sasha", LENINA_1_1);
    protected static final UserInfoDto USER_DASHA_INFO = new UserInfoDto(ID_3, "1_2", "dasha", LENINA_1_2);
    protected static final UserInfoDto USER_MISHA_INFO = new UserInfoDto(ID_4, "2_11", "misha", PUSHKINA_2_11);
    protected static final UserInfoDto USER_MASHA_INFO = new UserInfoDto(ID_5, "2_22", "masha", PUSHKINA_2_22);

    protected static final UserRequestDto USER_GRISHA_ADDRESS = new UserRequestDto(ID_6, "2_33", "2_33", "central", "pushkina", "2", "33", List.of(ID_2));

    protected static final UserAuthoritiesDto USER_SASHA_NEW_AUTHORITIES = new UserAuthoritiesDto(ID_2, List.of(ID_1, ID_2));

    protected static final CartDto CART_1 = new CartDto(ID_1, STATUS_IN_PROCESSING, USER_SASHA_INFO, List.of(BAG_GLASS, BAG_PAPER));
    protected static final CartDto CART_2 = new CartDto(ID_2, STATUS_IN_PROCESSING, USER_DASHA_INFO, List.of(BAG_GLASS, BAG_PAPER));
    protected static final CartDto CART_3 = new CartDto(ID_3, STATUS_NEW, USER_MISHA_INFO, List.of(BAG_GLASS, BAG_PAPER, BAG_PLASTIC, BAG_PLASTIC));
    protected static final CartDto CART_4 = new CartDto(ID_4, STATUS_NEW, USER_MASHA_INFO, List.of(BAG_PLASTIC, BAG_MIXED));
    protected static final CartDto CART_5 = new CartDto(ID_5, STATUS_PROCESSED, USER_DASHA_INFO, List.of(BAG_PLASTIC));

    protected static final CartStatusBagsDto CART_STATUS_BAGS_1 = new CartStatusBagsDto(ID_1, STATUS_IN_PROCESSING, ID_2, List.of(BAG_GLASS, BAG_PAPER));
    protected static final CartStatusBagsDto CART_STATUS_BAGS_2 = new CartStatusBagsDto(ID_2, STATUS_IN_PROCESSING, ID_3, List.of(BAG_GLASS, BAG_PAPER));
    protected static final CartStatusBagsDto CART_STATUS_BAGS_3 = new CartStatusBagsDto(ID_3, STATUS_NEW, ID_4, List.of(BAG_GLASS, BAG_PAPER, BAG_PLASTIC, BAG_PLASTIC));
    protected static final CartStatusBagsDto CART_STATUS_BAGS_4 = new CartStatusBagsDto(ID_4, STATUS_NEW, ID_5, List.of(BAG_PLASTIC, BAG_MIXED));
    protected static final CartStatusBagsDto CART_STATUS_BAGS_5 = new CartStatusBagsDto(ID_5, STATUS_PROCESSED, ID_3, List.of(BAG_PLASTIC));

    protected static final CartUserDto CART_USER_1 = new CartUserDto(ID_1, ID_2, USER_SASHA_INFO);
    protected static final CartUserDto CART_USER_2 = new CartUserDto(ID_2, ID_2, USER_DASHA_INFO);
    protected static final CartUserDto CART_USER_3 = new CartUserDto(ID_3, ID_1, USER_MISHA_INFO);
    protected static final CartUserDto CART_USER_4 = new CartUserDto(ID_4, ID_1, USER_MASHA_INFO);
    protected static final CartUserDto CART_USER_5 = new CartUserDto(ID_5, ID_3, USER_DASHA_INFO);

    protected static final CartRequestDto NEW_CART = new CartRequestDto(0, ID_1, ID_2, ID_3);
}
