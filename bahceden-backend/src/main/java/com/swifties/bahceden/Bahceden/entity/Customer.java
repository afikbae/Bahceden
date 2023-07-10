package com.swifties.bahceden.Bahceden.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "customers")
@Getter
@Setter
@NoArgsConstructor(force = true)
@RequiredArgsConstructor
@AllArgsConstructor
@ToString
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @NonNull
    @Column(name = "name")
    private String name;

    @NonNull
    @Column(name = "email")
    private String email;

    @NonNull
    @Column(name = "profile_image_url")
    private String profileImageURL;

    @NonNull
    @OneToMany(mappedBy = "customerId")
    private List<Address> addresses;

    @ManyToMany
    @JoinTable(
            name = "favorite_products",
            joinColumns = @JoinColumn(name = "customer_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id")
    )
    private Set<Product> favoriteProducts = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "favorite_producers",
            joinColumns = @JoinColumn(name = "customer_id"),
            inverseJoinColumns = @JoinColumn(name = "producer_id")
    )
    private Set<Producer> favoriteProducers = new HashSet<>();

    @OneToMany(mappedBy = "customer")
    private Set<Order> orders = new HashSet<>();
}
