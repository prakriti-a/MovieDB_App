package com.prakriti.moviedbapp;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.prakriti.moviedbapp.adapter.MostPopularAdapter;
import com.prakriti.moviedbapp.adapter.NowPlayingAdapter;
import com.prakriti.moviedbapp.network.ApiCaller;
import com.prakriti.moviedbapp.network.RetrofitClient;
import com.prakriti.moviedbapp.pojo.MovieInfoWrapper;
import com.prakriti.moviedbapp.pojo.ResultsClass;
import com.prakriti.moviedbapp.viewmodel.MovieViewModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
// implements event injection listener

    private String apiKey = "d704e1c019b3f0cbd05294ca7851b0a6";
    private String language = "en-US";
    private RecyclerView horizontalRecyclerView, verticalRecyclerView;
    private NowPlayingAdapter nowPlayingAdapter;
    private MostPopularAdapter mostPopularAdapter; // will use pagination
    private List<ResultsClass> myNowPlayingResultsList, myMostPopularResultsList;
    private LinearLayoutManager verticalLayoutManager, horizontalLayoutManager;
    private ApiCaller apiCaller;
    private ProgressBar progressBar;
    private Handler handler;
    private MovieViewModel movieViewModel;

    // pagination variables for pages of Most Popular movies
    private static int START_PAGE = 1;
    private static int PAGE_SIZE = 20;
    private static int TOTAL_PAGES = 5; // get from json object
    private static int currentPage = START_PAGE;
    private boolean isLoading = false;
    private boolean isLastPage = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // center text in action bar
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.custom_action_bar);

        apiCaller = RetrofitClient.getClient(this).create(ApiCaller.class);
        handler = new Handler();

        movieViewModel = new ViewModelProvider(this).get(MovieViewModel.class);

        progressBar = findViewById(R.id.progressBar);
        horizontalRecyclerView = findViewById(R.id.horizontalRecyclerView);
        verticalRecyclerView = findViewById(R.id.verticalRecyclerView);

        myNowPlayingResultsList = new ArrayList<>();
        myMostPopularResultsList = new ArrayList<>();
        mostPopularAdapter = new MostPopularAdapter(MainActivity.this, myMostPopularResultsList);
        nowPlayingAdapter = new NowPlayingAdapter(MainActivity.this, myNowPlayingResultsList);
        verticalRecyclerView.setAdapter(mostPopularAdapter);
        horizontalRecyclerView.setAdapter(nowPlayingAdapter);

        verticalLayoutManager = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.VERTICAL, false);
        horizontalLayoutManager = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL, false);
        verticalRecyclerView.setLayoutManager(verticalLayoutManager);
        horizontalRecyclerView.setLayoutManager(horizontalLayoutManager);

        // retrofit call for now playing movie list
        getNowPlayingMoviesInfo();
        movieViewModel.getLiveDataInstance().observe(this, new Observer<MovieInfoWrapper>() {
            @Override
            public void onChanged(MovieInfoWrapper movieInfoWrapper) {
                if(movieInfoWrapper != null) {
                    // set data to adapter
                    myNowPlayingResultsList = movieInfoWrapper.getResultsList();
                    Log.i(getString(R.string.TAG), "NOW PLAYING: " + myNowPlayingResultsList.size());
                    nowPlayingAdapter.addAllItems(myNowPlayingResultsList);
                }
                else {
                    Log.e(getString(R.string.TAG), "NOW PLAYING Failure");
                }
            }
        });

        // set listener for vertical recycler view -> pagination
        verticalRecyclerView.addOnScrollListener(new PageScrollListener(verticalLayoutManager) {
            @Override
            public boolean isLoadingPage() {
                return isLoading;
            }
            @Override
            public boolean isLastPage() {
                return isLastPage;
            }
            @Override
            protected void loadNext() {
                isLoading = true;
                currentPage++;
                loadMostPopularNextPage();
            }
        });

        // retrofit call for most popular movie list
        getMostPopularMoviesFirstPage(); // loads first page
        movieViewModel.getLiveDataInstance().observe(this, new Observer<MovieInfoWrapper>() {
            @Override
            public void onChanged(MovieInfoWrapper movieInfoWrapper) {
                if(movieInfoWrapper != null) {
                    progressBar.setVisibility(View.GONE);
                    myMostPopularResultsList = movieInfoWrapper.getResultsList();
                    Log.i(getString(R.string.TAG), "MOST POPULAR: PG NO " + movieInfoWrapper.getPageNumber());
                    // set total number of pages
//                    TOTAL_PAGES = response.body().getTotalPages();
                    mostPopularAdapter.addAllItems(myMostPopularResultsList);
                }
                else {
                    Log.e(getString(R.string.TAG), "MOST POPULAR Failure");
                }
            }
        });
    }

    private void getNowPlayingMoviesInfo() {
        Call<MovieInfoWrapper> movieInfo = apiCaller.getNowPlayingMovies(language, 1, apiKey); // page=undefined
        // pass call to view model
        movieViewModel.makeApiCall(movieInfo);
    }

    private void getMostPopularMoviesFirstPage() { // loads first page
        progressBar.setVisibility(View.VISIBLE);
        Call<MovieInfoWrapper> movieInfo = apiCaller.getMostPopularMovies(apiKey, language, 1); // PAGE_START
        movieViewModel.makeApiCall(movieInfo);
    }

    private void loadMostPopularNextPage() {
        progressBar.setVisibility(View.VISIBLE);
        Log.i(getString(R.string.TAG), "MOST POPULAR: CURRENT PAGE " + currentPage);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Call<MovieInfoWrapper> movieInfo = apiCaller.getMostPopularMovies(apiKey, language, currentPage);

                movieViewModel.makeApiCall(movieInfo);
//                movieViewModel.getLiveDataInstance().observe(MainActivity.this, new Observer<MovieInfoWrapper>() {
//                    @Override
//                    public void onChanged(MovieInfoWrapper movieInfoWrapper) {
//                        if(movieInfoWrapper != null) {
//                            myMostPopularResultsList = movieInfoWrapper.getResultsList();
//                            Log.i(getString(R.string.TAG), "MOST POPULAR PG NO CALLED: " + movieInfoWrapper.getPageNumber());
//                            progressBar.setVisibility(View.GONE);
//                            isLoading = false;
//                            // add items to adapter
//                            mostPopularAdapter.addAllItems(myMostPopularResultsList);
//
//                            if(currentPage == TOTAL_PAGES) {
//                                isLastPage = true;
//                                Log.i(getString(R.string.TAG), "END REACHED");
//                            }
//                        }
//                        else {
//                            Log.e(getString(R.string.TAG), "MOST POPULAR Failure");
//                        }
//                    }
//                });
            }
        }, 1500);
    }
}