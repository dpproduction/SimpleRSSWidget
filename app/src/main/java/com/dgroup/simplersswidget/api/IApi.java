package com.dgroup.simplersswidget.api;

import com.dgroup.simplersswidget.model.RSS;

import java.util.Map;

import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.QueryMap;


public interface IApi {

    @GET("/{path}")
    RSS getRSSFeed(
            @Path(value = "path", encode = false) String path,
            @QueryMap Map<String,String> queryMap
    );

}
