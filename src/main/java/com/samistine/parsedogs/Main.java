package com.samistine.parsedogs;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.LockSupport;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 *
 * @author Samuel Seidel
 */
public class Main {

    public static void main(String[] args) throws IOException, InterruptedException
    {
        List<String> dogBreedURLs = new ArrayList<>();

        //Go through all the index pages and collect the individual links to each of the dog breeds
        for (char alphabet = 'A'; alphabet <= 'Z'; alphabet++)
        {
            String url = ("http://www.akc.org/dog-breeds/?letter=" + alphabet);

            //Get HTML Document
            System.out.println("Connecting to " + url);
            Document doc = Jsoup.connect(url).timeout(20_000).get();

            //Gather the html data we are looking for
            Elements links = doc.select("#breed-type-card- > a");
            System.out.println(links.size() + " dog breeds found.");

            //Iterate over said data
            for (Element link : links)
            {
                //Get the url of the dog breed
                String dogBreedURL = link.attr("abs:href");
                //Add it to our list
                dogBreedURLs.add(dogBreedURL);
            }
            //Wait 500 miliseconds to prevent tripping web filter
            LockSupport.parkNanos(500_000_000);
        }

        //Output total dog breed found
        System.out.println("Total dog breed: " + dogBreedURLs.size());

        System.out.println("Beginning search of breeds");
        LockSupport.parkNanos(1_000_000_000);

        final int total = dogBreedURLs.size();
        final List<Breed> breeds = Collections.synchronizedList(new ArrayList<Breed>());
        final AtomicInteger successfull = new AtomicInteger(0);

        ExecutorService pool = Executors.newFixedThreadPool(24);
        for (String url : dogBreedURLs)
        {
            pool.submit(new BreedDownloadAndProcessRunnable(total, breeds, url, successfull));
        }
        pool.shutdown();
        pool.awaitTermination(10, TimeUnit.MINUTES);

        System.out.println("Finished downloading and parsing dog breeds,\r\n"
                + " successfully downloaded and parsed " + successfull + "/" + total);

        //Convert our java objects to xml foramatting
        Collections.sort(breeds);
        String xml = Breed.toXMLObject(breeds);

        //Store the file here
        File file = new File("output.xml");

        //Write the xml to file
        try (BufferedWriter writer = Files.newBufferedWriter(file.toPath(), Charset.defaultCharset()))
        {
            writer.write(xml);
        }

        System.out.println(xml);
        System.out.println("Output saved at " + file.getAbsolutePath());

    }

}
