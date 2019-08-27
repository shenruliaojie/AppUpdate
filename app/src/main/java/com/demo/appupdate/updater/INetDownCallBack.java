package com.demo.appupdate.updater;

import java.io.File;

/**
 * author： liguangwei
 * date： 2019/08/27
 **/
public interface INetDownCallBack {

    void success(File targetFile);

    void progress(int progress);

    void failed(Throwable throwable);
}
