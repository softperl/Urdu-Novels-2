package com.softperl.urdunovelscollections.ActivityUtil;

import android.net.Uri;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.webkit.URLUtil;
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
import com.softperl.urdunovelscollections.DatabaseUtil.DatabaseObject;
import com.softperl.urdunovelscollections.ManagementUtil.Management;
import com.softperl.urdunovelscollections.ObjectUtil.DataObject;
import com.softperl.urdunovelscollections.ObjectUtil.PdfContentObject;
import com.softperl.urdunovelscollections.R;
import com.softperl.urdunovelscollections.Utility.Utility;
import com.ixidev.gdpr.GDPRChecker;

import java.util.ArrayList;

public class ReadBook extends AppCompatActivity implements View.OnClickListener, OnPageChangeListener, OnLoadCompleteListener,
        OnPageErrorListener {

    private TextView txtMenu;
    private ImageView imageMenu;
    private Management management;
    private String TAG = ReadBook.class.getName();
    private String bookUrl;
    private PDFView pdfView;
    private ArrayList<Object> objectArrayList = new ArrayList<>();
    private ArrayList<PdfContentObject> pdfList = new ArrayList<>();
    private int pdfPage = 0;
    private DataObject detailObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Utility.changeAppTheme(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_book);

        getIntentData(); //Receive Intent Data
        initUI(); //Initialize UI
        initAD();
    }


    /**
     * <p>It is used to receive Intent Data</p>
     */
    private void getIntentData() {
        detailObject = getIntent().getParcelableExtra(Constant.IntentKey.BOOK_DETAIL);
    }


    /**
     * <p>It initialize the UI</p>
     */
    private void initUI() {

        bookUrl = detailObject.getBookUrl();
        Utility.Logger(TAG, "Book Url = " + bookUrl + " Title= " + detailObject.getTitle());

        management = new Management(this);
        objectArrayList.addAll(management.getDataFromDatabase(new DatabaseObject()
                .setTypeOperation(Constant.TYPE.FILE_READING_STATUS)
                .setDbOperation(Constant.DB.SPECIFIC_BOOK)
                .setDataObject(new DataObject()
                        .setBookUrl(bookUrl)
                        .setFileType(Constant.DataType.PDF))));


        txtMenu = findViewById(R.id.txt_menu);
        txtMenu.setText(Utility.getStringFromRes(this, R.string.book_reader));

        imageMenu = findViewById(R.id.image_back);
        imageMenu.setVisibility(View.VISIBLE);
        imageMenu.setImageResource(R.drawable.ic_back);

        //Utility.Logger(TAG, "Formatted Url = " + formattedUrl);

        pdfView = findViewById(R.id.pdfView);

        if (URLUtil.isFileUrl(bookUrl)) {


            if (objectArrayList.size() > 0) {

                DataObject data = (DataObject) objectArrayList.get(0);
                pdfPage = Integer.parseInt(data.getCurrentPage());


                pdfView.fromUri(Uri.parse(bookUrl))
                        .defaultPage(pdfPage)
                        .onPageChange(this)
                        .enableAnnotationRendering(true)
                        .onLoad(this)
                        .scrollHandle(new DefaultScrollHandle(this))
                        .spacing(10) // in dp
                        .onPageError(this)
                        .enableAntialiasing(true)
                        .load();

            } else {

                pdfPage = 0;
                pdfView.fromUri(Uri.parse(bookUrl))
                        .defaultPage(pdfPage)
                        .onPageChange(this)
                        .enableAnnotationRendering(true)
                        .onLoad(this)
                        .scrollHandle(new DefaultScrollHandle(this))
                        .spacing(10) // in dp
                        .onPageError(this)
                        .enableAntialiasing(true)
                        .onLoad(new OnLoadCompleteListener() {
                            @Override
                            public void loadComplete(int nbPages) {

                                management.getDataFromDatabase(new DatabaseObject()
                                        .setTypeOperation(Constant.TYPE.FILE_READING_STATUS)
                                        .setDbOperation(Constant.DB.INSERT)
                                        .setDataObject(new DataObject()
                                                .setTitle(detailObject.getTitle())
                                                .setFileType(Constant.DataType.PDF)
                                                .setBookUrl(bookUrl)
                                                .setCoverUrl(detailObject.getCoverUrl())
                                                .setBookPage(String.valueOf(nbPages))
                                                .setCurrentPage(String.valueOf(pdfPage))));

                            }
                        })
                        .load();


            }


        }


        imageMenu.setOnClickListener(this);

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
        if (v == imageMenu) {
            finish();
        }
    }

    @Override
    public void loadComplete(int nbPages) {
        Utility.Logger(TAG, "Pages = " + nbPages + " " + pdfView.getTableOfContents().size());

        //getTableOfContent(pdfView.getTableOfContents(), "-");

    }

    @Override
    public void onPageChanged(int page, int pageCount) {
        Utility.Logger(TAG, "PageChange = Page No. " + page + " PageCount. " + pageCount);
    }

    @Override
    public void onPageError(int page, Throwable t) {
        t.printStackTrace();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        objectArrayList.clear();
        objectArrayList.addAll(management.getDataFromDatabase(new DatabaseObject()
                .setTypeOperation(Constant.TYPE.FILE_READING_STATUS)
                .setDbOperation(Constant.DB.SPECIFIC_BOOK)
                .setDataObject(new DataObject()
                        .setBookUrl(bookUrl)
                        .setFileType(Constant.DataType.PDF))));

        if (objectArrayList.size() > 0) {

            DataObject dataObject = (DataObject) objectArrayList.get(0);
            management.getDataFromDatabase(new DatabaseObject()
                    .setTypeOperation(Constant.TYPE.FILE_READING_STATUS)
                    .setDbOperation(Constant.DB.UPDATE)
                    .setDataObject(dataObject
                            .setFileType(Constant.DataType.PDF)
                            .setCurrentPage(String.valueOf(pdfView.getCurrentPage()))));

        }

    }
}
