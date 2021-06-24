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

//    private static final int LOADING_BAR = 0;
//    private static final int NEXT_ITEM = 1;

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
//        switch (viewType) {
//            case LOADING_BAR:
//                View v1 = inflater.inflate(R.layout.progress_bar_layout, parent, false);
//                new ProgressBarViewHolder(v1);
//                break;
//            case NEXT_ITEM:
//                View v2 = inflater.inflate(R.layout.vertical_rv, parent, false);
//                return new MostPopularViewHolder(v2);
//            break;
//        }
    }

    @Override
    public void onBindViewHolder(@NonNull MostPopularViewHolder holder, int position) {
        ResultsClass result = resultList.get(position);
        holder.movieTitleMostPopular.setText(result.getTitle());
        holder.movieReleaseDateMostPopular.setText(result.getReleaseDate());
        Glide.with(context).load(result.getPosterPath()).into(holder.imageMostPopular);
//        Picasso.get().load(result.getPosterPath()).into(holder.imageMostPopular);
    }

    @Override
    public int getItemCount() {
        return resultList.size();
    }

//    @Override
//    public int getItemViewType(int position) {
//        if(position == resultList.size() - 1) // end of list reached
//            return LOADING_BAR;
//        else
//            return NEXT_ITEM;
//    }

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

//    public class ProgressBarViewHolder extends RecyclerView.ViewHolder {
//
//        ProgressBar progressBar;
//
//        public ProgressBarViewHolder(@NonNull View itemView) {
//            super(itemView);
//            progressBar = itemView.findViewById(R.id.progressBar);
//        }
//    }
}
