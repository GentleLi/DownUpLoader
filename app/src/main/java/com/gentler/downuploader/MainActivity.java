package com.gentler.downuploader;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;


import com.gentler.downuploader.model.DownloadInfo;
import com.gentler.downuploader.task.DownloadTask;

import butterknife.BindView;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.btn_download)
    AppCompatButton mBtnDownload;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



    }

    @OnClick(R.id.btn_download)
    public void onClickDownload(){
        DownloadInfo downloadInfo=new DownloadInfo();
        downloadInfo.setCurrPos(0);
        downloadInfo.setDownloadUrl("http://resource.peppertv.cn/gift/meteor_3d416423dbca1a0940fc3d8ac81f9410_2559755.zip");
        DownloadTask mDownloadTask=new DownloadTask(downloadInfo);

    }
}
