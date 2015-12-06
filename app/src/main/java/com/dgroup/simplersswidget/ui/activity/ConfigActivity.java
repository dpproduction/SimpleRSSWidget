package com.dgroup.simplersswidget.ui.activity;

import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import com.dgroup.simplersswidget.R;
import com.dgroup.simplersswidget.constants.AppConstants;
import com.dgroup.simplersswidget.receiver.RSSWidgetProvider;
import com.dgroup.simplersswidget.util.Utils;

public class ConfigActivity extends AppCompatActivity {

    private int mAppWidgetId;

    private AutoCompleteTextView urlAutoCompleteTextView;

    final String[] urls = { "ttp://www.aweber.com/blog/feed/",
            "http://www.vedomosti.ru/rss/themes/finance.xml",
            "http://ria.ru/export/rss2/politics/index.xml",
            "http://news.yandex.ru/auto.rss",
            "http://news.yandex.ru/world.rss",
            "http://news.yandex.ru/fashion.rss",
            "http://feeds.bbci.co.uk/news/world/rss.xml" };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);

        urlAutoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.rss_uri);
        urlAutoCompleteTextView.setAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, urls));

        findViewById(R.id.ok_btn).setOnClickListener(mOnClickListener);

        Intent intent = getIntent();

        Bundle extras = intent.getExtras();
        if (extras != null) {
            mAppWidgetId = extras.getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        if (mAppWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish();
        }
    }

    View.OnClickListener mOnClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            String rssUri = urlAutoCompleteTextView.getText().toString();
            if (URLUtil.isValidUrl(rssUri)) {

                Utils.saveToPref(ConfigActivity.this, mAppWidgetId, AppConstants.RSS_URL, rssUri);

                RSSWidgetProvider.syncData(mAppWidgetId);

                Intent resultValue = new Intent();
                resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
                setResult(RESULT_OK, resultValue);
                finish();
            } else {
                Toast.makeText(ConfigActivity.this, getString(R.string.error_check_url), Toast.LENGTH_SHORT).show();
            }
        }
    };

}
