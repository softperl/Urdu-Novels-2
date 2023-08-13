package com.softperl.urdunovelscollections.DatabaseUtil;

import android.database.DatabaseUtils;

import com.softperl.urdunovelscollections.ConstantUtil.Constant;
import com.softperl.urdunovelscollections.InterfaceUtil.SqlQueries;
import com.softperl.urdunovelscollections.ObjectUtil.DataObject;
import com.softperl.urdunovelscollections.Utility.Utility;

public class QueryFactory extends DbQuery {


    public QueryFactory() {
        Utility.Logger(QueryFactory.class.getName(), "Setting : Working");
    }


    /**
     * <p>It is used to get Required Formmatted for getting required data</p>
     *
     * @param databaseObject
     * @return
     */
    public String getRequiredFormattedQuery(DatabaseObject databaseObject) {

        SqlQueries sqlQueries = null;
        String formattedQuery = null;
        DataObject dataObject = null;

        if (databaseObject.getDataObject() == null)
            return "null";
        else
            dataObject = databaseObject.getDataObject();

        if (databaseObject.getTypeOperation() == Constant.TYPE.FAVOURITES) {

            sqlQueries = getFavouriteQueries();

            if (databaseObject.getDbOperation() == Constant.DB.RETRIEVE) {

                formattedQuery = sqlQueries.retrieving();

            } else if (databaseObject.getDbOperation() == Constant.DB.INSERT) {

                formattedQuery = String.format(sqlQueries.insert()
                        , sqlString(dataObject.getTitle())
                        , sqlString(dataObject.getOriginalUrl())
                        , sqlString(dataObject.getArtistName())
                        , sqlString(dataObject.getBookUrl())
                        , sqlString(dataObject.getId())
                        , sqlString(dataObject.getUserId())
                        , sqlString(dataObject.getTwitterUrl()));

            } else if (databaseObject.getDbOperation() == Constant.DB.DELETE) {

                formattedQuery = String.format(sqlQueries.delete()
                        , sqlString(dataObject.getId())
                        , sqlString(dataObject.getUserId()));

            } else if (databaseObject.getDbOperation() == Constant.DB.SPECIFIC_BOOK) {

                formattedQuery = String.format(sqlQueries.retrieveSpecificTags()
                        , sqlString(dataObject.getPostType()));

            } else if (databaseObject.getDbOperation() == Constant.DB.UPDATE) {

                formattedQuery = String.format(sqlQueries.update()
                        , sqlString(dataObject.getType())
                        , sqlString(dataObject.getId()));

            } else if (databaseObject.getDbOperation() == Constant.DB.SPECIFIC_TYPE) {

                formattedQuery = String.format(sqlQueries.retrieveSpecificType()
                        , sqlString(dataObject.getId())
                        , sqlString(dataObject.getUserId()));

            } else if (databaseObject.getDbOperation() == Constant.DB.DELETE_FAVOURITES) {

                formattedQuery = String.format(sqlQueries.deleteSpecificDownload()
                        , sqlString(dataObject.getUserId()));

            }

        } else if (databaseObject.getTypeOperation() == Constant.TYPE.FILE_READING_STATUS) {

            sqlQueries = getFileQueries();

            if (databaseObject.getDbOperation() == Constant.DB.RETRIEVE) {

                formattedQuery = sqlQueries.retrieving();

            } else if (databaseObject.getDbOperation() == Constant.DB.UPDATE) {

                formattedQuery = String.format(sqlQueries.update()
                        , sqlString(dataObject.getCurrentPage())
                        , sqlString(dataObject.getId()));

            } else if (databaseObject.getDbOperation() == Constant.DB.INSERT) {

                formattedQuery = String.format(sqlQueries.insert()
                        , sqlString(dataObject.getTitle())
                        , sqlString(dataObject.getFileType())
                        , sqlString(dataObject.getBookUrl())
                        , sqlString(dataObject.getCoverUrl())
                        , sqlString(dataObject.getBookPage())
                        , sqlString(dataObject.getCurrentPage()));

            } else if (databaseObject.getDbOperation() == Constant.DB.DELETE) {

                formattedQuery = String.format(sqlQueries.delete()
                        , sqlString(dataObject.getId()));

            } else if (databaseObject.getDbOperation() == Constant.DB.SPECIFIC_BOOK) {

                formattedQuery = String.format(sqlQueries.retrieveSpecificTags()
                        , sqlString(dataObject.getBookUrl())
                        , sqlString(dataObject.getFileType()));

            } else if (databaseObject.getDbOperation() == Constant.DB.SPECIFIC_TYPE) {

                formattedQuery = String.format(sqlQueries.retrieveSpecificType(),
                        sqlString(dataObject.getFileType()));

            } else if (databaseObject.getDbOperation() == Constant.DB.SPECIFIC_BOOK_BY_NAME) {

                formattedQuery = String.format(sqlQueries.deleteSpecificDownload()
                        , sqlString(dataObject.getTitle())
                        , sqlString(dataObject.getFileType()));

            }

        } else if (databaseObject.getTypeOperation() == Constant.TYPE.DOWNLOAD) {

            sqlQueries = getDownloadQueries();

            if (databaseObject.getDbOperation() == Constant.DB.RETRIEVE) {

                formattedQuery = sqlQueries.retrieving();

            } else if (databaseObject.getDbOperation() == Constant.DB.UPDATE) {

                formattedQuery = String.format(sqlQueries.update()
                        , sqlString(dataObject.getHistoryUrl())
                        , sqlString(dataObject.getHistoryId()));

            } else if (databaseObject.getDbOperation() == Constant.DB.INSERT) {

                formattedQuery = String.format(sqlQueries.insert()
                        , sqlString(dataObject.getTitle())
                        , sqlString(dataObject.getArtistName())
                        , sqlString(dataObject.getFileType())
                        , sqlString(dataObject.getCoverUrl())
                        , sqlString(dataObject.getBookUrl()));

            } else if (databaseObject.getDbOperation() == Constant.DB.DELETE) {

                formattedQuery = String.format(sqlQueries.delete()
                        , sqlString(dataObject.getId()));

            } else if (databaseObject.getDbOperation() == Constant.DB.SPECIFIC_BOOK) {

                formattedQuery = String.format(sqlQueries.retrieveSpecificTags()
                        , sqlString(dataObject.getId()));

            } else if (databaseObject.getDbOperation() == Constant.DB.SPECIFIC_TYPE) {

                formattedQuery = String.format(sqlQueries.retrieveSpecificType(),
                        sqlString(dataObject.getFileType()));

            }

        } else if (databaseObject.getTypeOperation() == Constant.TYPE.HISTORY) {

            sqlQueries = getMp3Queries();

            if (databaseObject.getDbOperation() == Constant.DB.RETRIEVE) {

                formattedQuery = sqlQueries.retrieving();

            } else if (databaseObject.getDbOperation() == Constant.DB.UPDATE) {

                formattedQuery = String.format(sqlQueries.update(), sqlString(dataObject.getHistoryUrl())
                        , sqlString(dataObject.getHistoryId()));

            } else if (databaseObject.getDbOperation() == Constant.DB.INSERT) {

                formattedQuery = String.format(sqlQueries.insert(), sqlString(dataObject.getPlaylistId())
                        , sqlString(dataObject.getId())
                        , sqlString(dataObject.getTitle())
                        , sqlString(dataObject.getArtistName())
                        , sqlString(dataObject.getCoverUrl())
                        , sqlString(dataObject.getBookUrl()));

            } else if (databaseObject.getDbOperation() == Constant.DB.DELETE) {

                formattedQuery = String.format(sqlQueries.delete(), sqlString(dataObject.getId()));

            } else if (databaseObject.getDbOperation() == Constant.DB.SPECIFIC_BOOK) {

                formattedQuery = String.format(sqlQueries.retrieveSpecificTags()
                        , sqlString(dataObject.getId())
                        , sqlString(dataObject.getFileType()));

            }

        }


        return formattedQuery;
    }


    /**
     * <p>It is used to convert String into
     * Database friendly String </p>
     *
     * @param data
     * @return
     */
    private String sqlString(String data) {
        String sql = null;

        if (Utility.isEmptyString(data))
            sql = "null";
        else {
            sql = DatabaseUtils.sqlEscapeString(data);
        }

        return sql;

    }

}
