package com.demo.appupdate.updater.entity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * author： liguangwei
 * date： 2019/08/27
 **/
public class ApkInfo implements Serializable {
    public String title;
    public String content;
    public String url;
    public String md5;
    public String versionCode;


    public ApkInfo parse(String json){
        ApkInfo apkInfo = new ApkInfo();
        try {
            JSONObject jsonObject = new JSONObject(json);
            apkInfo.title = jsonObject.optString("title");
            apkInfo.content = jsonObject.optString("content");
            apkInfo.url = jsonObject.optString("url");
            apkInfo.md5 = jsonObject.optString("md5");
            apkInfo.versionCode = jsonObject.optString("versionCode");
            return apkInfo;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

}
