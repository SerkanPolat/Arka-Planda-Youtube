package com.example.sp.applicationyoutube;

import android.webkit.WebView;
import android.webkit.WebViewClient;

public class Browser extends WebViewClient
{
    @Override
    public boolean shouldOverrideUrlLoading(WebView paramWebView, String paramString)
    {
        paramWebView.loadUrl(paramString);
        return true;
    }
}