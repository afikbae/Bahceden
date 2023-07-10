package com.swifties.bahceden.Bahceden.service;

import com.swifties.bahceden.Bahceden.entity.ScrapedData;

import java.util.List;

public interface ScrapedDataService {
    public void saveAll();

    List<ScrapedData> getScrapedDataByCategoryId(Integer categoryId);

}
