package com.dgroup.simplersswidget.api;

import com.dgroup.simplersswidget.model.RSS;
import com.squareup.okhttp.OkHttpClient;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import retrofit.RestAdapter;
import retrofit.client.OkClient;
import retrofit.converter.SimpleXMLConverter;


public class Api {

    private final static int TIMEOUT = 30;

    private IApi mApiInterface;

    private Api(){}

    public Api(String endpoint){
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setClient(new OkClient(getOkHttpClient()))
                        .setLogLevel(RestAdapter.LogLevel.FULL )
                        .setEndpoint(endpoint)
                        .setConverter(new SimpleXMLConverter())
                .build();
        mApiInterface = restAdapter.create(IApi.class);
    }

    private OkHttpClient getOkHttpClient() {
        OkHttpClient client = new OkHttpClient();
        client.setConnectTimeout(TIMEOUT, TimeUnit.SECONDS);
        client.setReadTimeout(TIMEOUT, TimeUnit.SECONDS);
        return client;
    }

    public RSS getRSSFeed(String path, Map<String, String> queryMap) throws Exception{
        return mApiInterface.getRSSFeed(path, queryMap);
    }
}
