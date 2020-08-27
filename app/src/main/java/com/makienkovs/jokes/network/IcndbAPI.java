package com.makienkovs.jokes.network;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface IcndbAPI {
    @GET("jokes/random/{count}")
    Call<PostModel> getData(@Path("count") int count);
}