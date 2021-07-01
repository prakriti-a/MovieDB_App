package com.prakriti.moviedbapp.viewmodel;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.prakriti.moviedbapp.network.ApiCaller;
import com.prakriti.moviedbapp.network.RetrofitClient;
import com.prakriti.moviedbapp.network.RetrofitResponse;
import com.prakriti.moviedbapp.pojo.MovieInfoWrapper;

import retrofit2.Call;

public class MovieViewModel extends AndroidViewModel {

    private MutableLiveData<MovieInfoWrapper> movieResults;
    private ApiCaller apiCaller;
    private Context context;
    private String apiKey = "d704e1c019b3f0cbd05294ca7851b0a6";
    private String language = "en-US";

    public MovieViewModel(@NonNull Application application) {
        super(application);
        context = application.getApplicationContext();
        apiCaller = RetrofitClient.getClient(context).create(ApiCaller.class);
    }

    public MutableLiveData<MovieInfoWrapper> getLiveDataForHorizontalView(int pageNumber) {
        if(movieResults == null) {
            movieResults = new MutableLiveData<>();
            makeApiCallForHorizontalData(pageNumber);
        }
        return movieResults;
    }

    public MutableLiveData<MovieInfoWrapper> getLiveDataForVerticalView(int pageNumber) {
        if(movieResults == null) {
            movieResults = new MutableLiveData<>();
            makeApiCallForVerticalData(pageNumber);
        }
        return movieResults;
    }

    private void makeApiCallForHorizontalData(int pageNumber) {
        Call<MovieInfoWrapper> horizontalMovieInfo = apiCaller.getNowPlayingMovies(language, pageNumber, apiKey); // page=undefined
        movieResults = RetrofitResponse.getInstance().getCallResponse(horizontalMovieInfo);
    }

    private void makeApiCallForVerticalData(int pageNumber) {
        Call<MovieInfoWrapper> verticalMovieInfo = apiCaller.getMostPopularMovies(apiKey, language, pageNumber);
        movieResults = RetrofitResponse.getInstance().getCallResponse(verticalMovieInfo);
    }
}