package com.prakriti.moviedbapp.repository;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.prakriti.moviedbapp.network.ApiCaller;
import com.prakriti.moviedbapp.network.RetrofitClient;
import com.prakriti.moviedbapp.pojo.MovieInfoWrapper;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RetrofitResponse {

    private static RetrofitResponse retrofitResponse;

    private MutableLiveData<MovieInfoWrapper> mostPopularMovieResults = new MutableLiveData<>(); // vertical
    private MutableLiveData<MovieInfoWrapper> nowPlayingMovieResults = new MutableLiveData<>(); // horizontal

    private ApiCaller apiCaller = RetrofitClient.getClient().create(ApiCaller.class);

    private final String apiKey = "d704e1c019b3f0cbd05294ca7851b0a6";
    private String language = "en-US";
    private final String horizontalPage = "undefined";

    public final static String TAG = "RetrofitResponse";

    public static RetrofitResponse getInstance() {
        if(retrofitResponse == null) {
            retrofitResponse = new RetrofitResponse();
        }
        return retrofitResponse;
    }

    public MutableLiveData<MovieInfoWrapper> getNowPlayingResponse() { // Horizontal
        Call<MovieInfoWrapper> horizontalMovieInfo = apiCaller.getNowPlayingMovies(language, horizontalPage, apiKey);
        horizontalMovieInfo.enqueue(new Callback<MovieInfoWrapper>() {
            @Override
            public void onResponse(Call<MovieInfoWrapper> call, Response<MovieInfoWrapper> response) {
                if(response.isSuccessful()) {
                    nowPlayingMovieResults.setValue(response.body());
                }
            }
            @Override
            public void onFailure(Call<MovieInfoWrapper> call, Throwable t) {
                Log.e(TAG, "NowPlaying Response Failure: "+ t.getMessage());
            }
        });
        return nowPlayingMovieResults;
    }

    public MutableLiveData<MovieInfoWrapper> getMostPopularResponse(int pageNumber) { // Vertical
        Call<MovieInfoWrapper> verticalMovieInfo = apiCaller.getMostPopularMovies(apiKey, language, pageNumber);
        verticalMovieInfo.enqueue(new Callback<MovieInfoWrapper>() {
            @Override
            public void onResponse(Call<MovieInfoWrapper> call, Response<MovieInfoWrapper> response) {
                if(response.isSuccessful()) {
                    mostPopularMovieResults.setValue(response.body());
                }
            }
            @Override
            public void onFailure(Call<MovieInfoWrapper> call, Throwable t) {
                Log.e(TAG, "MostPopular Response Failure: "+ t.getMessage());
            }
        });
        return mostPopularMovieResults;
    }
}