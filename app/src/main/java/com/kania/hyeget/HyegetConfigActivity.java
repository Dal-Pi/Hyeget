package com.kania.hyeget;

import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.RemoteViews;

public class HyegetConfigActivity extends AppCompatActivity {

    public static final int REQ_CODE_GALLERY = 1000;
    public static final String REQ_TYPE_IMAGE = "image/*";

    private int mAppWidgetId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            mAppWidgetId = extras.getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);
        }
        if (savedInstanceState == null)
            getImageFromGallery();
    }

    private void getImageFromGallery() {
        Intent intent = new Intent();
        //intent.setAction(Intent.ACTION_GET_CONTENT); it only launched google documentation
        intent.setAction(Intent.ACTION_PICK);

        /*
        if(Build.VERSION.SDK_INT >= 19) {
            intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
        }else{
            intent = new Intent(Intent.ACTION_GET_CONTENT);
        }
        intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        */
        intent.setType(REQ_TYPE_IMAGE);
        Intent.createChooser(intent, "select image");
        startActivityForResult(intent, REQ_CODE_GALLERY);
    }

    /*
    private void getPersistablePermission(Intent data) {
        if(Build.VERSION.SDK_INT >= 19) {
            final int takeFlags = data.getFlags() & Intent.FLAG_GRANT_READ_URI_PERMISSION;
            ContentResolver resolver = this.getContentResolver();
            resolver.takePersistableUriPermission(data.getData(), takeFlags);
        }
    }
    */

    private void setImageToWidget(Uri imgUri) {
        Log.d("hyeget_log", "setImageToWidget with " + imgUri.toString());

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        RemoteViews views = new RemoteViews(this.getPackageName(), R.layout.hyeget_provider);
        //Bitmap bitmap = getResizedBitmapIfNeed(imgUri);
        Bitmap bitmap = ImageUtils.getResizedBitmapIfNeed(this, imgUri);
        views.setImageViewBitmap(R.id.widget_image_target, bitmap);
        appWidgetManager.updateAppWidget(mAppWidgetId, views);

        //TODO separate to func
        //TODO save to file and key
        /*
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        String keyWithId = HyegetConstants.KEY_IMAGE_URI_PREFIX + mAppWidgetId;
        editor.putString(keyWithId, imgUri.toString());
        editor.apply();
        Log.d("hyeget_log", "saved uri to preference. ("
                + keyWithId + ", " + imgUri.toString() + ")");
        */

        //TODO use runtime permission
        //TODO save normally *****
        //ImageUtils.saveBitmapToFile(this, bitmap, keyWithId);
    }
/*
    private Bitmap getResizedBitmapIfNeed(Uri uri) {
        Bitmap resizedBitmap = null;
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
            int bitmapWidth = bitmap.getWidth();
            int bitmapHeight = bitmap.getHeight();
            DisplayMetrics metrics = new DisplayMetrics();
            Display display = this.getWindowManager().getDefaultDisplay();
            if (Build.VERSION.SDK_INT >= 17)
                display.getRealMetrics(metrics);
            int layoutWidth = metrics.widthPixels;
            int layoutHeight = metrics.heightPixels;

            if (layoutWidth >= bitmapWidth && layoutHeight >= bitmapHeight) {
                return bitmap;
            } else {
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
            }
        } catch (FileNotFoundException e) {
            Toast.makeText(this, "Cannot find file : " + uri.getPath(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        } catch (IOException e) {
            Toast.makeText(this, "Occured an error to load image", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

        return resizedBitmap;
    }
*/


    private void finishConfigActivity() {
        Intent resultValue = new Intent();
        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
        setResult(RESULT_OK, resultValue);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode)
        {
            case REQ_CODE_GALLERY:
                if (data != null) {
                    //getPersistablePermission(data);
                    setImageToWidget(data.getData());
                    finishConfigActivity();
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
