package com.makienkovs.jokes.network;

import com.makienkovs.jokes.ui.Joke.JokeFragment;

import java.util.ArrayList;

import io.reactivex.Observable;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Downloader {

    private IcndbAPI icndbAPI;
    private ArrayList<Value> posts;

    private Downloader() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(logging)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.icndb.com")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();

        icndbAPI = retrofit.create(IcndbAPI.class);

    }

    private static Downloader instance;

    public static Downloader getInstance() {
        if (instance == null)
            instance = new Downloader();
        return instance;
    }

    public void download(int count) {
        icndbAPI.getData(count).enqueue(new Callback<PostModel>() {
            @Override
            public void onResponse(Call<PostModel> call, Response<PostModel> response) {
                if (response.body() != null) {
                    System.out.println("!!!!! Download success");
                    PostModel postModel = response.body();
                    posts = (ArrayList<Value>) postModel.getValue();
                    Observable<String> observable = Observable.just("download");
                    observable.subscribe(JokeFragment.observer);
                }
            }

            @Override
            public void onFailure(Call<PostModel> call, Throwable t) {
                System.out.println("!!!!! Download failed");
            }
        });
    }

    public ArrayList<Value> getPosts() {
        return posts;
    }
}