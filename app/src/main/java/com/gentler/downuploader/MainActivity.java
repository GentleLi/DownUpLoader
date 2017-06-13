package com.gentler.downuploader;

import android.Manifest;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.widget.Toast;


import com.gentler.downuploader.manager.ThreadPoolManager;
import com.gentler.downuploader.model.DownloadInfo;
import com.gentler.downuploader.task.DownloadTask;

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

    @BindView(R.id.btn_download)
    AppCompatButton mBtnDownload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

    }

    @NeedsPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    @OnClick(R.id.btn_download)
    public void onClickDownload(View view) {
        DownloadInfo downloadInfo = new DownloadInfo();
        downloadInfo.setCurrPos(0);
        downloadInfo.setSize(2559755);
        downloadInfo.setName("gift-19");
        downloadInfo.setDownloadUrl("http://resource.peppertv.cn/gift/meteor_3d416423dbca1a0940fc3d8ac81f9410_2559755.zip");
        DownloadTask mDownloadTask = new DownloadTask(downloadInfo);
        ThreadPoolManager.getInstance().execute(mDownloadTask);
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
