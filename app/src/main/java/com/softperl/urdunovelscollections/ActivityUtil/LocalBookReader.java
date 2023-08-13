package com.softperl.urdunovelscollections.ActivityUtil;

import android.content.ContentResolver;
import android.net.Uri;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;
import com.github.barteksc.pdfviewer.listener.OnPageErrorListener;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;
import com.google.ads.mediation.admob.AdMobAdapter;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.softperl.urdunovelscollections.ConstantUtil.Constant;
import com.softperl.urdunovelscollections.R;
import com.softperl.urdunovelscollections.Utility.Utility;
import com.ixidev.gdpr.GDPRChecker;

public class LocalBookReader extends AppCompatActivity implements View.OnClickListener, OnPageChangeListener, OnLoadCompleteListener,
        OnPageErrorListener {

    private String action;
    private String scheme;
    private Uri dataUri;
    private String TAG = LocalBookReader.class.getName();
    private TextView txtMenu;
    private ImageView imageClose;
    private PDFView pdfView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Utility.changeAppTheme(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local_mime_reader);

        getIntentData(); //Retrieve Intent Data
        initUI();  //Initialize UI
        initAD();
    }


    /**
     * <p>It is used to retrieve intent data</p>
     */
    private void getIntentData() {

        action = getIntent().getAction();
        scheme = getIntent().getScheme();
        dataUri = getIntent().getData();

    }


    /**
     * <p>It is used to initialize UI</p>
     */
    private void initUI() {


        txtMenu = findViewById(R.id.txt_menu);
        txtMenu.setText(Utility.getStringFromRes(this, R.string.book_reader));

        imageClose = findViewById(R.id.image_back);
        imageClose.setVisibility(View.VISIBLE);
        imageClose.setImageResource(R.drawable.ic_close);

        pdfView = findViewById(R.id.pdfView);

        if (scheme.compareTo(ContentResolver.SCHEME_FILE) == 0) {

            //String name = dataUri.getLastPathSegment();
            String fileUrl = dataUri.getEncodedPath();

            Utility.Logger(TAG, "Content intent detected : " + action + " Data String :" +
                    getIntent().getDataString() + " Type :" + getIntent().getType() + " File Path : " + fileUrl);

            pdfView.fromUri(dataUri)
                    .defaultPage(0)
                    .onPageChange(this)
                    .enableAnnotationRendering(true)
                    .onLoad(this)
                    .scrollHandle(new DefaultScrollHandle(this))
                    .spacing(10) // in dp
                    .onPageError(this)
                    .enableAntialiasing(true)
                    .load();

        }

        imageClose.setOnClickListener(this);


    }
    private void initAD() {

        if (Constant.Credentials.isAdmobBannerAds) {

            LinearLayout mAdView = findViewById(R.id.adView);
            mAdView.setVisibility(View.VISIBLE);

            AdView adView = new AdView(this);
            adView.setAdSize(AdSize.BANNER);
            adView.setAdUnitId(Constant.Credentials.ADMOB_BANNER_ID);

            AdRequest.Builder adRequest = new AdRequest.Builder();

            GDPRChecker.Request request = GDPRChecker.getRequest();
            if (request == GDPRChecker.Request.NON_PERSONALIZED) {
                // load non Personalized ads
                Bundle extras = new Bundle();
                extras.putString("npa", "1");
                adRequest.addNetworkExtrasBundle(AdMobAdapter.class, extras);
            } // else do nothing , it will load PERSONALIZED ads

            adView.loadAd(adRequest.build());
            mAdView.addView(adView);

        }

    }

    @Override
    public void onClick(View v) {
        if (v == imageClose) {
            finish();
        }
    }

    @Override
    public void loadComplete(int nbPages) {

    }

    @Override
    public void onPageChanged(int page, int pageCount) {

    }

    @Override
    public void onPageError(int page, Throwable t) {

    }
}
