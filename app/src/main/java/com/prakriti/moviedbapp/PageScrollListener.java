package com.prakriti.moviedbapp;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public abstract class PageScrollListener extends RecyclerView.OnScrollListener {

    private LinearLayoutManager linearLayoutManager;
    private int visibleItems, totalItems, firstVisibleItemPos;

    public PageScrollListener(LinearLayoutManager linearLayoutManager) {
        this.linearLayoutManager = linearLayoutManager;
    }

    @Override
    public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        // detect vertical scroll - load next page if end reached
        if(dy > 0) {
            totalItems = linearLayoutManager.getItemCount(); // number of items in adapter bound to parent RV
            visibleItems = linearLayoutManager.getChildCount(); // number of child views attached to parent RV
            firstVisibleItemPos = linearLayoutManager.findFirstVisibleItemPosition(); // adapter position of first visible view

            if(!isLastPage() && !isLoadingPage()) {
                // if page is not currently loading &is not last page, load more pages
                if(firstVisibleItemPos>=0 && (visibleItems + firstVisibleItemPos) >= totalItems) {
                    loadNext();
                }
            }
        }
    }

    public abstract boolean isLoadingPage();
    public abstract boolean isLastPage();
    protected abstract void loadNext();
}
