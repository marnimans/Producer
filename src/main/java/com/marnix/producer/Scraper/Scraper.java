package com.marnix.producer.Scraper;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.google.gson.Gson;
import com.marnix.producer.jms.JmsPublisher;
import com.marnix.producer.models.Product;

import java.util.List;

public class Scraper {

    private int scrapedProducts;
    private static WebClient client = new WebClient();
    private JmsPublisher publisher;


    public Scraper(JmsPublisher x) {
        System.out.println("Starting scraper");
        this.publisher = x;
    }

    //Haalt middels XPath alle category links op.
    public void startScraping() {
        client.getOptions().setCssEnabled(false);
        client.getOptions().setJavaScriptEnabled(false);
        try {
            String searchUrl = "https://www.megekko.nl/sitemap";
            HtmlPage page = client.getPage(searchUrl);
            List<HtmlElement> items = page.getByXPath("//a[@class='sitemap_prodvan']");
            if (items.isEmpty()) {
                System.out.println("No items found !");
            } else {
                for (HtmlElement subClass : items) {
                    scrapeSubclass(subClass);
                }
            }
        } catch (Exception e) {
            System.out.println("Something went wrong!");
            e.printStackTrace();
        }
    }

    //Haalt middels Xpath de juiste subclass links op.
    private void scrapeSubclass(HtmlElement subClass) throws Exception {
        String subclassUrl = subClass.getAttribute("href");
        HtmlPage subPage = client.getPage("https://www.megekko.nl" + subclassUrl);
        List<HtmlElement> subItems = subPage.getByXPath("//main/div[@class='teksten']/ul/li/a");
        if (subItems.isEmpty()) {
            System.out.println("No Subitems found, moving to other product category");
        } else {
            for (HtmlElement product : subItems) {
                scrapeProductPage(product);

            }
        }
    }

    //Haalt middels XPath de juiste productpagina links op.
    private void scrapeProductPage(HtmlElement product) throws Exception {
        String productUrl = product.getAttribute("href");
        HtmlPage productPage = client.getPage("https://www.megekko.nl" + productUrl);
        getData(productPage);
    }

    //Haalt middels XPath de juiste gegevens van een product pagina.
    private void getData(HtmlPage productPage) {
        Product product = new Product();

        HtmlElement price = (productPage.getFirstByXPath("//div[@class='pricer large']/div[@class='pricecontainer']"));
        String itemPrice = price == null ? "no price" : price.asText();
        itemPrice = removeHtmlTags(itemPrice);
        product.setProductPrice(itemPrice);

        HtmlElement name = (productPage.getFirstByXPath("//a/h1[@class='title']"));
        String itemName = name == null ? "no name" : name.asText();
        itemName = removeHtmlTags(itemName);
        product.setProductName(itemName);

        HtmlElement description = (productPage.getFirstByXPath("//div[@id='proddesccontainer']"));
        String itemDescription = description == null ? "no description" : description.asText();
        itemDescription = removeHtmlTags(itemDescription);
        product.setProductDescription(itemDescription);

        scrapedProducts++;
        if (scrapedProducts % 20 == 0) {
            System.out.println("Scraped " + scrapedProducts + " products.");
        }
        Gson g = new Gson();
        String test = g.toJson(product);
        publisher.send(test);
    }

    //Een filter voor het verwijderen van alle HTML-tags die perongeluk mee gescraped worden.
    private String removeHtmlTags(String str) {

        return str.replaceAll("\\r\\n", "");
    }
}
