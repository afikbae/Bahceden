package com.swifties.bahceden.Bahceden.repository;

import com.swifties.bahceden.Bahceden.entity.Product;
import com.swifties.bahceden.Bahceden.entity.ScrapedData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScrapedDataRepository extends JpaRepository<ScrapedData, Integer> {
    @Query("SELECT sd FROM ScrapedData sd WHERE sd.categoryId = :subCategoryId")
    List<ScrapedData> findBySubCategoryAll(@Param("subCategoryId") int subCategoryId);

    List<ScrapedData> findByCategoryId(Integer categoryId);
}
