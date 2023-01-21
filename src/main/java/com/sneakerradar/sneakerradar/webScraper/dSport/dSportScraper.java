package com.sneakerradar.sneakerradar.webScraper.dSport;

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

public class dSportScraper extends Thread {

    private SneakerService sneakerService;
    Sites dSport = Sites.Dsport;

    public dSportScraper(SneakerService sneakerService) {
        this.sneakerService = sneakerService;
    }

    @Override
    public void run() {
        print("DsportMen&Unisex running...");
        Document document;
        Document documentSneakerSizes;
        try {
            //Get Document object after parsing the html from given url.
            String url;
            document = Jsoup.connect("https://www.dsport.mk/patike-unisex-muski").get();
            print(String.valueOf(LocalDateTime.now()));

            Element totalNumberSneakersElements = document
                    .select("div.filter-top-info")
                    .select("span")
                    .first();

            int totalNumberSneakers = Integer.parseInt(totalNumberSneakersElements.text());
            int lastPageNumber = (int) (totalNumberSneakers / 30.00) + 1;

            url = "https://www.dsport.mk/patike-unisex-muski&Pages=" + lastPageNumber;

            document = Jsoup.connect(url).timeout(600000).get();
            String title = document.title(); //Get title
            print("  Title: " + title); //Print title.

            Elements items = document
                    .select("#productDetailLink");


            for (Element item : items) {
                String nameAndBrandInfo = item.attr("onclick");
                String nameAndBrandText = nameAndBrandInfo.substring(nameAndBrandInfo.indexOf('(') + 1, nameAndBrandInfo.indexOf(')'));
                String nameAndBrand[] = nameAndBrandText.split(",");
                String sneakerName = nameAndBrand[1].substring(2, nameAndBrand[1].length() - 1);
                String link = "https://www.dsport.mk/" + item.attr("href").trim();
                if (!sneakerService.checkIfLinkExists(link)) {
                    String brand = nameAndBrand[3].substring(2, nameAndBrand[3].length() - 1);
                    documentSneakerSizes = Jsoup.connect(link).timeout(600000).get();
                    Elements sneakerSizesElements = documentSneakerSizes
                            .select("ul.sizes.no-margin")
                            .select("li.normal"); //#ctl06_ctl02_ulRaster
                    Element priceElement = documentSneakerSizes.selectFirst("span.price-new");
                    assert priceElement != null;
                    String price = priceElement.text().split(",")[0].replaceAll("[^a-zA-Z0-9]", "").trim();
                    List<SneakerSizes> sneakerSizes = new ArrayList<>();
                    for (Element sneakerSizeElement : sneakerSizesElements) {
                        String sneakerSizeInfo = sneakerSizeElement.attr("translatedname");
                        String euroSneakerSize[] = sneakerSizeInfo.substring(sneakerSizeInfo.indexOf('(') + 1, sneakerSizeInfo.indexOf(')')).split(" ");
                        if (euroSneakerSize.length > 1) {
                            SneakerSizes size = new SneakerSizes(euroSneakerSize[1]);
                            sneakerSizes.add(size);
                        } else {
                            SneakerSizes size = new SneakerSizes(euroSneakerSize[0]);
                            sneakerSizes.add(size);
                        }
                    }
                    Elements imgSrcElements = documentSneakerSizes
                            .select("div.product-top-images")
                            .select("img");


                    String imgSrc = "https://www.dsport.mk/" + imgSrcElements.get(1).attr("src");
                    sneakerService.insert(sneakerName, Integer.parseInt(price), link, imgSrc, dSport.name(), brand, sneakerSizes);
                }

            }
            print(String.valueOf(LocalDateTime.now()));
            print("done dSportMenUnisex");
            runWomen();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void runWomen() {
        print("DsportWomen running...");
        Document document;
        Document documentSneakerSizes;
        try {
            //Get Document object after parsing the html from given url.
            String url;
            document = Jsoup.connect("https://www.dsport.mk/patike-zenski").get();
            print(String.valueOf(LocalDateTime.now()));
            Element totalNumberSneakersElements = document
                    .select("div.filter-top-info")
                    .select("span")
                    .first();


            int totalNumberSneakers = Integer.parseInt(totalNumberSneakersElements.text());
            int lastPageNumber = (int) (totalNumberSneakers / 30.00) + 1;

            url = "https://www.dsport.mk/patike-zenski&Pages=" + lastPageNumber;

            document = Jsoup.connect(url).timeout(600000).get();
            String title = document.title(); //Get title
            print("  Title: " + title); //Print title.

            Elements items = document.select("#productDetailLink"); //div.row


            for (Element item : items) {
                String nameAndBrandInfo = item.attr("onclick");
                String nameAndBrandText = nameAndBrandInfo.substring(nameAndBrandInfo.indexOf('(') + 1, nameAndBrandInfo.indexOf(')'));
                String nameAndBrand[] = nameAndBrandText.split(",");
                String sneakerName = nameAndBrand[1].substring(2, nameAndBrand[1].length() - 1);
                String link = "https://www.dsport.mk/" + item.attr("href").trim();
                if (!sneakerService.checkIfLinkExists(link)) {
                    String brand = nameAndBrand[3].substring(2, nameAndBrand[3].length() - 1);

                    documentSneakerSizes = Jsoup.connect(link).timeout(600000).get();
                    Elements sneakerSizesElements = documentSneakerSizes
                            .select("ul.sizes.no-margin")
                            .select("li.normal");
                    Element priceElement = documentSneakerSizes.selectFirst("span.price-new");
                    assert priceElement != null;
                    String price = priceElement.text().split(",")[0].replaceAll("[^a-zA-Z0-9]", "").trim();
                    List<SneakerSizes> sneakerSizes = new ArrayList<>();
                    for (Element sneakerSizeElement : sneakerSizesElements) {
                        String sneakerSizeInfo = sneakerSizeElement.attr("translatedname");
                        String euroSneakerSize[] = sneakerSizeInfo.substring(sneakerSizeInfo.indexOf('(') + 1, sneakerSizeInfo.indexOf(')')).split(" ");
                        if (euroSneakerSize.length > 1) {
                            SneakerSizes size = new SneakerSizes(euroSneakerSize[1]);
                            sneakerSizes.add(size);
                        } else {
                            SneakerSizes size = new SneakerSizes(euroSneakerSize[0]);
                            sneakerSizes.add(size);
                        }
                    }
                    Elements imgSrcElements = documentSneakerSizes
                            .select("div.product-top-images")
                            .select("img");


                    String imgSrc = "https://www.dsport.mk/" + imgSrcElements.get(1).attr("src");
                    sneakerService.insert(sneakerName, Integer.parseInt(price), link, imgSrc, dSport.name(), brand, sneakerSizes);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        print(String.valueOf(LocalDateTime.now()));
        print("done dSportWomen");
    }

    public static void print(String string) {
        System.out.println(string);
    }
}
