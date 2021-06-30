package com.prakriti.moviedbapp.pojo;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MovieInfoWrapper {

    @SerializedName("page")
    private int pageNumber;
    @SerializedName("results")
    private List<ResultsClass> resultsList;
    @SerializedName("total_pages")
    private int totalPages;

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public List<ResultsClass> getResultsList() {
        return resultsList;
    }

    public void setResultsList(List<ResultsClass> resultsList) {
        this.resultsList = resultsList;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }
}
