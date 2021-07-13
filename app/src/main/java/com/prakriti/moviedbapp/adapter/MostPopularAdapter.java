package com.prakriti.moviedbapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.prakriti.moviedbapp.R;
import com.prakriti.moviedbapp.pojo.ResultsClass;

import java.util.List;

public class MostPopularAdapter extends RecyclerView.Adapter<MostPopularAdapter.MostPopularViewHolder> {
// Pagination code here

    private final Context context;
    private List<ResultsClass> resultList;

    public MostPopularAdapter(Context context, List<ResultsClass> resultList) {
        this.context = context;
        this.resultList = resultList;
    }

    @NonNull
    @Override
    public MostPopularViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v2 = inflater.inflate(R.layout.vertical_rv, parent, false);
        return new MostPopularViewHolder(v2);
    }

    @Override
    public void onBindViewHolder(@NonNull MostPopularViewHolder holder, int position) {
        ResultsClass result = resultList.get(position);
        holder.getMovieTitleMostPopular().setText(result.getTitle());
        holder.getMovieReleaseDateMostPopular().setText(result.getReleaseDate());
        Glide.with(context).load(result.getPosterPath()).into(holder.getImageMostPopular());
    }

    @Override
    public int getItemCount() {
        return resultList.size();
    }

    public void addAllItems(List<ResultsClass> myMostPopularResultsList) {
        for(ResultsClass item : myMostPopularResultsList) {
            resultList.add(item);
            notifyItemInserted(resultList.size() - 1);
        }
    }

    public static class MostPopularViewHolder extends RecyclerView.ViewHolder {

        private ImageView imageMostPopular;
        private TextView movieTitleMostPopular, movieReleaseDateMostPopular;

        public MostPopularViewHolder(@NonNull View itemView) {
            super(itemView);
            imageMostPopular = itemView.findViewById(R.id.imageMostPopular);
            movieTitleMostPopular = itemView.findViewById(R.id.movieTitleMostPopular);
            movieReleaseDateMostPopular = itemView.findViewById(R.id.movieReleaseDateMostPopular);
        }

        public ImageView getImageMostPopular() {
            return imageMostPopular;
        }

        public TextView getMovieTitleMostPopular() {
            return movieTitleMostPopular;
        }

        public TextView getMovieReleaseDateMostPopular() {
            return movieReleaseDateMostPopular;
        }
    }

}
