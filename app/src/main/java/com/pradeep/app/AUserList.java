package com.pradeep.app;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class AUserList {
    @SerializedName("page")
    public Integer page;
    @SerializedName("per_page")
    public Integer perPage;
    @SerializedName("total")
    public Integer total;
    @SerializedName("total_pages")
    public Integer totalPages;
    @SerializedName("data")
    public List<AUser> data = new ArrayList();

    @Override
    public String toString() {
        return "AUserList{" +
                "page=" + page +
                ", perPage=" + perPage +
                ", total=" + total +
                ", totalPages=" + totalPages +
                ", data=" + data +
                '}';
    }
}
