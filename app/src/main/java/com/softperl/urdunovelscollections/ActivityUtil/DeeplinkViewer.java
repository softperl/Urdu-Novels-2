package com.softperl.urdunovelscollections.ActivityUtil;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import com.softperl.urdunovelscollections.ConstantUtil.Constant;
import com.softperl.urdunovelscollections.InterfaceUtil.ConnectionCallback;
import com.softperl.urdunovelscollections.ManagementUtil.Management;
import com.softperl.urdunovelscollections.ObjectUtil.ArtistHeaderObject;
import com.softperl.urdunovelscollections.ObjectUtil.BookHeaderObject;
import com.softperl.urdunovelscollections.ObjectUtil.DataObject;
import com.softperl.urdunovelscollections.ObjectUtil.RequestObject;
import com.softperl.urdunovelscollections.R;
import com.softperl.urdunovelscollections.Utility.Utility;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class DeeplinkViewer extends AppCompatActivity implements ConnectionCallback {

    private String TAG = DeeplinkViewer.class.getName();
    private String postId;
    private String postType;
    private Management management;
    private Constant.CONNECTION connection;
    private String functionality;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deeplink_viewer);

        getIntentData(); //Retrieve Intent Data
        initUI(); //Initialize UI

    }


    /**
     * <p>It is used to retrieve Intent Data</p>
     */
    private void getIntentData() {

        Intent mainIntent = getIntent();

        if (mainIntent != null && mainIntent.getData() != null
                && (mainIntent.getData().getScheme().equals("http"))) {

            Uri data = mainIntent.getData();
            postId = data.getQueryParameter("post_id");
            postType = data.getQueryParameter("post_type");

            Utility.Logger(TAG, "Manual = " + data.getQueryParameter("post_id") + " Link = " +
                    data.toString());
            List<String> pathSegments = data.getPathSegments();

        }

    }


    /**
     * <p>It is used to retrieve Intent Data</p>
     */
    private void initUI() {


        management = new Management(getApplicationContext());

        if (postType.equalsIgnoreCase(Constant.PostType.AUTHOR_TYPE)) {

            functionality = "specific_author_detail";
            connection = Constant.CONNECTION.SPECIFIC_AUTHOR_DETAIL;

        } else if (postType.equalsIgnoreCase(Constant.PostType.POST_TYPE)) {

            functionality = "specific_book";
            connection = Constant.CONNECTION.SPECIFIC_BOOK;

        }

        //Send request to Server

        management.sendRequestToServer(new RequestObject()
                .setContext(getApplicationContext())
                .setJson(getJson(functionality, postId))
                .setConnection(connection)
                .setConnectionType(Constant.CONNECTION_TYPE.UI)
                .setConnectionCallback(this));
    }

    /**
     * <p>It is used to convert Object into Json</p>
     *
     * @param
     * @return
     */
    private String getJson(String functionality, String postID) {
        String json = null;

        // 1. build jsonObject
        JSONObject jsonObject = new JSONObject();
        try {

            jsonObject.accumulate("functionality", functionality);
            jsonObject.accumulate("post_id", postID);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        // 2. convert JSONObject to JSON to String
        json = jsonObject.toString();
        Utility.Logger(TAG, "JSON " + json);

        return json;
    }


    @Override
    public void onSuccess(Object data, RequestObject requestObject) {

        if (data != null && requestObject != null) {

            DataObject dtObject = (DataObject) data;
            DataObject dataObject = dtObject.getWallpaperList().get(0);

            if (requestObject.getConnection() == Constant.CONNECTION.SPECIFIC_AUTHOR_DETAIL) {

                ArtistHeaderObject artistHeaderObject = new ArtistHeaderObject()
                        .setArtistId(dataObject.getId()).setAuthorName(dataObject.getTitle())
                        .setAuthorWork(dataObject.getAuthorWork()).setAuthorDescription(dataObject.getAuthorDescription())
                        .setBookCount(dataObject.getBookCount()).setDownloadCount(dataObject.getDownloadCount())
                        .setReviewCount(dataObject.getReviewCount()).setAuthorPicture(dataObject.getOriginalUrl());

                Intent intent = new Intent(getApplicationContext(), AuthorBook.class);
                intent.putExtra(Constant.IntentKey.ARTIST_DETAIL, artistHeaderObject);
                intent.putExtra(Constant.IntentKey.BACK_ACTION, true);
                startActivity(intent);
                finish();

            } else if (requestObject.getConnection() == Constant.CONNECTION.SPECIFIC_BOOK) {

                BookHeaderObject bookHeaderObject = new BookHeaderObject()
                        .setBookId(dataObject.getId()).setBookName(dataObject.getTitle())
                        .setBookDescription(dataObject.getDescription()).setBookAuthorName(dataObject.getArtistName())
                        .setBookDownloadCount(dataObject.getDownloads()).setBookReviewCount(dataObject.getComments())
                        .setBookTag(dataObject.getTags()).setBookRating(dataObject.getRating())
                        .setBookImage(dataObject.getOriginalUrl()).setBookUrl(dataObject.getBookUrl());

                Intent intent = new Intent(getApplicationContext(), Viewer.class);
                intent.putExtra(Constant.IntentKey.BOOK_DETAIL, bookHeaderObject);
                intent.putExtra(Constant.IntentKey.BACK_ACTION, true);
                startActivity(intent);
                finish();
            }

        }
    }

    @Override
    public void onError(String data, RequestObject requestObject) {

    }
}
