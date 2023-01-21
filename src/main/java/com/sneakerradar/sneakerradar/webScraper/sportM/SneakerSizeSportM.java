package com.sneakerradar.sneakerradar.webScraper.sportM;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class SneakerSizeSportM {

    public int menSneakerNumber = menSneakerSizeSportM();
    public int womenSneakerNumber = womenSneakerSizeSportM();

    public SneakerSizeSportM() throws IOException {
    }

    private int menSneakerSizeSportM() throws IOException {
        Document documentSportMMen;
        documentSportMMen = Jsoup.connect("https://www.sport-m.com.mk/ProductCatalog?Segment=03&Grupa=01&Popust=False&CenaOd=0&CenaDo=0&PopustProcentOd=0&PopustProcentDo=0&Page=1").timeout(600000).get();

        Elements lastNumberElementMen = documentSportMMen.select("div.pagination.PagedList-pager")
                .select("li.next.PagedList-skipToLast")
                .select("a");
        String lastPageMen[] = lastNumberElementMen.attr("href").split("=");
        int lastPageNumberMen = Integer.parseInt(lastPageMen[lastPageMen.length - 1]);

        documentSportMMen = Jsoup.connect("https://www.sport-m.com.mk/ProductCatalog?Segment=03&Grupa=01&Popust=False&CenaOd=0&CenaDo=0&PopustProcentOd=0&PopustProcentDo=0&Page=" + lastPageNumberMen).get();
        Elements lastItemsMen = documentSportMMen
                .select(".col-md-3.col-sm-4.col-xs-6.product_partial"); //div.row
        int sneakerNumberMen = (20 * (lastPageNumberMen - 1)) + lastItemsMen.size();


        return sneakerNumberMen;
    }

    private int womenSneakerSizeSportM() throws IOException {
        Document documentSportMWomen;
        documentSportMWomen = Jsoup.connect("https://www.sport-m.com.mk/ProductCatalog?Segment=04&Grupa=01&Popust=False&CenaOd=0&CenaDo=0&PopustProcentOd=0&PopustProcentDo=0&Page=1").get();

        Elements lastNumberElementWomen = documentSportMWomen.select("div.pagination.PagedList-pager")
                .select("li.next.PagedList-skipToLast")
                .select("a");
        String lastPageWomen[] = lastNumberElementWomen.attr("href").split("=");
        int lastPageNumberWomen = Integer.parseInt(lastPageWomen[lastPageWomen.length - 1]);
        documentSportMWomen = Jsoup.connect("https://www.sport-m.com.mk/ProductCatalog?Segment=04&Grupa=01&Popust=False&CenaOd=0&CenaDo=0&PopustProcentOd=0&PopustProcentDo=0&Page=" + lastPageNumberWomen).get();

        Elements lastItemsWomen = documentSportMWomen.select(".col-md-3.col-sm-4.col-xs-6.product_partial"); //div.row
        int sneakerNumberWomen = (20 * (lastPageNumberWomen - 1)) + lastItemsWomen.size();

        return sneakerNumberWomen;
    }
}
