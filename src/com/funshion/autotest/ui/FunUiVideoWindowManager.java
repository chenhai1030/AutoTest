package com.funshion.autotest.ui;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.VideoView;

import com.funshion.autotest.R;
import com.mstar.android.tv.TvCommonManager;
import com.mstar.android.tvapi.common.PictureManager;
import com.mstar.android.tvapi.common.TvManager;
import com.mstar.android.tvapi.common.exception.TvCommonException;
import com.mstar.android.tvapi.common.vo.EnumScalerWindow;
import com.mstar.android.tvapi.common.vo.PanelProperty;
import com.mstar.android.tvapi.common.vo.VideoWindowType;

public class FunUiVideoWindowManager implements FunUiBase{
    private Context mContext = null;
    private VideoView mVideoView = null;
    private TextView mTextView = null;

    private Handler mHandler = new Handler();
    // 设备屏幕宽高
    public int mScreenWidthPixels = 0;
    public int mScreenHeightPixels = 0;

    private int mInputSource = 0;

    private String TAG = "FunUiVideoWindowManager";

    public FunUiVideoWindowManager(Context context){
        mContext = context;
    }

    @Override
    public boolean OnCreate(View view) {
        LinearLayout _LinearLayout = (LinearLayout) view;
        View _view = View.inflate(mContext, R.layout.video_window, null);

        mVideoView = (VideoView) _view.findViewById(R.id.vv_video_surface_view);
        mTextView = (TextView) _view.findViewById(R.id.vv_item_bar_name);

        _LinearLayout.addView(_view);

        DisplayMetrics dm = mContext.getResources().getDisplayMetrics();
        mScreenWidthPixels = dm.widthPixels;
        mScreenHeightPixels = dm.heightPixels;
        Log.d(TAG, "mScreenWidthPixels: "+mScreenWidthPixels+"mScreenHeightPixels: "+mScreenHeightPixels);

        mInputSource = TvCommonManager.getInstance().getCurrentTvInputSource();
        mHandler.post(task);
        return false;
    }

    private Runnable task = new Runnable(){

        @Override
        public void run() {
            mHandler.postDelayed(this, 1000);
            int _inputSource = TvCommonManager.getInstance().getCurrentTvInputSource();
            if (_inputSource != mInputSource){
                mInputSource = _inputSource;
                OnShow();
            }
        }
    };

    public void setVideoRectangle() {
        try {
            PictureManager pm = TvManager.getInstance().getPictureManager();
            PanelProperty pp = pm.getPanelWidthHeight();
            float scale_w = ((float)mScreenWidthPixels / pp.width);
            float scale_h = ((float)mScreenHeightPixels / pp.height);
            Log.d(TAG, "pp.width:" + pp.width + ",pp.height:" + pp.height + "scale_w:"+ scale_w + "scale_h:"+scale_h);
            VideoWindowType videoWindowType = new VideoWindowType();
            videoWindowType.x = (int) (mVideoView.getLeft() / scale_w);
            videoWindowType.y = (int) (mVideoView.getTop() / scale_h);
            videoWindowType.width = (int) (mVideoView.getWidth() / scale_w);
            videoWindowType.height = (int) (mVideoView.getHeight() / scale_h);
            Log.d(TAG, "left = "+ mVideoView.getLeft()+ "top = "+ mVideoView.getTop()+
                    "W = "+mVideoView.getWidth()+"H = "+mVideoView.getHeight() );
            Log.i(TAG,"setVideoRectangle  w:" + videoWindowType.width + " h:" +
                    videoWindowType.height + " x:" + videoWindowType.x + " y:" + videoWindowType.y);
            TvManager.getInstance().getPictureManager()
                    .selectWindow(EnumScalerWindow.E_MAIN_WINDOW);
            TvManager.getInstance().getPictureManager().setDisplayWindow(videoWindowType);
            TvManager.getInstance().getPictureManager().scaleWindow();
            Log.i(TAG,"setVideoRectangle end");
        } catch (TvCommonException e) {
            e.printStackTrace();
        }
    }


    @Override
    public boolean OnShow() {
        int _inputSource = mInputSource;
        String value = null;

        switch (_inputSource){
            case TvCommonManager.INPUT_SOURCE_NONE:
            case TvCommonManager.INPUT_SOURCE_STORAGE:
                value = "NONE";
                break;
            case TvCommonManager.INPUT_SOURCE_CVBS:
                value = "AV";
                break;
            case TvCommonManager.INPUT_SOURCE_YPBPR:
                value = "YUV";
                break;
            case TvCommonManager.INPUT_SOURCE_HDMI:
                value = "HDMI1";
                break;
            case TvCommonManager.INPUT_SOURCE_HDMI2:
                value = "HDMI2";
                break;
            case TvCommonManager.INPUT_SOURCE_HDMI3:
                value = "HDMI3";
                break;
            case TvCommonManager.INPUT_SOURCE_ATV:
                value = "ATV";
                break;
            case TvCommonManager.INPUT_SOURCE_DTV:
                value = "DTV";
                break;
            case TvCommonManager.INPUT_SOURCE_VGA:
                value = "VGA";
                break;
            default:
                value = "NONE";
                break;
        }

        mTextView.setText(value);

        return false;
    }

    @Override
    public boolean OnRefresh() {
        return false;
    }

    @Override
    public boolean OnDestroy() {
        return false;
    }

    @Override
    public void OnPause() {
        mVideoView.stopPlayback();
    }

    @Override
    public void OnResume() {
        mVideoView.setFocusable(false);
        mVideoView.setVideoURI(Uri.parse("android.resource://"
                + mContext.getPackageName() + "/" + R.raw.test));

        mVideoView.start();
        mVideoView.setOnPreparedListener(new OnPreparedListener() {

            @Override
            public void onPrepared(MediaPlayer mp) {

                mp.start();
                mp.setLooping(true);
            }
        });
    }

    @Override
    public void SetTestSuccess() {

    }
}
