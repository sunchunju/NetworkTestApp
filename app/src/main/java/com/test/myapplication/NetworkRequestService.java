package com.test.myapplication;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class NetworkRequestService extends Service {
    private static final String TAG = "NetworkRequestService";
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand");

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkRequest.Builder requestBuilder = new NetworkRequest.Builder();
        requestBuilder.addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR);

        connectivityManager.requestNetwork(requestBuilder.build(), new ConnectivityManager.NetworkCallback() {
            @Override
            public void onAvailable(Network network) {
                // 当蜂窝网络可用时执行操作
                // 可以使用此网络进行互联网访问
                Log.i(TAG," onAvailable");
                //每10s执行一次访问百度网页的操作
                performNetworkRequest();
            }

            @Override
            public void onLost(Network network) {
                // 当蜂窝网络丢失时执行操作
                Log.i(TAG," onLost");
            }
        });

        AlarmManagerUtils.getInstance(getApplicationContext()).getUpAlarmManagerWorkOnOthers();
        return super.onStartCommand(intent, flags, startId);
    }

    private void performNetworkRequest() {
        // 在这里执行网络请求的逻辑, 并处理响应数据
        try {
            URL url = new URL("https://www.baidu.com/");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            // 处理服务器响应
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                // 读取服务器响应数据
                InputStream inputStream = connection.getInputStream();
                // 处理输入流数据
                Log.i(TAG," HTTP_OK");
            }

            // 关闭连接
            connection.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onDestroy() {
        Log.i(TAG,"onDestroy called");
        super.onDestroy();
    }
}
