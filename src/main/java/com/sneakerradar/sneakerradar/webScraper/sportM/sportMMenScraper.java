package com.sneakerradar.sneakerradar.webScraper.sportM;

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
import java.util.HashMap;
import java.util.List;

public class sportMMenScraper extends Thread {

    private SneakerService sneakerService;
    private int sneakerNumber;
    private SneakerSizeSportM sneakerSizeSportM;
    HashMap<String, String> menSneakerMap = new HashMap<>();
    Sites SportM = Sites.SportM;


    public sportMMenScraper(SneakerService sneakerService, SneakerSizeSportM sneakerSizeSportM) {
        this.sneakerService = sneakerService;
        this.sneakerSizeSportM = sneakerSizeSportM;
        this.sneakerNumber = sneakerSizeSportM.menSneakerNumber;
    }

    @Override
    public void run() {
        print("SportMmen running...");
        Document document;
        Document documentSneakerSizes;

        //int sneakerNumber = 0;

        try {
            //Get Document object after parsing the html from given url.

            String url;
            document = Jsoup.connect("https://www.sport-m.com.mk/ProductCatalog?Segment=03&Grupa=01&Popust=False&CenaOd=0&CenaDo=0&PopustProcentOd=0&PopustProcentDo=0&Page=1").timeout(600000).get();
            print(String.valueOf(LocalDateTime.now()));


            Elements lastNumberElement = document.select("div.pagination.PagedList-pager")
                    .select("li.next.PagedList-skipToLast")
                    .select("a");
            String lastPage[] = lastNumberElement.attr("href").split("=");
            int lastPageNumber = Integer.parseInt(lastPage[lastPage.length - 1]);

            for (int i = 1; i <= lastPageNumber; i++) {

                url = "https://www.sport-m.com.mk/ProductCatalog?Segment=03&Grupa=01&Popust=False&CenaOd=0&CenaDo=0&PopustProcentOd=0&PopustProcentDo=0&Page=" + i;
                document = Jsoup.connect(url).timeout(600000).get();
                String title = document.title(); //Get title
                print("  Title: " + title); //Print title.

                Elements items = document.select(".col-md-3.col-sm-4.col-xs-6.product_partial"); //div.row
                Elements pricesElements = items.select(".price-box");
                Elements linksElements = items.select(".description-product.searchProdDesc");
                Elements brandElements = items.select(".product-preview__info").select(".brand_name");
                Elements imgSrcsElements = items //images
                        .select(".product-preview__image")
                        .select("img[src$= ]");

                if (imgSrcsElements.size() != 20) {
                    System.out.println("warning");
                }

                for (int j = 0; j < items.size(); j++) {
                    String sneakerName = linksElements.get(j).text();
                    String priceString;
                    if (pricesElements.get(j).childrenSize() > 0) {
                        priceString = pricesElements.get(j).select(".colorRed").text().split(" ")[0].split(",")[0].replaceAll("[^a-zA-Z0-9]", "");
                    } else {
                        priceString = pricesElements.get(j).text().split(" ")[0].split(",")[0].replaceAll("[^a-zA-Z0-9]", "");
                    }
                    int price = Integer.parseInt(priceString);
                    String link = "https://www.sport-m.com.mk" + linksElements.get(j).attr("href");
                    if (!menSneakerMap.containsKey(link)) {
                        String brand = brandElements.get(j).text(); // items.get(j).select("brand_name").text()
                        documentSneakerSizes = Jsoup.connect(link).timeout(600000).get();
                        Elements sneakerSizesElements = documentSneakerSizes
                                .select("ul.options-swatch.options-swatch--size.options-swatch--lg")
                                .select("li[data-toggle=tooltip]");
                        List<SneakerSizes> sneakerSizes = new ArrayList<>();
                        for (Element sneakerSizeElement : sneakerSizesElements) {
                            String sneakerSize = sneakerSizeElement.text();
                            String sneakerSizeCheck[] = sneakerSize.split(" ");
                            if (sneakerSizeCheck.length > 0) {
                                SneakerSizes sneakerSizeEntity = new SneakerSizes(sneakerSizeCheck[0]);
                                sneakerSizes.add(sneakerSizeEntity);

                            } else {
                                SneakerSizes sneakerSizeEntity = new SneakerSizes(sneakerSize);
                                sneakerSizes.add(sneakerSizeEntity);
                            }
                        }

                        String imgSrc = "https://www.sport-m.com.mk" + imgSrcsElements.get(j).attr("src");
                        menSneakerMap.put(link, sneakerName);
                        sneakerService.insert(sneakerName, price, link, imgSrc, SportM.name(), brand, sneakerSizes);
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        if (menSneakerMap.size() != sneakerNumber)
            run();
        print(String.valueOf(LocalDateTime.now()));
        print("done sportM men");
    }

    public static void print(String string) {
        System.out.println(string);
    }
}
