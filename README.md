## Intro:
SneakerRadar is a web app whose sole purpose is to make shopping for sneakers or any kind of footwear easier and more relaxing for the consumer.
This web app provides the user with footwear from the following websites: Sport Reality, Sport Vision, Inter Sport, The athlete's foot, Buzz, SportM and Dsport.


## Technologies used:
- Java 11
- Spring Boot 
- PostgreSql 
- Jsoup
- Cron
- Threads
- Angular 

## Additional information:
The architecture of the app is MVC, so the app is divided into three logical components, but it also has a package named "webScraper".
Here is where the magic happens.This package has packages named after the websites and these packages have classes where using the Jsoup library the necessary data is extracted.
There also is an "Initalizer" class where all the other web scrapper classes can be invoked and using threads the scraping can be done in parallel.

## Link to video of APP
https://jmp.sh/eQxETDJ5
