package com.blackhole.crackle;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.io.Serializable;
import java.util.ArrayList;

import static android.text.Spanned.SPAN_INCLUSIVE_INCLUSIVE;

public class MovieActivity extends AppCompatActivity {

    private TextView ratings,genre,cast,plot,director;
    ImageView thumbnail;
    RecyclerView recyclerView;
    MovieAdapter movieAdapter;
    Movie movie;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MovieActivity.super.onBackPressed();
            }
        });
        Intent intent = getIntent();
        if(intent==null)
        {
            return;
        }
        movie = (Movie) intent.getSerializableExtra(Movie.MOVIE);
        getSupportActionBar().setTitle(movie.title);
        thumbnail = findViewById(R.id.thumbnail);
        Glide.with(this).load(movie.getThumbnail()).into(thumbnail);
        ratings=findViewById(R.id.ratings);
        setRatings();
        plot=findViewById(R.id.plot);
        plot.setText(movie.getPlot());
        genre=findViewById(R.id.genre);
        setDetail(genre,"Genre",movie.getGenres());
        cast=findViewById(R.id.cast);
        setDetail(cast,"Cast",movie.getCast());
        director=findViewById(R.id.director);
        setDetail(director,"Director",movie.getDirector());
        recyclerView=findViewById(R.id.related);
        movieAdapter=new MovieAdapter(this, movie.related, MovieAdapter.GRID);
        recyclerView.setLayoutManager(new GridLayoutManager(this,3));
//        recyclerView.addItemDecoration(new GridSpacingItemDecoration(3,R.dimen.margin,true));
        recyclerView.setAdapter(movieAdapter);
    }
    void setRatings()
    {
        String rt=movie.getRatings()+" Ratings";
        String ratingText=rt+"  "+movie.getYear()+"  "+movie.getRunningTime();
        Spannable spannableText = new SpannableString(ratingText);
        spannableText.setSpan(
                new RelativeSizeSpan(1.2f),
                0, rt.length(),
                SPAN_INCLUSIVE_INCLUSIVE);
        spannableText.setSpan(new StyleSpan(android.graphics.Typeface.BOLD),0,rt.length(),Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        spannableText.setSpan(new ForegroundColorSpan(Color.GREEN), 0, rt.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        ratings.setText(spannableText, TextView.BufferType.SPANNABLE);
    }
    void setDetail(TextView textView,String topic,String detail)
    {
     Spannable spannable=new SpannableString(topic+": "+detail);
     spannable.setSpan(new ForegroundColorSpan(Color.WHITE),0,topic.length(),Spannable.SPAN_INCLUSIVE_INCLUSIVE);
     textView.setText(spannable,TextView.BufferType.SPANNABLE);
    }
}
