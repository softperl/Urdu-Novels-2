package com.softperl.urdunovelscollections.ObjectUtil;

public class PrefObject {
    private String tags;
    private boolean saveTags;
    private boolean retrieveTags;

    private String nextPage;
    private boolean saveNext;
    private boolean retrieveNext;

    private int currentPosition;
    private boolean savePosition;
    private boolean retrievePosition;

    private boolean firstLaunch;
    private boolean saveFirstLaunch;
    private boolean retrieveFirstLaunch;

    private boolean isLogin;
    private boolean saveLogin;
    private boolean retrieveLogin;

    private String userId;
    private boolean saveUserId;
    private boolean retrieveUserId;

    private boolean userRemember;
    private boolean saveUserRemember;
    private boolean retrieveUserRemember;

    private String uid;
    private String loginType;
    private String userEmail;
    private String userPassword;
    private String firstName;
    private String lastName;
    private String pictureUrl;
    private boolean saveUserCredential;
    private boolean retrieveUserCredential;

    private boolean saveNewsfeed;
    private boolean retrieveNewsfeed;
    private String newsfeedId;

    private boolean saveNightMode;
    private boolean retrieveNightMode;
    private boolean isNightMode;

    private boolean saveDownloadWifi;
    private boolean retrieveDownloadWifi;
    private boolean isDownloadWifi;

    private boolean savePush;
    private boolean retrievePush;
    private boolean isPush;

    public String getUid() {
        return uid;
    }

    public PrefObject setUid(String uid) {
        this.uid = uid;
        return this;
    }

    public String getLoginType() {
        return loginType;
    }

    public PrefObject setLoginType(String loginType) {
        this.loginType = loginType;
        return this;
    }

    public boolean isSavePush() {
        return savePush;
    }

    public PrefObject setSavePush(boolean savePush) {
        this.savePush = savePush;
        return this;
    }

    public boolean isRetrievePush() {
        return retrievePush;
    }

    public PrefObject setRetrievePush(boolean retrievePush) {
        this.retrievePush = retrievePush;
        return this;
    }

    public boolean isPush() {
        return isPush;
    }

    public PrefObject setPush(boolean push) {
        isPush = push;
        return this;
    }

    public boolean isSaveDownloadWifi() {
        return saveDownloadWifi;
    }

    public PrefObject setSaveDownloadWifi(boolean saveDownloadWifi) {
        this.saveDownloadWifi = saveDownloadWifi;
        return this;
    }

    public boolean isRetrieveDownloadWifi() {
        return retrieveDownloadWifi;
    }

    public PrefObject setRetrieveDownloadWifi(boolean retrieveDownloadWifi) {
        this.retrieveDownloadWifi = retrieveDownloadWifi;
        return this;
    }

    public boolean isDownloadWifi() {
        return isDownloadWifi;
    }

    public PrefObject setDownloadWifi(boolean downloadWifi) {
        isDownloadWifi = downloadWifi;
        return this;
    }

    public boolean isSaveNightMode() {
        return saveNightMode;
    }

    public PrefObject setSaveNightMode(boolean saveNightMode) {
        this.saveNightMode = saveNightMode;
        return this;
    }

    public boolean isRetrieveNightMode() {
        return retrieveNightMode;
    }

    public PrefObject setRetrieveNightMode(boolean retrieveNightMode) {
        this.retrieveNightMode = retrieveNightMode;
        return this;
    }

    public boolean isNightMode() {
        return isNightMode;
    }

    public PrefObject setNightMode(boolean nightMode) {
        isNightMode = nightMode;
        return this;
    }

    public boolean isSaveNewsfeed() {
        return saveNewsfeed;
    }

    public PrefObject setSaveNewsfeed(boolean saveNewsfeed) {
        this.saveNewsfeed = saveNewsfeed;
        return this;
    }

    public boolean isRetrieveNewsfeed() {
        return retrieveNewsfeed;
    }

    public PrefObject setRetrieveNewsfeed(boolean retrieveNewsfeed) {
        this.retrieveNewsfeed = retrieveNewsfeed;
        return this;
    }

    public String getNewsfeedId() {
        return newsfeedId;
    }

    public PrefObject setNewsfeedId(String newsfeedId) {
        this.newsfeedId = newsfeedId;
        return this;
    }

    public String getTags() {
        return tags;
    }

    public PrefObject setTags(String tags) {
        this.tags = tags;
        return this;
    }

    public boolean isSaveTags() {
        return saveTags;
    }

    public PrefObject setSaveTags(boolean saveTags) {
        this.saveTags = saveTags;
        return this;
    }

    public boolean isRetrieveTags() {
        return retrieveTags;
    }

    public PrefObject setRetrieveTags(boolean retrieveTags) {
        this.retrieveTags = retrieveTags;
        return this;
    }

    public String getNextPage() {
        return nextPage;
    }

    public PrefObject setNextPage(String nextPage) {
        this.nextPage = nextPage;
        return this;
    }

    public boolean isSaveNext() {
        return saveNext;
    }

    public PrefObject setSaveNext(boolean saveNext) {
        this.saveNext = saveNext;
        return this;
    }

    public boolean isRetrieveNext() {
        return retrieveNext;
    }

    public PrefObject setRetrieveNext(boolean retrieveNext) {
        this.retrieveNext = retrieveNext;
        return this;
    }

    public int getCurrentPosition() {
        return currentPosition;
    }

    public PrefObject setCurrentPosition(int currentPosition) {
        this.currentPosition = currentPosition;
        return this;
    }

    public boolean isSavePosition() {
        return savePosition;
    }

    public PrefObject setSavePosition(boolean savePosition) {
        this.savePosition = savePosition;
        return this;
    }

