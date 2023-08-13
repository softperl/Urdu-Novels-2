package com.softperl.urdunovelscollections.DatabaseUtil;

import com.softperl.urdunovelscollections.ConstantUtil.Constant;
import com.softperl.urdunovelscollections.InterfaceUtil.SqlQueries;
import com.softperl.urdunovelscollections.Utility.Utility;

public class Mp3Queries implements SqlQueries {


    public Mp3Queries() {
        Utility.Logger(Mp3Queries.class.getName(), "Setting : Working");
    }

    @Override
    public String retrieving() {
        return "SELECT * FROM " + Constant.DatabaseColumn.PLAYLIST_MP3_TABLE_NAME;
    }

    @Override
    public String update() {
        return "UPDATE " + Constant.DatabaseColumn.PLAYLIST_MP3_TABLE_NAME + " SET " + Constant.DatabaseColumn.PLAYLIST_MP3_COLUMN_TITLE + "=" + "%s"
                + " WHERE " + Constant.DatabaseColumn.PLAYLIST_MP3_COLUMN_ID + "=" + "%s";

    }

    @Override
    public String insert() {
        return "INSERT INTO " + Constant.DatabaseColumn.PLAYLIST_MP3_TABLE_NAME + "(" + Constant.DatabaseColumn.PLAYLIST_MP3_COLUMN_PLAYLIST_ID
                + "," + Constant.DatabaseColumn.PLAYLIST_MP3_COLUMN_SERVER_ID + "," + Constant.DatabaseColumn.PLAYLIST_MP3_COLUMN_TITLE
                + "," + Constant.DatabaseColumn.PLAYLIST_MP3_COLUMN_ARTIST_NAME + "," + Constant.DatabaseColumn.PLAYLIST_MP3_COLUMN_COVER_URL
                + "," + Constant.DatabaseColumn.PLAYLIST_MP3_COLUMN_MEDIA_URL + ") VALUES (%s,%s,%s,%s,%s,%s)";
    }

    @Override
    public String delete() {
        return "DELETE FROM " + Constant.DatabaseColumn.PLAYLIST_MP3_TABLE_NAME + " WHERE " + Constant.DatabaseColumn.PLAYLIST_MP3_COLUMN_ID + "=" + "%s";
    }

    @Override
    public String retrieveSpecificType() {
        return null;
    }


    @Override
    public String retrieveSpecificTags() {
        return "SELECT * FROM " + Constant.DatabaseColumn.PLAYLIST_MP3_TABLE_NAME + " WHERE " + Constant.DatabaseColumn.PLAYLIST_MP3_COLUMN_PLAYLIST_ID + " =%s";

    }

    @Override
    public String deleteSpecificDownload() {
        return null;
    }
}
