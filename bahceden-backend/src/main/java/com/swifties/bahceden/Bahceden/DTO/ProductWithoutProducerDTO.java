package com.swifties.bahceden.Bahceden.DTO;

import com.swifties.bahceden.Bahceden.entity.Category;
import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ProductWithoutProducerDTO {

    private int id;

    private String name;

    private String description;

    private Category category;

    private Integer unitType;

    private Double pricePerUnit;

    private String imageURL;

    private Integer availableAmount;

    private Double rating;
}
