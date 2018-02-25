package com.kania.hyeget;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Seonghan Kim on 2018-02-26.
 */

public class ImageUtils {
    public static Bitmap getResizedBitmapIfNeed(Context context, Uri uri) {
        Bitmap resizedBitmap = null;
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), uri);
            int bitmapWidth = bitmap.getWidth();
            int bitmapHeight = bitmap.getHeight();
            DisplayMetrics metrics = new DisplayMetrics();

            WindowManager windowManager =
                    (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            Display display = windowManager.getDefaultDisplay();
            if (Build.VERSION.SDK_INT >= 17)
                display.getRealMetrics(metrics);
            int layoutWidth = metrics.widthPixels;
            int layoutHeight = metrics.heightPixels;

            //commented will needs original scale
            //TODO arrange it
//            if (layoutWidth >= bitmapWidth && layoutHeight >= bitmapHeight) {
//                return bitmap;
//            } else {
                float bitmapRatio = (float) bitmapWidth / (float) bitmapHeight;
                Log.d("hyeget_log", "bitmapRatio = " + bitmapRatio + " / bitmapScale = " + bitmapWidth + ", " + bitmapHeight);
                float layoutRatio = (float) layoutWidth / (float) layoutHeight;
                Log.d("hyeget_log", "layoutRatio = " + layoutRatio + " / layoutScale = " + layoutWidth + ", " + layoutHeight);
                if (layoutRatio < bitmapRatio) {
                    Log.d("hyeget_log", "scaled case  layoutRatio < bitmapRatio / scale = " + layoutWidth + ", " + (int) (layoutWidth / bitmapRatio));
                    resizedBitmap = Bitmap.createScaledBitmap(bitmap, layoutWidth, (int) (layoutWidth / bitmapRatio), true);
                } else if (layoutRatio == bitmapRatio) {
                    Log.d("hyeget_log", "scaled case  layoutRatio == bitmapRatio / scale = " + layoutWidth + ", " + layoutHeight);
                    resizedBitmap = Bitmap.createScaledBitmap(bitmap, layoutWidth, layoutHeight, true);
                } else {
                    Log.d("hyeget_log", "scaled case  layoutRatio > bitmapRatio / scale = " + (int) (layoutHeight * bitmapRatio) + ", " + layoutHeight);
                    resizedBitmap = Bitmap.createScaledBitmap(bitmap, (int) (layoutHeight * bitmapRatio), layoutHeight, true);
                }
//            }
        } catch (FileNotFoundException e) {
            Toast.makeText(context, "Cannot find file : " + uri.getPath(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        } catch (IOException e) {
            Toast.makeText(context, "Occured an error to load image", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

        return resizedBitmap;
    }

    public static void saveBitmapToFile(Context context, Bitmap bitmap, String filename) {
        String projectPath = Environment.getExternalStorageDirectory().getAbsolutePath();
        File dir = new File(projectPath + "/" + HyegetConstants.PATH_FOLDER_NAME);
        if (!dir.exists()) {
            dir.mkdir();
        }
        try {
            File filepath = new File(projectPath + "/" + HyegetConstants.PATH_FOLDER_NAME + "/"
                    + filename);
            FileOutputStream fos = new FileOutputStream(filepath);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            context.sendBroadcast(
                    new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(filepath)));
            //TODO remove
            Toast.makeText(context, "save to file : " + projectPath + "/"
                    + HyegetConstants.PATH_FOLDER_NAME + "/" + filename, Toast.LENGTH_LONG).show();
        } catch (FileNotFoundException e) {
            Toast.makeText(context, "error occured on save wallpaper to file!",
                    Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
}
