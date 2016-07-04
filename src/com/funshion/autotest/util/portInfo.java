package com.funshion.autotest.util;

/**
 * Created by chenhai on 5/27/16.
 */
public class portInfo {
    private static final int DEFAULT_HDMI_PORT = 3;
    private static final int DEFAULT_USB_PORT = 2;
    private static final int DEFAULT_ETH_PORT = 1;
    private static final int DEFAULT_AV_PORT = 1;
    private static final int DEFAULT_YUV_PORT = 1;
    private static final int DEFAULT_PC_PORT = 1;

    public static final boolean DEFAULT_WIFI_SUPPORT = true;
    public static final boolean DEFAULT_BT_SUPPORT = false;
    public static final boolean DEFAULT_HDCP_SUPPORT = true;

    private int hdmiPortNum;
    private boolean hdimRevert;
    private int usbPortNum;
    private int ethPortNum;
    private int yuvPortNum;
    private int avPortNum;
    private int pcPortNum;

    public portInfo(int hdmiPortNum, boolean hdmiRevert, int usbPortNum,
                    int ethPortNum, int avPort, int yuvPort, int pcPort){
        this.hdmiPortNum = hdmiPortNum;
        this.hdimRevert = hdmiRevert;
        this.usbPortNum = usbPortNum;
        this.ethPortNum = ethPortNum;
        this.yuvPortNum = avPort;
        this.avPortNum = yuvPort;
        this.pcPortNum = pcPort;
    }

    public portInfo(){
        this(DEFAULT_HDMI_PORT, false, DEFAULT_USB_PORT, DEFAULT_ETH_PORT,
                DEFAULT_AV_PORT, DEFAULT_YUV_PORT, DEFAULT_PC_PORT);
    }

    public int getHdmiPortNum(){return hdmiPortNum;}

    public void setHdmiPortNum(int hdmi){this.hdmiPortNum = hdmi;}

    public boolean isHdmiRevert(){return this.hdimRevert;}

    public void setHdmiRevert(boolean hdmiRevert){this.hdimRevert = hdmiRevert;}

    public int getUsbPortNum(){return usbPortNum;}

    public void setUsbPortNum(int usb){this.usbPortNum = usb;}

    public int getEthPortNum(){return ethPortNum;}

    public void setEthPortNum(int eth){this.ethPortNum = eth;}

    public int getAvPortNum(){return this.avPortNum;}

    public void setAvPortNum(int av){this.avPortNum = av;}

    public int getYuvPortNum(){return this.yuvPortNum;}

    public void setYuvPortNum(int yuv){this.yuvPortNum = yuv;}

    public int getPcPortNum(){return this.pcPortNum;}

    public void setPcPortNum(int pc){this.pcPortNum = pc;}

}
