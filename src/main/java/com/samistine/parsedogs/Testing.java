/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.samistine.parsedogs;

import java.io.IOException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/**
 *
 * @author Samuel
 */
public class Testing {

    public static void main(String[] args) throws IOException
    {
        Document doc = Jsoup.connect("http://www.akc.org/dog-breeds/american-hairless-terrier/").timeout(10_000).get();
        Breed breed = Breed.parseDogBreed(doc);
        System.out.println(breed);
    }
}
