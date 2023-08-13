package com.softperl.urdunovelscollections.InterfaceUtil;

import com.softperl.urdunovelscollections.ObjectUtil.AuthorObject;

public interface ArtistCallback {

    void onSelect(int parentPosition,int childPosition);

    void onShare(AuthorObject authorObject);

}
