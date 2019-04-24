package top.fksoft.test.android.nutil;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.List;

public class File2Utils {
    /**
     * <p>对File 的 List进行排序</p>
     * @param fileList 原始数据
     */
    public static void sort(List<File> fileList){
        Collections.sort(fileList, (o1, o2) -> {
            if (o1.isDirectory() && o2.isFile()) {
                return -1;
            } else if (o1.isFile() && o2.isDirectory()) {
                return 1;
            } else {
                String name1 = o1.getName();
                String name2 = o2.getName();
                if (name1.equals(name2))
                    return 0;
                char[] char1 = name1.toCharArray();
                char[] char2 = name2.toCharArray();
                int len = char1.length > char2.length ? char2.length : char1.length;
                if (Character.isUpperCase(char1[0]) && !Character.isUpperCase(char2[0])) {
                    return -1;
                } else if (!Character.isUpperCase(char1[0]) && Character.isUpperCase(char2[0])) {
                    return 1;
                }
                for (int i = 0; i < len; i++) {
                    int c1 = getGBCode(char1[i]);
                    int c2 = getGBCode(char2[i]);
                    return c1 - c2;
                }
                return char1.length - char2.length;
            }
        });
    }
    /**
     * <p>对File 的 List进行排序</p>
     * @param fileList 原始数据
     */
    public static void sort2(List<File> fileList){
        Collections.sort(fileList, (o1, o2) -> {
            if (o1.isDirectory() && o2.isFile()) {
                return -1;
            } else if (o1.isFile() && o2.isDirectory()) {
                return 1;
            } else {
                String name1 = o1.getName();
                String name2 = o2.getName();
                if (name1.equals(name2))
                    return 0;
                char[] char1 = name1.toCharArray();
                char[] char2 = name2.toCharArray();
                int len = char1.length > char2.length ? char2.length : char1.length;
                for (int i = 0; i < len; i++) {
                    char c1 = char1[i];
                    char c2 = char2[i];
                    if (c1 != c2){
                        continue;
                    }
                    if ((Character.isLetter(c1) && Character.isLetter(c2))||(isNumber(c1) && isNumber(c2))){
                        if(Character.isLetter(c1) && Character.isLetter(c2)){
                            
                        }else {
                            return c1 - c2;
                        }
                    }else if(Character.isLetter(c1) || Character.isLetter(c2)){
                        if (Character.isLetter(c1)){
                            return -1;
                        }else {
                            return 1;
                        }
                    }else {
                        int i1 = getGBCode(c1);
                        int i2 = getGBCode(c2);
                        return i1 - i2;
                    }
                }
                return char1.length - char2.length;
            }
        });
    }

    public static boolean isNumber(char c) {
        return c <='9' && c>='0';
    }


    /**
     * <p>计算单一char 字符的GBK位置</p>
     * @param c 字符
     * @return 顺序
     */
    public static int getGBCode(char c) {
        byte[] bytes = null;
        try {
            bytes = new StringBuffer().append(c).toString().getBytes("gbk");
        } catch (UnsupportedEncodingException e) {
        }
        if (bytes.length == 1) {
            return bytes[0];
        }
        int a = bytes[0] - 0xA0 + 256;
        int b = bytes[1] - 0xA0 + 256;

        return a * 100 + b;
    }
}
