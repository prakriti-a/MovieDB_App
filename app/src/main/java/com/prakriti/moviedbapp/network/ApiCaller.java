package com.prakriti.moviedbapp.network;

import com.prakriti.moviedbapp.pojo.MovieInfoWrapper;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiCaller {

//    "https://api.themoviedb.org/3/movie/now_playing?language=en-US&page=undefined&api_key=d704e1c019b3f0cbd05294ca7851b0a6"
//    "https://api.themoviedb.org/3/movie/popular?api_key=d704e1c019b3f0cbd05294ca7851b0a6&language=en-US&page=1"

//    private String apiKey = "d704e1c019b3f0cbd05294ca7851b0a6";
//    private String language = "en-US";

    String BASE_URL = "https://api.themoviedb.org/3/movie/"; // use page to change pages


    @GET("now_playing") // horizontal rv
    Call<MovieInfoWrapper> getNowPlayingMovies(@Query("language") String language, @Query("page") String page, @Query("api_key") String api_key);

    @GET("popular") // vertical rv
    Call<MovieInfoWrapper> getMostPopularMovies(@Query("api_key") String api_key, @Query("language") String language, @Query("page") int page);
}
