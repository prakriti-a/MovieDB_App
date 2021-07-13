package com.prakriti.moviedbapp.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.prakriti.moviedbapp.repository.RetrofitResponse;
import com.prakriti.moviedbapp.pojo.MovieInfoWrapper;

public class MovieViewModel extends ViewModel {

    private final RetrofitResponse retrofitResponse = RetrofitResponse.getInstance();

    private MutableLiveData<MovieInfoWrapper> mostPopularMovieResults; // vertical
    private MutableLiveData<MovieInfoWrapper> nowPlayingMovieResults; // horizontal

    public MutableLiveData<MovieInfoWrapper> getLiveDataForHorizontalView() {
        if(nowPlayingMovieResults == null) {
            nowPlayingMovieResults = new MutableLiveData<>();
        }
        return nowPlayingMovieResults;
    }

    public MutableLiveData<MovieInfoWrapper> getLiveDataForVerticalView() {
        if(mostPopularMovieResults == null) {
            mostPopularMovieResults = new MutableLiveData<>();
        }
        return mostPopularMovieResults;
    }

    public void makeApiCallForHorizontalData() {
        nowPlayingMovieResults = retrofitResponse.getNowPlayingResponse();
    }

    public void makeApiCallForVerticalData(int pageNumber) {
        mostPopularMovieResults = retrofitResponse.getMostPopularResponse(pageNumber);
    }
}