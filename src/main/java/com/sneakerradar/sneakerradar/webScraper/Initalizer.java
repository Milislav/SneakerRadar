package com.sneakerradar.sneakerradar.webScraper;

import com.sneakerradar.sneakerradar.service.SneakerService;
import com.sneakerradar.sneakerradar.webScraper.InterSport.InterSportMen;
import com.sneakerradar.sneakerradar.webScraper.InterSport.InterSportWomen;
import com.sneakerradar.sneakerradar.webScraper.buzz.BuzzScraper;
import com.sneakerradar.sneakerradar.webScraper.buzz.BuzzScraperTwo;
import com.sneakerradar.sneakerradar.webScraper.dSport.dSportScraper;
import com.sneakerradar.sneakerradar.webScraper.sportM.SneakerSizeSportM;
import com.sneakerradar.sneakerradar.webScraper.sportM.sportMMenScraper;
import com.sneakerradar.sneakerradar.webScraper.sportM.sportMWomenScraper;
import com.sneakerradar.sneakerradar.webScraper.sportReality.SportRealityScraper;
import com.sneakerradar.sneakerradar.webScraper.sportVision.SportVisionScraperMenAndUnisex;
import com.sneakerradar.sneakerradar.webScraper.sportVision.SportVisionScraperWomen;
import com.sneakerradar.sneakerradar.webScraper.taf.SneakerSizeTaf;
import com.sneakerradar.sneakerradar.webScraper.taf.TafMenScraper;
import com.sneakerradar.sneakerradar.webScraper.taf.TafWomenScraper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;


@Component
public class Initalizer {

    @Autowired
    SneakerService sneakerService;
    @Autowired
    SneakerSizeSportM sneakerSizeSportM;
    @Autowired
    SneakerSizeTaf sneakerSizeTaf;


    @PostConstruct
    @Scheduled(cron = "0 0 4 * * *")
    public void scrape() {
        Thread buzz = new BuzzScraper(sneakerService);
        Thread buzz2 = new BuzzScraperTwo(sneakerService);
        Thread sportReality = new SportRealityScraper(sneakerService);
        Thread sportVisionMenAndUnisex = new SportVisionScraperMenAndUnisex(sneakerService);
        Thread sportVisionWomen = new SportVisionScraperWomen(sneakerService);
        Thread sportMMen = new sportMMenScraper(sneakerService, sneakerSizeSportM);
        Thread sportMWomen = new sportMWomenScraper(sneakerService, sneakerSizeSportM);
        Thread InterSportMen = new InterSportMen(sneakerService);
        Thread dSport = new dSportScraper(sneakerService);
        Thread InterSportWomen = new InterSportWomen(sneakerService);
        Thread TafMen = new TafMenScraper(sneakerService, sneakerSizeTaf);
        Thread TafWomen = new TafWomenScraper(sneakerService, sneakerSizeTaf);

        buzz.start();
        buzz2.start();
        sportVisionMenAndUnisex.start();
        sportVisionWomen.start();
        InterSportMen.start();
        InterSportWomen.start();
        sportMMen.start();
        sportMWomen.start();
        dSport.start();
        TafMen.start();
        TafWomen.start();
        sportReality.start();

    }
}
