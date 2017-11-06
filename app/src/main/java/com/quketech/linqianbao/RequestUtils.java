package com.quketech.linqianbao;


import android.util.Log;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;

/**
 * HTTP请求封装类
 *
 * @author zachary.wu
 */
public class RequestUtils {
    public static final String TAG = "RequestUtils";

    public static final int CONNECT_TIME_OUT_NORMAL = 60000;
    public static final int READ_TIME_OUT_NORMAL = 60000;

    public static final String REQUEST_POST = "POST";
    public static final String REQUEST_GET = "GET";
    public static final String LANGUAGE = "utf-8";

    public static final String CONTENT_TYPE = "application/json";
    public static final String CONTENT_SEND_PICTURE_TYPE = "multipart/form-data";
    public static final String CONNECTION = "keep-alive";
    public static final String ACCEPT = "*/*";
    private static String JSESSIONID = "";

    /**
     * 所有接口的HTTP Header中增加”User-Agent” :”lincomb-ios”,”lincomb-android”
     */
    public static final String USER_AGENT = "lincomb-android";

    private static final String PREFIX = "--";// 行开始
    private static final String LINE_END = "\r\n";// 行结束

    /**
     * get请求访问网络，获取response
     *
     * @param url url
     * @return response
     * @throws IOException
     */
    public static String get(String url) throws IOException {
        HttpURLConnection connection = null;
        String result = null;
        try {
            Log.d(TAG, "GET,URL:" + url);
            connection = buildConnection(url, REQUEST_GET, null);
            result = doResponse(connection, LANGUAGE);
        } finally {
            closeConnect(connection);
        }
        return result;

    }

    /**
     * get请求访问网络，获取response
     *
     * @param url     url
     * @param headers header
     * @return response
     * @throws IOException
     */
    public static String get(String url, Map<String, String> headers) throws IOException {
        HttpURLConnection connection = null;
        String result = null;
        try {
            Log.d(TAG, "GET,URL:" + url);
            connection = buildConnection(url, REQUEST_GET, headers);
            result = doResponse(connection, LANGUAGE);
        } finally {
            closeConnect(connection);
        }
        return result;

    }

    /**
     * get请求访问网络，获取response
     *
     * @param url url
     * @return response
     * @throws IOException
     */
    public static String get(String url, String data) throws IOException {
        HttpURLConnection connection = null;
        String result = null;
        try {
            url += "?" + data;
            Log.d(TAG, "POST URL:" + url + " DATA:" + data);
            connection = buildConnection(url, REQUEST_POST, null);
            Log.d(TAG, "connection = " + connection);
            sendData(connection, data, LANGUAGE);
            result = doResponse(connection, LANGUAGE);
            Log.d(TAG, "result = " + result);
        } finally {
            closeConnect(connection);
        }
        return result;
    }

    /**
     * post请求访问网络，获取response
     *
     * @param url url
     * @return response
     * @throws IOException
     */
    public static String post(String url, String data) throws IOException {
        HttpURLConnection connection = null;

        String result = null;
        try {
            url += "?" + data;
            Log.d(TAG, "POST URL:" + url + " DATA:" + data);
            connection = buildConnection(url, REQUEST_POST, null);
            Log.d(TAG, "connection = " + connection);
            sendData(connection, data, LANGUAGE);

            result = doResponse(connection, LANGUAGE);
            Log.d(TAG, "result = " + result);
        } finally {
            closeConnect(connection);
        }
        return result;
    }

    /**
     * post请求访问网络，获取response
     *
     * @param url     url
     * @param headers header
     * @return response
     * @throws IOException
     */
    public static String post(String url, String data, Map<String, String> headers) throws IOException {
        HttpURLConnection connection = null;
        String result = null;
        try {
            Log.d(TAG, "POST,,URL:" + url + "  DATA:" + data);
            connection = buildConnection(url, REQUEST_POST, headers);
            sendData(connection, data, LANGUAGE);
            result = doResponse(connection, LANGUAGE);
            Log.e(TAG, "result:" + result);
        } finally {
            closeConnect(connection);
        }
        return result;
    }


    /**
     * build a connection with api
     */
    private static HttpURLConnection buildConnection(String url, String requestMethod, Map<String, String> headers)
            throws IOException {
        //        if (url.indexOf("shareNotify")!=-1){
        //            url="http://114.141.172.206:7001/javaservice/"+url;
        //        }else {

//        if (url.indexOf("getSystemSeetingInfo") != -1) {
//            url = Constants.SERVICE_BASE_URL + url;
//        } else if (url.indexOf("http://ip.taobao.com/service/getIpInfo.php") != -1) {
//            url = url + "ip=myip";
//        } else if (url.indexOf("shopservice") != -1) {
//            url = Constants.URL_BASE + url;
//        } else {
//        if (url.indexOf("appSystemNotice")!=-1){
//             url= Constants.NOTICE_URL+ url;
//        }else{
//            url = "http://www.quketech.com/xloan2-web/upload-app-data";
//        }
//        }
        Log.d(TAG, "url:" + url);
        URL net = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) net.openConnection();
        connection.setConnectTimeout(CONNECT_TIME_OUT_NORMAL);
        connection.setReadTimeout(READ_TIME_OUT_NORMAL);
        connection.setRequestMethod(requestMethod);
                connection.setRequestProperty("Content-Type", CONTENT_TYPE);
//        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

