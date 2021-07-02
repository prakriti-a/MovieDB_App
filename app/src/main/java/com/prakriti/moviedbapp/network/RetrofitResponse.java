package com.prakriti.moviedbapp.network;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.prakriti.moviedbapp.pojo.MovieInfoWrapper;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RetrofitResponse {

    private static RetrofitResponse retrofitResponse;
    private MutableLiveData<MovieInfoWrapper> movieResultData = new MutableLiveData<>();

    public static RetrofitResponse getInstance() {
        if(retrofitResponse == null) {
            retrofitResponse = new RetrofitResponse();
        }
        return retrofitResponse;
    }

    public MutableLiveData<MovieInfoWrapper> getCallResponse(Call<MovieInfoWrapper> call) {
        call.enqueue(new Callback<MovieInfoWrapper>() {
            @Override
            public void onResponse(Call<MovieInfoWrapper> call, Response<MovieInfoWrapper> response) {
                if(response.isSuccessful()) {
                    movieResultData.setValue(response.body());
                    // EIM
                }
            }
            @Override
            public void onFailure(Call<MovieInfoWrapper> call, Throwable t) {
                Log.e("RetrofitResponse", "Retrofit Call Response Failure: "+ t.getMessage());
            }
        });
        return movieResultData;
    }
}