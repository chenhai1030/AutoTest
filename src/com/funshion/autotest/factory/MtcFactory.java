package com.funshion.autotest.factory;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;

import com.mstar.android.tv.TvAudioManager;
import com.mstar.android.tv.TvCommonManager;
import com.mstar.android.tvapi.common.TvManager;
import com.mstar.android.tvapi.common.exception.TvCommonException;

/**
 * Created by chenhai on 6/7/16.
 */
public class MtcFactory {
    public static String TAG = "MtcFactory";
    public static final String MTC_SMT_ETH_ACTION = "Fun_tv_SMT_WIRE_TestResult";
    public static final String MTC_ETH_ACTION = "Fun_tv_WIRE_TestResult";
    public static final String MTC_WIFI_BT_ACTION = "Fun_tv_WIFI_BT_TestResult";

    private static final String FACTORY_TEST_DONE = "factory_test_done";
    private static final String NEED_HANDLE_FACTORY_RESET_EVENT = "need_handle_factory_reset_event";

    private Context mContext = null;
    private Handler mHandler = new Handler();

    public MtcFactory(Context context){
        mContext = context;
    }

    public void factoryTestKey(int KeyCode){
        Intent intent = null;
        switch (KeyCode){
            case 4228://KeyEvent.KEYCODE_HISENSE_FAC_NEC_AV:
                break;
            case 4230://KeyEvent.KEYCODE_HISENSE_FAC_NEC_AGING:
                Log.i(TAG, ".........KEYCODE_HISENSE_FAC_NEC_AGING.............");
                try {
                    TvCommonManager.getInstance().setTvosCommonCommand("JustTestAGING");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case 4252://KeyEvent.KEYCODE_HISENSE_TEST_BROAD_SVIDEO1:
                Log.i(TAG, ".........KEYCODE_HISENSE_TEST_BROAD_SVIDEO1.............");
                try
                {
                    TvCommonManager.getInstance().setTvosCommonCommand("JustTestDTV1");
                } catch (Exception e)
                {
                    e.printStackTrace();
                }
                TvAudioManager.getInstance().setSpeakerVolume(93);
                break;
            case 4253://KeyEvent.KEYCODE_HISENSE_TEST_BROAD_SVIDEO2:
                Log.i(TAG, ".........KEYCODE_HISENSE_TEST_BROAD_SVIDEO2.............");
                try
                {
                    TvCommonManager.getInstance().setTvosCommonCommand("SMTTestDTV");
                } catch (Exception e)
                {
                    e.printStackTrace();
                }
                mHandler.postDelayed(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        try
                        {
                            TvCommonManager.getInstance().setTvosCommonCommand("SMTSETRGB");
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                    }
                }, 3500);
                TvAudioManager.getInstance().setSpeakerVolume(93);
            case 4227://KeyEvent.KEYCODE_HISENSE_FAC_NEC_M:
                Log.i(TAG, ".........WIFI.............");
                intent = new Intent("Fun_tv_WIFI_BT_TestResult");
                mContext.startService(intent);
                break;
            case 4118://KeyEvent.KEYCODE_CHANGHONGIR_HAND:
                Log.i("mstartvserver",".........SMT NETWORK.............");
                intent = new Intent("Fun_tv_SMT_WIRE_TestResult");
//            intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
                //intent.putExtra("actionflag", 4);
                //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startService(intent);
                break;
            case 4212://KeyEvent.KEYCODE_HISENSE_FAC_NEC_IP:
                intent = new Intent("Fun_tv_WIRE_TestResult");
//    		intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
                mContext.startService(intent);
                break;
            case 4247://KeyEvent.KEYCODE_HISENSE_TEST_BROAD_TV:
                Log.i("mstartvserver", ".........KEYCODE_HISENSE_TEST_BROAD_TV.............");
                try
                {
                    TvCommonManager.getInstance().setTvosCommonCommand("SMTTestATV");
                } catch (Exception e)
                {
                    e.printStackTrace();
                }
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        try
                        {
                            TvCommonManager.getInstance().setTvosCommonCommand("JustTestATVCOLOR");
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                    }
                }, 300);
                mHandler.postDelayed(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        try
                        {
                            TvCommonManager.getInstance().setTvosCommonCommand("SMTSETRGB");

                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                    }
                }, 3500);
                TvAudioManager.getInstance().setSpeakerVolume(93);
                break;
            case 4248://KeyEvent.KEYCODE_HISENSE_TEST_BROAD_DTV:
                Log.i(TAG, ".........KEYCODE_HISENSE_TEST_BROAD_DTV.............");
                try
                {
                    TvCommonManager.getInstance().setTvosCommonCommand("SMTTestDTV");
                } catch (Exception e)
                {
                    e.printStackTrace();
                }
                mHandler.postDelayed(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        try
                        {
                            TvCommonManager.getInstance().setTvosCommonCommand("SMTSETRGB");

                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                    }
                }, 3500);
                TvAudioManager.getInstance().setSpeakerVolume(93);
                break;

            case 4261://KeyEvent.KEYCODE_HISENSE_TEST_BOARD_VGA:
                Log.i(TAG, ".........KEYCODE_SKYWORTH_FACTORY_VGA.............");

                //Fun.tv Begin Factory Test key handle
                //change factory test done to off
                try{
                    TvCommonManager.getInstance().setTvosCommonCommand("poweron_direct");
                    TvManager.getInstance().setEnvironment(FACTORY_TEST_DONE, "off");
                    TvManager.getInstance().setEnvironment(NEED_HANDLE_FACTORY_RESET_EVENT, "1");
                    clearEnviromentValues();
                    TvCommonManager.getInstance().setTvosCommonCommand("SetOpenUart");
                } catch(TvCommonException e){
                    e.printStackTrace();
                }
                //Fun.tv End Factory Test key handle

                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run()
                    {
                        try
                        {
                            TvCommonManager.getInstance().setTvosCommonCommand("reboot");
                            // TvCommonManager.getInstance().setTvosCommonCommand("JustTestATVCOLOR");
                            //mPowerManager.reboot(null);
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                    }
                }, 3000);
                break;
            case 4120://KEYCODE_CHANGHONGIR_HELP:
                Log.i(TAG, ".........factory. xx............");
                intent = new Intent("mstar.tvsetting.factory.intent.action.MainmenuActivity");
                intent.putExtra("actionflag", 2);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);
                break;
            default:
                break;
        }

    }

    /**
     * 清除一些环境变量，在reset 和 pc键等需要恢复去掉环境变量的地方进行强制清除;
     * 设置借口调用底层native方法；TvCommonException抛出，调用者需要主动try catch该异常；
     *
     * @throws TvCommonException
     */
    private void clearEnviromentValues() throws TvCommonException {
        // <<<<<< 这些都是出厂设置相关的环境变量，清除掉
        TvManager.getInstance().setEnvironment("wifi_state_before_factory_recovery_event",
                "");
        TvManager.getInstance().setEnvironment("need_handle_wifi_recovery_event", "");
        TvManager.getInstance().setEnvironment("mode_name_before_factory_recovery_event",
                "");
        TvManager.getInstance()
                .setEnvironment("dev_name_before_factory_recovery_event", "");
        TvManager.getInstance().setEnvironment("ipaddr_before_factory_recovery_event", "");
        TvManager.getInstance().setEnvironment("netmask_before_factory_recovery_event", "");
        TvManager.getInstance().setEnvironment("route_before_factory_recovery_event", "");
        TvManager.getInstance().setEnvironment("dns_before_factory_recovery_event", "");
        TvManager.getInstance().setEnvironment("dns2_name_before_factory_recovery_event",
                "");
        TvManager.getInstance().setEnvironment(
                "proxy_on_name_before_factory_recovery_event", "");
        TvManager.getInstance().setEnvironment("proxy_host_before_factory_recovery_event",
                "");
        TvManager.getInstance().setEnvironment("proxy_port_before_factory_recovery_event",
                "");
        TvManager.getInstance().setEnvironment(
                "proxy_exclusion_before_factory_recovery_event", "");
        // >>>>>> 清除完毕

        // TODO 后续需要清除其他的环境变量，继续从此处往下即可
    }
}
