package iitmad.com.a20425418.knowyourgovernment;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Paint;
import android.media.Image;
import android.net.Uri;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import iitmad.com.a20425418.knowyourgovernment.beans.OfficesLocationBean;
import iitmad.com.a20425418.knowyourgovernment.beans.OfficialAddressBean;
import iitmad.com.a20425418.knowyourgovernment.beans.OfficialsBean;

public class OfficialActivity extends AppCompatActivity implements View.OnClickListener {

    Intent intent;
    TextView tvLocationInfo;
    OfficesLocationBean officesLocationBean;
    ConstraintLayout clOfficialDetails;
    TextView tvOfficeName, tvOfficialName, tvOfficialParty;
    ImageView imgvOfficialImage;
    TextView tvAddress, tvPhone, tvEmail, tvWebsite;
    //String officialsIndex = "";
    String officesName = "";
    OfficialsBean officialsBean;
    LinearLayout llYoutube, llGooglePlus, llTwitter, llFacebook;
    String strYoutube = "", strGooglePlus = "", strTwitter = "", strFacebook = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_official);

        tvLocationInfo = (TextView) findViewById(R.id.tvLocationInfo);
        clOfficialDetails = (ConstraintLayout) findViewById(R.id.clOfficialDetails);
        tvOfficeName = (TextView) findViewById(R.id.tvOfficeName);
        tvOfficialName = (TextView) findViewById(R.id.tvOfficialName);
        tvOfficialParty = (TextView) findViewById(R.id.tvOfficialParty);
        imgvOfficialImage = (ImageView) findViewById(R.id.imgvOfficialImage);
        imgvOfficialImage.setOnClickListener(this);
        tvAddress = (TextView) findViewById(R.id.tvAddress);
        tvAddress.setOnClickListener(this);
        tvPhone = (TextView) findViewById(R.id.tvPhone);
        tvPhone.setOnClickListener(this);
        tvEmail = (TextView) findViewById(R.id.tvEmail);
        tvEmail.setOnClickListener(this);
        tvWebsite = (TextView) findViewById(R.id.tvWebsite);
        tvWebsite.setOnClickListener(this);
        llYoutube = (LinearLayout) findViewById(R.id.llYoutube);
        llYoutube.setVisibility(View.INVISIBLE);
        llYoutube.setOnClickListener(this);
        llGooglePlus = (LinearLayout) findViewById(R.id.llGooglePlus);
        llGooglePlus.setVisibility(View.INVISIBLE);
        llGooglePlus.setOnClickListener(this);
        llTwitter = (LinearLayout) findViewById(R.id.llTwitter);
        llTwitter.setVisibility(View.INVISIBLE);
        llTwitter.setOnClickListener(this);
        llFacebook = (LinearLayout) findViewById(R.id.llFacebook);
        llFacebook.setVisibility(View.INVISIBLE);
        llFacebook.setOnClickListener(this);


        try {

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
                if (!(officesLocationBean.getCity().equals("")))
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
                if (officialsBean.getOfficialParty().toUpperCase().equals("UNKNOWN")) {
                    tvOfficialParty.setVisibility(View.GONE);
                } else {
                    tvOfficialParty.setVisibility(View.VISIBLE);
                    tvOfficialParty.setText("(" + officialsBean.getOfficialParty() + ")");
                }
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
                        //imgvOfficialImage.setClickable(false);
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
                imgvOfficialImage.setClickable(false);
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

            //Binding Address to textView
            if (officialsBean.getOfficialAddress() != null) {
                OfficialAddressBean addressBean = new OfficialAddressBean();
                addressBean = officialsBean.getOfficialAddress();
                String address = "";
                if (addressBean.getAddrLine1() != null) {
                    address += addressBean.getAddrLine1() + " ";
                }
                if (addressBean.getAddrLine2() != null) {
                    address += addressBean.getAddrLine2() + " ";
                }
                if (addressBean.getAddrLine3() != null) {
                    address += addressBean.getAddrLine3() + " ";
                }
                if (addressBean.getAddrCity() != null) {
                    address += "\n";
                    address += addressBean.getAddrCity() + ", ";
                }
                if (addressBean.getAddrState() != null) {
                    address += addressBean.getAddrState();
                }
                if (addressBean.getAddrZip() != null) {
                    address += " " + addressBean.getAddrZip();
                }
                if (address.trim().length() > 0) {
                    tvAddress.setClickable(true);
                    tvAddress.setText(address);
                    tvAddress.setPaintFlags(tvAddress.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                } else {
                    tvAddress.setText(this.getResources().getString(R.string.strNoDataProvided));
                    tvAddress.setClickable(false);
                }
            } else {
                tvAddress.setText(this.getResources().getString(R.string.strNoDataProvided));
                tvAddress.setClickable(false);
            }

            //Binding Phone No
            if (officialsBean.getOfficialPhones() != null) {
                String phones = "";
                if (officialsBean.getOfficialPhones().length > 0) {
                    /*for (int i = 0; i < officialsBean.getOfficialPhones().length; i++) {
                        if (officialsBean.getOfficialPhones().length > 1)
                            phones += officialsBean.getOfficialPhones()[i] + ",";
                        else
                            phones += officialsBean.getOfficialPhones()[i];
                    }*/
                    phones += officialsBean.getOfficialPhones()[0];
                    tvPhone.setClickable(true);
                    tvPhone.setText(phones);
                    tvPhone.setPaintFlags(tvPhone.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                } else {
                    tvPhone.setText(this.getResources().getString(R.string.strNoDataProvided));
                    tvPhone.setClickable(false);
                }

            } else {
                tvPhone.setText(this.getResources().getString(R.string.strNoDataProvided));
                tvPhone.setClickable(false);
            }

            //Binding Emails
            if (officialsBean.getOfficialEmails() != null) {
                String emails = "";
                if (officialsBean.getOfficialEmails().length > 0) {
                    for (int i = 0; i < officialsBean.getOfficialEmails().length; i++) {
                        if (officialsBean.getOfficialEmails().length > 1)
                            emails += officialsBean.getOfficialEmails()[i] + ",";
                        else
                            emails += officialsBean.getOfficialEmails()[i];
                    }
                    tvEmail.setClickable(true);
                    tvEmail.setText(emails);
                    tvEmail.setPaintFlags(tvEmail.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                } else {
                    tvEmail.setClickable(false);
                    tvEmail.setText(this.getResources().getString(R.string.strNoDataProvided));

                }
            } else {
                tvEmail.setText(this.getResources().getString(R.string.strNoDataProvided));
                tvEmail.setClickable(false);
            }


            //Binding Websites
            if (officialsBean.getOfficialUrls() != null) {
                String urls = "";
                if (officialsBean.getOfficialUrls().length > 0) {
                    /*for (int i = 0; i < officialsBean.getOfficialUrls().length; i++) {
                        if (officialsBean.getOfficialUrls().length > 1)
                            urls += officialsBean.getOfficialUrls()[i] + ",";
                        else
                            urls += officialsBean.getOfficialUrls()[i];
                    }*/
                    urls += officialsBean.getOfficialUrls()[0];
                    tvWebsite.setText(urls);
                    tvWebsite.setClickable(true);
                    tvWebsite.setPaintFlags(tvWebsite.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                } else {
                    tvWebsite.setText(this.getResources().getString(R.string.strNoDataProvided));
                    tvWebsite.setClickable(false);
                }
            } else {
                tvWebsite.setText(this.getResources().getString(R.string.strNoDataProvided));
                tvWebsite.setClickable(false);
            }

            //Binding Channels with the URL
            if (officialsBean.getOfficialChannels() != null) {
                if (officialsBean.getOfficialChannels().size() > 0) {
                    for (int i = 0; i < officialsBean.getOfficialChannels().size(); i++) {
                        if (officialsBean.getOfficialChannels().get(i).getChannelType().toUpperCase().equals("GOOGLEPLUS")) {
                            strGooglePlus = officialsBean.getOfficialChannels().get(i).getChannelId();
                            llGooglePlus.setVisibility(View.VISIBLE);
                        }
                        if (officialsBean.getOfficialChannels().get(i).getChannelType().toUpperCase().equals("FACEBOOK")) {
                            strFacebook = officialsBean.getOfficialChannels().get(i).getChannelId();
                            llFacebook.setVisibility(View.VISIBLE);
                        }
                        if (officialsBean.getOfficialChannels().get(i).getChannelType().toUpperCase().equals("TWITTER")) {
                            strTwitter = officialsBean.getOfficialChannels().get(i).getChannelId();
                            llTwitter.setVisibility(View.VISIBLE);
                        }
                        if (officialsBean.getOfficialChannels().get(i).getChannelType().toUpperCase().equals("YOUTUBE")) {
                            strYoutube = officialsBean.getOfficialChannels().get(i).getChannelId();
                            llYoutube.setVisibility(View.VISIBLE);
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.tvAddress:
                String address = tvAddress.getText().toString();

                Uri mapUri = Uri.parse("geo:0,0?q=" + Uri.encode(address));

                Intent intentMap = new Intent(Intent.ACTION_VIEW, mapUri);
                intentMap.setPackage("com.google.android.apps.maps");

                if (intentMap.resolveActivity(getPackageManager()) != null) {
                    startActivity(intentMap);
                } else {
                    Toast.makeText(this, "No Application found that handles ACTION_VIEW (geo) intents",
                            Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.tvPhone:
                String number = tvPhone.getText().toString();

                Intent intentPhone = new Intent(Intent.ACTION_DIAL);
                intentPhone.setData(Uri.parse("tel:" + number));

                if (intentPhone.resolveActivity(getPackageManager()) != null) {
                    startActivity(intentPhone);
                } else {
                    Toast.makeText(this, "No Application found that handles ACTION_DIAL (tel) intents",
                            Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.tvEmail:
                String[] addresses;
                if (tvEmail.getText().toString().contains(",")) {
                    addresses = tvEmail.getText().toString().split(",");
                } else {
                    addresses = new String[]{tvEmail.getText().toString()};
                }
                Intent intentEmail = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:"));

                intentEmail.putExtra(Intent.EXTRA_EMAIL, addresses);
                //intentEmail.putExtra(Intent.EXTRA_SUBJECT, "Email from Sample Implied Intent App");
                //intentEmail.putExtra(Intent.EXTRA_TEXT, "Email text body...");

                if (intentEmail.resolveActivity(getPackageManager()) != null) {
                    startActivity(intentEmail);
                } else {
                    Toast.makeText(this, "No Application found that handles SENDTO (mailto) intents",
                            Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.tvWebsite:
                String URL = tvWebsite.getText().toString();
                Intent intentBrowser = new Intent(Intent.ACTION_VIEW);
                intentBrowser.setData(Uri.parse(URL));
                startActivity(intentBrowser);
                break;
            case R.id.imgvOfficialImage:
                Intent intentImage = new Intent(OfficialActivity.this, PhotoDetailActivity.class);
                intentImage.putExtra(this.getResources().getString(R.string.strIntentExtraLocationDetails), officesLocationBean);
                intentImage.putExtra(this.getResources().getString(R.string.strIntentExtraOfficesName), officesName);
                intentImage.putExtra(this.getResources().getString(R.string.strIntentExtraOfficialDetails), officialsBean);
                startActivity(intentImage);
                break;
            case R.id.llGooglePlus:
                String nameGooglePlus = strGooglePlus;
                Intent intentGooglePlus = null;
                try {
                    intentGooglePlus = new Intent(Intent.ACTION_VIEW);
                    intentGooglePlus.setClassName("com.google.android.apps.plus",
                            "com.google.android.apps.plus.phone.UrlGatewayActivity");
                    intentGooglePlus.putExtra("customAppUri", nameGooglePlus);
                    startActivity(intentGooglePlus);
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://plus.google.com/" + nameGooglePlus)));
                }
                break;
            case R.id.llYoutube:
                String nameYoutube = strYoutube;
                Intent intentYoutube = null;
                try {
                    intentYoutube = new Intent(Intent.ACTION_VIEW);
                    intentYoutube.setPackage("com.google.android.youtube");
                    intentYoutube.setData(Uri.parse("https://www.youtube.com/" + nameYoutube));
                    startActivity(intentYoutube);
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("https://www.youtube.com/" + nameYoutube)));
                }
                break;
            case R.id.llTwitter:
                Intent intentTwitter = null;
                String nameTwitter = strTwitter;
                try {
                    // get the Twitter app if possible
                    getPackageManager().getPackageInfo("com.twitter.android", 0);
                    intentTwitter = new Intent(Intent.ACTION_VIEW, Uri.parse("twitter://user?screen_name=" + nameTwitter));
                    intentTwitter.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                } catch (Exception e) {
                    // no Twitter app, revert to browser
                    intentTwitter = new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/" + nameTwitter));
                }
                startActivity(intentTwitter);
                break;
            case R.id.llFacebook:
                String FACEBOOK_URL = "https://www.facebook.com/" + strFacebook;
                String urlToUse;
                PackageManager packageManager = getPackageManager();
                try {
                    int versionCode = packageManager.
                            getPackageInfo("com.facebook.katana", 0).versionCode;
                    if (versionCode >= 3002850) { //newer versions of fb app
                        urlToUse = "fb://facewebmodal/f?href=" + FACEBOOK_URL;
                    } else { //older versions of fb app
                        urlToUse = "fb://page/" + strFacebook;
                    }
                } catch (PackageManager.NameNotFoundException e) {
                    urlToUse = FACEBOOK_URL; //normal web url
                }
                Intent facebookIntent = new Intent(Intent.ACTION_VIEW);
                facebookIntent.setData(Uri.parse(urlToUse));
                startActivity(facebookIntent);
                break;
        }
    }
}
