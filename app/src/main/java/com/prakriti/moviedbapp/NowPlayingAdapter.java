package com.prakriti.moviedbapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import java.util.List;

public class NowPlayingAdapter extends RecyclerView.Adapter<NowPlayingAdapter.NowPlayingViewHolder> {

    private Context context;
    private List<ResultsClass> resultList;

    public NowPlayingAdapter(Context context, List<ResultsClass> resultList) {
        this.context = context;
        this.resultList = resultList;
    }

    @NonNull
    @Override
    public NowPlayingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.horizontal_rv, parent, false);
        return new NowPlayingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NowPlayingViewHolder holder, int position) {
        ResultsClass result = resultList.get(position);
        Glide.with(context).load(result.getPosterPath()).into(holder.imageNowPlaying);
//        Picasso.get().load(result.getPosterPath()).into(holder.imageNowPlaying);
    }

    @Override
    public int getItemCount() {
        return resultList.size();
    }

    public void addAllItems(List<ResultsClass> myNowPlayingResultsList) {
        for(ResultsClass item : myNowPlayingResultsList) {
            resultList.add(item);
            notifyItemInserted(resultList.size() - 1);
        }
    }

    public class NowPlayingViewHolder extends RecyclerView.ViewHolder {

        ImageView imageNowPlaying;

        public NowPlayingViewHolder(@NonNull View itemView) {
            super(itemView);
            imageNowPlaying = itemView.findViewById(R.id.imageNowPlaying);
        }
    }
}
