package com.adapter.statusbar.adapterstatusbar;

import android.app.Instrumentation;
import android.util.Log;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Created by xuzhendong on 2018/9/21.
 */

public class HookHelper {  /**
 * hook 替换系统中的ActivityThread类中的Instrumentation对象
 * @throws Exception
 */
public static void replaceInstrumentation() throws Exception {
    Log.v("verf","statusbatr  replaceInstrumentation");
    // 先获取到当前的ActivityThread对象
    Class<?> activityThreadClass = Class.forName("android.app.ActivityThread");
    Method currentActivityThreadMethod = activityThreadClass.getDeclaredMethod("currentActivityThread");
    currentActivityThreadMethod.setAccessible(true);
    //currentActivityThread是一个static函数所以可以直接invoke，不需要带实例参数
    Object currentActivityThread = currentActivityThreadMethod.invoke(null);

    // 拿到原始的 mInstrumentation字段
    Field mInstrumentationField = activityThreadClass.getDeclaredField("mInstrumentation");
    mInstrumentationField.setAccessible(true);
    Instrumentation mInstrumentation = (Instrumentation) mInstrumentationField.get(currentActivityThread);
//         mInstrumentation.send
    // 创建代理对象
    Instrumentation evilInstrumentation = new MyInstrumentationProxy(mInstrumentation);
    // 偷梁换柱
    mInstrumentationField.set(currentActivityThread, evilInstrumentation);
    //-------------------
    Instrumentation mInstrumentation1 = (Instrumentation) mInstrumentationField.get(currentActivityThread);
    Log.v("verf","statusbatr replaceInstrumentation " + mInstrumentation1.getClass().getSimpleName());
//
}

}
