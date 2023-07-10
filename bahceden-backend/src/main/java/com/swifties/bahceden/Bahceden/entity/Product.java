package com.swifties.bahceden.Bahceden.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "products")
@Getter
@Setter
@NoArgsConstructor(force = true)
@RequiredArgsConstructor
@AllArgsConstructor
@ToString
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @NonNull
    @Column(name = "name")
    private String name;

    @NonNull
    @Column(name = "description")
    private String description;

    @NonNull
    @OneToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @NonNull
    @OneToOne
    @JoinColumn(name = "sub_category_id")
    private Category subCategory;

    @NonNull
    @Column(name = "unit_type")
    private Integer unitType;

    @NonNull
    @Column(name = "price_per_unit")
    private Double pricePerUnit;

    @NonNull
    @OneToOne
    @JoinColumn(name = "producer_id")
    private Producer producer;

    @Column(name = "image_url")
    private String imageURL;

    @OneToMany(mappedBy = "product")
    private Set<Comment> comments;

    @NonNull
    @Column(name = "available_amount")
    private Integer availableAmount;

    @Column(name = "rating", columnDefinition = "DOUBLE DEFAULT 0.0")
    private Double rating;
}
