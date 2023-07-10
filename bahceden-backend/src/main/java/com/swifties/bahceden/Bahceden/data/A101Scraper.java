package com.swifties.bahceden.Bahceden.data;

import com.swifties.bahceden.Bahceden.entity.ScrapedData;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;


public class A101Scraper
{ private final String url = "https://www.a101.com.tr";
    private final String primaryEndPoint = "/online-alisveris";

    public Set<Product> Scrape ()
    {
        System.setProperty("file.encoding", "UTF-8");

        Set<Product> products = new HashSet<>();

        Document doc = getDocumentFrom(url + primaryEndPoint);
        Elements categories = selectRelatedCategories(doc);
        for (Element category : categories)
        {
            String categoryEndpoint = category.attr("href");
            String categoryUrl = url + categoryEndpoint;
            Document categoryDoc = getDocumentFrom(categoryUrl);

            Set<String> pagesDiscovered = new HashSet<>();
            Set<String> pagesToScrape = new HashSet<>();

            pagesToScrape.add(categoryUrl);

            System.out.println("Started looking for another pages from " + categoryUrl);

            findPages(pagesToScrape, categoryDoc, categoryUrl);

            System.out.println("Product Scraping Started for " + categoryUrl);

            for (String page : pagesToScrape)
            {
                if (!pagesDiscovered.add(page))
                {
                    break;
                }
                Document pageDoc = getDocumentFrom(page);

                scrapeProducts(products, pageDoc, categoryEndpoint.replace("market", "").replace("/", ""));
            }
        }

        return products;
    }

    private Elements selectRelatedCategories (Document doc)
    {
        return doc.select(
                "a[href$=sut/]"

//                "[href^=\"/market/\"]:" +
//                        "not([href$=\"atistirmalik/\"]," +
//                        "[href$=\"ambalaj-malzemeleri/\"]," +
//                        "[href$=\"temel-gida/\"]," +
//                        "[href$=\"/market/\"]," +
//                        "[href$=\"/market/\"]," +
//                        "[href$=\"saglikli-yasam-urunleri/\"]," +
//                        "[href$=\"icecek/\"]," +
//                        "[href$=\"kahvaltilik-sut-urunleri/\"]," +
//                        "[href$=\"ev-bakim-temizlik/\"])"
        );
    }

    private Document getDocumentFrom (String url)
    {
        try {
            return Jsoup
                    .connect(url)
                    .header("Accept-Charset", "UTF-8")
                    .get();
        } catch (IOException e) {
            System.out.println("An error occured while trying to connect " + url);
            throw new RuntimeException(e);
        }
    }
    private void findPages (Set<String> pagesToScrape, Document doc, String categoryUrl)
    {

        Elements pageLinks = doc.select("a.page-link");

        for (Element pageLink : pageLinks)
        {
            String page = pageLink.selectFirst("a.page-link").attr("href");
            if (page.startsWith("/market"))
            {
                page = "";
            }
            String newUrl = categoryUrl + page;
            if (pagesToScrape.add(newUrl))
            {
                System.out.printf("found new page : " + newUrl + "\n");
                Document pageDoc = getDocumentFrom( newUrl);
                findPages(pagesToScrape, pageDoc, categoryUrl);
            }
        }
    }

    private void scrapeProducts (Set<Product> products, Document pageDoc, String categoryName)
    {
        Elements productsNamePrice = pageDoc.select("a.name-price");

        for (Element productNamePrice : productsNamePrice) {
            String name = Objects.requireNonNull(productNamePrice
                            .selectFirst("h3.name"))
                    .text();
            String price = productNamePrice.selectFirst("span.current").text().substring(1);
            Product product = new Product(name, price, categoryName);
            products.add(product);
//            System.out.println(product);
        }
    }

    @Getter
    @Setter
    @ToString
    @EqualsAndHashCode
    public
    static
    class Product { // only for süt
        private String name;
        private String price;
        private String category;
        public Product(String name, String price, String category) {
            this.name = name;
            this.price = price;
            this.category = category;
        }

        public ScrapedData getAsScraped()
        {
            ScrapedData sd = new ScrapedData();
            sd.setName(getProperName());
            sd.setUnit(3);
            sd.setCategoryId(1);
            
            sd.setMinPrice(Double.parseDouble(price.replace(",", ".")));
            sd.setMaxPrice(Double.parseDouble(price.replace(",", ".")));
            return sd;
        }

        private String getProperName()
        {
            return "Süt";
        }
    }
}
//@Getter
//@Setter
//@ToString
//@EqualsAndHashCode
//class Product {
//    private String name;
//    private String price;
//    private String category;
//    public Product(String name, String price, String category) {
//        this.name = name;
//        this.price = price;
//        this.category = category;
//    }
//
//
//}

