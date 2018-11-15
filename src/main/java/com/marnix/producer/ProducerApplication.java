package com.marnix.producer;

import com.marnix.producer.Scraper.Scraper;
import com.marnix.producer.jms.JmsPublisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ProducerApplication implements CommandLineRunner {

    @Autowired
    JmsPublisher publisher;

    public static void main(String[] args) {
        SpringApplication.run(ProducerApplication.class, args);
    }

    @Override
    public void run(String... args) {
        Scraper scraper = new Scraper(publisher);
        scraper.startScraping();
    }
}
