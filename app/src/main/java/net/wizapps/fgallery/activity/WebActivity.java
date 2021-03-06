package net.wizapps.fgallery.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionMenu;
import com.wang.avi.AVLoadingIndicatorView;

import net.wizapps.fgallery.R;
import net.wizapps.fgallery.base.BaseActivity;
import net.wizapps.fgallery.tool.Constants;
import net.wizapps.fgallery.util.FabUtils;

import butterknife.BindView;
import butterknife.OnClick;

public class WebActivity extends BaseActivity {

    @BindView(R.id.picture_owner) TextView mPictureOwner;
    @BindView(R.id.layout_no_uri) View mNoUriLayout;
    @BindView(R.id.progress_view) AVLoadingIndicatorView mProgressView;
    @BindView(R.id.web_view_picture) WebView mWebView;
    @BindView(R.id.fab_menu_web) FloatingActionMenu mFabMenu;
    @BindView(R.id.fence_web_view) View mFenceView;

    private static final int ONE_HUNDRED_PERCENT = 100;
    private String mItemUri = Constants.EMPTY;

    @Override
    protected int layoutRes() {
        return R.layout.activity_web;
    }

    @Override
    protected void setUi(Bundle savedInstanceState) {
        getPictureOwner();
        setupWebView();
        setupFab();
    }

    @Override
    public void onBackPressed() {
        if (mWebView.canGoBack()) mWebView.goBack();
        if (mFabMenu.isOpened()) {
            mFenceView.setVisibility(View.INVISIBLE);
            mFabMenu.close(Boolean.TRUE);
        } else {
            super.onBackPressed();
        }
    }

    @OnClick({R.id.fab_browser, R.id.fab_share, R.id.fab_copy})
    public void onFabClicked(View view) {
        switch (view.getId()) {
            case R.id.fab_browser:
                FabUtils.browserUrl(this, mItemUri);
                break;
            case R.id.fab_share:
                FabUtils.shareUrl(this, mItemUri);
                break;
            case R.id.fab_copy:
                FabUtils.copyData(this, mItemUri);
                Toast.makeText(this, getString(R.string.fab_copied, mItemUri), Toast.LENGTH_SHORT).show();
                break;
        }
        mFabMenu.close(Boolean.TRUE);
    }

    @OnClick(R.id.picture_return)
    public void onReturnClick() {
        finish();
    }

    private void getPictureOwner() {
        Intent intent = getIntent();
        if (intent != null) {
            String pictureOwner = intent.getStringExtra(Constants.OWNER_KEY);
            mItemUri = intent.getStringExtra(Constants.URI_KEY);
            if (pictureOwner.isEmpty())
                pictureOwner = getString(R.string.text_empty_title);
            mPictureOwner.setText(pictureOwner);
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
                if (newProgress == ONE_HUNDRED_PERCENT) mProgressView.smoothToHide();
                else mProgressView.smoothToShow();
            }
        });
        mWebView.loadUrl(mItemUri);
    }

    private void setupFab() {
        mFabMenu.setOnMenuButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!mFabMenu.isOpened()) {
                    mFenceView.setVisibility(View.VISIBLE);
                    mFabMenu.open(Boolean.TRUE);
                } else {
                    mFenceView.setVisibility(View.INVISIBLE);
                    mFabMenu.close(Boolean.TRUE);
                }
            }
        });
    }
}