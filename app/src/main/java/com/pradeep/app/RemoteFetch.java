package com.pradeep.app;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RemoteFetch {
    private static RemoteFetch instance = null;
    private Api myApi;

    private RemoteFetch() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(Api.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        myApi = retrofit.create(Api.class);
    }

    public static synchronized RemoteFetch getInstance() {
        if (instance == null) {
            instance = new RemoteFetch();
        }
        return instance;
    }

    public Api getMyApi() {
        return myApi;
    }
}
