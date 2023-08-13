package com.softperl.urdunovelscollections.ServiceUtil;

import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.core.app.JobIntentService;

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

public class OreoIntentService extends JobIntentService {
    /**
     * Unique job ID for this service.
     */
    static final int JOB_ID = 1000;
    private String TAG = OreoIntentService.class.getName();

    public OreoIntentService() {
    }

    /**
     * Convenience method for enqueuing work in to this service.
     */
    public static void enqueueWork(Context context, Intent intent) {

        enqueueWork(context, OreoIntentService.class, JOB_ID, intent);
    }

    @Override
    protected void onHandleWork(@NonNull Intent intent) {

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
