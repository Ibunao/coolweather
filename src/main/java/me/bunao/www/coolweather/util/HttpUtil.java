package me.bunao.www.coolweather.util;

import android.os.Build;
import android.util.Log;

import java.io.BufferedReader;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Expect on 2016/6/19.
 */
/*读取网络资源*/
public class HttpUtil {
    //使用回调函数
    public static void sendHttpRequest(final String address,
                                       final HttpCallbackListener listener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                try {

                    URL url = new URL(address);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(8000);
                    connection.setReadTimeout(8000);
                    /*解决这个错误System.err: java.io.EOFException,添加后就天气就可以更新成功了*/
                    if (Build.VERSION.SDK != null && Build.VERSION.SDK_INT > 13)
                    { connection.setRequestProperty("Connection", "close"); }
                    Log.i("ding",connection.toString());
                    InputStream in= connection.getInputStream();
                    Log.i("ding","ding");
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    if (listener != null) {
                        // 调用回调函数的方法
                        listener.onFinish(response.toString());
                    }

                } catch (Exception e) {
                    if (listener != null) {
                        // 调用回调函数的方法
                        listener.onError(e);
                    }
                } finally {
                    if (connection != null) {
                        connection.disconnect();
                    }

                }
            }
        }).start();
    }
}
