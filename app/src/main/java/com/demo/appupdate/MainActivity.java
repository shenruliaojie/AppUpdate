package com.demo.appupdate;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.demo.appupdate.updater.entity.ApkInfo;
import com.demo.appupdate.updater.INetCallBack;
import com.demo.appupdate.updater.INetDownCallBack;
import com.demo.appupdate.updater.ui.UpdateVersionShowDialog;
import com.demo.appupdate.updater.utils.AppUtils;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btn_update = findViewById(R.id.btn_update);
        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppUpdater.getInstance().getNetManager().get("http://59.110.162.30/app_updater_version.json", new INetCallBack() {
                    @Override
                    public void success(String response) {
                        System.out.println(response);
                        ApkInfo apkInfo = new ApkInfo();
                        apkInfo = apkInfo.parse(response);
                        long versionCode = Long.parseLong(apkInfo.versionCode);
                        if(versionCode > AppUtils.getVersionCode(MainActivity.this)){
                            UpdateVersionShowDialog.show(MainActivity.this,apkInfo);
                        }
                    }

                    @Override
                    public void failed(Throwable throwable) {
                        Toast.makeText(MainActivity.this,"版本更新接口请求失败",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}
