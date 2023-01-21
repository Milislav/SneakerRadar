package com.sneakerradar.sneakerradar.webScraper.sportVision;

import com.sneakerradar.sneakerradar.domain.SneakerSizes;
import com.sneakerradar.sneakerradar.service.SneakerService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

//@Component
public class SportVisionScraperMenAndUnisex extends Thread {

    private SneakerService sneakerService;

    public SportVisionScraperMenAndUnisex(SneakerService sneakerService) {
        this.sneakerService = sneakerService;
    }

    @Override
    public void run() {
        print("SportVision running...");
        Document document;
        Document documentSneakerSizes;
        try {
            //Get Document object after parsing the html from given url.

            String url;
            document = Jsoup.connect("https://www.sportvision.mk/obuvki/maski+unisex").timeout(1800000).get();
            print(String.valueOf(LocalDateTime.now()));
            Elements lastPageElement = document.select("ul.pagination")
                    .select("li.number")
                    .select("a[rel=last]");

            int lastPageNumber = Integer.parseInt(lastPageElement.get(0).text());

            for (int i = 0; i < lastPageNumber; i++) {
                if (i == 0) {
                    url = "https://www.sportvision.mk/obuvki/maski+unisex";
                } else {
                    url = "https://www.sportvision.mk/obuvki/maski+unisex/page-" + i;
                }
                document = Jsoup.connect(url).timeout(1800000).get();
                String title = document.title(); //Get title
                print("  Title: " + title); //Print title.

                Elements items = document.select(".item-data.col-xs-12"); //div.row
                Elements pricesElements = items.select(".current-price");
                Elements linksElements = items.select(".text-wrapper").select(".title").select("a");
                Elements imgSrcsElements = items.select(".img-wrapper")
                        .select("img")
                        .select("img[src$=jpg.webp]")
                        .select("img[alt~= ]");

                for (int j = 0; j < pricesElements.size(); j++) {
                    String sneakerName = linksElements.get(j).text();
                    String priceAndCurrency = pricesElements.get(j).text().split(" ")[0].replaceAll("[^a-zA-Z0-9]", "");
                    int price = Integer.parseInt(priceAndCurrency.trim());
                    String link = linksElements.get(j).attr("href");
                    String brand = sneakerName.split(" ")[0].trim();
                    if(sneakerName.split(" ")[0].trim().equals("THE")){
                        brand = sneakerName.split(" ")[0]
                                + " "
                                + sneakerName.split(" ")[1]
                                + " "
                                + sneakerName.split(" ")[2];
                    }
                    if (sneakerName.split(" ")[0].trim().equals("NEW") ||
                            sneakerName.split(" ")[0].trim().equals("SERGIO") ||
                            sneakerName.split(" ")[0].trim().equals("UNDER") ||
                            sneakerName.split(" ")[0].trim().equals("HELLY")) {
                        brand = sneakerName.split(" ")[0] + " " + sneakerName.split(" ")[1];
                    }
                    documentSneakerSizes = Jsoup.connect(link).timeout(1800000).get();
                    Elements sneakerSizesElements = documentSneakerSizes
                            .select("ul.product-attributes.list-inline.product-attributes-two-sizes")
                            .select("li[data-html]")
                            .select("span.eur-size");
                    List<SneakerSizes> sneakerSizes = new ArrayList<>();

                    for (Element sneakerSizeElement : sneakerSizesElements) {
                        SneakerSizes sneakerSize = new SneakerSizes(sneakerSizeElement.text());
                        sneakerSizes.add(sneakerSize);
                    }

                    String imgSrc = "https://www.sportvision.mk" + imgSrcsElements.get(j).attr("src");
                    sneakerService.insert(sneakerName, price, link, imgSrc, "Sport-Vision", brand, sneakerSizes);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        print(String.valueOf(LocalDateTime.now()));
        print("done");
    }

    public static void print(String string) {
        System.out.println(string);
    }

}
