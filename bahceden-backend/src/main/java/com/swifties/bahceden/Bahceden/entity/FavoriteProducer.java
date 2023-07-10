package com.swifties.bahceden.Bahceden.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "favorite_producers")
@Getter
@Setter
@NoArgsConstructor(force = true)
@AllArgsConstructor
@ToString
public class FavoriteProducer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @NonNull
    @Column(name = "customer_id")
    private int customerId;

    @NonNull
    @Column(name = "producer_id")
    private  int producerId;
}
