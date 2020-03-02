package iitmad.com.a20425418.newsgateway;


import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import iitmad.com.a20425418.newsgateway.bean.ArticleBean;

public class NewsDetailFragment extends Fragment implements View.OnClickListener {
    public static final String EXTRA_MESSAGE = "EXTRA_MESSAGE";
    public static final String ARTICLE_BEAN = "ARTICLE_BEAN";
    private static final String TAG = "NewsDetailFragment";
    ArticleBean bean;
    TextView tvNewsArticleTitle, tvNewsArticlePublishedDate, tvNewsArticleAuthor, tvNewsArticleDescription, tvNewsArticleCounter;
    ImageView imgNewsArticleImage;
    View v;

    public static final NewsDetailFragment newInstance(String message, ArticleBean bean) {
        NewsDetailFragment f = new NewsDetailFragment();
        // Constructs a new, empty Bundle sized to hold the given number of elements (1).
        Bundle bdl = new Bundle(1);
        bdl.putString(EXTRA_MESSAGE, message);
        bdl.putSerializable(ARTICLE_BEAN, bean);
        f.setArguments(bdl);
        return f;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        try {
            String message = getArguments().getString(EXTRA_MESSAGE);
            bean = (ArticleBean) getArguments().getSerializable(ARTICLE_BEAN);
            Log.d(TAG, "onCreateView: " + bean.getArticle_title());
            Log.d(TAG, "onCreateView: " + message);
            v = inflater.inflate(R.layout.newsdetailfragment_layout, container, false);
            tvNewsArticleTitle = (TextView) v.findViewById(R.id.tvNewsArticleTitle);
            tvNewsArticlePublishedDate = (TextView) v.findViewById(R.id.tvNewsArticlePublishedDate);
            tvNewsArticleAuthor = (TextView) v.findViewById(R.id.tvNewsArticleAuthor);
            imgNewsArticleImage = (ImageView) v.findViewById(R.id.imgNewsArticleImage);
            tvNewsArticleDescription = (TextView) v.findViewById(R.id.tvNewsArticleDescription);
            tvNewsArticleCounter = (TextView) v.findViewById(R.id.tvNewsArticleCounter);
            if (bean.getArticle_title() != null &&
                    !(bean.getArticle_title().equals("")) && !(bean.getArticle_title().equals("null"))) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    tvNewsArticleTitle.setText(Html.fromHtml(bean.getArticle_title(), Html.FROM_HTML_MODE_COMPACT));
                } else {
                    tvNewsArticleTitle.setText(Html.fromHtml(bean.getArticle_title()));
                }
                //tvNewsArticleTitle.setText(bean.getArticle_title());
                tvNewsArticleTitle.setClickable(true);
            } else {
                tvNewsArticleTitle.setText(this.getActivity().getResources().getString(R.string.strNodata));
                tvNewsArticleTitle.setClickable(false);
            }

            if (bean.getArticle_publishedAt() != null &&
                    !(bean.getArticle_publishedAt().equals("")) && !(bean.getArticle_publishedAt().equals("null"))) {
                String formatDateTime = this.getString(R.string.strNodata);
                try {
                    DateTimeFormatter f;
                    if (bean.getArticle_publishedAt().contains("Z"))
                        f = DateTimeFormatter.ISO_INSTANT;
                    else
                        f = DateTimeFormatter.ISO_DATE_TIME;
                    Instant dateInstant = Instant.from(f.parse(bean.getArticle_publishedAt()));
                    LocalDateTime date = LocalDateTime.ofInstant(dateInstant, ZoneId.systemDefault()); //ZoneId.of(ZoneOffset.UTC.getId()));

                    //Log.d(TAG, "onCreateView: date" + date);
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, YYYY HH:mm");
                    formatDateTime = date.format(formatter);

                    //Log.d(TAG, "onCreateView: " + formatDateTime);

                } catch (Exception e) {
                    e.printStackTrace();
                }
                tvNewsArticlePublishedDate.setText(formatDateTime);

            } else {
                tvNewsArticlePublishedDate.setText(this.getActivity().getResources().getString(R.string.strNodata));
            }

            if (bean.getArticle_author() != null &&
                    !(bean.getArticle_author().equals("")) && !(bean.getArticle_author().equals("null"))) {
                tvNewsArticleAuthor.setText(bean.getArticle_author());
            } else {
                tvNewsArticleAuthor.setText(this.getActivity().getResources().getString(R.string.strNodata));
            }

            if (bean.getArticle_description() != null &&
                    !(bean.getArticle_description().equals("")) && !(bean.getArticle_description().equals("null"))) {
                tvNewsArticleDescription.setText(bean.getArticle_description());
                tvNewsArticleDescription.setClickable(true);
            } else {
                tvNewsArticleDescription.setText(this.getActivity().getResources().getString(R.string.strNodata));
                tvNewsArticleDescription.setClickable(false);
            }


            tvNewsArticleCounter.setText(message);

            tvNewsArticleTitle.setOnClickListener(this);
            imgNewsArticleImage.setOnClickListener(this);
            tvNewsArticleDescription.setOnClickListener(this);

            //Downloading Image Using Picasso
            if (bean.getArticle_urlToImage() != null &&
                    !(bean.getArticle_urlToImage().equals("")) && !(bean.getArticle_urlToImage().equals("null"))) {
                Picasso picasso = new Picasso.Builder(this.getActivity()).listener(new Picasso.Listener() {
                    @Override
                    public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {
                        // Here we try https if the http image attempt failed
                        final String changedUrl = bean.getArticle_urlToImage().replace("http:", "https:");
                        picasso.load(changedUrl).error(R.drawable.brokenimage)
                                .placeholder(R.drawable.placeholder)
                                .into(imgNewsArticleImage);
                    }
                }).build();
                picasso.load(bean.getArticle_urlToImage()).error(R.drawable.brokenimage)
                        .placeholder(R.drawable.placeholder)
                        .into(imgNewsArticleImage);
                imgNewsArticleImage.setClickable(true);
            } else {
                imgNewsArticleImage.setImageResource(R.drawable.missingimage);
                imgNewsArticleImage.setClickable(false);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return v;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.tvNewsArticleTitle:
                openURL();
                break;
            case R.id.imgNewsArticleImage:
                openURL();
                break;
            case R.id.tvNewsArticleDescription:
                openURL();
                break;
        }
    }

    public void openURL() {
        String URL = bean.getArticle_url();
        Intent intentBrowser = new Intent(Intent.ACTION_VIEW);
        intentBrowser.setData(Uri.parse(URL));
        startActivity(intentBrowser);
    }

}