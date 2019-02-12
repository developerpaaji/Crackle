package com.blackhole.crackle;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bhavneet singh on 05-Feb-19.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieHolder>{

    private Context context;
    private ArrayList<Movie> movies;
    private int type=1;
    static final int LIST=1,GRID=2;
    public MovieAdapter(Context context, ArrayList<Movie> movies,int type) {
        this.context = context;
        this.movies = movies;
        this.type=type;
    }

    @NonNull
    @Override
    public MovieHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if(type==GRID)
        return new MovieHolder(layoutInflater.inflate(R.layout.movie_thumbnail,parent,false));
        return new MovieHolder(layoutInflater.inflate(R.layout.movie_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull final MovieHolder holder, final int position) {
          Movie movie=movies.get(position);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   onMovieClick(position);
                }
            });
          Glide.with(context).load(movie.getThumbnail()).transition(DrawableTransitionOptions.withCrossFade()).into(holder.thumbnail);
          if(type==LIST)
          {
              holder.title.setText(movie.title);
              holder.genre.setText(movie.getGenres());
              holder.plot.setText(movie.getPlot());
              holder.ratings.setText(movie.getRatings());
          }
    }
    private void onMovieClick(int index)
    {
        Intent intent=new Intent(context,MovieActivity.class);
        Movie movie=movies.get(index);
        ArrayList<Movie> related=Networking.getRelated(movie.info.genres);
        movie.related= new ArrayList<>(related.subList(0,related.size()>=9?9:related.size()));
        intent.putExtra(Movie.MOVIE,movie);
        context.startActivity(intent);
    }
    @Override
    public int getItemCount() {
        return movies.size();
    }

    public void addNew(List<Movie> movies) {
        this.movies.clear();
        this.movies.addAll(movies);
        this.notifyDataSetChanged();
    }

    public class MovieHolder extends RecyclerView.ViewHolder{

        ImageView thumbnail;
        TextView title,genre,ratings, plot;
        public MovieHolder(View itemView) {
            super(itemView);
            thumbnail=itemView.findViewById(R.id.thumbnail);
            title=itemView.findViewById(R.id.title);
            genre=itemView.findViewById(R.id.genre);
            ratings=itemView.findViewById(R.id.ratings);
            plot =itemView.findViewById(R.id.plot);
        }
    }
    public ArrayList<Movie> getMovies()
    {
        return movies;
    }

}
