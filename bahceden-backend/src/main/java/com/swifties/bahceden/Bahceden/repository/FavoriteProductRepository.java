package com.swifties.bahceden.Bahceden.repository;

import com.swifties.bahceden.Bahceden.entity.FavoriteProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FavoriteProductRepository extends JpaRepository<FavoriteProduct, Integer> {

    @Query("SELECT fp FROM FavoriteProduct fp WHERE fp.customerId = :customerId AND fp.productId = :productId")
    FavoriteProduct findFavoriteProductByIds(@Param("customerId") int customerId, @Param("productId") int productId);
}
