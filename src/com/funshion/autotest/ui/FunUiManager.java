package com.funshion.autotest.ui;

import android.util.Log;
import android.view.View;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * When you want add a ui module, pls follow the steps: 1. call function
 * getInstanceto to get the instance 2. call function addUiModule to add ui module
 * to list, this will be called only once 3. call function onCreateUiModuleByName to create ui
 * module, called only once 4. call function onShowAllUiModule to show all ui
 * module 5. call function onRefreshAllUiModule to refresh ui module display
 * 
 * @author chenhai
 * 
 */
public class FunUiManager {
	private static Map<String, FunUiBase> mUiList = new HashMap<String, FunUiBase>();
	private static FunUiManager mFunUiManager = null;
	private static final String TAG = "FunUiManager";
	
	private FunUiManager(){
		mUiList.clear();
	}
	
	public synchronized static FunUiManager getInstance() {
		if (null == mFunUiManager) {
			mFunUiManager = new FunUiManager();
		}
		return mFunUiManager;
	}
	
	public boolean addUiModule(String key, FunUiBase FunUiBase) {
		Log.i(TAG, "addUiModule key = " + key);
		mUiList.put(key, FunUiBase);
		return true;
	}
	
	public FunUiBase getLogicModuleByModule(String name) {
		if (null == name) {
			return null;
		}
		FunUiBase _FunUiBase = mUiList.get(name);
		if (null == _FunUiBase) {
			return null;
		}
		return _FunUiBase;
	}
	
	/**
	 * must call addUiModule first before use onCreateUiModuleByName
	 * 
	 * @param name
	 * @param view
	 * @return
	 */
	public Boolean onCreateUiModuleByName(String name, View view) {
		if (mUiList.size() == 0){
			return false;
		}
		
		FunUiBase _FunUiBase = mUiList.get(name);
		if (_FunUiBase == null){
			return false;
		}
		
		return _FunUiBase.OnCreate(view);
	}
	
	public Boolean onShowAllUiModule() {
		if (mUiList.size() == 0){
			return false;
		}
		
		Iterator uiItem = mUiList.entrySet().iterator();
		while (uiItem.hasNext()) {
			@SuppressWarnings("rawtypes")
			Map.Entry mapEntry = (Map.Entry) uiItem.next();
			FunUiBase _FunUiBase = (FunUiBase) mapEntry.getValue();
			String name = (String) mapEntry.getKey();

			if (_FunUiBase.OnShow()) {
				Log.e(TAG, "onShow [" + name + "] failure !!");
			}
		}
		return true;
	}
	
	public boolean onRefreshAllUiModule() {
		int listSize = 0;

		listSize = mUiList.size();
		if (listSize == 0) {
			return false;
		}

		@SuppressWarnings("rawtypes")
		Iterator uiItem = mUiList.entrySet().iterator();
		while (uiItem.hasNext()) {
			@SuppressWarnings("rawtypes")
			Map.Entry mapEntry = (Map.Entry) uiItem.next();
			FunUiBase _FunUiBase = (FunUiBase) mapEntry.getValue();
			String name = (String) mapEntry.getKey();

			if (!_FunUiBase.OnRefresh()) {
				Log.e(TAG, "onRefresh [" + name + "] failure !!");
			}
		}
		return true;
	}

	public boolean onRefreshModuleByName(String name) {
		FunUiBase _FunUiBase = mUiList.get(name);
		if (_FunUiBase == null) {
			return false;
		}

		if (!_FunUiBase.OnRefresh()) {
			Log.e(TAG,"onRefresh [" + name + "] failure !!");
		}
		return true;
	}


	public void onResumeModuleByName(String name){
		if(null == name){
			return;
		}
		FunUiBase _FunUiBase = mUiList.get(name);
		if(_FunUiBase == null){
			return;
		}
		_FunUiBase.OnResume();
	}

	public void	onPauseModuleByName(String name){
		if (null == name){
			return;
		}
		FunUiBase _FunUiBase = mUiList.get(name);
		if(null == _FunUiBase){
			return;
		}
		_FunUiBase.OnPause();
	}
}