    public boolean isRetrievePosition() {
        return retrievePosition;
    }

    public PrefObject setRetrievePosition(boolean retrievePosition) {
        this.retrievePosition = retrievePosition;
        return this;
    }

    public boolean isFirstLaunch() {
        return firstLaunch;
    }

    public PrefObject setFirstLaunch(boolean firstLaunch) {
        this.firstLaunch = firstLaunch;
        return this;
    }

    public boolean isSaveFirstLaunch() {
        return saveFirstLaunch;
    }

    public PrefObject setSaveFirstLaunch(boolean saveFirstLaunch) {
        this.saveFirstLaunch = saveFirstLaunch;
        return this;
    }

    public boolean isRetrieveFirstLaunch() {
        return retrieveFirstLaunch;
    }

    public PrefObject setRetrieveFirstLaunch(boolean retrieveFirstLaunch) {
        this.retrieveFirstLaunch = retrieveFirstLaunch;
        return this;
    }

    public boolean isLogin() {
        return isLogin;
    }

    public PrefObject setLogin(boolean login) {
        isLogin = login;
        return this;
    }

    public boolean isSaveLogin() {
        return saveLogin;
    }

    public PrefObject setSaveLogin(boolean saveLogin) {
        this.saveLogin = saveLogin;
        return this;
    }

    public boolean isRetrieveLogin() {
        return retrieveLogin;
    }

    public PrefObject setRetrieveLogin(boolean retrieveLogin) {
        this.retrieveLogin = retrieveLogin;
        return this;
    }

    public String getUserId() {
        return userId;
    }

    public PrefObject setUserId(String userId) {
        this.userId = userId;
        return this;
    }

    public boolean isSaveUserId() {
        return saveUserId;
    }

    public PrefObject setSaveUserId(boolean saveUserId) {
        this.saveUserId = saveUserId;
        return this;
    }

    public boolean isRetrieveUserId() {
        return retrieveUserId;
    }

    public PrefObject setRetrieveUserId(boolean retrieveUserId) {
        this.retrieveUserId = retrieveUserId;
        return this;
    }

    public boolean isUserRemember() {
        return userRemember;
    }

    public PrefObject setUserRemember(boolean userRemember) {
        this.userRemember = userRemember;
        return this;
    }

    public boolean isSaveUserRemember() {
        return saveUserRemember;
    }

    public PrefObject setSaveUserRemember(boolean saveUserRemember) {
        this.saveUserRemember = saveUserRemember;
        return this;
    }

    public boolean isRetrieveUserRemember() {
        return retrieveUserRemember;
    }

    public PrefObject setRetrieveUserRemember(boolean retrieveUserRemember) {
        this.retrieveUserRemember = retrieveUserRemember;
        return this;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public PrefObject setUserEmail(String userEmail) {
        this.userEmail = userEmail;
        return this;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public PrefObject setUserPassword(String userPassword) {
        this.userPassword = userPassword;
        return this;
    }

    public boolean isSaveUserCredential() {
        return saveUserCredential;
    }

    public PrefObject setSaveUserCredential(boolean saveUserCredential) {
        this.saveUserCredential = saveUserCredential;
        return this;
    }

    public boolean isRetrieveUserCredential() {
        return retrieveUserCredential;
    }

    public PrefObject setRetrieveUserCredential(boolean retrieveUserCredential) {
        this.retrieveUserCredential = retrieveUserCredential;
        return this;
    }

    public String getFirstName() {
        return firstName;
    }

    public PrefObject setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public String getLastName() {
        return lastName;
    }

    public PrefObject setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public PrefObject setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
        return this;
    }


    @Override
    public String toString() {
        return "PrefObject{" +
                "tags='" + tags + '\'' +
                ", saveTags=" + saveTags +
                ", retrieveTags=" + retrieveTags +
                ", nextPage='" + nextPage + '\'' +
                ", saveNext=" + saveNext +
                ", retrieveNext=" + retrieveNext +
                ", currentPosition=" + currentPosition +
                ", savePosition=" + savePosition +
                ", retrievePosition=" + retrievePosition +
                ", firstLaunch=" + firstLaunch +
                ", saveFirstLaunch=" + saveFirstLaunch +
                ", retrieveFirstLaunch=" + retrieveFirstLaunch +
                ", isLogin=" + isLogin +
                ", saveLogin=" + saveLogin +
                ", retrieveLogin=" + retrieveLogin +
                ", userId='" + userId + '\'' +
                ", saveUserId=" + saveUserId +
                ", retrieveUserId=" + retrieveUserId +
                ", userRemember=" + userRemember +
                ", saveUserRemember=" + saveUserRemember +
                ", retrieveUserRemember=" + retrieveUserRemember +
                ", uid='" + uid + '\'' +
                ", loginType='" + loginType + '\'' +
                ", userEmail='" + userEmail + '\'' +
                ", userPassword='" + userPassword + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", pictureUrl='" + pictureUrl + '\'' +
                ", saveUserCredential=" + saveUserCredential +
                ", retrieveUserCredential=" + retrieveUserCredential +
                ", saveNewsfeed=" + saveNewsfeed +
                ", retrieveNewsfeed=" + retrieveNewsfeed +
                ", newsfeedId='" + newsfeedId + '\'' +
                ", saveNightMode=" + saveNightMode +
                ", retrieveNightMode=" + retrieveNightMode +
                ", isNightMode=" + isNightMode +
                ", saveDownloadWifi=" + saveDownloadWifi +
                ", retrieveDownloadWifi=" + retrieveDownloadWifi +
                ", isDownloadWifi=" + isDownloadWifi +
                ", savePush=" + savePush +
                ", retrievePush=" + retrievePush +
                ", isPush=" + isPush +
                '}';
    }
}
