package top.fksoft.test.android;

import android.app.Dialog;
import android.content.Context;
import android.os.Environment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FileChooseUtils {
    private static final String TAG = "FileChooseUtils";

    final Context context;
    private String title; //标题
    private Dialog dialog = null;// dialog 对象
    private File initDir = Environment.getExternalStorageDirectory();
    private boolean chooseFiles = false;
    private List<File> resultArray = new ArrayList<>();

    //-------------View findViewById()
    private Toolbar toolbar;
    private View view;
    private ListFileChooseResult callBack = null;

    //-------------View findViewById() END

    public FileChooseUtils(Context context) {
        this.context = context;
        this.title = context.getString(R.string.choose_file);
    }

    public void showOpenDialog(FileChooseResult result) {
        chooseFiles = false;
        this.callBack = resultList -> {
            if (resultList!=null && resultList.size() > 0){
                if (result!= null){
                    result.result(resultList.get(0));
                }else {
                    result.result(null);
                }
            }else {
                result.result(null);
            }
        };

        showDialog();
    }
    public void showOpenListDialog(ListFileChooseResult result) {
        chooseFiles = true;
        this.callBack = result;
        showDialog();
    }

    public String getTitle() {
        return title;
    }

    public FileChooseUtils setTitle(String title) {
        this.title = (title == null && "".equals(title.trim())) ? context.getString(R.string.choose_file) : title;
        return this;
    }



    private void showDialog() {
        if (dialog!=null) {
            Log.w(TAG, "showDialog: 在Dialog 未关闭时调用了开启方法.");
            return;
        }
        resultArray.clear();
        dialog = new Dialog(context, R.style.FileChooseDialogStyle);
        view = View.inflate(context, R.layout.file_choose_dialog, null);
        dialog.setContentView(view);
        dialog.setOnDismissListener(dialog -> {
            this.dialog = null;
            Log.i(TAG, "showDialog: Dialog已经销毁！");
            if (callBack!=null) {
                callBack.result(resultArray);
            }
        });
        dialog.show();
        Log.i(TAG, "showDialog: Dialog已经打开");
        toolbar = view.findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(v -> dialog.dismiss());
        toolbar.setTitle(title);
    }
    public interface FileChooseResult {
        void result(File result);
    }
    public interface ListFileChooseResult  {
        void result(List<File> result);
    }

}
