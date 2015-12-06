package com.dgroup.simplersswidget.ui.widget;


import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.text.Html;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.dgroup.simplersswidget.R;
import com.dgroup.simplersswidget.app.RSSWidgetApplication;
import com.dgroup.simplersswidget.constants.AppConstants;
import com.dgroup.simplersswidget.core.RSSDataSource;

public class WidgetService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new ViewFactory(intent);
    }

    private static class ViewFactory implements RemoteViewsService.RemoteViewsFactory, RSSDataSource.ContentListener {

        private RSSDataSource mRssDataSource;

        private int mInstanceId = AppWidgetManager.INVALID_APPWIDGET_ID;

        public ViewFactory(Intent intent) {
            mInstanceId = intent.getIntExtra(
                    AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);

            mRssDataSource = new RSSDataSource();
            mRssDataSource.setContentListener(this);
        }

        @Override
        public void onCreate() {
        }

        @Override
        public void onDataSetChanged() {
            mRssDataSource.reload(mInstanceId);
        }

        @Override
        public void onDestroy() {
            mRssDataSource.clear();
            mRssDataSource = null;
        }

        @Override
        public int getCount() {
            return mRssDataSource.getSize();
        }

        @Override
        public RemoteViews getViewAt(int position) {
            if(mRssDataSource.getSize()>0) {
                RemoteViews page = new RemoteViews(RSSWidgetApplication.getInstance().getPackageName(), R.layout.page);

                page.setTextViewText(R.id.title, Html.fromHtml(mRssDataSource.getItem(position).getTitle()));
                page.setTextViewText(R.id.desc, Html.fromHtml(mRssDataSource.getItem(position).getDescription()));
                page.setTextViewText(R.id.count, String.valueOf((position + 1) + " / " + mRssDataSource.getSize()));

                Intent clickIntent = new Intent();
                clickIntent.putExtra(AppConstants.RSS_URL, mRssDataSource.getItem(position).getLink());
                page.setOnClickFillInIntent(R.id.desc, clickIntent);

                return page;
            }else{
                return getLoadingView();
            }
        }

        @Override
        public RemoteViews getLoadingView() {
            return new RemoteViews(
                    RSSWidgetApplication.getInstance().getPackageName(),
                    R.layout.empty_layout);
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        @Override
        public void onDataUpdated() {
            AppWidgetManager.getInstance(RSSWidgetApplication.getInstance())
                    .notifyAppWidgetViewDataChanged(
                            mInstanceId, R.id.page_flipper);
        }

    }

}
