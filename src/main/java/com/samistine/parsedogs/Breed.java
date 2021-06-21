package com.samistine.parsedogs;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import java.util.Collection;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.Document;

/**
 * @author Samuel Seidel
 */
@XStreamAlias("breed")
public class Breed implements Comparable<Breed> {

    private final String name;
    private final String description;
    private final String ranking;
    private final String personality;

    public Breed(String name, String description, String ranking, String personality) {
        Validate.notNull(name);
        Validate.notNull(description);
        Validate.notNull(ranking);
        Validate.notNull(personality);
        this.name = name;
        this.description = description;
        this.ranking = ranking;
        this.personality = personality;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getRanking() {
        return ranking;
    }

    public String getPersonality() {
        return personality;
    }

    @Override
    public int compareTo(Breed o) {
        return this.name.compareTo(o.name);
    }

    @Override
    public String toString() {
        return "Breed{" + "\r\n name=" + name + ",\r\n description=" + description + ",\r\n ranking=" + ranking
                + ",\r\n personality=" + personality + "\r\n}";
    }

    @XStreamAlias("breeds")
    private static class Breeds {

        @XStreamImplicit
        Collection<Breed> breeds;

        public Breeds(Collection<Breed> breeds) {
            this.breeds = breeds;
        }
    }

    public static Breed parseDogBreed(Document document) throws NullPointerException {
        String name = document.select("#page-title > h1").text();
        String description = document.select("#breed-info .breed-info__content-wrap").first().text();
        String ranking = document
                .select("#panel-overview > div > div > div.panel-flex__aside > ul > li:nth-child(2) > span.attribute-list__description.attribute-list__text")
                .text();
        String personality = document.select("#panel-overview > div > div > div.panel-flex__aside > ul > li.attribute-list__row.attribute-list__row--has-large > span.attribute-list__description.attribute-list__text.attribute-list__text--lg.mb4.bpm-mb5.pb0.d-block").text();

        if(ranking.contains("Ranks"))
            ranking = ranking.substring(6,ranking.indexOf("of")-1);
        else
            ranking = "";

        return new Breed(name, description, ranking, personality);
    }

    public static String toXMLObject(Breed breed) {
        return Utils.getXStream().toXML(breed);
    }

    public static String toXMLObject(Collection<Breed> breeds) {
        return Utils.getXStream().toXML(new Breeds(breeds));
    }

}
