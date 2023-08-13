package com.softperl.urdunovelscollections.FragmentUtil;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.signature.ObjectKey;
import com.folioreader.FolioReader;
import com.folioreader.model.ReadPosition;
import com.folioreader.model.ReadPositionImpl;
import com.folioreader.util.ReadPositionListener;
import com.google.gson.Gson;
import com.softperl.urdunovelscollections.ActivityUtil.AuthorBook;
import com.softperl.urdunovelscollections.ActivityUtil.Base;
import com.softperl.urdunovelscollections.ActivityUtil.Categories;
import com.softperl.urdunovelscollections.ActivityUtil.CategorizedBook;
import com.softperl.urdunovelscollections.ActivityUtil.History;
import com.softperl.urdunovelscollections.ActivityUtil.ListOfAuthor;
import com.softperl.urdunovelscollections.ActivityUtil.ListOfBooks;
import com.softperl.urdunovelscollections.ActivityUtil.Profile;
import com.softperl.urdunovelscollections.ActivityUtil.ReadBook;
import com.softperl.urdunovelscollections.ActivityUtil.Search;
import com.softperl.urdunovelscollections.ActivityUtil.Viewer;
import com.softperl.urdunovelscollections.AdapterUtil.HomeAdapter;
import com.softperl.urdunovelscollections.ConstantUtil.Constant;
import com.softperl.urdunovelscollections.CustomUtil.GlideApp;
import com.softperl.urdunovelscollections.DatabaseUtil.DatabaseObject;
import com.softperl.urdunovelscollections.InterfaceUtil.ConnectionCallback;
import com.softperl.urdunovelscollections.InterfaceUtil.HomeCallback;
import com.softperl.urdunovelscollections.ManagementUtil.Management;
import com.softperl.urdunovelscollections.ObjectUtil.ArtistHeaderObject;
import com.softperl.urdunovelscollections.ObjectUtil.BookHeaderObject;
import com.softperl.urdunovelscollections.ObjectUtil.DataObject;
import com.softperl.urdunovelscollections.ObjectUtil.HomeObject;
import com.softperl.urdunovelscollections.ObjectUtil.PrefObject;
import com.softperl.urdunovelscollections.ObjectUtil.ProgressObject;
import com.softperl.urdunovelscollections.ObjectUtil.RequestObject;
import com.softperl.urdunovelscollections.ObjectUtil.SearchObject;
import com.softperl.urdunovelscollections.R;
import com.softperl.urdunovelscollections.Utility.Utility;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;

