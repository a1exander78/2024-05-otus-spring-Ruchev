package ru.otus.project.dto.address;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddressRequestDto {
    @NotBlank(message = "This field should not be blank")
    @Size(max = 255, message = "This field should has expected size")
    private String street;

    @NotBlank(message = "This field should not be blank")
    @Size(max = 255, message = "This field should has expected size")
    private String streetNumber;
}
