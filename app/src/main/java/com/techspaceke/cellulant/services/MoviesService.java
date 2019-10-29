package com.techspaceke.cellulant.services;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.techspaceke.cellulant.BuildConfig;
import com.techspaceke.cellulant.Constants;
import com.techspaceke.cellulant.models.Movies;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MoviesService {

    public static void getPopularMovies(Callback callback) {
        OkHttpClient client = new OkHttpClient.Builder().build();

        HttpUrl.Builder urlBuilder = HttpUrl.parse(Constants.MOVIES_DB_BASE_URL+"/popular").newBuilder();
        urlBuilder.setQueryParameter("api_key", BuildConfig.API_kEY);
        urlBuilder.addQueryParameter("language","en-US");
        String url = urlBuilder.build().toString();

        Request request = new Request.Builder()
                .url(url)
                .build();

        Call call = client.newCall(request);
        call.enqueue(callback);
    }

    public static List<Movies> processResults(Response response){
        List<Movies> movies = new ArrayList<>();
        Gson gson = new Gson();


        try{
            String data = response.body().string();
            JSONObject dataJson = new JSONObject(data);
            JSONArray moviesArray = dataJson.getJSONArray("results");
            if (response.isSuccessful()){
                Type listType = new TypeToken<List<Movies>>(){}.getType();
                movies = gson.fromJson(moviesArray.toString(),listType);
            }

        }catch (IOException e){
            e.printStackTrace();
        }catch (JSONException e){
            e.printStackTrace();
        }

        return movies;
    }
}
