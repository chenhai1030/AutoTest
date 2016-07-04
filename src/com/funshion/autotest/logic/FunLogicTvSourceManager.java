package com.funshion.autotest.logic;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.funshion.autotest.ui.FunUiManager;
import com.funshion.autotest.ui.FunUiVideoWindowManager;
import com.mstar.android.tv.TvChannelManager;
import com.mstar.android.tv.TvCommonManager;
import com.mstar.android.tvapi.atv.listener.OnAtvPlayerEventListener;
import com.mstar.android.tvapi.atv.vo.AtvEventScan;
import com.mstar.android.tvapi.common.listener.OnTvEventListener;
import com.mstar.android.tvapi.common.listener.OnTvPlayerEventListener;
import com.mstar.android.tvapi.common.vo.HbbtvEventInfo;
import com.mstar.android.tvapi.dtv.listener.OnDtvPlayerEventListener;
import com.mstar.android.tvapi.dtv.vo.DtvEventScan;


/**
 * Created by chenhai on 5/19/16.
 */
public class FunLogicTvSourceManager {
    private static final String TAG = "TvSourceManager";
    public static final int SIGNAL_LOCK = 1001;
    public static final int SIGNAL_UNLOCK = 1002;
    public static boolean mIsTvPlaying = false;
    public static boolean mIsLocalPlaying = false;

    private FunUiManager mFunUiManager = FunUiManager.getInstance();;
    private static FunLogicTvSourceManager mFunLogicTvSourceManager = null;

    /* TV source monitor */
    private OnTvEventListener mTvEventListener = null;
    private OnTvPlayerEventListener mTvPlayerEventListener = null;
    private OnAtvPlayerEventListener mAtvPlayerEventListener = null;
    private OnDtvPlayerEventListener mDtvPlayerEventListener = null;
    private TvCommonManager mTvCommonManager = null;

    public static FunLogicTvSourceManager getInstance(){
        if (mFunLogicTvSourceManager == null){
            mFunLogicTvSourceManager = new FunLogicTvSourceManager();
        }
        return mFunLogicTvSourceManager;
    }

    public static int getCurrentSource(){
        return TvCommonManager.getInstance().getCurrentTvInputSource();
    }

    public void registerScanListener() {
        Log.d(TAG, "register Scan Event Listener");
        /* TV source monitor */
        mTvEventListener = new TvEventListener();
        TvCommonManager.getInstance().registerOnTvEventListener(mTvEventListener);

        mTvPlayerEventListener = new TvPlayerEventListener();
        TvChannelManager.getInstance().registerOnTvPlayerEventListener(mTvPlayerEventListener);

        mAtvPlayerEventListener = new AtvPlayerEventListener();
        TvChannelManager.getInstance().registerOnAtvPlayerEventListener(mAtvPlayerEventListener);

        mDtvPlayerEventListener = new DtvPlayerEventListener();
        TvChannelManager.getInstance().registerOnDtvPlayerEventListener(mDtvPlayerEventListener);

    }

    public void unregisterScanListener(){
        Log.d(TAG, "unregister Scan Event Listener");

        TvCommonManager.getInstance().unregisterOnTvEventListener(mTvEventListener);
        mTvEventListener = null;

        TvChannelManager.getInstance().unregisterOnTvPlayerEventListener(mTvPlayerEventListener);
        mTvPlayerEventListener = null;

        TvChannelManager.getInstance().unregisterOnAtvPlayerEventListener(mAtvPlayerEventListener);
        mAtvPlayerEventListener = null;

        TvChannelManager.getInstance().unregisterOnDtvPlayerEventListener(mDtvPlayerEventListener);
        mDtvPlayerEventListener = null;
    }

