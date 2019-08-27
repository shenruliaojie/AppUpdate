package com.demo.appupdate.updater;

import java.io.File;

/**
 * author： liguangwei
 * date： 2019/08/27
 **/
public interface INetManager {

    void get(String url,INetCallBack callback);

    void downLoad(String url, File targetFile,INetDownCallBack callBack);
}
