package com.moyck.recoder251.utils;

import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static android.os.Environment.DIRECTORY_PICTURES;

/**
 * @author Moyuk
 * @package:com.moyck.barcodemaster.utils
 * @date on 2019/11/28 13:37
 */
public class FileUtil {


    public static void save2Album(Bitmap bitmap) throws IOException {
        File appDir = new File(Environment.getExternalStoragePublicDirectory(DIRECTORY_PICTURES), System.currentTimeMillis() + ".png");
        FileOutputStream fos = new FileOutputStream(appDir);
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
        fos.flush();
        fos.close();
    }

    /**
     * 获取目录下所有文件
     *
     * @param path 指定目录路径
     * @return
     */
    public static List<File> getFilesAllFile(String path) {
        File file = new File(path);
        File[] files = file.listFiles();
        if (files == null) {
            return new ArrayList<>();
        }
        return Arrays.asList(files);
    }



}
