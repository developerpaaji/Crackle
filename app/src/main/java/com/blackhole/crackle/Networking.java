package com.blackhole.crackle;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

/**
 * Created by bhavneet singh on 05-Feb-19.
 */

public class Networking {
    //Implementing Singleton design
    //Initializing retrofit
    public static final ArrayList<Movie>movies=new ArrayList<>();
    private static Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://test.terasol.in/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    private static TerasolService service=retrofit.create(TerasolService.class);

    //Getting data
    public static void getMovies(final OnDownloadComplete onDownloadComplete)
    {
        Call<ArrayList<Movie>> call=service.getMovies();
        call.enqueue(new Callback<ArrayList<Movie>>() {
            @Override
            public void onResponse(Call<ArrayList<Movie>> call, Response<ArrayList<Movie>> response) {
                Log.d("Response",(response.body()==null)+"");
                movies.clear();
                movies.addAll(response.body());
                onDownloadComplete.onDownloadComplete(response.body());
            }

            @Override
            public void onFailure(Call<ArrayList<Movie>> call, Throwable t) {
                Log.d("Response",t.getMessage());
                onDownloadComplete.onDownloadComplete(null);
            }
        });
    }
    static ArrayList<Movie>search(String query)
    {
        ArrayList<Movie>result=new ArrayList<>();
        if(query==null||query.length()==0)
        {
         return result;
        }
        for(Movie movie:movies)
        {
            if(movie.getTitle().toLowerCase().startsWith(query.toLowerCase()))
            {
                result.add(movie);
            }
        }
        return result;

    }
    static ArrayList<Movie> getRelated(ArrayList<String> genres)
    {
        return Movie.filterMovies(movies,"Any",genres,"Any");
    }
}
interface TerasolService{
    @GET("/moviedata.json")
    Call<ArrayList<Movie>> getMovies();
}
interface OnDownloadComplete{
    void onDownloadComplete(ArrayList<Movie> results);
}
