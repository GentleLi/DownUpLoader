package com.gentler.downuploader;

import android.Manifest;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.gentler.downuploader.manager.DownloaderManager;
import com.gentler.downuploader.model.DownloadInfo;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    @BindView(R.id.btn_start)
    AppCompatButton mBtnStart;
    @BindView(R.id.pb_download)
    ProgressBar mProgressBar;
    private Context mContext;
    private DownloadInfo mDownloadInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext=getApplicationContext();
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @NeedsPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    @OnClick(R.id.btn_start)
    public void onClickDownload(View view) {
        mDownloadInfo = new DownloadInfo();
        mDownloadInfo.setId("gift-19");
        mDownloadInfo.setCurrPos(0);
        mDownloadInfo.setSize(13462118);
        mDownloadInfo.setName("gift-19");
//        downloadInfo.setDownloadUrl("http://resource.peppertv.cn/gift/meteor_3d416423dbca1a0940fc3d8ac81f9410_2559755.zip");
        mDownloadInfo.setDownloadUrl("http://192.168.1.105:8080/AdobePatcher.zip");
        DownloaderManager.getInstance().download(mDownloadInfo);
        DownloaderManager.getInstance().registerObserver(new DownloaderManager.DownloaderObserver() {
            @Override
            public void onDownloadStateChanged(DownloadInfo downloadInfo) {//下载状态发生改变
                Log.e(TAG,"onDownloadStateChanged downloadInfo.getCurrState()："+downloadInfo.getCurrState());
            }

            @Override
            public void onDownloadProgressChanged(DownloadInfo downloadInfo) {
                Log.e(TAG,"当前下载："+downloadInfo.getCurrPos());
                mProgressBar.setProgress((int) (downloadInfo.getCurrPos()*1000/downloadInfo.getSize()));
            }
        });

    }

    @OnClick(R.id.btn_pause)
    public void onClickPause(View view){
        DownloaderManager.getInstance().pause(mDownloadInfo);
    }




    @OnShowRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    void showRationaleForCamera(final PermissionRequest request) {
        new AlertDialog.Builder(this)
                .setMessage(R.string.permission_camera_rationale)
                .setPositiveButton(R.string.button_allow, (dialog, button) -> request.proceed())
                .setNegativeButton(R.string.button_deny, (dialog, button) -> request.cancel())
                .show();
    }

    @OnPermissionDenied(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    void showDeniedForCamera() {
        Toast.makeText(this, R.string.permission_camera_denied, Toast.LENGTH_SHORT).show();
    }


    @OnNeverAskAgain(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    void showNeverAskForCamera() {
        Toast.makeText(this, R.string.permission_camera_neverask, Toast.LENGTH_SHORT).show();
    }

}
