/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.samistine.parsedogs;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import com.thoughtworks.xstream.io.xml.DomDriver;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Document;

/**
 *
 * @author Samuel
 */
@XStreamAlias("breed")
public class Breed implements Comparable<Breed> {

    private final String name;
    private final String description;
    private final String ranking;
    private final String personality;

    public Breed(String name, String description, String ranking, String personality)
    {
        Validate.notNull(name);
        Validate.notNull(description);
        Validate.notNull(ranking);
        Validate.notNull(personality);
        this.name = name;
        this.description = description;
        this.ranking = ranking;
        this.personality = personality;
    }

    public String getName()
    {
        return name;
    }

    public String getDescription()
    {
        return description;
    }

    public String getRanking()
    {
        return ranking;
    }

    public String getPersonality()
    {
        return personality;
    }

    @Override
    public int compareTo(Breed o)
    {
        return this.name.compareTo(o.name);
    }

    @Override
    public String toString()
    {
        return "Breed{" + "\r\n name=" + name + ",\r\n description=" + description + ",\r\n ranking=" + ranking + ",\r\n personality=" + personality + "\r\n}";
    }

    @XStreamAlias("breeds")
    private static class Breeds {

        @XStreamImplicit
        Collection<Breed> breeds;

        public Breeds(Collection<Breed> breeds)
        {
            this.breeds = breeds;
        }
    }

    public static Breed parseDogBreed(Document document) throws NullPointerException
    {
        String name = document.select("body > main > div.breadcrumbs > ul > li.last").text();
        String description = document.select("body > main > section.feed-standard > article > figure > figcaption > div > ul > li.slide[data-tip-id=\"1\"] p").text();
        String ranking = document.select(".bigrank").text();
        String personality = document.select(".info").text();
        return new Breed(name, description, ranking, personality);
    }

    public static String toXMLObject(Breed breed)
    {
        return Utils.getXStream().toXML(breed);
    }

    public static String toXMLObject(Collection<Breed> breeds)
    {
        return Utils.getXStream().toXML(new Breeds(breeds));
    }

}
