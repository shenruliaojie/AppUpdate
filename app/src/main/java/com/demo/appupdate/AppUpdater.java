package com.demo.appupdate;

import com.demo.appupdate.updater.INetManager;
import com.demo.appupdate.updater.OkHttpNetManager;

/**
 * author： liguangwei
 * date： 2019/08/27
 **/
public class AppUpdater {

    private static AppUpdater appUpdater = new AppUpdater();

    private INetManager iNetManager = new OkHttpNetManager();

    public void setNetManager(INetManager iNetManager){
        this.iNetManager = iNetManager;
    }

    public INetManager getNetManager(){
        return iNetManager;
    }

    public static AppUpdater getInstance(){
        return appUpdater;
    }



}
