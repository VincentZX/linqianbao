package com.quketech.linqianbao;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author VincnetZX    17/10/10 上午10:17
 */

public class UserInfoActivity extends AppCompatActivity {


    private Button mButton, mUploadBtn;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private double latitude = 0;
    private double longitude = 0;
    private String mIMEI;
    private String mUUID;
    private String mMacAddres;
    private String mIP;
    //    private ArrayList<CallInfoLog> callInfoLogs = new ArrayList<>();

    private ContentResolver cr;
    private List<Map<String, String>> mp = new ArrayList<>();
    private MobileInfo mMobileInfo;
    private GPSEntity mGPSEntity;
    private TextView mTextView;
    private String mMobileInfoString;

    private ProgressDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        mMobileInfo = new MobileInfo();
        mButton = (Button) findViewById(R.id.get_user_info);
        mUploadBtn = (Button) findViewById(R.id.upload_user_info);
        mButton.setOnClickListener(mOnClickListener);
        mUploadBtn.setOnClickListener(mOnClickListener);

    }


    private void getlocation() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (location != null) {
                latitude = location.getLatitude();
                longitude = location.getLongitude();
            } else {
                locationListener = new LocationListener() {
                    // Provider的状态在可用、暂时不可用和无服务三个状态直接切换时触发此函数
                    @Override
                    public void onStatusChanged(String provider, int status,
                                                Bundle extras) {
                    }

                    // Provider被enable时触发此函数，比如GPS被打开
                    @Override
                    public void onProviderEnabled(String provider) {
                    }

                    // Provider被disable时触发此函数，比如GPS被关闭
                    @Override
                    public void onProviderDisabled(String provider) {
                    }

                    // 当坐标改变时触发此函数，如果Provider传进相同的坐标，它就不会被触发
                    @Override
                    public void onLocationChanged(Location location) {
                        if (location != null) {
                            latitude = location.getLatitude(); // 纬度
                            longitude = location.getLongitude(); // 经度
                        }
                    }
                };
                locationManager.requestLocationUpdates(
                        LocationManager.NETWORK_PROVIDER, 1000, 0,
                        locationListener);
                Location location1 = locationManager
                        .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                if (location1 != null) {
                    latitude = location1.getLatitude(); // 经度
                    longitude = location1.getLongitude(); // 纬度
                }
            }
            //            getAddress();
        } else {
            latitude = 0;
            longitude = 0;
            //            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            //            startActivity(intent);
        }

    }


    //    /**
    //     * 获取所有的通话记录
    //     *
    //     * @param context
    //     */
    //    public void getCallLog(Context context) {
    //        try {
    //            callInfoLogs.clear();
    //            ContentResolver cr = context.getContentResolver();
    //            Uri uri = CallLog.Calls.CONTENT_URI;
    //            String[] projection = new String[]{ CallLog.Calls.NUMBER, CallLog.Calls.DATE,
    //                    CallLog.Calls.TYPE, CallLog.Calls.CACHED_NAME, CallLog.Calls.DURATION, CallLog.Calls.GEOCODED_LOCATION };
    //            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {
    //            }
    //            Cursor cursor = cr.query(uri, projection, null, null, CallLog.Calls.DATE + " DESC");
    //            while (cursor.moveToNext()) {
    //                CallInfoLog callInfoLog = new CallInfoLog();
    //                String number = cursor.getString(0);//电话号码
    //                long date = cursor.getLong(1);//通话时间
    //                int type = cursor.getInt(2);//通话类型
    //                String name = cursor.getString(3);//名字
    //                String duration = cursor.getString(4);//通话时长
    //                String areaCode = cursor.getString(5);//归属地
    //                String callTime = TransitionTime.convertTimeFirstStyle(date);
    //                if (TransitionTime.getTodayData().equals(callTime)) {//如果是今天的话
    //                    callInfoLog.setCallTime("今天");
    //                } else if (TransitionTime.getYesData().equals(callTime)) {
    //                    callInfoLog.setCallTime("昨天");
    //                } else {
    //                    callInfoLog.setCallTime(callTime);
    //                }
    //                callInfoLog.setNumber(number);
    //                callInfoLog.setDate(date);
    //                callInfoLog.setType(type);
    //                callInfoLog.setName(name);
    //                callInfoLog.setCountType(1);
    //                callInfoLog.setDuration(duration);
    //                callInfoLog.setCode(areaCode);
    //                //筛选数据
    //                if (TextUtils.isEmpty(number)) {
    //                    callInfoLogs.add(callInfoLog);
    //                    continue;
    //                }
    //                boolean isadd = screenData(callInfoLogs, callInfoLog);
    //                if (isadd) {
    //                    callInfoLogs.add(callInfoLog);
    //                }
    //            }
    //            cursor.close();
    //            //            callLogAdapter.notifyDataSetChanged();
    //            Log.e("record", "record = " + callInfoLogs.toString());
    //        } catch (Exception e) {
    //            e.printStackTrace();
    //        }
    //    }

    //    /**
    //     * 筛选数据
    //     *
    //     * @param callInfoLogs
    //     * @param info
    //     * @return
    //     */
    //    private boolean screenData(ArrayList<CallInfoLog> callInfoLogs, CallInfoLog info) {
    //        if (callInfoLogs.size() > 0) {
    //            for (int i = 0; i < callInfoLogs.size(); i++) {
    //                CallInfoLog callInfoLog = callInfoLogs.get(i);
    //                //如果说是日期和类型全部一样的话那么这个通话记录就不要,变成一个数量归为最近一次记录里面
    //                if (callInfoLog.getCallTime().equals(info.getCallTime()) && callInfoLog.getType() == info.getType() && info.getNumber().equals(callInfoLog.getNumber())) {
    //                    callInfoLog.setCountType(callInfoLog.getCountType() + 1);//递增一次
    //                    //结束这次数据查找
    //                    return false;
    //                }
    //            }
    //        }
    //        return true;
    //    }


    public void getContacts() {
        cr = getContentResolver();
        Uri uri = Uri.parse("content://com.android.contacts/raw_contacts");
        Cursor cs = cr.query(uri, null, null, null, null, null);
        while (cs.moveToNext()) {
            //拿到联系人id 跟name
            int id = cs.getInt(cs.getColumnIndex("_id"));
            String name = cs.getString(cs.getColumnIndex("display_name"));
            //得到这个id的所有数据（data表）
            Uri uri1 = Uri.parse("content://com.android.contacts/raw_contacts/" + id + "/data");
            Cursor cs2 = cr.query(uri1, null, null, null, null, null);
            Map<String, String> maps = new HashMap<>();//实例化一个map
            while (cs2.moveToNext()) {
                //得到data这一列 ，包括很多字段
                String data1 = cs2.getString(cs2.getColumnIndex("data1"));
                //得到data中的类型
                String type = cs2.getString(cs2.getColumnIndex("mimetype"));
                String str = type.substring(type.indexOf("/") + 1, type.length());//截取得到最后的类型
                if ("name".equals(str)) {//匹配是否为联系人名字
                    maps.put("name", data1);
                }
                if ("phone_v2".equals(str)) {//匹配是否为电话
                    maps.put("phone", data1);
                }
                Log.i("test", data1 + "       " + type);
            }
            mp.add(maps);//将map加入list集合中
        }
        //        simpleAdapter.notifyDataSetChanged();//通知适配器发生数据改变
        //        List<Map<String, String>> temp = mp;
        mMobileInfo.setMp(mp);
        //        Button button=(Button)view;//得到button
        //        button.setEnabled(false);//设置不可点击
    }


    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(final View view) {
            switch (view.getId()) {
                case R.id.get_user_info:
                    mGPSEntity = new GPSEntity(latitude, longitude);
                    mIMEI = InfoUtil.getIMEI(UserInfoActivity.this);
                    mUUID = InfoUtil.getUUID(UserInfoActivity.this);
                    mMacAddres = InfoUtil.getMAC(UserInfoActivity.this);
                    mIP = InfoUtil.GetNetIp(UserInfoActivity.this);
                    mMobileInfo.setIMEI(mIMEI);
                    mMobileInfo.setUUID(mUUID);
                    mMobileInfo.setMacAddres(mMacAddres);
                    mMobileInfo.setIP(mIP);
                    getContacts();
                    mMobileInfo.setGps(mGPSEntity);
                    Gson mg = new Gson();
                    mMobileInfoString = mg.toJson(mMobileInfo);
                    Log.e("test", mMobileInfoString);
                    mTextView = (TextView) findViewById(R.id.user_info);
                    mTextView.setText(mMobileInfoString);
                    //                getCallLog(MainActivity.this);
                    //                Log.e("location", "latitude = " + latitude + "\n" + "longitude = " + longitude + "\n" + "IEMI = " + mIMEI
                    //                        + "\n" + "UUID = " + mUUID + "\n" + "MACAddress = " + mMacAddres + "\n" + "IP = " + mIP);

                    break;
                case R.id.upload_user_info:
                    uploadUserInfo(mMobileInfo);
                    break;
            }
            getlocation();
        }
    };

    private void uploadUserInfo(final MobileInfo mobileInfo) {
        isShowDialog(true);
        BackgroundExecutor.execute(new BackgroundExecutor.Task("", 0, "") {
            @Override
            public void execute() {
                try {
                    final String responseStr = RequestUtils.postBody("http://www.quketech.com/xloan2-web/upload-app-data", "", mMobileInfoString);
                    Log.d("responseStr", "responseStr:" + responseStr);
                    isShowDialog(false);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(UserInfoActivity.this, "responseStr==" + responseStr, Toast.LENGTH_LONG).show();
                            mTextView.setText(responseStr + "");
                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                    isShowDialog(false);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(UserInfoActivity.this, "上传失败！", Toast.LENGTH_LONG).show();
                        }
                    });

                }
            }
        });
    }


    private void isShowDialog(boolean isShow) {
        if (mDialog == null) {
            mDialog = new ProgressDialog(this);
            mDialog.setMessage("uploading");
        }
        if (!mDialog.isShowing() && isShow) {
            mDialog.show();
            return;
        }
        if (mDialog.isShowing() && !isShow) {
            mDialog.dismiss();
            return;
        }
    }

}
