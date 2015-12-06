package com.dgroup.simplersswidget.core;


import android.net.Uri;

import com.dgroup.simplersswidget.app.RSSWidgetApplication;
import com.dgroup.simplersswidget.constants.AppConstants;
import com.dgroup.simplersswidget.core.async.DownloadRSSTask;
import com.dgroup.simplersswidget.model.Article;
import com.dgroup.simplersswidget.model.RSS;
import com.dgroup.simplersswidget.receiver.RSSWidgetProvider;
import com.dgroup.simplersswidget.util.Utils;

import java.util.List;

public class RSSDataSource implements DownloadRSSTask.LoadingCallback {

    private long lastSync;

    private String currentRssUrl;

    private int widgetId;

    private List<Article> rssFeed;

    private static boolean isForceUpdate;

    private ContentListener contentListener;

    public interface ContentListener {
        void onDataUpdated();
    }

    public void setContentListener(ContentListener contentListener) {
        this.contentListener = contentListener;
    }

    @Override
    public void onLoadingComplete(RSS rss) {
        RSSWidgetProvider.updateStatus(widgetId, false, false);
        this.rssFeed = rss.getChannel().getArticleList();
        if (contentListener != null) {
            contentListener.onDataUpdated();
        }
        currentRssUrl = getRssUrl();
    }

    @Override
    public void onLoadingError(Exception e) {
        String rssUrl = getRssUrl();
        if (isNeedToChangeStatus(rssUrl)) {
            RSSWidgetProvider.updateStatus(widgetId, true, false);
            rssFeed = null;
            if (contentListener != null) {
                contentListener.onDataUpdated();
            }
        }
        currentRssUrl = rssUrl;
    }

    public Article getItem(int pos) {
        return rssFeed.get(pos);
    }

    public int getSize() {
        return rssFeed != null ? rssFeed.size() : 0;
    }

    public void reload(int widgetId) {
        this.widgetId = widgetId;
        String rssUrl = getRssUrl();
        if (!rssUrl.trim().isEmpty() && ((System.currentTimeMillis() - lastSync) > 55 * AppConstants.SECOND || isForceUpdate)) {
            if (isNeedToChangeStatus(rssUrl)) {
                RSSWidgetProvider.updateStatus(widgetId, false, true);
            }
            lastSync = System.currentTimeMillis();
            new DownloadRSSTask(this).executeOnCustomExecutor(Uri.parse(rssUrl));
        }
        isForceUpdate = false;
    }

    private boolean isNeedToChangeStatus(String rssUrl) {
        return currentRssUrl == null || !currentRssUrl.equals(rssUrl) || rssFeed == null;
    }

    public void clear() {
        contentListener = null;
    }

    public static void setIsForceUpdate(boolean isForceUpdate) {
        RSSDataSource.isForceUpdate = isForceUpdate;
    }

    private String getRssUrl() {
        return Utils.loadFromPref(RSSWidgetApplication.getInstance().getApplicationContext(), AppConstants.RSS_URL, widgetId);
    }
}
