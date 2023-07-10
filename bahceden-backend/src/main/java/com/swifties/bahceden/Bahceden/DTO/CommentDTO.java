package com.swifties.bahceden.Bahceden.DTO;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CommentDTO {
    private int id;

    private Integer product;

    private Integer parent;

    private CustomerMainInfoDTO author;

    private Integer countOfLikes;

    private String message;

    private String productName;

    private Double rating;
}
