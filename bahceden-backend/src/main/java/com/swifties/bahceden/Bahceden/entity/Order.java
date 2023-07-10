package com.swifties.bahceden.Bahceden.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "orders")
@Getter
@Setter
@NoArgsConstructor(force = true)
@RequiredArgsConstructor
@AllArgsConstructor
@ToString
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @NonNull
    @ManyToOne
    @JoinColumn(name = "customer_id")
    @JsonIgnore
    private Customer customer;

    @NonNull
    @ManyToOne
    @JoinColumn(name = "producer_id")
    private Producer producer;

    @NonNull
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @NonNull
    @Column(name = "status")
    private Integer status;

    @NonNull
    @Column(name = "amount")
    private Integer amount;

    @NonNull
    @Column(name = "date_of_purchase")
    private String dateOfPurchase;

    @NonNull
    @ManyToOne
    @JoinColumn(name = "delivery_address_id")
    private Address deliveryAddress;

    @NonNull
    @Column(name = "shipment_type")
    private Integer shipmentType;
}
