package top.fksoft.test;

import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import top.fksoft.test.android.FileChooseUtils;
import top.fksoft.test.android.ToastUtils;
import top.fksoft.test.android.dao.BaseActivity;

import java.io.File;

public class MainActivity extends BaseActivity {


    private Button btnTest1;

    @Override
    public void initData() {

    }

    @Override
    public void initView() {
        btnTest1 = findViewById(R.id.btn_test_1);
        btnTest1.setOnClickListener(this::onClick);
    }

    @Override
    public int initLayout() {
        return R.layout.activity_main;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_test_1:
                new FileChooseUtils(this).showOpenDialog(new FileChooseUtils.FileChooseResult() {
                    @Override
                    public void result(File result) {
                        ToastUtils.showToast(getContext(),"%s",result);
                    }
                });
                break;
        }
    }
}
