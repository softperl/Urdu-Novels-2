package com.softperl.urdunovelscollections.InterfaceUtil;

import com.softperl.urdunovelscollections.ObjectUtil.RequestObject;

public interface ConnectionCallback {

    void onSuccess(Object data, RequestObject requestObject);

    void onError(String data, RequestObject requestObject);


}
