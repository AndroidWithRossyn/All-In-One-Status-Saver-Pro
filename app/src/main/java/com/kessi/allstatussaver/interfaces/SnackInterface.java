package com.kessi.allstatussaver.interfaces;

import androidx.annotation.Keep;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;


@Keep
public interface SnackInterface {
    @GET
    Call<JsonObject> getsnackvideoresult(@Url String url);

}