public class Home extends Fragment implements View.OnClickListener, ConnectionCallback, HomeCallback {
    private TextView txtMenu;
    private ImageView imageMenu;
    private Management management;
    private GridLayoutManager gridLayoutManager;
    private RecyclerView recyclerViewHome;
    private HomeAdapter homeAdapter;
    private ArrayList<Object> objectArrayList = new ArrayList<>();
    private ArrayList<DataObject> historyArraylist = new ArrayList<>();
    private String TAG = Home.class.getName();
    private ImageView imageProfile;
    private PrefObject prefObject;
    private String pictureUrl;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_home, null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initUI(view); //Initialize UI

    }


    /**
     * <p>It initialize the UI</p>
     */
    private void initUI(View view) {

        management = new Management(getActivity());
        prefObject = management.getPreferences(new PrefObject()
                .setRetrieveLogin(true)
                .setRetrieveUserCredential(true));

        imageProfile = view.findViewById(R.id.image_profile);
        imageProfile.setVisibility(View.GONE);

        if (prefObject.isLogin()) {

            if (prefObject.getLoginType().equalsIgnoreCase(Constant.LoginType.NATIVE_LOGIN)) {
                pictureUrl = Constant.ServerInformation.PICTURE_URL + prefObject.getPictureUrl();

            } else if (prefObject.getLoginType().equalsIgnoreCase(Constant.LoginType.GOOGLE_LOGIN)) {
                pictureUrl = prefObject.getPictureUrl();

            } else {
                pictureUrl = prefObject.getPictureUrl() + Constant.ServerInformation.FACEBOOK_HIGH_PIXEL_URL;

            }

            imageProfile.setVisibility(View.VISIBLE);
            GlideApp.with(this).load(pictureUrl)
                    .apply(new RequestOptions()
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .skipMemoryCache(true)
                            .placeholder(R.drawable.profile_picture)
                            .error(R.drawable.profile_picture)
                            .signature(new ObjectKey(System.currentTimeMillis())))
                    .into(imageProfile);

        }

        objectArrayList.add(new ProgressObject());

        gridLayoutManager = new GridLayoutManager(getActivity(), 1, LinearLayoutManager.VERTICAL, false);
        recyclerViewHome = view.findViewById(R.id.recycler_view_home);
        recyclerViewHome.setLayoutManager(gridLayoutManager);

        homeAdapter = new HomeAdapter(getActivity(), objectArrayList, this) {
            @Override
            public void select(boolean isLocked, int position) {

            }
        };
        recyclerViewHome.setAdapter(homeAdapter);

        //Send request to Server

        management.sendRequestToServer(new RequestObject()
                .setContext(getActivity())
                .setJson(getJson())
                .setConnection(Constant.CONNECTION.HOME)
                .setConnectionType(Constant.CONNECTION_TYPE.UI)
                .setConnectionCallback(this));

        imageProfile.setOnClickListener(this);

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

            jsonObject.accumulate("functionality", "home");
            PrefObject prefObject = management.getPreferences(new PrefObject().setRetrieveNewsfeed(true));
            jsonObject.accumulate("cat_id", prefObject.getNewsfeedId());

        } catch (JSONException e) {
            e.printStackTrace();
        }

        // 2. convert JSONObject to JSON to String
        json = jsonObject.toString();
        Utility.Logger(TAG, "JSON " + json);

        return json;
    }

    @Override
    public void onClick(View v) {
        if (v == imageMenu) {
            Base.layoutDrawer.openDrawer(Gravity.LEFT);
        }
        if (v == imageProfile) {

            startActivity(new Intent(getActivity(), Profile.class));

        }

    }

    @Override
    public void onSuccess(Object data, RequestObject requestObject) {

        if (data != null && requestObject != null) {

            if (data instanceof DataObject) {

                objectArrayList.clear();
                DataObject dataObject = (DataObject) data;
                objectArrayList.add(new SearchObject());
                objectArrayList.addAll(dataObject.getHomeList());

                if (historyArraylist.size() > 0)
                    objectArrayList.add(new HomeObject()
                            .setData_type(Constant.DATA_TYPE.HISTORY)
                            .setTitle(Utility.getStringFromRes(getActivity(), R.string.continue_reading))
                            .setDataObjectArrayList(historyArraylist));


                homeAdapter.notifyDataSetChanged();
            }


        }

    }

    @Override
    public void onError(String data, RequestObject requestObject) {

        if (!Utility.isEmptyString(data) && requestObject != null) {

            Utility.Logger(TAG, "Error = " + data);

        }

    }

    @Override
    public void onSelect(int parentPosition, int childPosition) {

        HomeObject homeObject = null;
        DataObject dataObject = null;


        if (parentPosition >= 0) {

            homeObject = (HomeObject) objectArrayList.get(parentPosition);
            dataObject = homeObject.getDataObjectArrayList().get(childPosition);

            if (homeObject.getData_type() == Constant.DATA_TYPE.ARTIST) {

                ArtistHeaderObject artistHeaderObject = new ArtistHeaderObject()
                        .setArtistId(dataObject.getId()).setAuthorName(dataObject.getTitle())
                        .setAuthorWork(dataObject.getAuthorWork()).setAuthorDescription(dataObject.getAuthorDescription())
                        .setBookCount(dataObject.getBookCount()).setDownloadCount(dataObject.getDownloadCount())
                        .setReviewCount(dataObject.getReviewCount()).setAuthorPicture(dataObject.getOriginalUrl());

                Intent intent = new Intent(getActivity(), AuthorBook.class);
                intent.putExtra(Constant.IntentKey.ARTIST_DETAIL, artistHeaderObject);
                startActivity(intent);

            } else if (homeObject.getData_type() == Constant.DATA_TYPE.CATEGORIES) {

                Intent intent = new Intent(getActivity(), CategorizedBook.class);
                intent.putExtra(Constant.IntentKey.CATEGORY, dataObject.getCategoryTitle());
                intent.putExtra(Constant.IntentKey.CATEGORY_ID, dataObject.getId());
                startActivity(intent);

            } else if (homeObject.getData_type() == Constant.DATA_TYPE.HISTORY) {

                if (dataObject.getFileType().equalsIgnoreCase(Constant.DataType.PDF)) {

                    Intent intent = new Intent(getActivity(), ReadBook.class);
                    intent.putExtra(Constant.IntentKey.BOOK_DETAIL, dataObject);
                    startActivity(intent);

                } else if (dataObject.getFileType().equalsIgnoreCase(Constant.DataType.EPUB)) {


                    FolioReader folioReader = FolioReader.getInstance(getActivity(), null);
                    folioReader.saveThemeOption(Utility.isNightMode(getActivity()));
                    final DataObject finalDataObject = dataObject;
                    folioReader.setReadPositionListener(new ReadPositionListener() {
                        @Override
                        public void saveReadPosition(ReadPosition readPosition) {

                            //In this part we update the last read position
                            //in db file so that user would start from where
                            //it left the book

                            management.getDataFromDatabase(new DatabaseObject()
                                    .setTypeOperation(Constant.TYPE.FILE_READING_STATUS)
                                    .setDbOperation(Constant.DB.UPDATE)
                                    .setDataObject(new DataObject()
                                            .setId(finalDataObject.getId())
                                            .setCurrentPage(readPosition.toJson())));


                        }
                    });

                    if (!Utility.isEmptyString(dataObject.getCurrentPage())) {

                        Gson gson = new Gson();
                        ReadPositionImpl readPosition = gson.fromJson(dataObject.getCurrentPage(), ReadPositionImpl.class);
                        folioReader.setReadPosition(readPosition);
                    }

                    String filePath = new File(Uri.parse(dataObject.getBookUrl()).getPath()).getAbsolutePath();
                    Utility.Logger(TAG, "Epub File Path = " + filePath);
                    folioReader.openBook(filePath);


                }

            } else {

                BookHeaderObject bookHeaderObject = new BookHeaderObject()
                        .setBookId(dataObject.getId()).setBookName(dataObject.getTitle())
                        .setBookDescription(dataObject.getDescription()).setBookAuthorName(dataObject.getArtistName())
                        .setBookDownloadCount(dataObject.getDownloads()).setBookReviewCount(dataObject.getComments())
                        .setBookTag(dataObject.getTags()).setBookRating(dataObject.getRating())
                        .setBookImage(dataObject.getOriginalUrl()).setBookUrl(dataObject.getBookUrl());

                Intent intent = new Intent(getActivity(), Viewer.class);
                intent.putExtra(Constant.IntentKey.BOOK_DETAIL, bookHeaderObject);
                startActivity(intent);

            }

        } else {

            dataObject = (DataObject) objectArrayList.get(childPosition);

            BookHeaderObject bookHeaderObject = new BookHeaderObject()
                    .setBookId(dataObject.getId()).setBookName(dataObject.getTitle())
                    .setBookDescription(dataObject.getDescription()).setBookAuthorName(dataObject.getArtistName())
                    .setBookDownloadCount(dataObject.getDownloads()).setBookReviewCount(dataObject.getComments())
                    .setBookTag(dataObject.getTags()).setBookRating(dataObject.getRating())
                    .setBookImage(dataObject.getOriginalUrl()).setBookUrl(dataObject.getBookUrl());

            Intent intent = new Intent(getActivity(), Viewer.class);
            intent.putExtra(Constant.IntentKey.BOOK_DETAIL, bookHeaderObject);
            startActivity(intent);

        }

        Utility.Logger(TAG, "Data = " + dataObject.toString());

    }

    @Override
    public void onSelectSearch() {
        Utility.Logger(TAG, "OnSearch Clicking");
        startActivity(new Intent(getActivity(), Search.class));
    }

    @Override
    public void onMore(Constant.DATA_TYPE dataType) {

        if (dataType == Constant.DATA_TYPE.POPULAR) {

            Intent intent = new Intent(getActivity(), ListOfBooks.class);
            intent.putExtra(Constant.IntentKey.PLAYLIST_NAME, Utility.getStringFromRes(getActivity(), R.string.popular));
            intent.putExtra(Constant.IntentKey.CONNECTION, Constant.CONNECTION.POPULAR.name());
            startActivity(intent);

        } else if (dataType == Constant.DATA_TYPE.CATEGORIES) {

            startActivity(new Intent(getActivity(), Categories.class));

        } else if (dataType == Constant.DATA_TYPE.FEED) {

            Intent intent = new Intent(getActivity(), ListOfBooks.class);
            intent.putExtra(Constant.IntentKey.PLAYLIST_NAME, Utility.getStringFromRes(getActivity(), R.string.personalized));
            intent.putExtra(Constant.IntentKey.CONNECTION, Constant.CONNECTION.NEWS_FEED.name());
            startActivity(intent);

        } else if (dataType == Constant.DATA_TYPE.ARTIST) {

            startActivity(new Intent(getActivity(), ListOfAuthor.class));

        } else if (dataType == Constant.DATA_TYPE.HISTORY) {

            startActivity(new Intent(getActivity(), History.class));

        }

    }

    @Override
    public void onResume() {
        super.onResume();

        ArrayList<Object> history = new ArrayList<>();
        history.addAll(management.getDataFromDatabase(new DatabaseObject()
                .setTypeOperation(Constant.TYPE.FILE_READING_STATUS)
                .setDbOperation(Constant.DB.RETRIEVE)
                .setDataObject(new DataObject())));

        historyArraylist.clear();
        int historySize = history.size() > 6 ? 6 : history.size();
        for (int i = 0; i < historySize; i++) {

            DataObject dtObject = (DataObject) history.get(i);
            historyArraylist.add(dtObject);

        }

    }
}
