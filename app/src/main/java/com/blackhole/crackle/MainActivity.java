package com.blackhole.crackle;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.PopupWindow;
import android.widget.SearchView;
import android.widget.Spinner;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.flexbox.FlexboxLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
/*
* Using retrofit for fetching data and facebook shimmer library for
* shimmer effect and glide for loading images
* */
public class MainActivity extends AppCompatActivity {

    ArrayList<Movie>movies=new ArrayList<>();
    RecyclerView moviesList;
    MovieAdapter movieAdapter;
    FloatingActionButton fab;
    ShimmerFrameLayout shimmer;
    ViewGroup parent;
    SearchFragment searchFragment;
    SwipeRefreshLayout refreshLayout;
    HashMap<String,HashMap<String,Boolean>>currentFilters=new HashMap<>();
    /*
    * Initializing Views
    * Setting SwipeRefreshLayout
    * */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        parent=findViewById(R.id.container);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        moviesList =findViewById(R.id.movies_list);
        moviesList.setLayoutManager(new LinearLayoutManager(MainActivity.this,LinearLayoutManager.VERTICAL,false));
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL);
        movieAdapter=new MovieAdapter(MainActivity.this, new ArrayList<Movie>(), MovieAdapter.LIST);
        moviesList.addItemDecoration(dividerItemDecoration);
        shimmer = findViewById(R.id.shimmer_view_container);
        refreshLayout=findViewById(R.id.refresh);

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
            }
        });
        shimmer.startShimmer();
        refresh();

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createAlertDialog();
            }
        });
    }
    /*
    * Starting Shimmer when refreshing is on
    * and add movies from network to movieAdapter
    * when refrehing done
    * */
    public void refresh()
    {
        refreshLayout.setRefreshing(true);
        shimmer.startShimmer();
        Networking.getMovies(new OnDownloadComplete() {
            @Override
            public void onDownloadComplete(final ArrayList<Movie> results) {
                refreshLayout.setRefreshing(false);
                currentFilters.put("d",new HashMap<String, Boolean>());
                currentFilters.put("g",new HashMap<String, Boolean>());
                currentFilters.put("s",new HashMap<String, Boolean>());

                shimmer.stopShimmer();
                shimmer.setVisibility(View.GONE);
                moviesList.setVisibility(View.VISIBLE);
                movies.clear();
                movies.addAll(results);
                movieAdapter.addNew(results);
                moviesList.setAdapter(movieAdapter);
            }
        });

    }
    /*
    * Adding new Search Fragment when searchveiw expanded and vice-versa
    * and Developer option
    * */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        //Open Developer Website
        MenuItem developer=menu.findItem(R.id.developer);
        developer.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/singhbhavneet"));
                startActivity(browserIntent);
                return true;
            }
        });
        //Search View initialization
        final SearchView searchView =
                (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setIconifiedByDefault(false);
        searchView.setQueryHint("Search Here");
        menu.findItem(R.id.search).setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                addFragment(true);
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                addFragment(false);
                return true;
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchFragment.changeQuery(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchFragment.changeQuery(newText);
                return true;
            }
        });
        return true;
    }
    public void addFragment(boolean check)
    {
        if(check)
        {
            fab.setVisibility(View.GONE);
            searchFragment=new SearchFragment();
            getSupportFragmentManager().beginTransaction().add(R.id.container,searchFragment).addToBackStack(null).commit();
        }
        else
        {
            fab.setVisibility(View.VISIBLE);

            super.onBackPressed();
        }
    }

    @Override
    public void onBackPressed() {
        if(popup!=null&&popup.isShowing())
        {
            popup.dismiss();
            return;
        }
        super.onBackPressed();
    }
    PopupWindow popup;
    AlertDialog.Builder builder;
    /*
    * Two Spinners for Sort and Directors
    * and One FlexBoxLayout for genres
    * */
    public void createAlertDialog()
    {
        {
            builder = new AlertDialog.Builder(this);
            View view=getLayoutInflater().inflate(R.layout.filter_layout,null);
            popup = new PopupWindow(view, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
            final FlexboxLayout flexboxLayout=view.findViewById(R.id.genres);
            ArrayList<String>features= getFilters(movies,"genre");
            HashMap<String,Boolean> map=currentFilters.get("g");
            for(String s:features)
            {
                OutlineButton button=new OutlineButton(this,s);
                button.setSelected(map.containsKey(button.getText()));
                flexboxLayout.addView(button);
            }
            final Spinner spinner=view.findViewById(R.id.sort_spinner);
            ArrayAdapter<String>adapter=new ArrayAdapter<String>(this,R.layout.text, getFilters(movies,"sort"));
            spinner.setAdapter(adapter);
            final Spinner directorSpinner=view.findViewById(R.id.directors_spinner);
            ArrayAdapter<String>directorAdpater=new ArrayAdapter<String>(this,R.layout.text, getFilters(movies,"director"));
            directorSpinner.setAdapter(directorAdpater);
            builder.setTitle("Filter Results")
                    .setPositiveButton("Apply", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            ArrayList<String>genres=new ArrayList<>();
                            for(int i=0;i<flexboxLayout.getChildCount();i++)
                            {
                                OutlineButton button=(OutlineButton)flexboxLayout.getChildAt(i);
                                if(button.isSelected())
                                {
                                    genres.add(button.getText());
                                }
                            }
                            Object dItem=directorSpinner.getSelectedItem();
                            String director=dItem!=null?dItem.toString():"Any";
                            String sort=spinner.getSelectedItem().toString();
                            applyFilters(genres,director,sort);
                        }
                    })
                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // do nothing
                        }
                    })
                    .setView(view);
            builder.show();
        }

    }
    //Add Filters
    public void applyFilters(ArrayList<String>genres,String director,String sort)
    {
        currentFilters.get("g").clear();
        for (String g:genres)
        {
            currentFilters.get("g").put(g,true);
        }
        currentFilters.get("d").clear();
        currentFilters.get("d").put(director,true);
        currentFilters.get("s").clear();
        currentFilters.get("s").put(sort,true);
        movieAdapter.addNew(Movie.filterMovies(movies,director,genres,sort));
    }
    //Get Filters
    ArrayList<String> getFilters(List<Movie>movies, String key)
    {
        HashMap<String,Boolean> genres=new HashMap<>();
        if(key.equals("genre")||key.equals("director"))
        for(Movie movie:movies)
        {
            ArrayList<String> filters=new ArrayList<String>();
            if(key.equals("genre"))
            {
                filters=movie.info.genres;
            }
            else if(key.equals("director"))
            {
                filters=movie.info.directors;
            }
            if(filters!=null)
            for(String genre:filters)
            {
             genres.put(genre,false);
            }
        }
        else
        {
            genres.put("Ratings",false);
            genres.put("Rank",false);
            genres.put("Date",false);
            genres.put("Running Time",false);

        }
        ArrayList<String>arrayList=new ArrayList<>();
        if(!key.equals("genre"))
        arrayList.add("Any");
        arrayList.addAll(genres.keySet());
        HashMap d=key.equals("director")?currentFilters.get("d"):key.equals("sort")?currentFilters.get("s"):new HashMap();
        for(int i=0;i<arrayList.size();i++)
        {
            String temp=arrayList.get(i);
           if (d.containsKey(temp))
           {
               arrayList.remove(i);
               arrayList.add(0,temp);
               break;
           }
        }
        return arrayList;
    }

}