        connection.setRequestProperty("accept", ACCEPT);
        connection.setRequestProperty("User-Agent", USER_AGENT);
        connection.setRequestProperty("Cookie", JSESSIONID);

//        if (headers != null) {
//            headers.put(Constants.CONCURRENTID, UuidUtil.getTime() + UuidUtil.getUUID(DDApplication.getContext()).replace("-",""));
//            for (String key : headers.keySet()) {
//                connection.setRequestProperty(key, headers.get(key));
//            }
//        }
        return connection;
    }

    /**
     * send data to api
     */
    private static void sendData(HttpURLConnection connection, String param, String charset) throws IOException {
        connection.setDoOutput(true);
        connection.setDoInput(true);
        PrintWriter out = new PrintWriter(new OutputStreamWriter(connection.getOutputStream(), charset));
        out.print(param);
        out.flush();
        out.close();
    }

    /**
     * 上传头像
     */
    private static void sendData(HttpURLConnection connection, File avatarFile, String boundary) throws IOException {
        if (avatarFile != null) {
            /**
             * 当文件不为空，把文件包装并且上传
             */
            OutputStream outputSteam = connection.getOutputStream();

            DataOutputStream dos = new DataOutputStream(outputSteam);
            StringBuilder sb = new StringBuilder();
            sb.append(PREFIX);
            sb.append(boundary);// 边界标识 随机生成
            sb.append(LINE_END);
            /**
             * 这里重点注意： name里面的值为服务器端需要key 只有这个key 才可以得到对应的文件
             * filename是文件的名字，包含后缀名的 比如:abc.png
             */
            Log.d(TAG, "file.getName():" + avatarFile.getName());
            sb.append("Content-Disposition: form-data; name=\"img\"; filename=\"").append(avatarFile.getName()).append("\"").append(LINE_END);
            sb.append("Content-Type: application/octet-stream; charset=").append(LANGUAGE).append(LINE_END);
            sb.append(LINE_END);
            dos.write(sb.toString().getBytes());
            InputStream is = new FileInputStream(avatarFile);
            Log.d(TAG, "file.available():" + is.available());
            byte[] bytes = new byte[1024];
            int len = 0;
            while ((len = is.read(bytes)) != -1) {
                dos.write(bytes, 0, len);
                if (len != 1024) {
                    System.out.println("len--" + len);
                }
            }

            is.close();
            dos.write(LINE_END.getBytes());
            byte[] end_data = (PREFIX + boundary + PREFIX + LINE_END).getBytes();
            dos.write(end_data);
            dos.flush();
            dos.close();
        }
    }

    /**
     * receive and read the response result.
     *
     */
    private static String doResponse(HttpURLConnection connection, String charset) throws IOException {
        StringBuilder result = new StringBuilder();
        java.io.BufferedReader bufferedReader;
        int responseCode = connection.getResponseCode();
        Log.d(TAG, "responseCode:" + responseCode);
        if (responseCode == HttpURLConnection.HTTP_OK) {
            setSessionId(connection);
            InputStream inputstream = connection.getInputStream();
            bufferedReader = new java.io.BufferedReader(new java.io.InputStreamReader(inputstream, charset));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                if (result.length() > 0) {
                    result.append("\n");
                }
                result.append(line);
            }
            inputstream.close();
            bufferedReader.close();
        } else {
//            throw new NNException(new BaseResult(String.valueOf(responseCode), connection.getResponseMessage()));
        }
        Log.d(TAG, "RESPONSE : " + result.toString());
        return result.toString();
    }

    /**
     * close the connection with api.
     */
    private static void closeConnect(HttpURLConnection connection) {
        if (connection != null) {
            connection.disconnect();
        }
    }

    private static void setSessionId(HttpURLConnection connection) {
        String cookieValue = connection.getHeaderField("Set-Cookie");
        if (cookieValue != null) {
            if (cookieValue.contains("JSESSIONID")) {
                JSESSIONID = cookieValue.substring(0, cookieValue.indexOf(";"));
            }
        }
    }



    /**
     * send data to api
     */
    private static void sendbody(HttpURLConnection connection, String param) throws IOException {
        connection.setDoOutput(true);
        connection.setDoInput(true);
        DataOutputStream out = new DataOutputStream(connection.getOutputStream());
        //        OutputStream out = connection.getOutputStream();
        if (param != null) {
            out.write(param.getBytes());
        }
        out.flush();
        out.close();
    }


    /**
     * post请求访问网络，获取response
     *
     * @param url url
     * @return response
     * @throws IOException
     */
    public static String postBody(String url, String data, String bodyData) throws IOException {
        HttpURLConnection connection = null;

        String result = null;
        try {
//            url += "?" + data;
            Log.d(TAG, "POST URL:" + url + " DATA:" + data);
            connection = buildConnection(url, REQUEST_POST, null);
            Log.d(TAG, "connection = " + connection);
//            sendbody(connection, "data=" + URLEncoder.encode(bodyData, LANGUAGE));
            Log.d(TAG, "data=" + bodyData);
            sendbody(connection,  bodyData);
            result = doResponse(connection, LANGUAGE);
            Log.d(TAG, "result = " + result);
        } finally {
            closeConnect(connection);
        }
        return result;
    }


    /**
     * post请求访问网络，获取response
     *
     * @param url url
     * @return response
     * @throws IOException
     */
    public static String postBodyHeaders(String url, String data, String bodyData, Map<String, String> headers) throws IOException {
        HttpURLConnection connection = null;

        String result = null;
        try {
            url += "?" + data;
            Log.d(TAG, "POST URL:" + url + " DATA:" + data);
            connection = buildConnection(url, REQUEST_POST, headers);
            Log.d(TAG, "connection = " + connection);
            //                        sendData(connection, bodyData, LANGUAGE);
            Log.d(TAG,"bodyData  =  "+bodyData);
            sendbody(connection, "data=" + URLEncoder.encode(bodyData, LANGUAGE));
            result = doResponse(connection, LANGUAGE);
            Log.d(TAG, "result = " + result);
        } finally {
            closeConnect(connection);
        }
        return result;
    }

}
