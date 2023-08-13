package com.softperl.urdunovelscollections.DatabaseUtil;

import com.softperl.urdunovelscollections.ConstantUtil.Constant;
import com.softperl.urdunovelscollections.InterfaceUtil.SqlQueries;
import com.softperl.urdunovelscollections.Utility.Utility;

public class FileQueries implements SqlQueries {


    public FileQueries() {
        Utility.Logger(FileQueries.class.getName(), "Setting : Working");
    }

    @Override
    public String retrieving() {
        return "SELECT * FROM " + Constant.DatabaseColumn.FILE_TABLE_NAME + " order by id desc";
    }

    @Override
    public String update() {
        return "UPDATE " + Constant.DatabaseColumn.FILE_TABLE_NAME + " SET "
                + Constant.DatabaseColumn.FILE_READ_PAGES_COLUMN + "=" + "%s"
                + " WHERE " + Constant.DatabaseColumn.FILE_ID_COLUMN + "=" + "%s";

    }

    @Override
    public String insert() {
        return "INSERT INTO " + Constant.DatabaseColumn.FILE_TABLE_NAME
                + "(" + Constant.DatabaseColumn.FILE_TITLE_COLUMN
                + "," + Constant.DatabaseColumn.FILE_TYPE_COLUMN
                + "," + Constant.DatabaseColumn.FILE_URL_COLUMN
                + "," + Constant.DatabaseColumn.FILE_COVER_COLUMN
                + "," + Constant.DatabaseColumn.FILE_PAGES_COLUMN
                + "," + Constant.DatabaseColumn.FILE_READ_PAGES_COLUMN
                + ") VALUES (%s,%s,%s,%s,%s,%s)";
    }

    @Override
    public String delete() {
        return "DELETE FROM " + Constant.DatabaseColumn.FILE_TABLE_NAME + " WHERE "
                + Constant.DatabaseColumn.FILE_ID_COLUMN + "=" + "%s";
    }

    @Override
    public String retrieveSpecificType() {
        return "SELECT * FROM " + Constant.DatabaseColumn.FILE_TABLE_NAME + " WHERE "
                + Constant.DatabaseColumn.FILE_TYPE_COLUMN + "=" + "%s";
    }


    @Override
    public String retrieveSpecificTags() {
        return "SELECT * FROM " + Constant.DatabaseColumn.FILE_TABLE_NAME + " WHERE "
                + Constant.DatabaseColumn.FILE_URL_COLUMN + " =%s AND "
                + Constant.DatabaseColumn.FILE_TYPE_COLUMN + " =%s";

    }

    @Override
    public String deleteSpecificDownload() {
        return "SELECT * FROM " + Constant.DatabaseColumn.FILE_TABLE_NAME + " WHERE "
                + Constant.DatabaseColumn.FILE_TITLE_COLUMN + " =%s AND "
                + Constant.DatabaseColumn.FILE_TYPE_COLUMN + " =%s";
    }
}
