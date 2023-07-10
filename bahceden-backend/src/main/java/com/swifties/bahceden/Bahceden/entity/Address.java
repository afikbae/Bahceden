package com.swifties.bahceden.Bahceden.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "addresses")
@Getter
@Setter
@NoArgsConstructor(force = true)
@RequiredArgsConstructor
@AllArgsConstructor
@ToString
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @NonNull
    @Column(name = "title")
    private String title;

    @NonNull
    @Column(name = "full_address")
    private String fullAddress;

    @NonNull
    @Column(name = "phone_number")
    private String phoneNumber;

    @NonNull
    @Column(name = "customer_id")
    private Integer customerId;
}
