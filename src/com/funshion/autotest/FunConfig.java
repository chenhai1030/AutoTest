package com.funshion.autotest;

/**
 * Created by chenhai on 7/4/16.
 */
public class FunConfig {
    /**
     * Funtv Test System current module config
     */
    private static ModuleConfig mModuleConfig = null;
    public static class ModuleConfig {
        /**
         * config test item
         */
        public boolean mModuleTestWifiLink = true;
        public boolean mModuleTestUsbDevice = true;
        public boolean mModuleTestWireLink = true;
        public boolean mModuleTestSdCard = false;
        public boolean mModuleTestLocalVideo = true;
        public boolean mModuleTestStatus = true;

		/*
		 * config show info
		 */

        public boolean mModuleInfoProperty = true;
        public boolean mModuleInfoParams = true;
        public boolean mModuleInfoPromptInfo = true;
        public boolean mModuleInfoButton = true;

        public ModuleConfig() {

        }
    }
}
