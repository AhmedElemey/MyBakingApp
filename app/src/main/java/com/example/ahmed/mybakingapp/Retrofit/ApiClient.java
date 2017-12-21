package com.example.ahmed.mybakingapp.Retrofit;

public class ApiClient {
    public final static String BASE_URL = "https://d17h27t6h515a5.cloudfront.net";

    public static ApiInterface getService(){
        return GetJson.getClient(BASE_URL).create(com.example.ahmed.mybakingapp.Retrofit.ApiInterface.class);
    }
}
