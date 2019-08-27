package com.demo.appupdate.updater;

/**
 * author： liguangwei
 * date： 2019/08/27
 **/
public interface INetCallBack {

    void success(String response);

    void failed(Throwable throwable);

}
