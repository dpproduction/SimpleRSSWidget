package com.dgroup.simplersswidget.ui.activity;

import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.dgroup.simplersswidget.R;
import com.dgroup.simplersswidget.app.RSSWidgetApplication;
import com.dgroup.simplersswidget.async.DownloadRSSTask;
import com.dgroup.simplersswidget.constants.AppConstants;
import com.dgroup.simplersswidget.util.Utils;

public class ConfigActivity extends AppCompatActivity {

    private int mAppWidgetId;

    private EditText rssUriEditText;

    private String defUrl = "http://www.aweber.com/blog/feed/";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);

        rssUriEditText = (EditText)findViewById(R.id.rss_uri);
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

        rssUriEditText.setText(defUrl);
    }

    View.OnClickListener mOnClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            String rssUri = rssUriEditText.getText().toString();
            //  if(URLUtil.isValidUrl(rssUri)){

            Utils.saveToPref(ConfigActivity.this, mAppWidgetId, AppConstants.RSS_URL, rssUri);

            Uri uri = Uri.parse(rssUri);

            new DownloadRSSTask().executeOnExecutor(RSSWidgetApplication.getInstance().getThreadPoolExecutor(), uri);

            Log.i("dfsf","sdfrisdf");


//            Intent resultValue = new Intent(AppConstants.ACTION_URL_CHANGED);
//            resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
//            setResult(RESULT_OK, resultValue);
//            finish();
//                }else{
//                    Toast.makeText(ConfigActivity.this, getString(R.string.error_check_url), Toast.LENGTH_SHORT).show();
//                }
        }
    };

}