    public Handler mTvSignalLockHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case SIGNAL_LOCK:
                    break;
                case SIGNAL_UNLOCK:
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    };

    public void startToPlay(){
        int mInputSource = TvCommonManager.getInstance().getCurrentTvInputSource();
        Log.i(TAG, "startToPlay, mInputSource:" + mInputSource);
        if(mInputSource != TvCommonManager.INPUT_SOURCE_NONE
                && mInputSource != TvCommonManager.INPUT_SOURCE_STORAGE){

            FunUiManager.getInstance().onPauseModuleByName(
                    FunUiVideoWindowManager.class.getSimpleName());

            FunUiVideoWindowManager _FunUiVideoWindowManager =
                    (FunUiVideoWindowManager) mFunUiManager.getLogicModuleByModule(FunUiVideoWindowManager.class.getSimpleName());

            _FunUiVideoWindowManager.setVideoRectangle();

            registerScanListener();

            mIsTvPlaying = true;
        }else {
            mIsLocalPlaying = true;
        }
    }

    public void stopToPlay(){
        if(mIsTvPlaying){
            mIsTvPlaying = false;
            unregisterScanListener();
        }else{
            mIsLocalPlaying = false;
        }
    }

    private class TvEventListener implements OnTvEventListener{
        @Override
        public boolean onDtvReadyPopupDialog(int i, int i1, int i2) {
            return false;
        }

        @Override
        public boolean onScartMuteOsdMode(int i) {
            return false;
        }

        @Override
        public boolean onSignalUnlock(int i) {
            Log.d(TAG, "TvEventListener SIGNAL UnLock***");
            mTvSignalLockHandler.sendEmptyMessage(SIGNAL_UNLOCK);
            return false;
        }

        @Override
        public boolean onSignalLock(int i) {
            return false;
        }

        @Override
        public boolean onUnityEvent(int i, int i1, int i2) {
            return false;
        }

        @Override
        public boolean onScreenSaverMode(int i, int i1, int i2) {
            return false;
        }

        @Override
        public boolean onAtscPopupDialog(int i, int i1, int i2) {
            return false;
        }

        @Override
        public boolean onDeadthEvent(int i, int i1, int i2) {
            return false;
        }

        @Override
        public boolean on4k2kHDMIDisablePip(int i, int i1, int i2) {
            return false;
        }

        @Override
        public boolean on4k2kHDMIDisablePop(int i, int i1, int i2) {
            return false;
        }

        @Override
        public boolean on4k2kHDMIDisableDualView(int i, int i1, int i2) {
            return false;
        }

        @Override
        public boolean on4k2kHDMIDisableTravelingMode(int i, int i1, int i2) {
            return false;
        }
    }

    private class TvPlayerEventListener implements OnTvPlayerEventListener{
        @Override
        public boolean onScreenSaverMode(int i, int i1) {
            return false;
        }

        @Override
        public boolean onHbbtvUiEvent(int i, HbbtvEventInfo hbbtvEventInfo) {
            return false;
        }

        @Override
        public boolean onPopupDialog(int i, int i1, int i2) {
            return false;
        }

        @Override
        public boolean onPvrNotifyPlaybackTime(int i, int i1) {
            return false;
        }

        @Override
        public boolean onPvrNotifyPlaybackSpeedChange(int i) {
            return false;
        }

        @Override
        public boolean onPvrNotifyRecordTime(int i, int i1) {
            return false;
        }

        @Override
        public boolean onPvrNotifyRecordSize(int i, int i1) {
            return false;
        }

        @Override
        public boolean onPvrNotifyRecordStop(int i) {
            return false;
        }

        @Override
        public boolean onPvrNotifyPlaybackStop(int i) {
            return false;
        }

        @Override
        public boolean onPvrNotifyPlaybackBegin(int i) {
            return false;
        }

        @Override
        public boolean onPvrNotifyTimeShiftOverwritesBefore(int i, int i1) {
            return false;
        }

        @Override
        public boolean onPvrNotifyTimeShiftOverwritesAfter(int i, int i1) {
            return false;
        }

        @Override
        public boolean onPvrNotifyOverRun(int i) {
            return false;
        }

        @Override
        public boolean onPvrNotifyUsbRemoved(int i, int i1) {
            return false;
        }

        @Override
        public boolean onPvrNotifyCiPlusProtection(int i) {
            return false;
        }

        @Override
        public boolean onPvrNotifyParentalControl(int i, int i1) {
            return false;
        }

        @Override
        public boolean onPvrNotifyAlwaysTimeShiftProgramReady(int i) {
            return false;
        }

        @Override
        public boolean onPvrNotifyAlwaysTimeShiftProgramNotReady(int i) {
            return false;
        }

        @Override
        public boolean onPvrNotifyCiPlusRetentionLimitUpdate(int i, int i1) {
            return false;
        }

        @Override
        public boolean onTvProgramInfoReady(int i) {
            return false;
        }

        @Override
        public boolean onSignalLock(int i) {
            Log.d(TAG, "TvPlayerEventListener SIGNAL Lock***");
            // Shielding storage upload the default postevent
            if (TvCommonManager.INPUT_SOURCE_STORAGE == mTvCommonManager.getCurrentTvInputSource()) {
                return true;
            }
            mTvSignalLockHandler.sendEmptyMessage(SIGNAL_LOCK);
            return false;
        }

        @Override
        public boolean onSignalUnLock(int i) {
            Log.d(TAG, "TvPlayerEventListener SIGNAL UnLock***");
            // Shielding storage upload the default postevent
            if (TvCommonManager.INPUT_SOURCE_STORAGE == mTvCommonManager.getCurrentTvInputSource()) {
                return true;
            }
            mTvSignalLockHandler.sendEmptyMessage(SIGNAL_UNLOCK);
            return false;
        }

        @Override
        public boolean onEpgUpdateList(int i, int i1) {
            return false;
        }

        @Override
        public boolean on4k2kHDMIDisablePip(int i, int i1, int i2) {
            return false;
        }

        @Override
        public boolean on4k2kHDMIDisablePop(int i, int i1, int i2) {
            return false;
        }

        @Override
        public boolean on4k2kHDMIDisableDualView(int i, int i1, int i2) {
            return false;
        }

        @Override
        public boolean on4k2kHDMIDisableTravelingMode(int i, int i1, int i2) {
            return false;
        }

        @Override
        public boolean onDtvPsipTsUpdate(int i, int i1, int i2) {
            return false;
        }

        @Override
        public boolean onEmerencyAlert(int i, int i1, int i2) {
            return false;
        }

        @Override
        public boolean onDtvChannelInfoUpdate(int i, int i1, int i2) {
            return false;
        }
    }

    private class AtvPlayerEventListener implements OnAtvPlayerEventListener{
        @Override
        public boolean onAtvAutoTuningScanInfo(int i, AtvEventScan atvEventScan) {
            return false;
        }

        @Override
        public boolean onAtvManualTuningScanInfo(int i, AtvEventScan atvEventScan) {
            return false;
        }

        @Override
        public boolean onSignalLock(int i) {
            return false;
        }

        @Override
        public boolean onSignalUnLock(int i) {
            return false;
        }

        @Override
        public boolean onAtvProgramInfoReady(int i) {
            return false;
        }
    }

    private class DtvPlayerEventListener implements OnDtvPlayerEventListener{
        @Override
        public boolean onDtvChannelNameReady(int i) {
            return false;
        }

        @Override
        public boolean onDtvAutoTuningScanInfo(int i, DtvEventScan dtvEventScan) {
            return false;
        }

        @Override
        public boolean onDtvProgramInfoReady(int i) {
            return false;
        }

        @Override
        public boolean onCiLoadCredentialFail(int i) {
            return false;
        }

        @Override
        public boolean onEpgTimerSimulcast(int i, int i1) {
            return false;
        }

        @Override
        public boolean onHbbtvStatusMode(int i, boolean b) {
            return false;
        }

        @Override
        public boolean onMheg5StatusMode(int i, int i1) {
            return false;
        }

        @Override
        public boolean onMheg5ReturnKey(int i, int i1) {
            return false;
        }

        @Override
        public boolean onOadHandler(int i, int i1, int i2) {
            return false;
        }

        @Override
        public boolean onOadDownload(int i, int i1) {
            return false;
        }

        @Override
        public boolean onDtvAutoUpdateScan(int i) {
            return false;
        }

        @Override
        public boolean onTsChange(int i) {
            return false;
        }

        @Override
        public boolean onPopupScanDialogLossSignal(int i) {
            return false;
        }

        @Override
        public boolean onPopupScanDialogNewMultiplex(int i) {
            return false;
        }

        @Override
        public boolean onPopupScanDialogFrequencyChange(int i) {
            return false;
        }

        @Override
        public boolean onRctPresence(int i) {
            return false;
        }

        @Override
        public boolean onChangeTtxStatus(int i, boolean b) {
            return false;
        }

        @Override
        public boolean onDtvPriComponentMissing(int i) {
            return false;
        }

        @Override
        public boolean onAudioModeChange(int i, boolean b) {
            return false;
        }

        @Override
        public boolean onMheg5EventHandler(int i, int i1) {
            return false;
        }

        @Override
        public boolean onOadTimeout(int i, int i1) {
            return false;
        }

        @Override
        public boolean onGingaStatusMode(int i, boolean b) {
            return false;
        }

        @Override
        public boolean onSignalLock(int i) {
            int currentSource = mTvCommonManager.getCurrentTvInputSource();
            boolean isDtvStable = mTvCommonManager.isSignalStable(TvCommonManager.INPUT_SOURCE_DTV);
            Log.d(TAG, "DtvPlayerEventListener SIGNAL Lock***, dtv stable status =" + isDtvStable + ",input source=" + currentSource);
            if ((currentSource == TvCommonManager.INPUT_SOURCE_DTV) && isDtvStable) {
                mTvSignalLockHandler.sendEmptyMessage(SIGNAL_LOCK);
            }
            return true;
        }

        @Override
        public boolean onSignalUnLock(int i) {
            Log.d(TAG, "DtvPlayerEventListener SIGNAL UnLock***");
            mTvSignalLockHandler.sendEmptyMessage(SIGNAL_UNLOCK);
            return false;
        }

        @Override
        public boolean onUiOPRefreshQuery(int i) {
            return false;
        }

        @Override
        public boolean onUiOPServiceList(int i) {
            return false;
        }

        @Override
        public boolean onUiOPExitServiceList(int i) {
            return false;
        }
    }



}
