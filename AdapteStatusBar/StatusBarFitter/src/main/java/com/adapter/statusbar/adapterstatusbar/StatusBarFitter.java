package com.adapter.statusbar.adapterstatusbar;

/**
 * Created by xuzhendong on 2018/9/21.
 */

public class StatusBarFitter {
    public static void init(){
        try {
            HookHelper.replaceInstrumentation();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
