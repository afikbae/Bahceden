package com.swifties.bahceden.Bahceden.DTO;

import com.swifties.bahceden.Bahceden.entity.Category;
import com.swifties.bahceden.Bahceden.entity.Producer;
import lombok.*;

import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ProductDTO {

    private int id;

    private String name;

    private String description;

    private Category category;

    private Integer unitType;

    private Double pricePerUnit;

    private Producer producer;

    private String imageURL;

    private List<CommentDTO> comments;

    private Integer availableAmount;

    private Double rating;
}
