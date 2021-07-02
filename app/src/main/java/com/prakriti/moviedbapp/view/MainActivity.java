package com.prakriti.moviedbapp.view;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.prakriti.moviedbapp.adapter.PageScrollListener;
import com.prakriti.moviedbapp.R;
import com.prakriti.moviedbapp.adapter.MostPopularAdapter;
import com.prakriti.moviedbapp.adapter.NowPlayingAdapter;
import com.prakriti.moviedbapp.pojo.MovieInfoWrapper;
import com.prakriti.moviedbapp.pojo.ResultsClass;
import com.prakriti.moviedbapp.viewmodel.MovieViewModel;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
// here event injection listener

    public final static String TAG = "MainActivity";

    private RecyclerView horizontalRecyclerView, verticalRecyclerView;
    private NowPlayingAdapter nowPlayingAdapter;
    private MostPopularAdapter mostPopularAdapter; // will use pagination
    private List<ResultsClass> myNowPlayingResultsList, myMostPopularResultsList;
    private LinearLayoutManager verticalLayoutManager, horizontalLayoutManager;
    private ProgressBar progressBar;
    private Handler handler;
    private MovieViewModel movieViewModel;

    // pagination variables for Most Popular movies
    private static final int START_PAGE = 1;
//    private static int PAGE_SIZE = 20;
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

        // NOW PLAYING horizontal list
        movieViewModel.getLiveDataForHorizontalView(1).observe(this, new Observer<MovieInfoWrapper>() {
            @Override
            public void onChanged(MovieInfoWrapper movieInfoWrapper) {
                if(movieInfoWrapper != null) {
                    // set data to adapter
                    myNowPlayingResultsList = movieInfoWrapper.getResultsList();
                    Log.i(TAG, "NOW PLAYING PG: " + movieInfoWrapper.getPageNumber());
                    nowPlayingAdapter.addAllItems(myNowPlayingResultsList);
                }
                else {
                    Log.e(TAG, "NOW PLAYING Failure");
                }
            }
        });

        // listener for vertical RV -> pagination
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
                loadMostPopularNextPage(currentPage);
//                Log.i(TAG, "loadNext() called = " + currentPage);
            }
        });

        // MOST POPULAR vertical list
        setMostPopularMoviesProgress();
        movieViewModel.getLiveDataForVerticalView().observe(this, new Observer<MovieInfoWrapper>() { // observe changes
            @Override
            public void onChanged(MovieInfoWrapper movieInfoWrapper) {
                if(movieInfoWrapper != null) {
                    progressBar.setVisibility(View.GONE);
                    isLoading = false;
                    myMostPopularResultsList = movieInfoWrapper.getResultsList();
                    Log.i(TAG, "MOST POPULAR: PG NO " + movieInfoWrapper.getPageNumber());
                    // to set total pages
//                    TOTAL_PAGES = response.body().getTotalPages();
                    mostPopularAdapter.addAllItems(myMostPopularResultsList);
                    if(currentPage == TOTAL_PAGES) {
                        isLastPage = true;
                        Log.i(TAG, "END REACHED");
                    }
                }
                else {
                    Log.e(TAG, "MOST POPULAR Failure");
                }
            }
        });
//        movieViewModel.makeApiCallForVerticalData(START_PAGE);
    }


    private void setMostPopularMoviesProgress() {
        progressBar.setVisibility(View.VISIBLE);
//        Call<MovieInfoWrapper> movieInfo = apiCaller.getMostPopularMovies(apiKey, language, 1); // PAGE_START
//        movieViewModel.makeApiCall(movieInfo);
    }


    private void loadMostPopularNextPage(int currentPageNumber) {
        progressBar.setVisibility(View.VISIBLE);
        Log.i(TAG, "CURRENT PAGE: " + currentPageNumber);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                movieViewModel.makeApiCallForVerticalData(currentPageNumber); // next page is fetched and changes observed above
            }}, 1500);
    }

}