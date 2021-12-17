package com.pradeep.app;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface Api {

    String BASE_URL = "https://reqres.in/api/";
    @GET("users")
    public Call<AUserList> getUserList(@Query("page") long id);
}
