package com.demo.appupdate.updater.ui;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;

import com.demo.appupdate.AppUpdater;
import com.demo.appupdate.MainActivity;
import com.demo.appupdate.R;
import com.demo.appupdate.updater.INetDownCallBack;
import com.demo.appupdate.updater.entity.ApkInfo;
import com.demo.appupdate.updater.utils.AppUtils;

import java.io.File;

/**
 * author： liguangwei
 * date： 2019/08/27
 **/
public class UpdateVersionShowDialog extends DialogFragment {
    public static final String KEY_DOWNLOAD_BEAN = "download_bean";

    private ApkInfo apkInfo;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
        if(arguments != null){
            apkInfo = (ApkInfo) arguments.getSerializable(KEY_DOWNLOAD_BEAN);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = inflater.inflate(R.layout.dialog_updater, container, false);
        bindEvents(view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    private void bindEvents(View view){
        TextView tv_title = view.findViewById(R.id.tv_title);
        TextView tv_content = view.findViewById(R.id.tv_content);
        TextView tv_update = view.findViewById(R.id.tv_update);

        tv_title.setText(apkInfo.title);
        tv_content.setText(apkInfo.content);
        tv_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_update.setClickable(false);
                File file = new File(getContext().getCacheDir(),"targetFile.apk");
                System.out.println(getContext().getCacheDir());
                AppUpdater.getInstance().getNetManager().downLoad(apkInfo.url, file, new INetDownCallBack() {
                    @Override
                    public void success(File targetFile) {
                        tv_update.setClickable(true);
                        dismiss();
                        AppUtils.installApk(getContext(),targetFile);
                        System.out.println("下载完成"+targetFile.getAbsolutePath());
                    }

                    @Override
                    public void progress(int progress) {
                        tv_update.setText(progress + "%");
                        System.out.println("下载进度："+progress);
                    }

                    @Override
                    public void failed(Throwable throwable) {
                        tv_update.setClickable(true);
                        Toast.makeText(getContext(),"下载失败",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    public static void show(FragmentActivity activity, ApkInfo apkInfo){
        Bundle bundle = new Bundle();
        bundle.putSerializable(KEY_DOWNLOAD_BEAN,apkInfo);
        UpdateVersionShowDialog dialog = new UpdateVersionShowDialog();
        dialog.setArguments(bundle);
        dialog.show(activity.getSupportFragmentManager(),"updateVersionShowDialog");
    }


}
