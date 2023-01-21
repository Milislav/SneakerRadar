package com.sneakerradar.sneakerradar.webScraper.taf;

import com.sneakerradar.sneakerradar.domain.SneakerSizes;
import com.sneakerradar.sneakerradar.service.SneakerService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TafMenScraper extends Thread {

    private SneakerService sneakerService;
    HashMap<String, String> menSneakerMap = new HashMap<>();
    private int sneakerNumber;


    public TafMenScraper(SneakerService sneakerService, SneakerSizeTaf sneakerSizeTaf) {
        this.sneakerService = sneakerService;
        this.sneakerNumber = sneakerSizeTaf.menSneakerNumber;
    }


    @Override
    public void run() {
        print("TafMen running...");
        Document document;
        Document documentSneakerSizes;
        try {
            //Get Document object after parsing the html from given url.

            String url;
            document = Jsoup.connect("https://www.taf.mk/ProductCatalog?Segment=41&Grupa=01&Popust=False&CenaOd=0&CenaDo=0&PopustProcentOd=0&PopustProcentDo=0&Page=1").get();
            print(String.valueOf(LocalDateTime.now()));
            Element lastPageNumberElement = document
                    .select("li.next.PagedList-skipToLast")
                    .first().selectFirst("a");

            String lastPageNumberStringArray[] = lastPageNumberElement.attr("href").split("=");
            int lastPageNumber = Integer.parseInt(lastPageNumberStringArray[lastPageNumberStringArray.length - 1]);


            for (int i = 1; i <= lastPageNumber; i++) {
                if (i == 1) {
                    url = "https://www.taf.mk/ProductCatalog?Segment=41&Grupa=01&Popust=False&CenaOd=0&CenaDo=0&PopustProcentOd=0&PopustProcentDo=0&Page=1";
                } else {
                    url = "https://www.taf.mk/ProductCatalog?Segment=41&Grupa=01&Popust=False&CenaOd=0&CenaDo=0&PopustProcentOd=0&PopustProcentDo=0&Page=" + i;
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
                    String price = pricesElements.get(j).text().split(",")[0].replaceAll("[^a-zA-Z0-9]", "").trim();
                    String link = "https://www.taf.mk" + linksElements.get(j).attr("href");
                    if (!menSneakerMap.containsKey(link)) {
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
                            String sneakerSize = sneakerSizeElement.text();
                            sneakerSizes.add(new SneakerSizes(sneakerSize));
                        }

                        String imgSrc = "https://www.taf.mk" + imgSrcsElements.get(j).attr("src");
                        menSneakerMap.put(link, sneakerName);
                        sneakerService.insert(sneakerName, Integer.parseInt(price), link, imgSrc, "Taf", brand, sneakerSizes);
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        if (menSneakerMap.size() != sneakerNumber)
            run();
        print(String.valueOf(LocalDateTime.now()));
        print("done TafMen");
    }

    public static void print(String string) {
        System.out.println(string);
    }
}
