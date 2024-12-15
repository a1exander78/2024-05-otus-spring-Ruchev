package ru.otus.project.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRequestDto {
    private long id;

    @NotBlank(message = "This field should not be blank")
    @Size(max = 32, message = "This field should has expected size")
    private String login;

    @NotBlank(message = "This field should not be blank")
    @Size(max = 128, message = "This field should has expected size")
    private String rawPassword;

    @NotBlank(message = "This field should not be blank")
    @Size(max = 255, message = "This field should has expected size")
    private String district;

    @NotBlank(message = "This field should not be blank")
    @Size(max = 255, message = "This field should has expected size")
    private String street;

    @NotBlank(message = "This field should not be blank")
    @Size(max = 255, message = "This field should has expected size")
    private String streetNumber;

    @NotBlank(message = "This field should not be blank")
    @Size(max = 255, message = "This field should has expected size")
    private String flatNumber;

    @NotNull
    private List<Long> authorities;
}
