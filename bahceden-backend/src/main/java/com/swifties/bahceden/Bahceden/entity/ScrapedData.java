package com.swifties.bahceden.Bahceden.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.context.annotation.Primary;

enum unit {KG, G, PIECE}

@Entity
@Table(name = "scraped_data")
@Getter
@Setter
@NoArgsConstructor (force = true)
@AllArgsConstructor
@RequiredArgsConstructor
@ToString
public class ScrapedData {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column (name = "id")
    private int id;

    @NonNull
    @Column (name = "name")
    private String name;

    @NonNull
    @Column (name = "unit")
    private int unit;

    @NonNull
    @Column (name = "min_price")
    private double minPrice;

    @NonNull
    @Column (name = "max_price")
    private double maxPrice;

    @Column(name = "category_id")
    private Integer categoryId;
}
