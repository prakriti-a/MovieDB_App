package com.prakriti.moviedbapp;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private String TAG = "MOVIE_DB";
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
    }

    private void getNowPlayingMoviesInfo() {
        Call<MovieInfoWrapper> movieInfo = apiCaller.getNowPlayingMovies(language, 1, apiKey); // page=undefined
        movieInfo.enqueue(new Callback<MovieInfoWrapper>() {
            @Override
            public void onResponse(Call<MovieInfoWrapper> call, Response<MovieInfoWrapper> response) {
                if(response.isSuccessful()) {
                    myNowPlayingResultsList = response.body().getResultsList();
                    Log.i(TAG, "NOW PLAYING: " + myNowPlayingResultsList.size());
                    nowPlayingAdapter.addAllItems(myNowPlayingResultsList);
                }
            }
            @Override
            public void onFailure(Call<MovieInfoWrapper> call, Throwable t) {
                Log.e(TAG, "NOW PLAYING onFailure: "+ t.getMessage());
            }
        });
    }

    private void getMostPopularMoviesFirstPage() { // loads first page
        progressBar.setVisibility(View.VISIBLE);
        Call<MovieInfoWrapper> movieInfo = apiCaller.getMostPopularMovies(apiKey, language, 1); // PAGE_START
        movieInfo.enqueue(new Callback<MovieInfoWrapper>() {
            @Override
            public void onResponse(Call<MovieInfoWrapper> call, Response<MovieInfoWrapper> response) {
                if(response.isSuccessful()) {
                    progressBar.setVisibility(View.GONE);
                    myMostPopularResultsList = response.body().getResultsList();
                    Log.i(TAG, "MOST POPULAR: PG NO " + response.body().getPageNumber());
                    // set total number of pages
//                    TOTAL_PAGES = response.body().getTotalPages();
                    mostPopularAdapter.addAllItems(myMostPopularResultsList);

                }
            }
            @Override
            public void onFailure(Call<MovieInfoWrapper> call, Throwable t) {
                Log.e(TAG, "MOST POPULAR onFailure: "+ t.getMessage());
            }
        });
    }

    private void loadMostPopularNextPage() {
        progressBar.setVisibility(View.VISIBLE);
        Log.i(TAG, "MOST POPULAR: CURRENT PAGE " + currentPage);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Call<MovieInfoWrapper> movieInfo = apiCaller.getMostPopularMovies(apiKey, language, currentPage);
                movieInfo.enqueue(new Callback<MovieInfoWrapper>() {
                    @Override
                    public void onResponse(Call<MovieInfoWrapper> call, Response<MovieInfoWrapper> response) {
                        if(response.isSuccessful()) {
                            myMostPopularResultsList = response.body().getResultsList();
                            Log.i(TAG, "MOST POPULAR: PG NO " + response.body().getPageNumber());
                            progressBar.setVisibility(View.GONE);
                            isLoading = false;
                            // add items to list in recycler adapter
                            mostPopularAdapter.addAllItems(myMostPopularResultsList);

                            if(currentPage == TOTAL_PAGES) {
                                isLastPage = true;
                                Log.i(TAG, "END REACHED");
                            }
                        }
                    }
                    @Override
                    public void onFailure(Call<MovieInfoWrapper> call, Throwable t) {
                        Log.e(TAG, "MOST POPULAR onFailure: "+ t.getMessage());
                    }
                });
            }}, 1500);
    }
}