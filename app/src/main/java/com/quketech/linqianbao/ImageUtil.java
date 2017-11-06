package com.quketech.linqianbao;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;

/**
 * Created by 亮亮 on 2017/10/10.
 */

public class ImageUtil {
    private static final String TAG = "ImageUtil";
    private String fileName;

    /**
     * go for Album.
     */
    public static final Intent choosePicture() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        //return Intent.createChooser(intent, null);
        // 搜一下Intent.createChooser(intent,null)与intent有什么区别
        return intent;
    }

    /**
     * go for camera.
     */
    public Intent takeBigPicture(Context mcontext) {
        fileName= String.valueOf(System.currentTimeMillis());
        Uri uri = FileProvider.getUriForFile(
                mcontext,
                mcontext.getPackageName() + ".fileprovider",
                new File(getNewPhotoPath()));
        Intent intent = new Intent();
        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        return intent;
    }

    public static final String getDirPath() {
        return Environment.getExternalStorageDirectory().getPath() + "/UploadImage";
    }

    public   final String getNewPhotoPath() {
        return getDirPath() + "/" + fileName+".jpg";
    }

    //获得对应的图片路径
    public static final String retrievePath(Context context, Intent sourceIntent, Intent dataIntent) {
        String picPath = null;
        try {
            Uri uri;
            if (dataIntent != null) {
                uri = dataIntent.getData();
                if (uri != null) {
                    picPath = ContentUtil.getPath(context, uri);
                }
                if (isFileExists(picPath)) {
                    return picPath;
                }

                Log.w(TAG, String.format("retrievePath failed from dataIntent:%s, extras:%s", dataIntent, dataIntent.getExtras()));
            }

            if (sourceIntent != null) {
                uri = sourceIntent.getParcelableExtra(MediaStore.EXTRA_OUTPUT);
                if (uri != null) {
                    String scheme = uri.getScheme();
                    if (scheme != null && scheme.startsWith("file")) {
                        picPath = uri.getPath();
                    }
                }
                if (!TextUtils.isEmpty(picPath)) {
                    File file = new File(picPath);
                    if (!file.exists() || !file.isFile()) {
                        Log.w(TAG, String.format("retrievePath file not found from sourceIntent path:%s", picPath));
                    }
                }
            }
            return picPath;
        } finally {
            Log.d(TAG, "retrievePath(" + sourceIntent + "," + dataIntent + ") ret: " + picPath);
        }
    }

    private static final Uri newPictureUri(String path) {
        return Uri.fromFile(new File(path));
    }

    private static final boolean isFileExists(String path) {
        if (TextUtils.isEmpty(path)) {
            return false;
        }
        File f = new File(path);
        if (!f.exists()) {
            return false;
        }
        return true;
    }
}
