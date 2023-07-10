package com.swifties.bahceden.Bahceden.entity;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "comments")
@Getter
@Setter
@NoArgsConstructor (force = true)
@RequiredArgsConstructor
@AllArgsConstructor
@ToString
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @NonNull
    @Column(name = "product_id")
    private Integer product;

    @Column(name = "parent_id")
    private Integer parent;

    @NonNull
    @Column(name = "author_id")
    private Integer author;

    @Column(name = "count_of_likes")
    private int countOfLikes;

    @Column(name = "rating", columnDefinition = "DOUBLE DEFAULT 0.0")
    private Double rating;

    @NonNull
    @Column(name = "message")
    private String message;
}
