package com.adapter.statusbar.adapterstatusbar;

import android.app.Activity;
import android.app.Instrumentation;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by xuzhendong on 2018/9/7.
 * hook ActivityThread 中的Instrumentation对象，劫持activity生命周期
 */
public  class MyInstrumentationProxy extends Instrumentation {
    Instrumentation mBase;
    public MyInstrumentationProxy(Instrumentation base) {
        Log.v("verf","MyInstrumentationProxy " + base.getClass().getSimpleName());
        mBase = base;
    }


    /**
     * callActivityOnCreate
     * @param activity
     * @param icicle
     */
    public void callActivityOnCreate(Activity activity, Bundle icicle) {
        try {
            if(activity.getWindow() != null) {
                Method callActivityOnCreate = Instrumentation.class.getDeclaredMethod("callActivityOnCreate", Activity.class, Bundle.class);
                callActivityOnCreate.setAccessible(true);
                callActivityOnCreate.invoke(mBase, activity, icicle);
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    /**
     * 保存当前正在显示的activity
     * @param activity
     */
    public void callActivityOnResume(Activity activity) {
        Log.v("verf","Recenser啦啦  callActivityOnResume");
        try {
            //记录当前正在显示的activity名称
            if(activity.getWindow() != null) {
                Method callActivityOnResume = Instrumentation.class.getDeclaredMethod("callActivityOnResume", Activity.class);
                callActivityOnResume.setAccessible(true);
                callActivityOnResume.invoke(mBase, activity);
            }
            if(activity.getWindow() != null && activity.getWindow().getDecorView() != null){
                Bitmap bitmap = screenShot(activity);
                if(bitmap != null) {
                    if (null != bitmap) {
                        /**
                         * 均匀取20个点的颜色作均值
                         */
                        int pixel = 0;
                        float red = 0;
                        float green = 0;
                        float blue = 0;
                        for(int i = 0; i < 20; i ++){
                            int color = bitmap.getPixel(0 + 7 * i ,0 + 2 * i);
                            red += Color.red(color);
                            green += Color.green(color);
                            blue += Color.blue(color);
                            pixel += color;
                        }
                        red = red/20;
                        green = green/20;
                        blue = blue/20;
                        pixel = pixel/20;
                        int light = 0;
                        if(red > 240){
                            light++;
                        }
                        if(green > 240){
                            light++;
                        }
                        if(blue > 240){
                            light++;
                        }
                        if (light >= 2) {
                            activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                        }else {
                            activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
                        }
                        StatusBarUtil.setStatusBarColor(activity, Color.parseColor("#" + Integer.toHexString(pixel).toUpperCase()));
                        bitmap.recycle();
                    }
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    public Bitmap screenShot(Activity activity) {
        View view = activity.getWindow().getDecorView();
        view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        view.buildDrawingCache();
        Bitmap bitmap = view.getDrawingCache();
        return  bitmap;
    }

}

