package com.adapter.statusbar;

import com.adapter.statusbar.adapterstatusbar.StatusBarFitter;

public class Application extends android.app.Application {

    @Override
    public void onCreate() {
        super.onCreate();
        StatusBarFitter.init();
    }
}
