package com.twitchable.project.model;

/**
 * Created by riste on 6/9/2016.
 */
public class RecomendedChannel implements Comparable<RecomendedChannel> {

    private String name;
    private Integer similarity;
    private String link;

    public RecomendedChannel() {}

    public RecomendedChannel(String name, Integer similarity) {
        this.name = name;
        this.similarity = similarity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getSimilarity() {
        return similarity;
    }

    public void setSimilarity(Integer similarity) {
        this.similarity = similarity;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    @Override
    public String toString() {
        return name + " : " + similarity;
    }

    @Override
    public int compareTo(RecomendedChannel o) {
        return this.getName().compareTo(o.getName());
    }

    @Override
    public boolean equals(Object o) {

        // If the object is compared with itself then return true
        if (o == this) {
            return true;
        }

        /* Check if o is an instance of Complex or not
          "null instanceof [type]" also returns false */
        if (!(o instanceof RecomendedChannel)) {
            return false;
        }

        // typecast o to Complex so that we can compare data members
        RecomendedChannel rc = (RecomendedChannel) o;

        // Compare the data members and return accordingly
        return this.getName().equals(rc.getName());
    }

    @Override
    public int hashCode() {
        return this.getName().hashCode();
    }
}
