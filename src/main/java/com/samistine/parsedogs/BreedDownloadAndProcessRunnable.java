package com.samistine.parsedogs;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.LockSupport;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/**
 *
 * @author Samuel Seidel
 */
public class BreedDownloadAndProcessRunnable implements Runnable {

    private final int totalBreeds;
    private final List<Breed> breeds;
    private final String url;
    private final AtomicInteger successfull;

    public BreedDownloadAndProcessRunnable(int totalBreeds, List<Breed> breeds, String url, AtomicInteger successfull)
    {
        this.totalBreeds = totalBreeds;
        this.breeds = breeds;
        this.url = url;
        this.successfull = successfull;
    }

    @Override
    public void run()
    {
        int retryCount = 3;
        int tryAttempts = 0;
        while (tryAttempts < retryCount)
        {
            if (tryAttempts != 0)
            {
                System.out.println("Retry (" + tryAttempts + 1 + "/" + 3 + ")");
            }
            try
            {
                //Wait 500 miliseconds to prevent tripping web filter
                LockSupport.parkNanos(500_000_000);

                //Download HTML
                Document doc = Jsoup.connect(url).timeout(10_000).get();

                //Parse data from HTML into the Breed object
                Breed breed = Breed.parseDogBreed(doc);

                //Add Breed object to overall list
                breeds.add(breed);

                System.out.println("Progress: " + successfull.addAndGet(1) + "/" + totalBreeds);
                break;
            } catch (Exception ex)
            {
                if (tryAttempts + 1 == 3)
                {
                    System.err.println("Skipping, due to errors on multiple retries");
                    ex.printStackTrace();
                } else
                {
                    System.err.println("An error occured on " + url);
                }
            }
        }
    }

}
