package iitmad.com.a20425418.multinotepad;

/**
 * Created by Ashok Kumar - A20425418 on 9/22/18.
 * Illinois Institute of Technology
 * fashokkumar@hawk.iit.edu
 */

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AboutActivity extends AppCompatActivity {

    TextView tvCopyrightDesc,tvVersionDetails;
    String version;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_about);
        DateFormat dateFormat = new SimpleDateFormat(this.getString(R.string.strDateFormatOnlyYear));
        Date date = new Date();
        tvCopyrightDesc = (TextView) findViewById(R.id.tvCopyrightDesc);
        tvCopyrightDesc.setText(this.getResources().getString(R.string.strAboutAppCopyrightStatement,dateFormat.format(date)));

        try {
            PackageInfo pInfo = this.getPackageManager().getPackageInfo(getPackageName(), 0);
            version = pInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        tvVersionDetails = (TextView) findViewById(R.id.tvVersionDetails);
        tvVersionDetails.setText(this.getResources().getString(R.string.strVersionDetails,version));
    }
}
