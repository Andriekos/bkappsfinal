package com.ndtv.bkreader_final;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.ndtv.pratap.R;


public class WebViewActivity extends AppCompatActivity {


    private WebView webView1;
    private Toolbar toolbar;
    private String title;
    private String url;
    private ProgressBar progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        title = getIntent().getExtras().getString("title");
        url = getIntent().getExtras().getString("url");
        webView1 = (WebView) findViewById(R.id.webView1);
        progress = (ProgressBar) findViewById(R.id.progressBar);

        getSupportActionBar().setTitle(title);

        if (savedInstanceState != null) {

            webView1.restoreState(savedInstanceState);
            setValue(100);
        } else {


            webView1.getSettings().setJavaScriptEnabled(true);

            webView1.setLayerType(View.LAYER_TYPE_SOFTWARE, null);

            progress.setVisibility(View.VISIBLE);
            progress.setMax(100);


            final Activity activity = this;


            webView1.setWebViewClient(new WebViewClient()

            {
                @Override
                public boolean shouldOverrideUrlLoading(WebView view,
                                                        String url) {

                    view.loadUrl(url);
                    return true;
                }
            });

            webView1.setWebChromeClient(new WebChromeClient() {
                public void onProgressChanged(WebView view, int progress) {
                    // Activities and WebViews measure progress with different scales.
                    // The progress meter will automatically disappear when we reach 100%
                    // activity.setProgress(progress * 1000);


                    WebViewActivity.this.setValue(progress);
                    super.onProgressChanged(view, progress);
                }
            });

            webView1.setWebViewClient(new WebViewClient() {
                public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                    Toast.makeText(activity, "Oh no! " + description, Toast.LENGTH_SHORT).show();
                }
            });


            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    // Code for WebView goes here
                    webView1.loadUrl(url);
                    WebViewActivity.this.progress.setProgress(0);
                }
            });
            Button btn_share=(Button)findViewById(R.id.shareit);
            btn_share.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    shareIt();
                }
            });


        }



    }
    private void shareIt() {
//sharing implementation here
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Berita Kebumen");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, title + "\n\n" + url + "\n\n" + "Berita Kebumen News Reader");
        startActivity(Intent.createChooser(sharingIntent, "Bagikan Via"));
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        ((WebView) findViewById(R.id.webView1)).saveState(outState);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // Check if the key event was the Back button and if there's history
        if ((keyCode == KeyEvent.KEYCODE_BACK) && webView1.canGoBack()) {
            webView1.goBack();
            return true;
        }
        // If it wasn't the Back key or there's no web page history, bubble up to the default
        // system behavior (probably exit the activity)
        return super.onKeyDown(keyCode, event);
    }

    public void setValue(int newProgress) {
        this.progress.setProgress(newProgress);

        if (newProgress == 100) {
            this.progress.setVisibility(View.GONE);

        } else {
            this.progress.setVisibility(View.VISIBLE);
        }


    }

}
