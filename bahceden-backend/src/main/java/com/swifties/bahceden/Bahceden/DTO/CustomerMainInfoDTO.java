package com.swifties.bahceden.Bahceden.DTO;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CustomerMainInfoDTO {
    private int id;

    private String name;

    private String email;

    private String profileImageURL;
}
