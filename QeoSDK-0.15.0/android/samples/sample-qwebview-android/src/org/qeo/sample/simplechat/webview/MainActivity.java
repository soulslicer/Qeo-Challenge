/************** COPYRIGHT AND CONFIDENTIALITY INFORMATION *********************
 **                                                                          **
 ** Copyright (c) 2013 Technicolor                                           **
 ** All Rights Reserved                                                      **
 **                                                                          **
 ** This program contains proprietary information which is a trade           **
 ** secret of TECHNICOLOR and/or its affiliates and also is protected as     **
 ** an unpublished work under applicable Copyright laws. Recipient is        **
 ** to retain this program in confidence and is not permitted to use or      **
 ** make copies thereof other than as permitted in a written agreement       **
 ** with TECHNICOLOR, UNLESS OTHERWISE EXPRESSLY ALLOWED BY APPLICABLE LAWS. **
 **                                                                          **
 ******************************************************************************/

package org.qeo.sample.simplechat.webview;

import java.io.IOException;

import org.qeo.android.webview.QeoWebview;
import org.qeo.sample.json.qsimplechat.R;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.ConsoleMessage;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Main Activity that loads a webview. All the logic is inside the webview.
 */
public class MainActivity
    extends FragmentActivity
{
    private static final String TAG = "QeoWebviewSample";
    private QeoWebview mJSInterface = null;
    private WebView mWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mWebView = (WebView) findViewById(R.id.webView);

        // enable the Qeo javascript bindings
        mJSInterface = QeoWebview.enableQeo(getApplicationContext(), mWebView);

        // set client to handle qeo.js interception
        mWebView.setWebViewClient(new QeoWebViewClient());

        // set chromeclient to handle console logging
        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onConsoleMessage(ConsoleMessage cm)
            {
                String msg = cm.message() + " -- From line " + cm.lineNumber() + " of " + cm.sourceId();
                switch (cm.messageLevel()) {
                    case ERROR:
                        Log.e(TAG, msg);
                        break;
                    case LOG:
                    case TIP:
                        Log.i(TAG, msg);
                        break;
                    case WARNING:
                        Log.w(TAG, msg);
                        break;
                    case DEBUG:
                    default:
                        Log.d(TAG, msg);
                        break;
                }

                return true;
            }
        });

        // show the sample selection dialog
        showDialog();

    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        if (mJSInterface != null) {
            // cleanup
            mJSInterface.close();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // enable options menu for selecting a sample
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if (item.getItemId() == R.id.action_select_sample) {
            // selecting another sample
            showDialog();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showDialog()
    {
        // show the dialogfragment to select a sample

        // DialogFragment.show() will take care of adding the fragment
        // in a transaction. We also want to remove any currently showing
        // dialog, so make our own transaction and take care of that here.
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment prev = getSupportFragmentManager().findFragmentByTag("dialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        // Create and show the dialog.
        DialogFragment newFragment = new SelectSampleFragment();
        newFragment.show(ft, "dialog");
    }

    /**
     * Load an html file from the android assets folder.
     * 
     * @param asset The name of the html file.
     */
    public void loadAsset(String asset)
    {
        Log.d(TAG, "Loading asset " + asset);
        mWebView.loadUrl("file:///android_asset/" + asset + ".html");
    }

    /**
     * Load an html file from an external url.
     * 
     * @param url The qeo enabled html file.
     */
    public void loadUrl(String url)
    {
        Log.d(TAG, "Loading url " + url);
        mWebView.loadUrl(url);
    }

    // ///////////////
    // Helper classes
    // ///////////////

    private class QeoWebViewClient
        extends WebViewClient
    {

        @Override
        public WebResourceResponse shouldInterceptRequest(WebView view, String url)
        {
            if (url.endsWith("/qeo.js")) {
                // this block will ensure that if an external html file includes qeo.js
                // it gets loaded from the local assets folder so it does not need to be served on the webserver
                Log.d(TAG, "loading qeo.js from local assets folder");
                try {
                    return new WebResourceResponse("text/javascript", "utf-8", MainActivity.this.getAssets().open(
                        "qeo.js"));
                }
                catch (IOException e) {
                    Log.e(TAG, "Can't load qeo.js from assets", e);
                }
            }
            return null;
        }

    }

}
