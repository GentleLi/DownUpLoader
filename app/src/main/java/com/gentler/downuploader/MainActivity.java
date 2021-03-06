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

import com.gentler.downloaderlib.config.DownloadState;
import com.gentler.downloaderlib.database.DBManager;
import com.gentler.downloaderlib.impl.BarDownloaderObserver;
import com.gentler.downloaderlib.manager.DownloaderManager;
import com.gentler.downloaderlib.model.DownloadInfo;
import com.gentler.downuploader.config.Storage;
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
        mTargetId = "game1003";
        if (DownloaderManager.getInstance().isTargetDownloading(mTargetId)) {
            Toast.makeText(mContext, "下载目标已经存在于任务列表！", Toast.LENGTH_SHORT).show();
            LogUtils.d(TAG, "当前任务正在下载！");
            mDownloadInfo = DownloaderManager.getInstance().getTarget(mTargetId);
            if (mDownloadInfo.getCurrState() == DownloadState.PAUSE) {
                mDownloadInfo.setCurrState(DownloadState.RESTART);
                DownloaderManager.getInstance().restart(mDownloadInfo);
            }
            return;
        }
        mDownloadInfo = DBManager.getInstance(mContext).find(mTargetId);
        Log.e(TAG, "新建下载任务");
        DownloadInfo.Builder builder = new DownloadInfo.Builder();
        mDownloadInfo = builder.id("game1003").name("game1003.zip").downloadUrl("http://resource.peppertv.cn/h5/game1003.171018b.zip").currState(DownloadState.IDLE).size(698818).currPos(0).dir(Storage.DOWNLOAD_DIR).build();
//        if (null == mDownloadInfo) {
//            Log.e(TAG, "新建下载任务");
//            DownloadInfo.Builder builder = new DownloadInfo.Builder();
//            mDownloadInfo = builder.id("game1003").name("game1003.zip").downloadUrl("http://resource.peppertv.cn/h5/game1003.171018b.zip").currState(DownloadState.IDLE).size(698818).currPos(0).dir(Storage.DOWNLOAD_DIR).build();
//        } else {
//            Log.e(TAG, "断点下载操作");
//            LogUtils.d(TAG, mDownloadInfo);
//            mProgressBar.setProgress((int) (mDownloadInfo.getCurrPos() * 1000 / mDownloadInfo.getSize()));
//            if (mDownloadInfo.getCurrPos()==mDownloadInfo.getSize()){
//                //说明已经下载完成
//                Toast.makeText(mContext, "文件已经下载完成", Toast.LENGTH_SHORT).show();
//                return;
//            }
//        }

        mDownloadObserver = new BarDownloaderObserver(mDownloadInfo.getId()) {
            @Override
            public void onDownloadPause(DownloadInfo downloadInfo) {
                super.onDownloadPause(downloadInfo);

            }

            @Override
            public void onDownloadProgressChanged(DownloadInfo downloadInfo) {
                super.onDownloadProgressChanged(downloadInfo);
                Log.d(TAG, "currPos:" + downloadInfo.getCurrPos());
            }

            @Override
            public void onDownloadSuccess(DownloadInfo downloadInfo) {
                super.onDownloadSuccess(downloadInfo);
                Log.d(TAG, "onDownloadSuccess:" + downloadInfo.getCurrState());
            }

            @Override
            public void onDownloadError(DownloadInfo downloadInfo) {
                super.onDownloadError(downloadInfo);
                Log.d(TAG, "onDownloadError:" + downloadInfo.getCurrState());
            }
        };
        mDownloadObserver.bindProgressBar(mProgressBar);
//        mDownloadObserver = new SimpleDownloaderObserver(mDownloadInfo.getId()) {
//            @Override
//            public void onDownloadPause(DownloadInfo downloadInfo) {
//                super.onDownloadPause(downloadInfo);
//                Log.e(TAG, "onDownloadPause downloadInfo.getCurrState()：" + downloadInfo.getCurrState());
//            }
//
//            @Override
//            public void onDownloadProgressChanged(DownloadInfo downloadInfo) {
//                super.onDownloadProgressChanged(downloadInfo);
//                Log.e(TAG, "当前下载：" + downloadInfo.getCurrPos());
//                mProgressBar.setProgress((int) (downloadInfo.getCurrPos() * 1000 / downloadInfo.getSize()));
//            }
//
//            @Override
//            public void onDownloadSuccess(DownloadInfo downloadInfo) {
//                super.onDownloadSuccess(downloadInfo);
//                LogUtils.d("onDownloadSuccess:" + downloadInfo.getName() + " 下载成功");
//            }
//
//            @Override
//            public void onDownloadError(DownloadInfo downloadInfo) {
//                super.onDownloadError(downloadInfo);
//                DownloaderManager.getInstance().unregisterObserver(downloadInfo.getId());
//                LogUtils.d("onDownloadError:" + downloadInfo.getName() + " 下载失败");
//            }
//        };
        DownloaderManager.getInstance().download(mDownloadInfo);
        DownloaderManager.getInstance().registerObserver(mDownloadObserver);
    }

    private BarDownloaderObserver mDownloadObserver;


    public void backupDownload() {
        mTargetId = "download_ali";
        if (DownloaderManager.getInstance().isTargetDownloading(mTargetId)) {
            Log.e(TAG, "当前任务正在下载");
            mDownloadInfo = DownloaderManager.getInstance().getTarget(mTargetId);
            if (null != mDownloadInfo) {
                LogUtils.d(TAG, mDownloadInfo.toString());
            }
            return;
        }
        mDownloadInfo = DBManager.getInstance(mContext).find(mTargetId);
        if (null == mDownloadInfo) {
            Log.e(TAG, "新建下载任务");
            DownloadInfo.Builder builder = new DownloadInfo.Builder();
            mDownloadInfo = builder.id("GSMAlarm").name("GSMAlarm.apk").downloadUrl("http://192.168.1.105:8080/GsmAlarm.apk").currState(DownloadState.IDLE).size(3054762).currPos(0).dir(Storage.DOWNLOAD_DIR).build();
        } else {
            Log.e(TAG, "断点下载操作");
            LogUtils.d(TAG, mDownloadInfo);
            mProgressBar.setProgress((int) (mDownloadInfo.getCurrPos() * 1000 / mDownloadInfo.getSize()));
        }
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

