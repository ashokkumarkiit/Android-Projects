package iitmad.com.a20425418.knowyourgovernment;

import android.content.Intent;
import android.net.Uri;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import iitmad.com.a20425418.knowyourgovernment.beans.OfficesLocationBean;
import iitmad.com.a20425418.knowyourgovernment.beans.OfficialsBean;

public class PhotoDetailActivity extends AppCompatActivity {

    Intent intent;
    OfficesLocationBean officesLocationBean;
    String officesName = "";
    OfficialsBean officialsBean;
    TextView tvLocationInfo;
    TextView tvOfficeName, tvOfficialName;
    ImageView imgvOfficialImage;
    ConstraintLayout clOfficialDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_detail);

        tvLocationInfo = (TextView) findViewById(R.id.tvLocationInfo);
        clOfficialDetails = (ConstraintLayout) findViewById(R.id.clOfficialDetails);
        tvOfficeName = (TextView) findViewById(R.id.tvOfficeName);
        tvOfficialName = (TextView) findViewById(R.id.tvOfficialName);
        imgvOfficialImage = (ImageView) findViewById(R.id.imgvOfficialImage);
        intent = getIntent();

        if (intent != null) {
            officesLocationBean = (OfficesLocationBean) intent.getSerializableExtra
                    (this.getResources().getString(R.string.strIntentExtraLocationDetails));
            //officialsIndex = intent.getStringExtra(this.getResources().getString(R.string.strIntentExtraPosition));
            officesName = intent.getStringExtra(this.getResources().getString(R.string.strIntentExtraOfficesName));
            officialsBean = (OfficialsBean) intent.getSerializableExtra
                    (this.getResources().getString(R.string.strIntentExtraOfficialDetails));

        }
        if (officesLocationBean != null) {
            String tempLocationString = "";
            if(!(officesLocationBean.getCity().equals("")))
                tempLocationString = officesLocationBean.getCity() + "," +
                        officesLocationBean.getState() + " " + officesLocationBean.getZip();
            else
                tempLocationString = officesLocationBean.getState() + " " + officesLocationBean.getZip();
            tvLocationInfo.setText(tempLocationString);
            /*tvLocationInfo.setText(officesLocationBean.getCity() + ","
                    + officesLocationBean.getState() + " "
                    + officesLocationBean.getZip());*/
        }

        if (!(officesName.trim().equals(""))) {
            tvOfficeName.setText(officesName);
        }
        if (officialsBean != null) {
            tvOfficialName.setText(officialsBean.getOfficialName());
        }

        //Downloading Image Using Picasso
        if (officialsBean.getOfficialPhotoUrl() != null &&
                !(officialsBean.getOfficialPhotoUrl().toString().equals(""))) {
            Picasso picasso = new Picasso.Builder(this).listener(new Picasso.Listener() {
                @Override
                public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {
                    // Here we try https if the http image attempt failed
                    final String changedUrl = officialsBean.getOfficialPhotoUrl().toString().replace("http:", "https:");
                    picasso.load(changedUrl).error(R.drawable.brokenimage)
                            .placeholder(R.drawable.placeholder)
                            .into(imgvOfficialImage);
                }
            }).build();
            picasso.load(officialsBean.getOfficialPhotoUrl()).error(R.drawable.brokenimage)
                    .placeholder(R.drawable.placeholder)
                    .into(imgvOfficialImage);
        } else {
            Picasso.get().load(officialsBean.getOfficialPhotoUrl())
                    .error(R.drawable.brokenimage)
                    .placeholder(R.drawable.missingimage)
                    .into(imgvOfficialImage);
        }

        //Republican = RED, Democratic = BLUE, Otherwise use BLACK
        //Setting Background Color depending on the type of Party
        if (officialsBean.getOfficialParty() != null && (!(officialsBean.getOfficialParty().trim().equals("")))) {
            if (officialsBean.getOfficialParty().toUpperCase().equals("REPUBLICAN"))
                clOfficialDetails.setBackgroundColor(getColor(R.color.colorOfficialBackgroundRed));
            else if (officialsBean.getOfficialParty().toUpperCase().equals("DEMOCRATIC") ||
                    officialsBean.getOfficialParty().toUpperCase().equals("DEMOCRAT"))
                clOfficialDetails.setBackgroundColor(getColor(R.color.colorOfficialBackgroundBlue));
            else
                clOfficialDetails.setBackgroundColor(getColor(R.color.colorOfficialBackgroundBlack));
        } else {
            clOfficialDetails.setBackgroundColor(getColor(R.color.colorOfficialBackgroundBlack));
        }

    }
}
