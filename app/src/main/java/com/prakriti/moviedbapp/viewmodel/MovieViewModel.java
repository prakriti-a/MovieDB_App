package com.prakriti.moviedbapp.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.prakriti.moviedbapp.network.RetrofitResponse;
import com.prakriti.moviedbapp.pojo.MovieInfoWrapper;

import retrofit2.Call;

public class MovieViewModel extends ViewModel {

    private MutableLiveData<MovieInfoWrapper> movieResults;

    public MutableLiveData<MovieInfoWrapper> getLiveDataInstance() {
        if(movieResults == null) {
            movieResults = new MutableLiveData<>();
        }
        return movieResults;
    }

    public void makeApiCall(Call<MovieInfoWrapper> call) {
        movieResults = RetrofitResponse.getInstance().getCallResponse(call);
    }
}