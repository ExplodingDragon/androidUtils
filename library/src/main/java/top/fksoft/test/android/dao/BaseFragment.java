package top.fksoft.test.android.dao;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import top.fksoft.test.android.nutil.ViewInterface;

public abstract class BaseFragment<T extends Activity> extends Fragment implements ViewInterface,View.OnClickListener {
    private View contentView;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        contentView = LayoutInflater.from(getContext()).inflate(initLayout(),container,false);
        return contentView;
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

    public void showToast(String text){
        Toast.makeText(getContext(),text,Toast.LENGTH_SHORT).show();
    }
}
