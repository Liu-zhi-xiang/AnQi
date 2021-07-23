package com.motorbike.anqi.util;

import android.content.Context;

/**
 * Created by Administrator on 2017/11/11.
 */
public class UserPreference extends BaseSharePreference {
    public static final String USER = "user";
    public static final String USER_ID = "user_id";
    public static final String USER_ROOM_NO = "ROOM_NO";
    public static final String USER_GONGHAO = "user_NICK_name";
    public static final String CarType = "CarType";



    public static final String ACCID = "accid";

    public static final String AREA = "area";



    public static final String LEVEL = "level";

    public static final String ryToken = "ryToken";
    public static final String ADDRESS = "address";


    public static final String TIME="time";

    public static final String TITLE="title";

    public static final String TOKEN="token";


    public static final String JSON="json";

    public static final String LoginPhone="loginPhone";
    private static volatile UserPreference userPreference;
    private static final String FIRST_LOGIN="first_login";

    private static final String JPUSH_ID="jpush_id";
    private static final String STARTTIME="startTime";//开始时间
    private static final String ENDTIME="endTime";//结束时间

    private static final String heardImage="heardImage";//
    private static final String GOODADDRESS="good_address";//党办电话
    private static final String PARTY_ADDRES="party_addres";//党办地址

    private static final String ROOM_NO_CURRENT="room_no_current";

    private static final String mode="mode";//
    private static final String ringmode="ringmode";//


    protected UserPreference(Context context)
    {
        super(context, USER);
    }

    public static UserPreference getUserPreference(Context context) {

            if (userPreference == null) {
                synchronized (UserPreference.class) {
                    if (userPreference==null) {
                        userPreference = new UserPreference(context);
                    }
                }
            }
        return userPreference;
    }

    public void setUserNickname(String userType) {
        putString(USER_GONGHAO, userType);
    }

    public String getUserNickname() {
        return getString(USER_GONGHAO, "");
    }



    public void setUserRoomNo(String userType) {
        putString(USER_ROOM_NO, userType);
    }

    public String getUserRoomNo() {
        return getString(USER_ROOM_NO, "");
    }
    public String getCarType() {
        return getString(CarType, "");
    }
    public void setCarType(String carType) {
        putString(CarType, carType);
    }

    public String getAddress() {
        return getString(ADDRESS, "");
    }
    public void setAddress(String userType) {
        putString(ADDRESS, userType);
    }

//    public void setLodingType(String lodingType) {
//        putString(LODING_TYPE, lodingType);
//    }
//    public String getLodingType(){
//        return getString(LODING_TYPE, "0");
//    }

    public void setUserId(String content) {
        putString(USER_ID, content);
    }
    public String getUserId()
    {
        return getString(USER_ID, "");
    }
    public void setJpushId(String companyName){
        putString(JPUSH_ID,companyName);
    }
    public String getJpushId(){
        return getString(JPUSH_ID,"");
    }
    public void setArea(String companyId){
        putString(AREA,companyId);
    }
    public String getArea(){
        return getString(AREA,"");
    }
    public String getTime(){
        return getString(TIME, null);
    }
    public void setTime(String content) {
        putString(TIME, content);
    }
    public String getTitle(){
        return getString(TITLE, null);
    }
    public void setTitle(String content) {
        putString(TITLE, content);
    }

    public String getToken()
    {
        return getString(TOKEN, "token");
    }
    public  void setToken(String content){
        putString(TOKEN, content);
    }

    public String getJson(){
        return getString(JSON, "json");
    }
    public  void setJson(String content){
        putString(JSON, content);
    }

    public String getLevel(){
        return getString(LEVEL, "1");
    }
    public  void setLevel(String content){
        putString(LEVEL, content);
    }
    public String getLoginPhone(){
        return getString(LoginPhone, null);
    }
    public  void setLoginPhone(String content)
    {
        putString(LoginPhone, content);
    }
    public void setFirstLogin(boolean firstLogin){
        putBoolean(FIRST_LOGIN,firstLogin);
    }

    public boolean getFirstLogin() {
        return getBoolean(FIRST_LOGIN,false);
    }


    public void setRyToken(String j) {
        putString(ryToken,j);
    }
    public String getRyToken(){
        return getString(ryToken,"");
    }

    public String getStarttime(){
        return getString(STARTTIME,null);
    }

    public void setStarttime(String j) {
        putString(STARTTIME,j);
    }
    public String getEndtime(){
        return getString(ENDTIME,null);
    }

    public void setEndtime(String j) {
        putString(ENDTIME,j);
    }
    public String getHeardImage(){
        return getString(heardImage,null);
    }

    public void setHeardImage(String j) {
        putString(heardImage,j);
    }
    public String getAccid(){
        return getString(ACCID,"");
    }

    public void setAccid(String j) {
        putString(ACCID,j);
    }

    public String getGoodaddress(){
        return getString(GOODADDRESS,"");
    }

    public void setGoodaddress(String j) {
        putString(GOODADDRESS,j);
    }

    public  String getRoomNoCurrent(){
        return getString(ROOM_NO_CURRENT,"");
    }
    public void setRoomNoCurrent(String noname){
        putString(ROOM_NO_CURRENT,noname);
    }

    public int getRingmode(){
        return getInt(ringmode,0);
    }

    public void setRingmode(int j) {
        putInt(ringmode,j);
    }
    public int getMode(){
        return getInt(mode,0);
    }

    public void setMode(int j) {
        putInt(mode,j);
    }


}
