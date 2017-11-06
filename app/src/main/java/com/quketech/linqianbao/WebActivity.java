package com.quketech.linqianbao;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.DownloadListener;
import android.webkit.SslErrorHandler;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class WebActivity extends AppCompatActivity implements MyWebChomeClient.OpenFileChooserCallBack {
    private static final String TAG = "WebActivity";
    //    private static final int REQUEST_CAMERA = 1;
    //    private static final int REQUEST_CHOOSE = 2;
    //
    //    private ValueCallback<Uri> mUploadMessage;
    //    private ValueCallback<Uri[]> mUploadMessagesAboveL;
    //    private Uri cameraUri;
    private WebView mWebView;
    private ProgressBar mProgressBar;
    private static final String BASE_URL = "http://www.quketech.com/xloan2/#login?channel=WX";
    //    private static final String BASE_URL="http://www.baidu.com";
    //    private EditText mUrlEdit;
    //    private Button mButton;

    private static final int REQUEST_CODE_PICK_IMAGE = 0;
    private static final int REQUEST_CODE_IMAGE_CAPTURE = 1;

    private Intent mSourceIntent;
    //分别是4.4之前的回调参数 ，
    private ValueCallback<Uri> mUploadMsg;
    // 5.0之后的回调参数
    public ValueCallback<Uri[]> mUploadMsgForAndroid5;

    private static final int P_CODE_PERMISSIONS = 101;
    private ImageUtil mImageUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mImageUtil = new ImageUtil();
        //        mUrlEdit = (EditText) findViewById(R.id.url_edit);
        //        mButton = (Button) findViewById(R.id.go);
        //        mButton.setOnClickListener(new View.OnClickListener() {
        //            @Override
        //            public void onClick(final View view) {
        //                if (TextUtils.isEmpty(mUrlEdit.getText().toString())) {
        //                    Toast.makeText(WebActivity.this, "please input url!!!", Toast.LENGTH_LONG).show();
        //                    return;
        //                }
        //                mWebView.loadUrl(mUrlEdit.getText().toString());
        //            }
        //        });
        fixDirPath();
        initView();

    }

    private void initView() {
        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);
        mWebView = (WebView) findViewById(R.id.web);
        mWebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);

        mWebView.setHorizontalScrollBarEnabled(false);//水平不显示滚动条     
        mWebView.setVerticalScrollBarEnabled(false);//垂直不显示滚动条


        //        mWebView.setWebViewClient(new MyWebViewClient());
        //        mWebView.setWebChromeClient(new MyWebChromeClient());
        mWebView.setWebChromeClient(new MyWebChomeClient(WebActivity.this){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                mProgressBar.setProgress(newProgress);
                super.onProgressChanged(view, newProgress);
            }
        });

        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed();
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                mProgressBar.setVisibility(View.GONE);
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                    CookieSyncManager.getInstance().sync();
                } else {
                    CookieManager.getInstance().flush();
                }
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                mProgressBar.setVisibility(View.VISIBLE);
                super.onPageStarted(view, url, favicon);
            }

        });
        WebSettings webSettings = mWebView.getSettings();


        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setDatabaseEnabled(true);


        // 设置可以访问文件
        webSettings.setAllowFileAccess(true);
        // 设置可以支持缩放
        webSettings.setSupportZoom(true);
        // 设置默认缩放方式尺寸是far
        webSettings.setDefaultZoom(WebSettings.ZoomDensity.MEDIUM);
        // 设置出现缩放工具
        webSettings.setBuiltInZoomControls(true);
        webSettings.setDefaultFontSize(16);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webSettings.setMixedContentMode(
                    WebSettings.MIXED_CONTENT_COMPATIBILITY_MODE);
        }


        /********************下载管理******************************/
        mWebView.setDownloadListener(new DownloadListener() {

            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
                // 监听下载功能，当用户点击下载链接的时候，直接调用系统的浏览器来下载

                Uri uri = Uri.parse(url);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });
        /**************************************************/

        mWebView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK && mWebView.canGoBack()) { // 表示按返回键

                        mWebView.goBack(); // 后退

                        // webview.goForward();//前进
                        return true; // 已处理
                    }
                }
                return false;
            }
        });
        mWebView.loadUrl(BASE_URL);
    }


    /******************************dfrr***************/
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            // 如果拍照或者是选择图片都没有成功，让他们都接受空的value
            if (mUploadMsg != null) {
                mUploadMsg.onReceiveValue(null);
            }

            if (mUploadMsgForAndroid5 != null) {         // for android 5.0+
                mUploadMsgForAndroid5.onReceiveValue(null);
            }
            //            return;
        }
        switch (requestCode) {
            //不管是拍照和选择图片，都走我这个逻辑
            case REQUEST_CODE_IMAGE_CAPTURE:
            case REQUEST_CODE_PICK_IMAGE: {
                try {
                    //如果当前手机版本小于5.0
                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                        //如果回调参数没有声明，那么就返回
                        if (mUploadMsg == null) {
                            return;
                        }
                        //如果声明了，就把网址发送到这个参数中去
                        String sourcePath = ImageUtil.retrievePath(this, mSourceIntent, data);

                        if (TextUtils.isEmpty(sourcePath) || !new File(sourcePath).exists()) {
                            Log.e(TAG, "sourcePath empty or not exists.");
                            break;
                        }
                        Uri uri = Uri.fromFile(new File(sourcePath));
                        mUploadMsg.onReceiveValue(uri);

                    } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        if (mUploadMsgForAndroid5 == null) {        // for android 5.0+
                            return;
                        }

                        String sourcePath = ImageUtil.retrievePath(this, mSourceIntent, data);

                        if (TextUtils.isEmpty(sourcePath) || !new File(sourcePath).exists()) {
                            Log.e(TAG, "sourcePath empty or not exists.");
                            sourcePath = mImageUtil.getNewPhotoPath();
                        }
                        Uri uri = Uri.fromFile(new File(sourcePath));
                        mUploadMsgForAndroid5.onReceiveValue(new Uri[]{ uri });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            }
        }
    }

    @Override
    public void openFileChooserCallBack(ValueCallback<Uri> uploadMsg, String acceptType) {
        mUploadMsg = uploadMsg;
        showOptions();
    }

    @Override
    public boolean openFileChooserCallBackAndroid5
            (WebView webView, ValueCallback<Uri[]> filePathCallback, WebChromeClient.FileChooserParams fileChooserParams) {
        //初始化好存储网址的参数了
        mUploadMsgForAndroid5 = filePathCallback;
        //弹出dialog
        showOptions();

        return true;
    }

    //弹出选择图片的dialog
    public void showOptions() {
        android.support.v7.app.AlertDialog.Builder alertDialog = new android.support.v7.app.AlertDialog.Builder(this);
        alertDialog.setOnCancelListener(new DialogOnCancelListener());

        alertDialog.setTitle("请选择操作");
        // gallery, camera.
        String[] options = { "相册", "拍照" };

        alertDialog.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            //TODO
                            if (PermissionUtil.isOverMarshmallow()) {
                                if (!PermissionUtil.isPermissionValid(WebActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                                    Toast.makeText(WebActivity.this,
                                            "请去\"设置\"中开启本应用的图片媒体访问权限",
                                            Toast.LENGTH_SHORT).show();

                                    restoreUploadMsg();
                                    requestPermissionsAndroidM();
                                    return;
                                }

                            }
                            //都要执行的代码 ，去打开系统相册
                            try {
                                mSourceIntent = ImageUtil.choosePicture();
                                startActivityForResult(mSourceIntent, REQUEST_CODE_PICK_IMAGE);
                            } catch (Exception e) {
                                e.printStackTrace();
                                Toast.makeText(WebActivity.this,
                                        "请去\"设置\"中开启本应用的图片媒体访问权限",
                                        Toast.LENGTH_SHORT).show();
                                restoreUploadMsg();
                            }

                        } else {
                            if (PermissionUtil.isOverMarshmallow()) {
                                if (!PermissionUtil.isPermissionValid(WebActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                                    Toast.makeText(WebActivity.this,
                                            "请去\"设置\"中开启本应用的图片媒体访问权限",
                                            Toast.LENGTH_SHORT).show();

                                    restoreUploadMsg();
                                    requestPermissionsAndroidM();
                                    return;
                                }

                                if (!PermissionUtil.isPermissionValid(WebActivity.this, Manifest.permission.CAMERA)) {
                                    Toast.makeText(WebActivity.this,
                                            "请去\"设置\"中开启本应用的相机权限",
                                            Toast.LENGTH_SHORT).show();

                                    restoreUploadMsg();
                                    requestPermissionsAndroidM();
                                    return;
                                }
                            }

                            try {
                                mSourceIntent = mImageUtil.takeBigPicture(WebActivity.this);
                                startActivityForResult(mSourceIntent, REQUEST_CODE_IMAGE_CAPTURE);

                            } catch (Exception e) {
                                e.printStackTrace();
                                Toast.makeText(WebActivity.this,
                                        "请去\"设置\"中开启本应用的相机和图片媒体访问权限",
                                        Toast.LENGTH_SHORT).show();

                                restoreUploadMsg();
                            }
                        }
                    }
                }
        );

        alertDialog.show();
    }

    private void fixDirPath() {
        String path = ImageUtil.getDirPath();
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    private class DialogOnCancelListener implements DialogInterface.OnCancelListener {
        @Override
        public void onCancel(DialogInterface dialogInterface) {
            restoreUploadMsg();
        }
    }

    //如果用户点击了取消按钮，就让存储图片的参数为空，?H5就不会做后续的操作了
    private void restoreUploadMsg() {
        if (mUploadMsg != null) {
            mUploadMsg.onReceiveValue(null);
            mUploadMsg = null;

        } else if (mUploadMsgForAndroid5 != null) {
            mUploadMsgForAndroid5.onReceiveValue(null);
            mUploadMsgForAndroid5 = null;
        }
    }


    //6.0以上动态申请权限的逻辑
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case P_CODE_PERMISSIONS:
                requestResult(permissions, grantResults);
                restoreUploadMsg();
                break;

            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void requestPermissionsAndroidM() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                List<String> needPermissionList = new ArrayList<>();
                needPermissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                needPermissionList.add(Manifest.permission.READ_EXTERNAL_STORAGE);
                needPermissionList.add(Manifest.permission.CAMERA);


                PermissionUtil.requestPermissions(WebActivity.this, P_CODE_PERMISSIONS, needPermissionList);
            }
        } else {
            return;
        }
    }

    public void requestResult(String[] permissions, int[] grantResults) {
        ArrayList<String> needPermissions = new ArrayList<String>();

        for (int i = 0; i < grantResults.length; i++) {
            if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                if (PermissionUtil.isOverMarshmallow()) {

                    needPermissions.add(permissions[i]);
                }
            }
        }

        if (needPermissions.size() > 0) {
            StringBuilder permissionsMsg = new StringBuilder();

            for (int i = 0; i < needPermissions.size(); i++) {
                String strPermissons = needPermissions.get(i);

                if (Manifest.permission.WRITE_EXTERNAL_STORAGE.equals(strPermissons)) {
                    permissionsMsg.append("," + getString(R.string.permission_storage));

                } else if (Manifest.permission.READ_EXTERNAL_STORAGE.equals(strPermissons)) {
                    permissionsMsg.append("," + getString(R.string.permission_storage));

                } else if (Manifest.permission.CAMERA.equals(strPermissons)) {
                    permissionsMsg.append("," + getString(R.string.permission_camera));

                }
            }

            String strMessage = "请允许使用\"" + permissionsMsg.substring(1).toString() + "\"权限, 以正常使用APP的所有功能.";

            Toast.makeText(WebActivity.this, strMessage, Toast.LENGTH_SHORT).show();

        } else {
            return;
        }
    }
}
