package top.fksoft.test.android.dao;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import top.fksoft.test.android.R;
import top.fksoft.test.android.ToastUtils;
import top.fksoft.test.android.data.SqlHelper;
import top.fksoft.test.android.nutil.ViewInterface;

import java.util.ArrayList;

public abstract class BaseActivity extends AppCompatActivity implements View.OnClickListener , ViewInterface {
    private static final int REQUEST_PERMISSION_CODE = 1;
    private SQLiteDatabase database;
    private SharedPreferences config;
    private SharedPreferences.Editor cfgEdit;
    private static final String TAG = "BaseActivity";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        database = SqlHelper.newInstance(getContext()).getWritableDatabase();
        setContentView(initLayout());
        config = getSharedPreferences("Config", MODE_PRIVATE);
        cfgEdit = config.edit();
        initView();
        initData();

    }

    public abstract void initData();

    public abstract void initView();

    public abstract int initLayout();

    @Override
    public Activity getContext() {
        return this;
    }

    public SQLiteDatabase getDatabase() {
        return database;
    }

    public SharedPreferences.Editor getCfgEdit() {
        return cfgEdit;
    }

    public SharedPreferences getConfig() {
        return config;
    }

    /**
     * 权限的授权请求
     *
     * 用于Android 5.0 以上的权限请求
     *
     * @param permissionsName 权限代表的名称
     * @param permissions 权限代表的相应值
     * @return 发送请求的状态
     */
    public boolean sendPermissions(String[] permissionsName,String[] permissions){
        if (permissions.length != permissionsName.length || permissions.length ==0)
            return false;
        ArrayList<String> permissionList = new ArrayList<>(permissions.length);
        ArrayList<String> permissionNameList = new ArrayList<>(permissions.length);
        permissionNameList.add(getString(R.string.permission_warn));
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            for (int i = 0; i < permissions.length; i++) {
                if (ActivityCompat.checkSelfPermission(getContext(), permissions[i]) != PackageManager.PERMISSION_GRANTED) {
                    permissionList.add(permissions[i]);
                    permissionNameList.add(" +" + permissionsName[i]);
                }
            }

        }
        permissionsName = permissionNameList.toArray(new String[0]);
        permissions = permissionList.toArray(new String[0]);
        String[] finalPermissions = permissions;
        AlertDialog.Builder permissionDialog = new AlertDialog.Builder(getContext());
        permissionDialog.setTitle(getString(R.string.allow_permission));
        permissionDialog.setAdapter(new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, permissionsName),null);
        permissionDialog.setPositiveButton(R.string.permission_allow, (dialog, which) -> ActivityCompat.requestPermissions(this, finalPermissions, REQUEST_PERMISSION_CODE));
        permissionDialog.setNegativeButton(R.string.permission_back, (dialog, which) -> permissionError(finalPermissions));
        if (permissionList.size()==0){
            permissionSuccessful(0);
        }else {
            permissionDialog.show();
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION_CODE) {
            ArrayList<String> arrayList = new ArrayList<>(permissions.length);
            for (int i = 0; i < permissions.length; i++) {
                if (ActivityCompat.checkSelfPermission(getContext(), permissions[i]) != PackageManager.PERMISSION_GRANTED) {
                    arrayList.add(permissions[i]);
                }
            }
            if (arrayList.size() == 0) {
                permissionSuccessful(permissions.length);
            } else {
                permissionError(arrayList.toArray(new String[0]));
            }
        }
    }

    /**
     * <p>如果权限请求成功则触发此回调</p>
     * @param i
     */
    public void permissionSuccessful(int i){
        if (i!=0){
            showToast(R.string.allow_permission_success);
        }

    }

    public void permissionError(String[] permissions){
        new AlertDialog.Builder(getContext()).setTitle("很抱歉").setMessage("此权限为软件的基础权限，如果不允许，可能导致软件崩溃，是否重新授权？")
                .setPositiveButton("跳转设置", (dialog, which) -> {
                    startActivity(getAppDetailSettingIntent());
                    System.exit(-1);
                }).setNegativeButton("关闭应用", (dialog, which) -> System.exit(-1)).setCancelable(false)
                .show();
    }

    private Intent getAppDetailSettingIntent() {
        Intent localIntent = new Intent();
        localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= 9) {
            localIntent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            localIntent.setData(Uri.fromParts("package", getPackageName(), null));
        } else if (Build.VERSION.SDK_INT <= 8) {
            localIntent.setAction(Intent.ACTION_VIEW);
            localIntent.setClassName("com.android.settings", "com.android.settings.InstalledAppDetails");
            localIntent.putExtra("com.android.settings.ApplicationPkgName", getPackageName());
        }
        return localIntent;
    }



    public void showToast(int id,Object ... data){
        ToastUtils.showToast(getContext(),id,data);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        database.close();
    }
}
