package com.prakriti.moviedbapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MostPopularAdapter extends RecyclerView.Adapter<MostPopularAdapter.MostPopularViewHolder> {
// Pagination code here

    private Context context;
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
        holder.movieTitleMostPopular.setText(result.getTitle());
        holder.movieReleaseDateMostPopular.setText(result.getReleaseDate());
        Glide.with(context).load(result.getPosterPath()).into(holder.imageMostPopular);
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

    public class MostPopularViewHolder extends RecyclerView.ViewHolder {

        ImageView imageMostPopular;
        TextView movieTitleMostPopular, movieReleaseDateMostPopular;

        public MostPopularViewHolder(@NonNull View itemView) {
            super(itemView);
            imageMostPopular = itemView.findViewById(R.id.imageMostPopular);
            movieTitleMostPopular = itemView.findViewById(R.id.movieTitleMostPopular);
            movieReleaseDateMostPopular = itemView.findViewById(R.id.movieReleaseDateMostPopular);
        }
    }

}
