package com.blackhole.crackle;

import android.support.annotation.NonNull;
import android.util.Log;

import java.util.Comparator;

/**
 * Created by bhavneet singh on 06-Feb-19.
 */

public class MovieComparator implements Comparator<Movie> {

    private String key="";
    public MovieComparator(String key) {
        this.key=key.toLowerCase();
    }

    @Override
    public int compare(Movie m1, Movie m2) {
        Movie.Info o1=m1.info,o2=m2.info;

       try {
           switch (key) {
               case "ratings":
                   return  (o1.rating == o2.rating)?0:(o1.rating < o2.rating)?1:-1;
               case "rank":
                   return  (o1.rank == o2.rank)?0:(o1.rank < o2.rank)?1:-1;
               case "running time":
                   return  (o1.running_time_secs == o2.running_time_secs)?0:(o1.running_time_secs < o2.running_time_secs)?1:-1;
               case "date":
                   return -m1.year.trim().compareTo(m2.year.trim());
           }
       }
       catch (Exception e)
       {
           return m1.title.length()-m2.title.length();
       }
        return m1.title.length()-m2.title.length();
    }
}
