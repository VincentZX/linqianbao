package com.quketech.linqianbao;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.provider.Settings;
import android.telephony.TelephonyManager;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;

/**
 * @author VincnetZX    17/9/28 下午10:40
 */

public class InfoUtil {
    /**
     * 获取手机IMEI
     *
     * @param context
     * @return
     */
    public static String getIMEI(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return telephonyManager.getDeviceId();
    }

    /**
     * 获取手机UUID
     *
     * @param context
     * @return
     */
    public static String getUUID(Context context) {

        return Settings.System.getString(context.getContentResolver(), Settings.System.ANDROID_ID);
    }


    /**
     * 获取手机mac地址
     *
     * @return
     */
    public static String getMAC(Context context) {
        String macSerial = null;
        String str = "";

        try {
            Process pp = Runtime.getRuntime().exec("cat /sys/class/net/wlan0/address ");
            InputStreamReader ir = new InputStreamReader(pp.getInputStream());
            LineNumberReader input = new LineNumberReader(ir);

            for (; null != str; ) {
                str = input.readLine();
                if (str != null) {
                    macSerial = str.trim();// 去空格
                    break;
                }
            }
        } catch (IOException ex) {
            // 赋予默认值
            ex.printStackTrace();
        }
        return macSerial;
    }


    public static String GetNetIp(Context context) {
      /*  URL infoUrl = null;
        InputStream inStream = null;
        try {
            //http://iframe.ip138.com/ic.asp
            //infoUrl = new URL("http://city.ip138.com/city0.asp");
            infoUrl = new URL("http://1212.ip138.com/ic.asp");
            URLConnection connection = infoUrl.openConnection();
            HttpURLConnection httpConnection = (HttpURLConnection) connection;
            int responseCode = httpConnection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                inStream = httpConnection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inStream, "utf-8"));
                StringBuilder strber = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null)
                    strber.append(line + "\n");
                inStream.close();
                //从反馈的结果中提取出IP地址
                int start = strber.indexOf("[");
                int end = strber.indexOf("]", start + 1);
                line = strber.substring(start + 1, end);
                return line;
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;*/

        WifiManager wifimanage = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);//获取WifiManager

        //检查wifi是否开启

        if (!wifimanage.isWifiEnabled()) {

            wifimanage.setWifiEnabled(true);

        }

        WifiInfo wifiinfo = wifimanage.getConnectionInfo();
        //        return String.valueOf(wifiinfo.getIpAddress());
        return intToIp(wifiinfo.getIpAddress());


    }
    //将获取的int转为真正的ip地址,参考的网上的，修改了下

    public static String intToIp(int ipInt) {
        StringBuilder sb = new StringBuilder();
        sb.append(ipInt & 0xFF).append(".");
        sb.append((ipInt >> 8) & 0xFF).append(".");
        sb.append((ipInt >> 16) & 0xFF).append(".");
        sb.append((ipInt >> 24) & 0xFF);
        return sb.toString();
    }
}
