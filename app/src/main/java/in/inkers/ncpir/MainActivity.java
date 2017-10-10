package in.inkers.ncpir;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

public class MainActivity extends AppCompatActivity {


    WebView webView;
    private String url;
    ProgressBar urlLoad;
    SwipeRefreshLayout swipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(!isConnected(MainActivity.this)) {  //checks internet connection
            Intent intent = new Intent(this, ErrorActivity.class);
            startActivity(intent);
            finish();
        }

        webView = (WebView) findViewById(R.id.webView);
        urlLoad = (ProgressBar)findViewById(R.id.progressUrl);
        swipe = (SwipeRefreshLayout) findViewById(R.id.swipe);

        webView.setWebViewClient(new MyBrowser());
        url = "http://www.jawaharlalcolleges.com/links";
        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        webView.loadUrl(url);

        swipe.setColorSchemeResources(R.color.Black);
        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                webView.reload();
            }
        });

    }

    public boolean isConnected(Context context) {

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netinfo = cm.getActiveNetworkInfo();

        if (netinfo != null && netinfo.isConnectedOrConnecting()) {
            android.net.NetworkInfo wifi = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            android.net.NetworkInfo mobile = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

            return (mobile != null && mobile.isConnectedOrConnecting()) || (wifi != null && wifi.isConnectedOrConnecting());
        }
        else
            return false;
    }

    private class MyBrowser extends WebViewClient {

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            // TODO Auto-generated method stub
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            urlLoad.setVisibility(View.VISIBLE);
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            // TODO Auto-generated method stub
            super.onPageFinished(view, url);
            webView.setVisibility(View.VISIBLE);
            urlLoad.setVisibility(View.GONE);
            swipe.setRefreshing(false);
        }

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            super.onReceivedError(view, request, error);
            webView.loadUrl("file:///android_asset/index.html");
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) { //to load last page when
        if (event.getAction() == KeyEvent.ACTION_DOWN) {    //when physical back button pressed
            switch (keyCode) {                              //instead of closing app
                case KeyEvent.KEYCODE_BACK:
                    if (webView.canGoBack()) {
                        webView.goBack();
                    } else {
                        finish();
                    }
                    return true;
            }

        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    public void aboutClick(MenuItem menuItem){
        Intent intent = new Intent(this, AboutActivity.class);
        startActivity(intent);
    }

    public void creditsClick(MenuItem menuItem){
        Intent intent = new Intent(this, CreditActivity.class);
        startActivity(intent);
    }

    public void homeClick(MenuItem menuItem){
        url = "http://www.jawaharlalcolleges.com/links";
        webView.loadUrl(url);
    }
}
