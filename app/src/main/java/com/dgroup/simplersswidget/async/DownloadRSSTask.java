package com.dgroup.simplersswidget.async;


import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.dgroup.simplersswidget.api.Api;
import com.dgroup.simplersswidget.model.RSS;

import java.util.HashMap;
import java.util.Set;

public class DownloadRSSTask extends AsyncTask<Uri, Void, DownloadRSSTask.ApiResult>{

    @Override
    protected ApiResult doInBackground(Uri... params) {
        ApiResult apiResult = new ApiResult();
        try {
            Uri rssFeedUrl = params[0];

            apiResult.mRSS = new Api(getEndpoint(rssFeedUrl))
                    .getRSSFeed(rssFeedUrl.getPath().substring(1), getQueryMap(rssFeedUrl));

        }catch (Exception e){
            e.printStackTrace();
            apiResult.mException = e;
        }
        return apiResult;
    }

    @Override
    protected void onPostExecute(ApiResult apiResult) {
        super.onPostExecute(apiResult);
        Log.i("ololo","dfsfsf");

    }

    private String getEndpoint(Uri rssFeedUrl){
        return rssFeedUrl.getScheme()+"://"+rssFeedUrl.getHost();
    }

    private HashMap<String, String> getQueryMap(Uri rssFeedUrl){
        HashMap<String, String> queryMap = new HashMap<>();
        Set<String> queryParameterNames = rssFeedUrl.getQueryParameterNames();

        for (String queryName : queryParameterNames) {
            String queryParameter = rssFeedUrl.getQueryParameter(queryName);
            queryMap.put(queryName, queryParameter);
        }
        return queryMap;
    }

    static class ApiResult{
        private Exception mException;
        private RSS mRSS;
    }
}
