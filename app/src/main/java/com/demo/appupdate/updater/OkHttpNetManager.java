package com.demo.appupdate.updater;

import android.os.Handler;
import android.os.Looper;

import androidx.constraintlayout.solver.widgets.ResolutionNode;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * author： liguangwei
 * date： 2019/08/27
 **/
public class OkHttpNetManager implements INetManager {

    private static OkHttpClient okHttpClient;
    private static Handler handler = new Handler(Looper.getMainLooper());

    static {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(60, TimeUnit.SECONDS);
        okHttpClient = builder.build();
//        证书验证配置
//        builder.sslSocketFactory()
    }

    @Override
    public void get(String url, INetCallBack callback) {
        Request.Builder builder = new Request.Builder();
        Request request = builder.url(url).get().build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        callback.failed(e);
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    String string = response.body().string();
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            callback.success(string);
                        }
                    });
                } catch (Throwable e) {
                    e.printStackTrace();
                    callback.failed(e);
                }
            }
        });
    }

    @Override
    public void downLoad(String url, File targetFile, INetDownCallBack callBack) {
        if(!targetFile.exists()){
            targetFile.getParentFile().mkdirs();
        }
        Request.Builder builder = new Request.Builder();
        Request request = builder.url(url).get().build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        callBack.failed(e);
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                InputStream is = null;
                OutputStream os = null;

                try {
                    long totalSize = response.body().contentLength();
                    long curLen = 0;

                    is = response.body().byteStream();
                    os = new FileOutputStream(targetFile);
                    byte[] bytes = new byte[8*1024];
                    int len = 0;
                    while ((len = is.read(bytes))!= -1){
                        os.write(bytes,0,len);
                        os.flush();
                        curLen += len;
                        long finalCurLen = curLen;
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                callBack.progress((int)(finalCurLen * 1.0f/totalSize*100));
                            }
                        });
                    }
                    try {
                        targetFile.setExecutable(true,false);
                        targetFile.setReadable(true,false);
                        targetFile.setWritable(true,false);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            callBack.success(targetFile);
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                    callBack.failed(e);
                }finally {
                    if(is != null){
                        is.close();
                    }
                    if(os != null){
                        os.close();
                    }
                }
            }
        });
    }
}
