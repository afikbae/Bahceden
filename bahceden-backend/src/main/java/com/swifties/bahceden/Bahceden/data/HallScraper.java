package com.swifties.bahceden.Bahceden.data;

import com.swifties.bahceden.Bahceden.entity.ScrapedData;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class HallScraper {
    Document doc;

    String[] selectedProducts = new String[]{"Armut", "Biber", "Domates", "Erik", "Fasulye", "Havuç", "Soğan"};

    public HallScraper() {
        try {
        doc = Jsoup.connect("https://www.ankara.bel.tr/hal-fiyatlari")
                .get();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public List<ScrapedData> scrape() {
        Elements rows = doc.select("table").first().select("tr:gt(0)");

        List<ScrapedData> dataList = new ArrayList<>();

        for (Element row : rows) {
            // select the cells from the current row
            Elements cells = row.select("td");

            String name = cells.get(0).text();
            name = name.split(" ")[0];

            if (Arrays.asList(selectedProducts).contains(name)) {
                dataList.add(new ScrapedData(name,
                        unitType(cells.get(1).text()),
                        Double.parseDouble(cells.get(2).text().substring(0, cells.get(2).text().indexOf(','))),
                        Double.parseDouble(cells.get(3).text().substring(0, cells.get(3).text().indexOf(',')))
                ));
            }
        }

        return dataList;
    }

    private int unitType(String unit) {
        if (unit.equals("kg"))
            return 1;
        else if (unit.equals("g"))
            return 2;
        else
            return 3;
    }
}
