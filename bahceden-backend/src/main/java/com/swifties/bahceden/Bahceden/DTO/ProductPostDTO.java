package com.swifties.bahceden.Bahceden.DTO;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ProductPostDTO {
    private int id;

    private String name;

    private String description;

    private Integer category;

    private Integer subCategory;

    private Integer unitType;

    private Double pricePerUnit;

    private Integer producer;

    private String imageURL;

    private Integer availableAmount;

}
