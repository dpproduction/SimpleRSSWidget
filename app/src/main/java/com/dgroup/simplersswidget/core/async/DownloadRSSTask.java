package com.dgroup.simplersswidget.core.async;


import android.net.Uri;

import com.dgroup.simplersswidget.api.Api;
import com.dgroup.simplersswidget.model.RSS;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Set;

public class DownloadRSSTask extends CustomAsyncTask<Uri, Void, DownloadRSSTask.ApiResult> {

    public interface LoadingCallback {

        void onLoadingComplete(RSS rss);

        void onLoadingError(Exception e);
    }

    private WeakReference<LoadingCallback> loadingCallbackWeakReference;

    public DownloadRSSTask(LoadingCallback loadingCallback) {
        loadingCallbackWeakReference = new WeakReference<>(loadingCallback);
    }

    @Override
    protected ApiResult doInBackground(Uri... params) {
        ApiResult apiResult = new ApiResult();
        try {

            Uri rssFeedUrl = params[0];

            apiResult.mRSS = new Api(getEndpoint(rssFeedUrl))
                    .getRSSFeed(rssFeedUrl.getPath().substring(1), getQueryMap(rssFeedUrl));

        } catch (Exception e) {
            e.printStackTrace();
            apiResult.mException = e;
        }
        return apiResult;
    }

    @Override
    protected void onPostExecute(ApiResult apiResult) {
        super.onPostExecute(apiResult);
        LoadingCallback loadingCallback = loadingCallbackWeakReference.get();
        if (loadingCallback != null) {
            if (apiResult.mRSS != null) {
                loadingCallback.onLoadingComplete(apiResult.mRSS);
            } else {
                loadingCallback.onLoadingError(apiResult.mException);
            }
        }
    }

    private String getEndpoint(Uri rssFeedUrl) {
        return rssFeedUrl.getScheme() + "://" + rssFeedUrl.getHost();
    }

    private HashMap<String, String> getQueryMap(Uri rssFeedUrl) {
        HashMap<String, String> queryMap = new HashMap<>();
        Set<String> queryParameterNames = rssFeedUrl.getQueryParameterNames();

        for (String queryName : queryParameterNames) {
            String queryParameter = rssFeedUrl.getQueryParameter(queryName);
            queryMap.put(queryName, queryParameter);
        }
        return queryMap;
    }

    static class ApiResult {
        private Exception mException;
        private RSS mRSS;
    }
}
