/**
 * 
 */
/**
 * @author chenhai
 *
 */
package com.funshion.autotest.logic;

import android.content.Context;
import android.os.storage.StorageManager;
import android.util.Log;

import java.lang.reflect.Method;

public class FunLogicStorageManager {
	private Context mContext = null;
	private Method mMethodGetPaths;
	private Method mMethdGetVolumeState;
	StorageManager mStorageManager;

	private String TAG = "FunLogicStorageManager";


	public FunLogicStorageManager(Context context) {
		mContext = context;
		mStorageManager = (StorageManager) mContext.getSystemService(Context.STORAGE_SERVICE);
	}

	public int getUsbMountedNum() {
		String[] paths;
		int count = 0;

		paths = getVolumesPaths();

		for (int i = 0; i < paths.length; i++) {
			if (checkDeviceMounted(paths[i])) {
				count++;
			}
		}
		Log.d(TAG, "Storage'count:" + count);
		/*there have a sdcard mounted*/
		if (count >= 1) {
			count -= 1;
		}

		return count;
	}

	private String[] getVolumesPaths() {
		String[] paths = null;
		try {
			mMethodGetPaths = mStorageManager.getClass().getMethod("getVolumePaths", null);
			paths = (String[]) mMethodGetPaths.invoke(mStorageManager, null);

			for (int i = 0; i < paths.length; i++) {
				Log.d(TAG, "Storage'paths[0]:" + paths[i]);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}

		return paths;
	}

	private boolean checkDeviceMounted(String path) {
		if (null == path) {
			return false;
		}
		String storageState = getStorageState(path);
		Log.d("FunLogicStorageManager", "Storage'storageState:" + storageState + " path:" + path);
		return "mounted".equalsIgnoreCase(storageState);
	}

	private String getStorageState(String path) {
		if (null == path) {
			return null;
		}
		String states = null;
		try {
			mMethdGetVolumeState = mStorageManager.getClass().getMethod("getVolumeState", String.class);
			states = (String) mMethdGetVolumeState.invoke(mStorageManager, path);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return states;
	}
}