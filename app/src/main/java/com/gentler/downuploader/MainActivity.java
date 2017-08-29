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

import com.gentler.downuploader.config.Storage;
import com.gentler.downuploader.database.DBManager;
import com.gentler.downuploader.impl.SimpleDownloaderObserver;
import com.gentler.downuploader.manager.DownloaderManager;
import com.gentler.downuploader.model.DownloadInfo;
import com.gentler.downuploader.utils.LogUtils;

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
    private String mTargetId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getApplicationContext();
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @NeedsPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    @OnClick(R.id.btn_start)
    public void onClickDownload(View view) {
//        mDownloadInfo.setDownloadUrl("http://192.168.1.105:8080/AdobePatcher.zip");


        mTargetId = "download_01";
        mDownloadInfo = DBManager.getInstance(mContext).find(mTargetId);
        if (null == mDownloadInfo) {
            mDownloadInfo = new DownloadInfo();
            mDownloadInfo.setId("download_01");
            mDownloadInfo.setCurrPos(0);
            mDownloadInfo.setSize(106173675);
            mDownloadInfo.setName("Python学习手册(第4版).pdf");
            mDownloadInfo.setDownloadUrl("http://192.168.1.6:8080/PythonLearning.pdf.zip");
            mDownloadInfo.setDir(Storage.DOWNLOAD_DIR);
//        downloadInfo.setDownloadUrl("http://resource.peppertv.cn/gift/meteor_3d416423dbca1a0940fc3d8ac81f9410_2559755.zip");
        }
//        if (DownloaderManager.getInstance().isTargetExists(mTargetId)){
//            Toast.makeText(mContext, "下载目标已经存在于任务列表！", Toast.LENGTH_SHORT).show();
//            return;
//        }
        DownloaderManager.getInstance().download(mDownloadInfo);
        DownloaderManager.getInstance().registerObserver(new SimpleDownloaderObserver(mDownloadInfo.getId()) {
            @Override
            public void onDownloadPause(DownloadInfo downloadInfo) {
                super.onDownloadPause(downloadInfo);
                Log.e(TAG, "onDownloadPause downloadInfo.getCurrState()：" + downloadInfo.getCurrState());

            }

            @Override
            public void onDownloadProgressChanged(DownloadInfo downloadInfo) {
                super.onDownloadProgressChanged(downloadInfo);
                Log.e(TAG, "当前下载：" + downloadInfo.getCurrPos());
                mProgressBar.setProgress((int) (downloadInfo.getCurrPos() * 1000 / downloadInfo.getSize()));
            }

            @Override
            public void onDownloadSuccess(DownloadInfo downloadInfo) {
                LogUtils.d("onDownloadSuccess:" + downloadInfo.getName()+" 下载成功");
            }

            @Override
            public void onDownloadError(DownloadInfo downloadInfo) {

                LogUtils.d("onDownloadError:" + downloadInfo.getName()+" 下载失败");
            }
        });

    }

    @OnClick(R.id.btn_pause)
    public void onClickPause(View view) {
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
