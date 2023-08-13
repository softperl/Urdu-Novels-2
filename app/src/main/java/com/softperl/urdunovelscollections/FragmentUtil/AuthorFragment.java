package com.softperl.urdunovelscollections.FragmentUtil;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.ads.mediation.admob.AdMobAdapter;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.softperl.urdunovelscollections.ActivityUtil.AuthorBook;
import com.softperl.urdunovelscollections.AdapterUtil.CategoriesAdapter;
import com.softperl.urdunovelscollections.ConstantUtil.Constant;
import com.softperl.urdunovelscollections.CustomUtil.GridSpacingItemDecoration;
import com.softperl.urdunovelscollections.InterfaceUtil.ConnectionCallback;
import com.softperl.urdunovelscollections.ManagementUtil.Management;
import com.softperl.urdunovelscollections.ObjectUtil.ArtistHeaderObject;
import com.softperl.urdunovelscollections.ObjectUtil.BarObject;
import com.softperl.urdunovelscollections.ObjectUtil.DataObject;
import com.softperl.urdunovelscollections.ObjectUtil.EmptyObject;
import com.softperl.urdunovelscollections.ObjectUtil.InternetObject;
import com.softperl.urdunovelscollections.ObjectUtil.ProgressObject;
import com.softperl.urdunovelscollections.ObjectUtil.RequestObject;
import com.softperl.urdunovelscollections.R;
import com.softperl.urdunovelscollections.Utility.Utility;
import com.ixidev.gdpr.GDPRChecker;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class AuthorFragment extends Fragment implements ConnectionCallback {
    private String TAG = AuthorFragment.class.getName();
    private TextView txtMenu;
    private Management management;
    private GridLayoutManager gridLayoutManager;
    private RecyclerView recyclerViewCategories;
    private CategoriesAdapter categoriesAdapter;
    private ArrayList<Object> objectArrayList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_categories, null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initUI(view); //Initialize UI
        initAD(view); //Initialize Admob Banner Ads

    }

    /**
     * <p>It initialize the UI</p>
     */
    private void initUI(View view) {

        txtMenu = view.findViewById(R.id.txt_menu);
        txtMenu.setText(Utility.getStringFromRes(getActivity(), R.string.author));

        management = new Management(getActivity());
        objectArrayList.add(new ProgressObject());

        //Initialize & Setup Layout Manager with Recycler View

        gridLayoutManager = new GridLayoutManager(getActivity(), 3, LinearLayoutManager.VERTICAL, false);
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
                } else {
                    return 1;
                }
            }
        });

        recyclerViewCategories = (RecyclerView) view.findViewById(R.id.recycler_view_categories);
        recyclerViewCategories.setLayoutManager(gridLayoutManager);

        int spanCount = 3; // 3 columns
        int spacing = 15; // 50px
        boolean includeEdge = true;
        recyclerViewCategories.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing, includeEdge));

        //Initialize & Setup Adapter with Recycler View

        categoriesAdapter = new CategoriesAdapter(getActivity(), objectArrayList) {
            @Override
            public void selectCategory(int position) {


                DataObject dataObject = (DataObject) objectArrayList.get(position);
                ArtistHeaderObject artistHeaderObject = new ArtistHeaderObject()
                        .setArtistId(dataObject.getId()).setAuthorName(dataObject.getTitle())
                        .setAuthorWork(dataObject.getAuthorWork()).setAuthorDescription(dataObject.getAuthorDescription())
                        .setBookCount(dataObject.getBookCount()).setDownloadCount(dataObject.getDownloadCount())
                        .setReviewCount(dataObject.getReviewCount()).setAuthorPicture(dataObject.getOriginalUrl());

                Intent intent = new Intent(getActivity(), AuthorBook.class);
                intent.putExtra(Constant.IntentKey.ARTIST_DETAIL, artistHeaderObject);
                startActivity(intent);

            }
        };
        recyclerViewCategories.setAdapter(categoriesAdapter);


        //Send request to Server for retrieving TrendingPhotos Wallpapers

        management.sendRequestToServer(new RequestObject()
                .setJson(getJson())
                .setConnectionType(Constant.CONNECTION_TYPE.UI)
                .setConnection(Constant.CONNECTION.ALL_ARTIST)
                .setConnectionCallback(this));

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

            jsonObject.accumulate("functionality", "all_artist");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        // 2. convert JSONObject to JSON to String
        json = jsonObject.toString();
        Utility.Logger(TAG, "JSON " + json);

        return json;
    }


    /**
     * <p>It initialize the Admob Banner Ad</p>
     */
    private void initAD(View view) {

        if (Constant.Credentials.isAdmobBannerAds) {

            LinearLayout mAdView = view.findViewById(R.id.adView);
            mAdView.setVisibility(View.VISIBLE);

            AdView adView = new AdView(getActivity());
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
    public void onSuccess(Object data, RequestObject requestObject) {

        if (data != null && requestObject != null) {

            objectArrayList.clear();

            if (requestObject.getConnection() == Constant.CONNECTION.ALL_ARTIST) {

                DataObject dataObject = (DataObject) data;
                objectArrayList.addAll(dataObject.getWallpaperList());

                /*for (int i = 0; i < dataObject.getWallpaperList().size(); i++) {

                    DataObject category = dataObject.getWallpaperList().get(i);
                    objectArrayList.add(new CategoryObject()
                            .setId(category.getId())
                            .setTitle(category.getTitle())
                            .setPicture(category.getOriginalUrl()));

                }*/

            }

            categoriesAdapter.notifyDataSetChanged();

        }

    }

    @Override
    public void onError(String data, RequestObject requestObject) {
        Utility.Logger(TAG, "Setting = " + data);
        objectArrayList.clear();
        objectArrayList.add(new EmptyObject()
                .setTitle(Utility.getStringFromRes(getActivity(), R.string.no_artist))
                .setDescription(Utility.getStringFromRes(getActivity(), R.string.no_artist_tagline))
                .setPlaceHolderIcon(R.drawable.em_no_author));
        categoriesAdapter.notifyDataSetChanged();
    }


}
