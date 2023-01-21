package com.sneakerradar.sneakerradar.webScraper.InterSport;

import com.sneakerradar.sneakerradar.domain.SneakerSizes;
import com.sneakerradar.sneakerradar.domain.enumeration.Sites;
import com.sneakerradar.sneakerradar.service.SneakerService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class InterSportWomen extends Thread {

    private SneakerService sneakerService;
    Sites InterSport = Sites.InterSport;

    public InterSportWomen(SneakerService sneakerService) {
        this.sneakerService = sneakerService;
    }

    @Override
    public void run() {
        print("InterSport running...");
        Document document;
        Document documentSneakerSizes;
        try {
            //Get Document object after parsing the html from given url.

            String url;
            document = Jsoup.connect("https://intersport.mk/ProductCatalog?Segment=29&Grupa=01").get();
            print(String.valueOf(LocalDateTime.now()));
            Element lastPageNumberElement = document
                    .select("li.next.PagedList-skipToLast")
                    .first().selectFirst("a");

            String lastPageNumberStringArray[] = lastPageNumberElement.attr("href").split("=");
            int lastPageNumber = Integer.parseInt(lastPageNumberStringArray[lastPageNumberStringArray.length - 1]);


            for (int i = 1; i <= lastPageNumber; i++) {
                if (i == 1) {
                    url = "https://intersport.mk/ProductCatalog?Segment=29&Grupa=01";
                } else {
                    url = "https://intersport.mk/ProductCatalog?Segment=29&Grupa=01&Popust=False&CenaOd=0&CenaDo=0&PopustProcentOd=0&PopustProcentDo=0&Page=" + i;
                }
                document = Jsoup.connect(url).timeout(600000).get();
                String title = document.title(); //Get title
                print("  Title: " + title); //Print title.

                Elements items = document.select("div.col-sm-4.col-xs-6.product_partial"); //div.row
                Elements pricesElements = items.select("div.price-box");
                Elements linksElements = items.select(".product-preview__info__title.prodlist_title").select("a");
                Elements imgSrcsElements = items.select(".product-preview__image")
                        .select("img");

                for (int j = 0; j < pricesElements.size(); j++) {
                    String sneakerName = linksElements.get(j).text();
                    String priceAndCurrency = pricesElements.get(j).text().split(" ")[0].split(",")[0].replaceAll("[^a-zA-Z0-9]", "");
                    int price = Integer.parseInt(priceAndCurrency.trim());
                    String link = "https://intersport.mk" + linksElements.get(j).attr("href");
                    if (!sneakerService.checkIfLinkExists(link)) {
                        String brand = sneakerName.split(" ")[0].trim();
                        if (sneakerName.split(" ")[0].trim().equals("THE")) {
                            brand = sneakerName.split(" ")[0]
                                    + " "
                                    + sneakerName.split(" ")[1]
                                    + " "
                                    + sneakerName.split(" ")[2];
                        }
                        if (sneakerName.split(" ")[0].trim().equals("NEW") ||
                                sneakerName.split(" ")[0].trim().equals("SERGIO") ||
                                sneakerName.split(" ")[0].trim().equals("Under") ||
                                sneakerName.split(" ")[0].trim().equals("HELLY")) {
                            brand = sneakerName.split(" ")[0] + " " + sneakerName.split(" ")[1];
                        }
                        documentSneakerSizes = Jsoup.connect(link).timeout(600000).get();
                        Elements sneakerSizesElements = documentSneakerSizes
                                .select("span.tooltiptext");

                        List<SneakerSizes> sneakerSizes = new ArrayList<>();
                        for (Element sneakerSizeElement : sneakerSizesElements) {
                            SneakerSizes sneakerSize = new SneakerSizes(sneakerSizeElement.text());
                            sneakerSizes.add(sneakerSize);
                        }

                        String imgSrc = "https://intersport.mk" + imgSrcsElements.get(j).attr("src");
                        sneakerService.insert(sneakerName, price, link, imgSrc, InterSport.name(), brand, sneakerSizes);
                    }
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

