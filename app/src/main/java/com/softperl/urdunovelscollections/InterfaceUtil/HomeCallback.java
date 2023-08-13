package com.softperl.urdunovelscollections.InterfaceUtil;

import com.softperl.urdunovelscollections.ConstantUtil.Constant;

public interface HomeCallback {

    void onSelect(int parentPosition,int childPosition);

    void onSelectSearch();

    void onMore(Constant.DATA_TYPE dataType);

}
