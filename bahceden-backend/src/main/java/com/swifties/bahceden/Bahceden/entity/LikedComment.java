package com.swifties.bahceden.Bahceden.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "liked_comments")
@Getter
@Setter
@NoArgsConstructor(force = true)
@AllArgsConstructor
@RequiredArgsConstructor
@ToString
public class LikedComment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @NonNull
    @Column(name = "customer_id")
    private int customerId;

    @NonNull
    @Column(name = "comment_id")
    private  int commentId;
}
