package com.blackhole.crackle;

import android.content.Intent;
import android.os.Parcelable;
import android.support.design.internal.ParcelableSparseArray;
import android.text.TextUtils;
import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;


public class Movie  implements Serializable {

    public static final String MOVIE = "movie",RELATED="related";
    String year,title;
    ArrayList<Movie>related=new ArrayList<>();
    Info info;

    public String getYear() {
        return year;
    }

    public String getTitle() {
        return title;
    }

    public String getThumbnail()
    {
        return  info.image_url;
    }
    public String getGenres() {
        if(info.genres!=null)
        return TextUtils.join(" , ",info.genres);
        return "";
    }

    public String getPlot() {
        return info.plot;
    }


    public String getRatings() {
        return info.rating+"";
    }

    public String getCast() {
        if(info.actors!=null)
        return TextUtils.join(" , ",info.actors);
        return "";
    }

    public String getRunningTime() {
        int hr= (int)info.running_time_secs/(60*60);
        int min= (int)info.running_time_secs/60%60;
        return hr+"hr "+min+"min";
    }

    public String getDirector() {
        if(info.directors!=null)
        return TextUtils.join(",",info.directors);
        return "";
    }

    public class Info implements Serializable{
        ArrayList<String>directors,genres,actors;
        String release_date,image_url,plot;
        double rating,rank,running_time_secs;
    }

    static ArrayList<Movie> filterMovies(ArrayList<Movie>input, final String director, ArrayList<String>genres, String sort)
    {
        if(input.size()==0)
        {
            return input;
        }
        ArrayList<Movie>movies=(ArrayList<Movie>) input.clone();
        Collections.sort(movies,new MovieComparator(sort));
        ArrayList<Movie>temp=new ArrayList<>();
        for(Movie m:movies)
        {
            if(!director.equals("Any"))
            {
                if(m.info.directors!=null&&m.info.directors.indexOf(director)!=-1)
                {
                    temp.add(m);
                }
            }
            else
                {
                   temp.add(m);
                }
        }
        movies=temp;
        temp=new ArrayList<>();
        if(genres.size()>0)
        {
           for(Movie m:movies)
           {
               boolean contains=true;
               for(String genre:genres)
               {
                   if(!m.getGenres().contains(genre))
                   {
                       contains=false;
                       break;
                   }
               }
               if(contains)
               {
                   Log.d("Tags",m.title);
                   temp.add(m);
               }
           }
          movies=temp;
        }

        return movies;
    }
}

