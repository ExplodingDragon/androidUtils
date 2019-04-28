package top.fksoft.test.android.dao;

import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import top.fksoft.test.android.ToastUtils;
import top.fksoft.test.android.data.SqlHelper;
import top.fksoft.test.android.nutil.ViewInterface;

public abstract class BaseFragment<T extends Activity> extends Fragment implements ViewInterface,View.OnClickListener {
    private View contentView;
    private SQLiteDatabase database;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        contentView = inflater.inflate(initLayout(),container,false);
        return contentView;
    }

    @Override
    public SQLiteDatabase getDatabase() {
        if (database == null) {
            database = SqlHelper.newInstance(getContext()).getWritableDatabase();
        }
        return database;
    }

    @Override
    public void onStart() {
        super.onStart();
        initView();
        initData();
    }
    @Nullable
    @Override
    public T getContext() {
        return (T) getActivity();
    }

    public <T extends View> T findViewById(int id) {
        T viewById = contentView.findViewById(id);
        return viewById;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (database!=null){
            database.close();
            database = null;
        }
    }
    public void showToast(int id,Object ... data){
        ToastUtils.showToast(getContext(),id,data);
    }
    public void showToast(String format,Object ... data){
        ToastUtils.showToast(getContext(),format,data);
    }
}
