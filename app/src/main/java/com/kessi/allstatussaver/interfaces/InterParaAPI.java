package com.kessi.allstatussaver.interfaces;

import com.google.gson.JsonObject;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Url;

public interface InterParaAPI {
    @GET
    Observable<JsonObject> getInsData(@Url String Value, @Header("Cookie") String cookie, @Header("User-Agent") String userAgent);

}