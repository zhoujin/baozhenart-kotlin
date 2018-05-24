package com.markjin.artmall.zxing;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.markjin.artmall.BaseActivity;
import com.markjin.artmall.Contants;
import com.markjin.artmall.R;
import com.markjin.artmall.ui.GoodsDetailActivity;
import com.markjin.artmall.ui.WebviewActivity;
import com.markjin.artmall.utils.PreferenceUtil;
import com.markjin.artmall.utils.ScreenUtils;
import com.markjin.artmall.utils.StringUtil;
import com.markjin.artmall.zxing.camera.CameraManager;
import com.markjin.artmall.zxing.decoding.CaptureActivityHandler;
import com.markjin.artmall.zxing.decoding.InactivityTimer;
import com.markjin.artmall.zxing.view.ViewfinderView;

import java.io.IOException;
import java.net.URL;
import java.util.Vector;

public class CaptureActivity extends BaseActivity implements Callback, OnClickListener {

    private static final float BEEP_VOLUME = 0.10f;
    private static final long VIBRATE_DURATION = 200L;
    private static int RESULTCODE_LOGIN = 1000;
    /**
     * When the beep has finished playing, rewind to queue up another one.
     */
    private final OnCompletionListener beepListener = new OnCompletionListener() {
        public void onCompletion(MediaPlayer mediaPlayer) {
            mediaPlayer.seekTo(0);
        }
    };
    private Activity context;
    private CaptureActivityHandler handler;
    private ViewfinderView viewfinderView;
    private boolean hasSurface;
    private Vector<BarcodeFormat> decodeFormats;
    private String characterSet;
    //private TextView txtResult;
    private InactivityTimer inactivityTimer;
    private MediaPlayer mediaPlayer;
    private boolean playBeep;
    private boolean vibrate;
    private Dialog dialog;
    private String result;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);
        context = this;
        findViewById(R.id.iv_left).setOnClickListener(this);
        ((TextView) findViewById(R.id.tv_title)).setText(R.string.scan);
        CameraManager.init(getApplication());
        viewfinderView = findViewById(R.id.viewfinder_view);
        hasSurface = false;
        inactivityTimer = new InactivityTimer(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        SurfaceView surfaceView = findViewById(R.id.preview_view);
        SurfaceHolder surfaceHolder = surfaceView.getHolder();
        if (hasSurface) {
            initCamera(surfaceHolder);
        } else {
            surfaceHolder.addCallback(this);
            surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        }
        decodeFormats = null;
        characterSet = null;
        playBeep = true;
        AudioManager audioService = (AudioManager) getSystemService(AUDIO_SERVICE);
        if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
            playBeep = false;
        }
        initBeepSound();
        vibrate = false;
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (handler != null) {
            handler.quitSynchronously();
            handler = null;
        }
        CameraManager.get().closeDriver();
    }

    @Override
    protected void onDestroy() {
        inactivityTimer.shutdown();
        super.onDestroy();
    }

    private void initCamera(SurfaceHolder surfaceHolder) {
        try {
            CameraManager.get().openDriver(surfaceHolder);
        } catch (IOException ioe) {
            return;
        } catch (RuntimeException e) {
            return;
        }
        if (handler == null) {
            handler = new CaptureActivityHandler(this, decodeFormats,
                    characterSet);
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (!hasSurface) {
            hasSurface = true;
            initCamera(holder);
        }

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        hasSurface = false;

    }

    public ViewfinderView getViewfinderView() {
        return viewfinderView;
    }

    public Handler getHandler() {
        return handler;
    }

    public void drawViewfinder() {
        viewfinderView.drawViewfinder();

    }

    /**
     * 扫描结果
     *
     * @param obj     getText();
     * @param barcode 扫码的截图
     */
    public void handleDecode(Result obj, Bitmap barcode) {
        result = obj.getText().toString();
        if (StringUtil.isEmpty(result)) {
            return;
        }
        inactivityTimer.onActivity();
        playBeepSoundAndVibrate();
        if (result.contains("http://") || result.contains("https://")) {//网页链接
            try {
                URL url = new URL(result);
                if (url.getHost().contains(Contants.Companion.getBASE_DOMAIN())) {//
                    if (result.toLowerCase().contains("needLogin=1".trim().toLowerCase())) {//是否需要登录needLogin=1
                        Bundle bundle = new Bundle();
                        if (PreferenceUtil.isLogin()) {
                            bundle.putString("web_url", result);
                            intentActivity(this, WebviewActivity.class, bundle);
                        }
                    } else {
                        Bundle bundle = new Bundle();
                        if (result.contains("goods.php?id=")) {
                            try {
                                bundle.putString("goods_id", result.substring(result.indexOf("?") + 4, result.indexOf("&")));
                                bundle.putString("url_params", result.substring(result.indexOf("&"), result.length()));
                            } catch (Exception e) {
                                String[] str_arr = result.split("=");
                                if (str_arr.length > 2) {
                                    String[] goods_info = str_arr[1].split("&");
                                    bundle.putString("goods_id", goods_info[0]);
                                } else {
                                    bundle.putString("goods_id", str_arr[1]);
                                }
                            }
                            bundle.putString("from", "buy");
                            intentActivity(this, GoodsDetailActivity.class, bundle);
                        } else {
                            bundle.putString("web_url", result);
                            intentActivity(this, WebviewActivity.class, bundle);
                        }
                    }
                } else {//无规则
                    showResultDialog(getString(R.string.scan_link_hint), result);
                }
            } catch (Exception e) {

            }
        } else {//外部链接
            showResultDialog(getString(R.string.scan_link_hint), result);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (RESULTCODE_LOGIN == requestCode) {
            if (PreferenceUtil.isLogin()) {
                Bundle bundle = new Bundle();
                bundle.putString("web_url", result);
                intentActivity(this, WebviewActivity.class, bundle);
            }
        }
    }

    private void initBeepSound() {
        if (playBeep && mediaPlayer == null) {
            setVolumeControlStream(AudioManager.STREAM_MUSIC);
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setOnCompletionListener(beepListener);

            AssetFileDescriptor file = getResources().openRawResourceFd(
                    R.raw.beep);
            try {
                mediaPlayer.setDataSource(file.getFileDescriptor(),
                        file.getStartOffset(), file.getLength());
                file.close();
                mediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME);
                mediaPlayer.prepare();
            } catch (IOException e) {
                mediaPlayer = null;
            }
        }
    }

    private void playBeepSoundAndVibrate() {
        if (playBeep && mediaPlayer != null) {
            mediaPlayer.start();
        }
        if (vibrate) {
            Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
            vibrator.vibrate(VIBRATE_DURATION);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_left:
                finishActivity(this);
                break;
            default:
                break;
        }

    }

    private void showResultDialog(String title, final String content) {
        if (dialog == null) {
            dialog = new Dialog(this, R.style.customDialogStyle);
        } else {
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
        }
        dialog.setCancelable(false);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_middle_normal, null);
        view.findViewById(R.id.tv_dialog_title).setVisibility(View.VISIBLE);
        ((TextView) view.findViewById(R.id.tv_dialog_title)).setText(title);
        ((TextView) view.findViewById(R.id.tv_dialog_content)).setText(content);
        ((TextView) view.findViewById(R.id.bt_dialog_right)).setText(R.string.open_link);
        dialog.setContentView(view);
        view.findViewById(R.id.bt_dialog_left).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                restartCamera();

            }
        });
        view.findViewById(R.id.bt_dialog_right).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                Uri uri = Uri.parse(content);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });
        dialog.getWindow().setLayout(ScreenUtils.getScreenWidth(this) - 160, LinearLayout.LayoutParams.WRAP_CONTENT);
        dialog.show();
    }

    private void restartCamera() {
        if (handler != null)
            handler.restartPreviewAndDecode();
        SurfaceView surfaceView = (SurfaceView) findViewById(R.id.preview_view);
        SurfaceHolder surfaceHolder = surfaceView.getHolder();
        initCamera(surfaceHolder);
    }
}
