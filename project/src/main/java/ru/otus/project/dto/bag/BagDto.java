package ru.otus.project.dto.bag;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BagDto {
    private long id;

    private String typeOfWaste;
}
