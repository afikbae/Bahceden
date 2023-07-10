package com.swifties.bahceden.Bahceden.DTO;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequestDTO {
    private int id;
    private int customerId;
    private int producerId;
    private int productId;
    private Integer status;
    private Integer amount;
    private String dateOfPurchase;
    private int deliveryAddressId;
    private Integer shipmentType;
}
