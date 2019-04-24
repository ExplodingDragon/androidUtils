package top.fksoft.test.android.dao;

import android.app.Service;

public abstract class BaseService extends Service {
    private static boolean start = false;

    @Override
    public void onCreate() {
        super.onCreate();
        start = true;
    }

    @Override
    public void onDestroy() {
        start =false;
        super.onDestroy();
    }

    public static boolean isStart() {
        return start;
    }
}
