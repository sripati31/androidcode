package com.divinetechs.gotstart.Vimeo;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.divinetechs.gotstart.R;

public class VimeoActivity extends AppCompatActivity {

    WebView webview_vimeo;
    String url;
    String[] Video_ID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vimeoactivity);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            url = bundle.getString("url");
            Log.e("url", "" + url);
            Video_ID = url.split("//vimeo.com/");
            Log.e("Video_ID[1]", "" + Video_ID[1]);

            getSupportActionBar().setTitle("" + bundle.getString("title"));

        }
        webview_vimeo = (WebView) findViewById(R.id.webview_vimeo);

        webview_vimeo.getSettings().setJavaScriptEnabled(true);
        webview_vimeo.getSettings().setAllowFileAccess(true);
        webview_vimeo.getSettings().setAppCacheEnabled(true);
        webview_vimeo.getSettings().setDomStorageEnabled(true);
        webview_vimeo.getSettings().setPluginState(WebSettings.PluginState.OFF);
        webview_vimeo.getSettings().setAllowFileAccess(true);
        webview_vimeo.setWebViewClient(new WebViewClient());

        webview_vimeo.loadUrl("http://player.vimeo.com/video/" + Video_ID[1] + "?player_id=player&autoplay=1&title=0&byline=0&portrait=0&api=1");

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
