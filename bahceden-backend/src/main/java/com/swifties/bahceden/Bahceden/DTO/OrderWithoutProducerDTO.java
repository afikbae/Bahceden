package com.swifties.bahceden.Bahceden.DTO;

import com.swifties.bahceden.Bahceden.entity.Address;
import com.swifties.bahceden.Bahceden.entity.Customer;
import com.swifties.bahceden.Bahceden.entity.Producer;
import com.swifties.bahceden.Bahceden.entity.Product;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class OrderWithoutProducerDTO {
    private int id;

    private CustomerMainInfoDTO customer;

    private ProductWithoutProducerDTO product;

    private Integer status;

    private Integer amount;

    private String dateOfPurchase;

    private Address deliveryAddress;

    private Integer shipmentType;
}
