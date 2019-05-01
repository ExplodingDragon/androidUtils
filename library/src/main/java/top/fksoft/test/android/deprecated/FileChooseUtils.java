package top.fksoft.test.android.deprecated;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import top.fksoft.test.android.R;
import top.fksoft.test.android.nutil.File2Utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class FileChooseUtils {
    final private File defaultDir;
    final private Context context;
    final private ArrayList<String> extensions;
    private File selectFile;
    private File chooseDir;
    private ArrayList<File> filesList = new ArrayList<>();
    private AlertDialog.Builder alert;
    private ArrayAdapter<File> array;
    private TextView path;
    private Button back;
    private ListView fileList;
    private TextView toast;
    private EditText selectName;
    private LinearLayout defaultItem;
    /**
     * <p>初始化选择</p>
     *
     * @param defaultDir 默认目录
     * @param context    上下文
     * @throws IOException 如果默认目录不是文件夹
     */
    public FileChooseUtils(File defaultDir, Context context) throws IOException {
        if (!defaultDir.isDirectory()) throw new IOException("这不是文件夹");
        this.defaultDir = defaultDir;
        this.context = context;
        extensions = new ArrayList<>();
        array = new ArrayAdapter<File>(context, R.layout.deprecated_choose_item, filesList) {
            private void initView(View convertView) {
                fileImage = convertView.findViewById(R.id.file_image);
                fileText = convertView.findViewById(R.id.file_text);
            }

            private TextView fileText;
            private ImageView fileImage;

            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.deprecated_choose_item, parent, false);
                initView(convertView);
                File file = filesList.get(position);
                if (file.isDirectory()) {
                    fileImage.setImageResource(R.mipmap.filechoose_directory);
                } else {
                    fileImage.setImageResource(R.mipmap.filechoose_file);
                }
                fileText.setText(file.getName());
                return convertView;
            }
        };
    }

    /**
     * <p>添加允许的文件后缀</p>
     *
     * @param extension 文件后缀 例如：[.mp3]
     */
    public FileChooseUtils addExtension(String extension) {
        if (!extension.startsWith(".")) {
            extension = "." + extension;
        }
        extensions.add(extension.toLowerCase());
        return this;
    }

    /**
     * <p>清除所有的文件后缀</p>
     */
    public FileChooseUtils clearExtensions() {
        extensions.clear();
        return this;
    }


    /**
     * <p>弹出文件选择框</p>
     *
     * @param title    选择框标题
     * @param callback 选择回调事件
     */
    public void showFileChooseDialog(final String title, final FileChooseCallback callback) {
        alert = new AlertDialog.Builder(context);
        alert.setTitle(title);
        View view = LayoutInflater.from(context).inflate(R.layout.deprecated_choose_dialog, null, false);
        alert.setView(view);
        alert.setPositiveButton("确定", (dialog, which) -> {
            if (selectFile == null) {
                callback.callback(false, null);
            }else {
                callback.callback(true,selectFile);
            }
        });
        alert.setNegativeButton("取消", (dialog, which) -> callback.callback(false, null));
        initDialogView(view);
        fileList.setAdapter(array);
        alert.setCancelable(false);
        alert.show();
        showFileList(defaultDir);
        this.fileList.setOnItemClickListener((parent, view1, position, id) -> {
            File choose = filesList.get(position);
            if (choose.isDirectory()){
                showFileList(choose);
            }else {
                updateChoose(choose);
            }
        });
        back.setOnClickListener(v -> {
            if (chooseDir.canRead()){
                String absolutePath = chooseDir.getAbsolutePath();
                if (absolutePath.equals("/"))
                    return;
                showFileList(new File(absolutePath.substring(0, absolutePath.lastIndexOf("/"))));
            }else {
                showFileList(defaultDir);
            }
        });


    }

    private void updateChoose(File choose) {
        selectFile = choose;
        selectName.setEnabled(true);
        selectName.setText(choose.getName());
        selectName.setEnabled(false);
    }

    private void showFileList(File dir) {
        filesList.clear();
        chooseDir = dir;
        path.setText(chooseDir.getAbsolutePath());
        if (!dir.canRead()) {
            showReadOnly();
        }else {
            File[] files = dir.listFiles(pathname -> {
                if (pathname.getName().startsWith("."))
                    return false;
                if (pathname.isDirectory())
                    return true;
                if (extensions.size() != 0) {
                    for (String extension : extensions) {
                        if (pathname.getName().toLowerCase().endsWith(extension)) {
                            return true;
                        }
                    }
                    return false;
                }
                return true;
            });
            if (files == null || files.length == 0){
                showNotFound();
            }else {
                filesList.addAll(Arrays.asList(files));
                File2Utils.sort(filesList);
                showNone();
            }
        }
        array.notifyDataSetChanged();
    }

    private void showNone() {
        back.setText("返回上级");
        toast.setVisibility(View.GONE);
        defaultItem.setVisibility(View.GONE);
    }

    private void showNotFound() {
        toast.setText("未发现符合条件的文件");
        toast.setVisibility(View.VISIBLE);
        defaultItem.setVisibility(View.VISIBLE);
    }
    private void showReadOnly() {
        toast.setText("文件夹不可读");
        back.setText("返回默认目录");
        toast.setVisibility(View.VISIBLE);
        defaultItem.setVisibility(View.VISIBLE);
    }




    private void initDialogView(View view) {
        path = view.findViewById(R.id.path);
        back = view.findViewById(R.id.back);
        fileList = view.findViewById(R.id.fileList);
        toast = view.findViewById(R.id.toast);
        selectName = view.findViewById(R.id.selectName);
        defaultItem = view.findViewById(R.id.default_item);
    }


    public interface FileChooseCallback {
        void callback(boolean isChecked, File check);
    }
}
