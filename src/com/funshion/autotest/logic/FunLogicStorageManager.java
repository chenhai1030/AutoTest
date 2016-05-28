/**
 * 
 */
/**
 * @author chenhai
 *
 */
package com.funshion.autotest.logic;

import java.lang.String;
import java.lang.reflect.Method;

import android.R.bool;
import android.content.Context;
import android.os.ServiceManager;
import android.os.storage.IMountService;
import android.os.storage.StorageManager;
import android.util.Log;

public class FunLogicStorageManager{
	private Context mContext = null;
	private Method mMethodGetPaths;
	private static final String SDCARD_PREFIX_PATH = "/mnt/sdcard";
	private static IMountService mMountService = null;
	
	private String TAG="FunLogicStorageManager";
	
	
	public FunLogicStorageManager (Context context) {
		mContext = context;
	}
	
	public int getUsbMountedNum(){
		String[] paths;
		int count = 0;
		
		paths = getVolumesPaths();
		
		for (int i = 0; i< paths.length; i++){
			if (checkDeviceMounted(paths[i])){
				count ++;
			}
		}
		/*there have a sdcard mounted*/
		if (count >= 1){
			count -=1;
		}
		return count;
	}
	
	private String[] getVolumesPaths(){
		String[] paths=null;
		StorageManager mStorageManager = (StorageManager) mContext.getSystemService(Context.STORAGE_SERVICE);
		
		try {
			mMethodGetPaths = mStorageManager.getClass().getMethod("getVolumePaths",null);
			paths = (String[]) mMethodGetPaths.invoke(mStorageManager, null);
			
			for (int i = 0; i< paths.length; i++){
				Log.d(TAG,"Storage'paths[0]:"+paths[i]);  
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		return paths;
	}
	
	private static boolean checkDeviceMounted(String path) {		
		if (null == path){
			return false;
		}
		String storageState = getStorageState(path);
		return "mounted".equalsIgnoreCase(storageState);
	}
	
	private static String getStorageState(String path) {
		if (null == path){
			return null;
		}
		if (null == mMountService){
			mMountService = IMountService.Stub.asInterface(ServiceManager
					.getService("mount"));
		}
		try {
			return mMountService.getVolumeState(path).toString();
		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;
	}
}