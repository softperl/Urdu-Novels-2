package com.softperl.urdunovelscollections.DatabaseUtil;

import com.softperl.urdunovelscollections.ConstantUtil.Constant;
import com.softperl.urdunovelscollections.InterfaceUtil.SqlQueries;
import com.softperl.urdunovelscollections.Utility.Utility;

public class FavouriteQueries implements SqlQueries {


    public FavouriteQueries() {
        Utility.Logger(FavouriteQueries.class.getName(), "Setting : Working");
    }

    @Override
    public String retrieving() {
        return "SELECT * FROM " + Constant.DatabaseColumn.TABLE_NAME;
    }

    @Override
    public String update() {
        return "UPDATE " + Constant.DatabaseColumn.TABLE_NAME + " SET " + Constant.DatabaseColumn.COLUMN_USER_ID + "=" + "%s"
                + " WHERE " + Constant.DatabaseColumn.COLUMN_ID + "=" + "%s";
    }

    @Override
    public String insert() {
        return "INSERT INTO " + Constant.DatabaseColumn.TABLE_NAME
                + "(" + Constant.DatabaseColumn.COLUMN_BOOK_TITLE + "," + Constant.DatabaseColumn.COLUMN_COVER_URL
                + "," + Constant.DatabaseColumn.COLUMN_ARTIST_NAME + "," + Constant.DatabaseColumn.COLUMN_BOOK_URL
                + "," + Constant.DatabaseColumn.COLUMN_BOOK_ID + "," + Constant.DatabaseColumn.COLUMN_USER_ID
                + "," + Constant.DatabaseColumn.COLUMN_TWITTER_URL + ") VALUES (%s,%s,%s,%s,%s,%s,%s)";
    }

    @Override
    public String delete() {
        return "DELETE FROM " + Constant.DatabaseColumn.TABLE_NAME + " WHERE " + Constant.DatabaseColumn.COLUMN_BOOK_ID + "=" + "%s AND " +
                Constant.DatabaseColumn.COLUMN_USER_ID + "=" + "%s";
    }

    @Override
    public String retrieveSpecificType() {
        return "SELECT * FROM " + Constant.DatabaseColumn.TABLE_NAME + " WHERE " + Constant.DatabaseColumn.COLUMN_BOOK_ID + " =%s AND " +
                Constant.DatabaseColumn.COLUMN_USER_ID + "=" + "%s";
    }


    @Override
    public String retrieveSpecificTags() {
        return "SELECT * FROM " + Constant.DatabaseColumn.TABLE_NAME + " WHERE " + Constant.DatabaseColumn.COLUMN_BOOK_ID + " =%s ORDER BY "
                + Constant.DatabaseColumn.COLUMN_ID + " DESC";

    }

    @Override
    public String deleteSpecificDownload() {
        return "DELETE FROM " + Constant.DatabaseColumn.TABLE_NAME + " WHERE "
                + Constant.DatabaseColumn.COLUMN_USER_ID + " =%s";
    }
}
