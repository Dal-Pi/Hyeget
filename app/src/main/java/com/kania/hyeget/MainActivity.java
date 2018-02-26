package com.kania.hyeget;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView textInfo = (TextView) findViewById(R.id.main_text_appinfo);
        String infoPrefix = getString(R.string.app_info);
        textInfo.setText(getInfoWithVersion(infoPrefix));
    }

    private String getInfoWithVersion(String prefix) {
        String version;
        try {
            PackageInfo packageInfo =
                    this.getPackageManager().getPackageInfo(this.getPackageName(), 0);
            version = packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            version = "unknown";
        }
        return prefix + " " + version;
    }
}
