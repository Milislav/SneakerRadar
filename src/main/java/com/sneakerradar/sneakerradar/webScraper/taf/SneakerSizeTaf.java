package com.sneakerradar.sneakerradar.webScraper.taf;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class SneakerSizeTaf {

    public int menSneakerNumber = menSneakerSizeTaf();
    public int womenSneakerNumber = womenSneakerSizeTaf();

    public SneakerSizeTaf() throws IOException {
    }

    //@PostConstruct
    private int menSneakerSizeTaf() throws IOException {
        Document documentTafMen;

        documentTafMen = Jsoup.connect("https://www.taf.mk/ProductCatalog?Segment=41&Grupa=01&Popust=False&CenaOd=0&CenaDo=0&PopustProcentOd=0&PopustProcentDo=0&Page=1").get();

        Element lastNumberElementMen = documentTafMen
                .select("li.next.PagedList-skipToLast")
                .first().selectFirst("a");

        String[] lastPageMen = lastNumberElementMen.attr("href").split("=");
        int lastPageNumberMen = Integer.parseInt(lastPageMen[lastPageMen.length - 1]);

        documentTafMen = Jsoup.connect("https://www.taf.mk/ProductCatalog?Segment=41&Grupa=01&Popust=False&CenaOd=0&CenaDo=0&PopustProcentOd=0&PopustProcentDo=0&Page=" + lastPageNumberMen).get();
        Elements lastItemsMen = documentTafMen.select("div.col-sm-4.col-xs-6.product_partial");
        int sneakerNumberMen = (24 * (lastPageNumberMen - 1)) + lastItemsMen.size();


        return sneakerNumberMen;
    }

    private int womenSneakerSizeTaf() throws IOException {
        Document documentSportTafWomen;
        documentSportTafWomen = Jsoup.connect("https://www.taf.mk/ProductCatalog?Segment=42&Grupa=01&Popust=False&CenaOd=0&CenaDo=0&PopustProcentOd=0&PopustProcentDo=0&Page=1").get();

        Element lastNumberElementWomen = documentSportTafWomen.select("div.pagination.PagedList-pager")
                .select("li.next.PagedList-skipToLast")
                .first().selectFirst("a");

        String[] lastPageWomen = lastNumberElementWomen.attr("href").split("=");
        int lastPageNumberWomen = Integer.parseInt(lastPageWomen[lastPageWomen.length - 1]);
        documentSportTafWomen = Jsoup.connect("https://www.taf.mk/ProductCatalog?Segment=42&Grupa=01&Popust=False&CenaOd=0&CenaDo=0&PopustProcentOd=0&PopustProcentDo=0&Page=" + lastPageNumberWomen).get();

        Elements lastItemsWomen = documentSportTafWomen.select("div.col-sm-4.col-xs-6.product_partial");
        int sneakerNumberWomen = (24 * (lastPageNumberWomen - 1)) + lastItemsWomen.size();

        return sneakerNumberWomen;
    }
}
