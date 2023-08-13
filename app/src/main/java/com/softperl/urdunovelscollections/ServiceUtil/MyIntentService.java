package com.softperl.urdunovelscollections.ServiceUtil;

import android.app.IntentService;
import android.content.Intent;

import com.google.gson.Gson;
import com.softperl.urdunovelscollections.ConnectionUtil.Connection;
import com.softperl.urdunovelscollections.ConstantUtil.Constant;
import com.softperl.urdunovelscollections.DatabaseUtil.DatabaseObject;
import com.softperl.urdunovelscollections.JsonUtil.FavouriteUtil.FavouriteJson;
import com.softperl.urdunovelscollections.ManagementUtil.Management;
import com.softperl.urdunovelscollections.ObjectUtil.DataObject;
import com.softperl.urdunovelscollections.ObjectUtil.PrefObject;
import com.softperl.urdunovelscollections.ObjectUtil.RequestObject;
import com.softperl.urdunovelscollections.Utility.Utility;

import java.util.ArrayList;


/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions and extra parameters.
 */
public class MyIntentService extends IntentService {
    private String TAG = MyIntentService.class.getName();

    public MyIntentService() {
        super("MyIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        Utility.Logger(TAG, "Setting : Working");

        if (intent != null) {


            //It load specific tags wallpaper at background
            //After loading it would add them into Wallpaper db

            RequestObject requestObject = intent.getParcelableExtra(Constant.IntentKey.REQUEST_OBJECT);
            String result = Connection.makeRequest(requestObject.getServerUrl(), requestObject.getJson(), requestObject.getRequestType());


            Utility.Logger(TAG, "JSON = " + requestObject.getJson());

            if (Utility.isEmptyString(result))
                return;

            if (result.equalsIgnoreCase(Constant.ImportantMessages.CONNECTION_ERROR))
                return;

            Gson gson = new Gson();
            Object object = null;
            DataObject dataObject = null;
            ArrayList<Object> objectList = new ArrayList<>();

            if (requestObject.getConnection() == Constant.CONNECTION.ALL_FAVOURITES) {

                object = gson.fromJson(result, FavouriteJson.class);
                dataObject = DataObject.getWallpaperObject(requestObject, object);

                if (dataObject.getCode().equalsIgnoreCase(Constant.ErrorCodes.success_code)) {

                    String userID = "null";

                    Management management = new Management(this);
                    PrefObject prefObject = management.getPreferences(new PrefObject()
                            .setRetrieveUserId(true).setRetrieveLogin(true));


                    if (prefObject.isLogin()) {
                        userID = prefObject.getUserId();
                    }

                    for (int i = 0; i < dataObject.getWallpaperList().size(); i++) {

                        DataObject dtObject = dataObject.getWallpaperList().get(i);
                        management.getDataFromDatabase(new DatabaseObject()
                                .setTypeOperation(Constant.TYPE.FAVOURITES)
                                .setDbOperation(Constant.DB.INSERT)
                                .setDataObject(new DataObject()
                                        .setId(dtObject.getId())
                                        .setUserId(userID)
                                        .setTitle(dtObject.getTitle())
                                        .setBookUrl(dtObject.getBookUrl())
                                        .setCoverUrl(dtObject.getOriginalUrl())
                                        .setArtistName(dtObject.getArtistName())
                                        .setOriginalUrl(dtObject.getOriginalUrl())));

                    }

                }

            }

            if (dataObject == null)
                return;


        }
    }


}
