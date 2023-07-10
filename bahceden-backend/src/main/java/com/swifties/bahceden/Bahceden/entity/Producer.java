package com.swifties.bahceden.Bahceden.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "producers")
@Getter
@Setter
@NoArgsConstructor(force = true)
@RequiredArgsConstructor
@AllArgsConstructor
@ToString
public class Producer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @NonNull
    @Column(name = "name")
    private String name;

    @NonNull
    @Column(name = "shop_name")
    private String shopName;

    @NonNull
    @Column(name = "email")
    private String email;

    @NonNull
    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "profile_image_url")
    private String profileImageURL;

    @Column(name = "background_image_url")
    private String backgroundImageURL;

    @Column(name = "rating")
    private Double rating;
}
