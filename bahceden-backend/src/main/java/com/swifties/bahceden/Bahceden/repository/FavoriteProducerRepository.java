package com.swifties.bahceden.Bahceden.repository;

import com.swifties.bahceden.Bahceden.entity.FavoriteProducer;
import com.swifties.bahceden.Bahceden.entity.FavoriteProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FavoriteProducerRepository extends JpaRepository<FavoriteProducer, Integer> {
    @Query("SELECT fp FROM FavoriteProducer fp WHERE fp.customerId = :customerId AND fp.producerId = :producerId")
    FavoriteProducer findFavoriteProducerByIds(@Param("customerId") int customerId, @Param("producerId") int producerId);
}
