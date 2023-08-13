package com.softperl.urdunovelscollections.InterfaceUtil;

import android.net.Uri;

import com.softperl.urdunovelscollections.ObjectUtil.RequestObject;

public interface DatabaseCallback {

    void onSuccess(Uri data, RequestObject requestObject);

    void onError(String data, RequestObject requestObject);

}
