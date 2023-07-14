package com.test.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;
import android.os.Bundle;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Timer timer = new Timer();
        //定时访问网页任务
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                try {
                    Log.i("MainActivity","TimerTask called");
                    URL url = new URL("https://www.baidu.com/");
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");

                    // 处理服务器响应
                    int responseCode = connection.getResponseCode();
                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        // 读取服务器响应数据
                        InputStream inputStream = connection.getInputStream();
                        // 处理输入流数据
                        Log.i("MainActivity"," HTTP_OK");
                    }

                    // 关闭连接
                    connection.disconnect();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkRequest.Builder requestBuilder = new NetworkRequest.Builder();
        requestBuilder.addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR);

        connectivityManager.requestNetwork(requestBuilder.build(), new ConnectivityManager.NetworkCallback() {
            @Override
            public void onAvailable(Network network) {
                // 当蜂窝网络可用时执行操作
                // 可以使用此网络进行互联网访问
                Log.i("MainActivity"," onAvailable");
                //每10s执行一次访问百度网页的操作
                timer.schedule(task, 0, 10000);
            }

            @Override
            public void onLost(Network network) {
                // 当蜂窝网络丢失时执行操作
                Log.i("MainActivity"," onLost");
                timer.cancel();
            }
        });
    }
}