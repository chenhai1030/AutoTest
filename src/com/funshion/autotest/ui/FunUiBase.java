package com.funshion.autotest.ui;

import android.view.View;

public interface FunUiBase{
	boolean OnCreate(View view);
	
	boolean OnShow();
	
	boolean OnRefresh();
		
	boolean OnDestroy();
	
	void OnPause();
	
	void OnResume();
	
	void SetTestSuccess();
}