package com.softperl.urdunovelscollections.ActivityUtil;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.ads.mediation.admob.AdMobAdapter;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.softperl.urdunovelscollections.AdapterUtil.BookAdapter;
import com.softperl.urdunovelscollections.ConstantUtil.Constant;
import com.softperl.urdunovelscollections.InterfaceUtil.ConnectionCallback;
import com.softperl.urdunovelscollections.ManagementUtil.Management;
import com.softperl.urdunovelscollections.ObjectUtil.BarObject;
import com.softperl.urdunovelscollections.ObjectUtil.BookHeaderObject;
import com.softperl.urdunovelscollections.ObjectUtil.DataObject;
import com.softperl.urdunovelscollections.ObjectUtil.EmptyObject;
import com.softperl.urdunovelscollections.ObjectUtil.InternetObject;
import com.softperl.urdunovelscollections.ObjectUtil.NativeAdObject;
import com.softperl.urdunovelscollections.ObjectUtil.PrefObject;
import com.softperl.urdunovelscollections.ObjectUtil.ProgressObject;
import com.softperl.urdunovelscollections.ObjectUtil.RequestObject;
import com.softperl.urdunovelscollections.R;
import com.softperl.urdunovelscollections.Utility.Utility;
import com.ixidev.gdpr.GDPRChecker;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Favourites extends AppCompatActivity implements View.OnClickListener, ConnectionCallback {
    private String TAG = Favourites.class.getName();
    private ImageView imageBack;
    private TextView txtMenu;
    private Management management;
    private GridLayoutManager gridLayoutManager;
    private RecyclerView recyclerViewCategories;
    private BookAdapter bookAdapter;
    private ArrayList<Object> objectArrayList = new ArrayList<>();
    private ArrayList<Object> favouriteList = new ArrayList<>();
    private PrefObject prefObject;
    private ImageView imageStyle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Utility.changeAppTheme(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourites);

        initUI(); //Initialize UI
        initAD();  //Initialize Admob Banner Ads

    }

    /**
     * <p>It initialize the UI</p>
     */
    private void initUI() {


        txtMenu = findViewById(R.id.txt_menu);
        txtMenu.setText(Utility.getStringFromRes(this, R.string.my_favourites));

        imageBack = findViewById(R.id.image_back);
        imageBack.setVisibility(View.VISIBLE);
        imageBack.setImageResource(R.drawable.ic_back);

        imageStyle = findViewById(R.id.image_share);
        imageStyle.setVisibility(View.VISIBLE);
        imageStyle.setTag(0);
        imageStyle.setImageResource(R.drawable.ic_list);

        management = new Management(this);
        prefObject = management.getPreferences(new PrefObject()
                .setRetrieveUserId(true).setRetrieveLogin(true));

        objectArrayList.clear();
        objectArrayList.add(new ProgressObject());

        //Initialize & Setup Layout Manager with Recycler View

        gridLayoutManager = new GridLayoutManager(this, 3, LinearLayoutManager.VERTICAL, false);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (objectArrayList.get(position) instanceof EmptyObject) {
                    return 3;
                } else if (objectArrayList.get(position) instanceof InternetObject) {
                    return 3;
                } else if (objectArrayList.get(position) instanceof ProgressObject) {
                    return 3;
                } else if (objectArrayList.get(position) instanceof BarObject) {
                    return 3;
                } else if (objectArrayList.get(position) instanceof DataObject) {

                    DataObject dataObject = (DataObject) objectArrayList.get(position);
                    if (dataObject.isList()) {
                        return 3;
                    } else {
                        return 1;
                    }

                } else {
                    return 1;
                }
            }
        });

        recyclerViewCategories = (RecyclerView) findViewById(R.id.recycler_view_categories);
        recyclerViewCategories.setLayoutManager(gridLayoutManager);

        //Initialize & Setup Adapter with Recycler View

        bookAdapter = new BookAdapter(this, objectArrayList) {
            @Override
            public void select(boolean isLocked, int position) {

                if (!isLocked) {

                    DataObject dataObject = (DataObject) objectArrayList.get(position);
                    BookHeaderObject bookHeaderObject = new BookHeaderObject()
                            .setBookId(dataObject.getId()).setBookName(dataObject.getTitle())
                            .setBookDescription(dataObject.getDescription()).setBookAuthorName(dataObject.getArtistName())
                            .setBookDownloadCount(dataObject.getDownloads()).setBookReviewCount(dataObject.getComments())
                            .setBookTag(dataObject.getTags()).setBookRating(dataObject.getRating())
                            .setBookImage(dataObject.getOriginalUrl()).setBookUrl(dataObject.getBookUrl());

                    Intent intent = new Intent(getApplicationContext(), Viewer.class);
                    intent.putExtra(Constant.IntentKey.BOOK_DETAIL, bookHeaderObject);
                    startActivity(intent);

                }

            }
        };
        recyclerViewCategories.setAdapter(bookAdapter);


        //Send request to Server for retrieving TrendingPhotos Wallpapers

        management.sendRequestToServer(new RequestObject()
                .setJson(getJson())
                .setConnectionType(Constant.CONNECTION_TYPE.UI)
                .setConnection(Constant.CONNECTION.ALL_FAVOURITES)
                .setConnectionCallback(this));

        imageBack.setOnClickListener(this);
        imageStyle.setOnClickListener(this);

    }


    /**
     * <p>It initialize the Admob Banner Ad</p>
     */
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


    /**
     * <p>It is used to convert Object into Json</p>
     *
     * @param
     * @return
     */
    private String getJson() {
        String json = null;

        // 1. build jsonObject
        JSONObject jsonObject = new JSONObject();
        try {

            jsonObject.accumulate("functionality", "favourites");
            jsonObject.accumulate("user_id", prefObject.getUserId());

        } catch (JSONException e) {
            e.printStackTrace();
        }

        // 2. convert JSONObject to JSON to String
        json = jsonObject.toString();
        Utility.Logger(TAG, "JSON " + json);

        return json;
    }


    /**
     * <p>It is used to Change layout style dynamically</p>
     */
    private void changeStyle(boolean isList) {

        ArrayList<Object> tempArraylist = new ArrayList<>(objectArrayList);
        objectArrayList.clear();

        for (int i = 0; i < tempArraylist.size(); i++) {

            if (tempArraylist.get(i) instanceof DataObject) {

                DataObject dataObject = (DataObject) tempArraylist.get(i);
                if (isList)
                    dataObject.setList(true);
                else
                    dataObject.setList(false);

                objectArrayList.add(dataObject);

            }
        }
        if (objectArrayList.size() > 0)
            bookAdapter.notifyDataSetChanged();

    }

    @Override
    public void onClick(View v) {
        if (v == imageBack) {
            finish();
        }
        if (v == imageStyle) {

            int tag = (int) imageStyle.getTag();
            if (tag == 0) {

                imageStyle.setImageResource(R.drawable.ic_grid);
                imageStyle.setTag(1);
                changeStyle(false);

            } else if (tag == 1) {

                imageStyle.setImageResource(R.drawable.ic_list);
                imageStyle.setTag(0);
                changeStyle(true);

            }

        }
    }


    @Override
    public void onResume() {
        super.onResume();


    }

    @Override
    public void onSuccess(Object data, RequestObject requestObject) {

        if (data != null && requestObject != null) {

            objectArrayList.clear();
            DataObject dataObject = (DataObject) data;

            for (int i = 0; i < dataObject.getWallpaperList().size(); i++) {

                if (i != 0 && 0 == i % Constant.Credentials.nativeAdInterval && Constant.Credentials.isFbNativeAds)
                    objectArrayList.add(new NativeAdObject());

                /*if (Utility.isLocked(i)) {
                    objectArrayList.add(dataObject.getWallpaperList().get(i).setLocked(true));
                } else {*/
                objectArrayList.add(dataObject.getWallpaperList().get(i));
                //}

            }

            bookAdapter.notifyDataSetChanged();

        }

    }

    @Override
    public void onError(String data, RequestObject requestObject) {
        Utility.Logger(TAG, "Error = " + data);
        objectArrayList.clear();
        objectArrayList.add(new EmptyObject()
                .setTitle(Utility.getStringFromRes(this, R.string.no_book))
                .setDescription(Utility.getStringFromRes(this, R.string.no_book_tagline))
                .setPlaceHolderIcon(R.drawable.em_no_book));
        bookAdapter.notifyDataSetChanged();
    }
}
