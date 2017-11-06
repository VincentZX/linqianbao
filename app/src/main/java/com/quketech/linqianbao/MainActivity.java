package com.quketech.linqianbao;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebView;
import android.widget.ProgressBar;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private WebView mWebView;
    private ProgressBar mProgressBar;
    private static final String BASE_URL = "http://www.quketech.com/xloan2/#login?channel=WX";

    //    private static final String BASE_URL="http://www.baidu.com";
    //    private EditText mUrlEdit;
    //    private Button mButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        //        mUrlEdit = (EditText) findViewById(R.id.url_edit);
        //        mButton = (Button) findViewById(R.id.go);
        //        mButton.setOnClickListener(new View.OnClickListener() {
        //            @Override
        //            public void onClick(final View view) {
        //                if (TextUtils.isEmpty(mUrlEdit.getText().toString())) {
        //                    Toast.makeText(MainActivity.this, "please input url!!!", Toast.LENGTH_LONG).show();
        //                    return;
        //                }
        //                mWebView.loadUrl(mUrlEdit.getText().toString());
        //            }
        //        });
        //        initView();
        findViewById(R.id.go_linqianbao).setOnClickListener(this);
        findViewById(R.id.collect_userinfo).setOnClickListener(this);

    }

    @Override
    public void onClick(final View view) {
        switch (view.getId()) {
            case R.id.go_linqianbao:
                startActivity(new Intent(MainActivity.this, WebActivity.class));
                break;
            case R.id.collect_userinfo:
                startActivity(new Intent(MainActivity.this, UserInfoActivity.class));
                break;
        }
    }

    //    private void initView() {
    //        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);
    //        mWebView = (WebView) findViewById(R.id.web);
    //        mWebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
    //
    //        mWebView.setHorizontalScrollBarEnabled(false);//水平不显示滚动条     
    //        mWebView.setVerticalScrollBarEnabled(false);//垂直不显示滚动条
    //
    //
    //        WebSettings webSettings = mWebView.getSettings();
    //
    //        webSettings.setJavaScriptEnabled(true);
    //        webSettings.setDomStorageEnabled(true);
    //        webSettings.setLoadWithOverviewMode(true);
    //        webSettings.setUseWideViewPort(true);
    //        webSettings.setDatabaseEnabled(true);
    //
    //
    //
    //        // 设置可以访问文件
    //        webSettings.setAllowFileAccess(true);
    //        // 设置可以支持缩放
    //        webSettings.setSupportZoom(true);
    //        // 设置默认缩放方式尺寸是far
    //        webSettings.setDefaultZoom(WebSettings.ZoomDensity.MEDIUM);
    //        // 设置出现缩放工具
    //        webSettings.setBuiltInZoomControls(true);
    //        webSettings.setDefaultFontSize(16);
    //        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
    //            webSettings.setMixedContentMode(
    //                    WebSettings.MIXED_CONTENT_COMPATIBILITY_MODE);
    //        }
    //
    //        mWebView.loadUrl(BASE_URL);
    //
    //        // 设置WebViewClient
    //        mWebView.setWebViewClient(new WebViewClient() {
    //            // url拦截
    //            @Override
    //            public boolean shouldOverrideUrlLoading(WebView view, String url) {
    //                // 使用自己的WebView组件来响应Url加载事件，而不是使用默认浏览器器加载页面
    //                //                view.loadUrl(url);
    //                // 相应完成返回true
    //                //                return true;
    //                //
    //                //                if (url.contains("tel:")) {
    //                //                    //                    String mobile = url.substring(url.lastIndexOf("/") + 1);
    //                //                    mPhoneUri = Uri.parse(url);
    //                //                    Intent intent = new Intent(Intent.ACTION_CALL, mPhoneUri);
    //                //                    if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
    //                //                        //申请电话权限
    //                //                        ActivityCompat.requestPermissions((WebViewActivity) mContext, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_PHONE);
    //                //                    } else {
    //                //                        mContext.startActivity(intent);
    //                //                        ((WebViewActivity) mContext).startActivityForResult(intent, REQUEST_PHONE);
    //                //                    }
    //                //
    //                //                    //这个超连接,java已经处理了，webview不要处理了
    //                //                    return true;
    //                //                }
    //
    //                return super.shouldOverrideUrlLoading(view, url);
    //            }
    //
    //            // 页面开始加载
    //            @Override
    //            public void onPageStarted(WebView view, String url, Bitmap favicon) {
    //                mProgressBar.setVisibility(View.VISIBLE);
    //                super.onPageStarted(view, url, favicon);
    //            }
    //
    //            // 页面加载完成
    //            @Override
    //            public void onPageFinished(WebView view, String url) {
    //                mProgressBar.setVisibility(View.GONE);
    //
    //                super.onPageFinished(view, url);
    //            }
    //
    //            // WebView加载的所有资源url
    //            @Override
    //            public void onLoadResource(WebView view, String url) {
    //                super.onLoadResource(view, url);
    //            }
    //
    //            @Override
    //            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
    //                //				view.loadData(errorHtml, "text/html; charset=UTF-8", null);
    //                super.onReceivedError(view, errorCode, description, failingUrl);
    //            }
    //
    //            @Override
    //            public void onReceivedSslError(WebView view,
    //                                           SslErrorHandler handler, SslError error) {
    //                // TODO Auto-generated method stub
    //                // handler.cancel();// Android默认的处理方式
    //                handler.proceed();// 接受所有网站的证书
    //                // handleMessage(Message msg);// 进行其他处理
    //            }
    //
    //
    //        });
    //
    //        //        // 设置WebChromeClient
    //        //        mWebView.setWebChromeClient(new WebChromeClient() {
    //        //            @Override
    //        //            // 处理javascript中的alert
    //        //            public boolean onJsAlert(WebView view, String url, String message, final JsResult result) {
    //        //                return super.onJsAlert(view, url, message, result);
    //        //            }
    //        //
    //        //            @Override
    //        //            // 处理javascript中的confirm
    //        //            public boolean onJsConfirm(WebView view, String url, String message, final JsResult result) {
    //        //                return super.onJsConfirm(view, url, message, result);
    //        //            }
    //        //
    //        //            @Override
    //        //            // 处理javascript中的prompt
    //        //            public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, final JsPromptResult result) {
    //        //                return super.onJsPrompt(view, url, message, defaultValue, result);
    //        //            }
    //        //
    //        //            // 设置网页加载的进度条
    //        //            @Override
    //        //            public void onProgressChanged(WebView view, int newProgress) {
    //        //                mProgressBar.setProgress(newProgress);
    //        //                super.onProgressChanged(view, newProgress);
    //        //            }
    //        //
    //        //            // 设置程序的Title
    //        //            @Override
    //        //            public void onReceivedTitle(WebView view, String title) {
    //        //
    //        //                super.onReceivedTitle(view, title);
    //        //            }
    //        //        });
    //        // 设置WebChromeClient
    //        mWebView.setWebChromeClient(new WebChromeClient() {
    //            @Override
    //            // 处理javascript中的alert
    //            public boolean onJsAlert(WebView view, String url, String message, final JsResult result) {
    //                return super.onJsAlert(view, url, message, result);
    //            }
    //
    //            @Override
    //            // 处理javascript中的confirm
    //            public boolean onJsConfirm(WebView view, String url, String message, final JsResult result) {
    //                return super.onJsConfirm(view, url, message, result);
    //            }
    //
    //            @Override
    //            // 处理javascript中的prompt
    //            public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, final JsPromptResult result) {
    //                return super.onJsPrompt(view, url, message, defaultValue, result);
    //            }
    //
    //            // 设置网页加载的进度条
    //            @Override
    //            public void onProgressChanged(WebView view, int newProgress) {
    //                mProgressBar.setProgress(newProgress);
    //                super.onProgressChanged(view, newProgress);
    //            }
    //
    //            // 设置程序的Title
    //            @Override
    //            public void onReceivedTitle(WebView view, String title) {
    //
    //                super.onReceivedTitle(view, title);
    //            }
    //
    //            //            public void openFileChooser(ValueCallback<Uri> uploadMsg) {
    //            //                this.openFileChooser(uploadMsg, "image/*");
    //            //            }
    //            //
    //            //            public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType) {
    //            //                this.openFileChooser(uploadMsg, acceptType, null);
    //            //            }
    //            //
    //            //            public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
    //            //                //        if (mUploadMessage!=null){
    //            //                //            mUploadMessage.onReceiveValue(null);
    //            //                //        }
    //            //                //                mUploadMessage = uploadMsg;
    //            //                if (mWebView.getContext() instanceof WebViewActivity) {
    //            //                    ((WebViewActivity) mWebView.getContext()).setUploadMessage(uploadMsg);
    //            //                    Intent i = new Intent(Intent.ACTION_GET_CONTENT);
    //            //                    i.addCategory(Intent.CATEGORY_OPENABLE);
    //            //                    i.setType("image/*");
    //            //                    ((WebViewActivity) mContext).startActivityForResult(Intent.createChooser(i, "Image Browser"), WebViewActivity.FILECHOOSER_RESULTCODE);
    //            //                }
    //            //            }
    //            //
    //            //            /**********************5.0调用*********************/
    //            //            public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> uploadMsg, WebChromeClient.FileChooserParams fileChooserParams) {
    //            //                //        if (mUploadMessageArray!=null){
    //            //                //            mUploadMessageArray.onReceiveValue(null);
    //            //                //        }
    //            //                //                mUploadMessageForAndroid5=uploadMsg;
    //            //                if (mWebView.getContext() instanceof WebViewActivity) {
    //            //                    ((WebViewActivity) mContext).setUploadMessageForAndroid5(uploadMsg);
    //            //                    Intent i = new Intent(Intent.ACTION_GET_CONTENT);
    //            //                    i.addCategory(Intent.CATEGORY_OPENABLE);
    //            //                    i.setType("image/*");
    //            //                    startActivityForResult(Intent.createChooser(i, "Image Browser"), WebViewActivity.FILECHOOSER_RESULTCODE_FOR_ANDROID_5);
    //            //                    return true;
    //            //                } else {
    //            //                    return false;
    //            //                }
    //            //            }
    //
    //
    //        });
    //
    //        /********************下载管理******************************/
    //        mWebView.setDownloadListener(new DownloadListener() {
    //
    //            @Override
    //            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
    //                // 监听下载功能，当用户点击下载链接的时候，直接调用系统的浏览器来下载
    //
    //                Uri uri = Uri.parse(url);
    //                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
    //                startActivity(intent);
    //            }
    //        });
    //        /**************************************************/
    //
    //        mWebView.setOnKeyListener(new View.OnKeyListener() {
    //            @Override
    //            public boolean onKey(View v, int keyCode, KeyEvent event) {
    //                if (event.getAction() == KeyEvent.ACTION_DOWN) {
    //                    if (keyCode == KeyEvent.KEYCODE_BACK && mWebView.canGoBack()) { // 表示按返回键
    //
    //                        mWebView.goBack(); // 后退
    //
    //                        // webview.goForward();//前进
    //                        return true; // 已处理
    //                    }
    //                }
    //                return false;
    //            }
    //        });
    //    }
}
