package com.choliy.igor.flickrgallery.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.choliy.igor.flickrgallery.FlickrConstants;
import com.choliy.igor.flickrgallery.R;
import com.choliy.igor.flickrgallery.util.AnimUtils;
import com.choliy.igor.flickrgallery.util.FabUtils;
import com.github.clans.fab.FloatingActionMenu;
import com.wang.avi.AVLoadingIndicatorView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class WebPictureActivity extends BroadcastActivity {

    @BindView(R.id.text_picture_title) TextView mPictureTitle;
    @BindView(R.id.layout_no_uri) LinearLayout mNoUriLayout;
    @BindView(R.id.progress_view) AVLoadingIndicatorView mProgressView;
    @BindView(R.id.web_view_picture) WebView mWebView;
    @BindView(R.id.fab_menu) FloatingActionMenu mFabMenu;
    private String mItemUri = FlickrConstants.STRING_EMPTY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture_web);
        ButterKnife.bind(this);
        setupUi();
        setupWebView();
    }

    @Override
    protected void onStart() {
        super.onStart();
        AnimUtils.animateView(this, mFabMenu, Boolean.TRUE);
    }

    private void setupUi() {
        Intent intent = getIntent();
        if (intent != null) {
            String pictureTitle = intent.getStringExtra(FlickrConstants.TITLE_KEY);
            mItemUri = intent.getStringExtra(FlickrConstants.URI_KEY);
            if (pictureTitle.isEmpty())
                pictureTitle = getString(R.string.text_picture_empty);
            mPictureTitle.setText(pictureTitle);
        }
    }

    private void setupWebView() {
        if (mItemUri.isEmpty()) {
            mNoUriLayout.setVisibility(View.VISIBLE);
            return;
        }
        mWebView.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
        mWebView.getSettings().setJavaScriptEnabled(Boolean.TRUE);
        mWebView.getSettings().setLoadWithOverviewMode(Boolean.TRUE);
        mWebView.getSettings().setUseWideViewPort(Boolean.TRUE);
        mWebView.getSettings().setBuiltInZoomControls(Boolean.TRUE);
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return Boolean.FALSE;
            }
        });
        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100) mProgressView.smoothToHide();
                else mProgressView.smoothToShow();
            }
        });
        mWebView.loadUrl(mItemUri);
    }

    @OnClick({R.id.fab_browser, R.id.fab_share, R.id.fab_copy})
    public void onFabClicked(View view) {
        switch (view.getId()) {
            case R.id.fab_browser:
                mFabMenu.close(Boolean.TRUE);
                FabUtils.browserUrl(this, mItemUri);
                break;
            case R.id.fab_share:
                mFabMenu.close(Boolean.TRUE);
                FabUtils.shareUrl(this, mItemUri);
                break;
            case R.id.fab_copy:
                mFabMenu.close(Boolean.TRUE);
                FabUtils.copyUrl(this, mItemUri);
                Toast.makeText(this, getString(R.string.fab_copied, mItemUri), Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (mWebView.canGoBack()) mWebView.goBack();
        else finishActivity();
    }

    @OnClick(R.id.ic_return_picture)
    public void onReturnClick() {
        finishActivity();
    }

    private void finishActivity() {
        NavUtils.navigateUpFromSameTask(this);
        finish();
    }
}