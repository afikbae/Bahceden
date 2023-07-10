package com.swifties.bahceden.Bahceden.service;

import com.swifties.bahceden.Bahceden.data.A101Scraper;
import com.swifties.bahceden.Bahceden.data.HallScraper;
import com.swifties.bahceden.Bahceden.entity.ScrapedData;
import com.swifties.bahceden.Bahceden.entity.Category;
import com.swifties.bahceden.Bahceden.repository.CategoryRepository;
import com.swifties.bahceden.Bahceden.repository.ScrapedDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ScrapedDataServiceImpl implements ScrapedDataService{

    private ScrapedDataRepository scrapedDataRepository;
    private HallScraper hallScraper;
    private CategoryRepository categoryRepository;

    @Autowired
    public ScrapedDataServiceImpl(ScrapedDataRepository scrapedDataRepository, HallScraper hallScraper, CategoryRepository categoryRepository, CategoryRepository categoryRepository1) {
        this.scrapedDataRepository = scrapedDataRepository;
        this.hallScraper = hallScraper;
        this.categoryRepository = categoryRepository1;
    }

    @Override
    @Scheduled (fixedRate = 3600000)
    public void saveAll() {
        List<ScrapedData> scrapedData = new ArrayList<>();
        scrapedData.addAll(new A101Scraper().Scrape().stream().filter(p -> p.getName().toLowerCase().contains("süt") && p.getName().toLowerCase().contains("1 l")).map(A101Scraper.Product::getAsScraped).toList());
        List<ScrapedData> hallItems = hallScraper.scrape();
        hallItems.forEach(d -> d.setCategoryId(2));
        scrapedData.addAll(hallItems);

        for (ScrapedData data : scrapedData) {
            Optional<Category> category = categoryRepository.findAllSubCategories().stream()
                    .filter(c -> c.getName().equalsIgnoreCase(data.getName())).findFirst();

            if (category.isPresent())
            {
                data.setCategoryId(category.get().getId());
            }
            else
            {
                Category subCategory = new Category(0, data.getName(), data.getCategoryId());
                Category savedCategory = categoryRepository.save(subCategory);
                data.setCategoryId(savedCategory.getId());
            }
        }

        scrapedDataRepository.saveAll(scrapedData);
    }

    @Override
    public List<ScrapedData> getScrapedDataByCategoryId(Integer categoryId) {
        return scrapedDataRepository.findByCategoryId(categoryId);
    }
}
