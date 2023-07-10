package com.swifties.bahceden.Bahceden.DTO;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponseDTO {
    private int id;
    private Integer status;
    private Integer amount;
    private String dateOfPurchase;
    private Integer shipmentType;
}
