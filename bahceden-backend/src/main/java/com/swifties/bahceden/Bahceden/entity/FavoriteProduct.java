package com.swifties.bahceden.Bahceden.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "favorite_products")
@Getter
@Setter
@NoArgsConstructor(force = true)
@AllArgsConstructor
@ToString
public class FavoriteProduct {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @NonNull
    @Column(name = "customer_id")
    private int customerId;

    @NonNull
    @Column(name = "product_id")
    private  int productId;
}
