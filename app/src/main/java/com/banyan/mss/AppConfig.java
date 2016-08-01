package com.banyan.mss;

import java.util.ArrayList;

/**
 * Created by Sangavi on 5/21/2016.
 */
public class AppConfig {

    /*Register Email user*/
   // public static final String url_from_list = "http://www.mssbus.com/api/app/?GetStationList&userId=mssapp&password=india44&format=json";

    public static final String url_from_list ="http://www.mssbus.com/api/app/?GetStationList&Appkey=HaPpY&format=json";
    public static String url_login = null;
    public static String url_register = null;
    public static String url_forgot_psw = null;
    public static final String url_fromTo_list = "http://www.mssbus.com/api/app/?GetFromToStationIdList&Appkey=HaPpY&format=json";

    public static String url_boarding_list = null;

    public static String url_trip_list = null;

    public static String url_trip_details = null;

    public static int from_id = 0;

    public static int to_id = 0;

    public static int schedule_Id = 0;

    public static String day = null;

    public static String month = null;

    public static String from = null;

    public static String to = null;

    public static String year = null;

    public static String date = null;

    public static int fare1 = 0;

    public static int tax1 = 0;

    public static int ticket_count = 0;

    public static String total_fare = null;

    public static String jsonurl = null;

    public static String bookingId = null;

    public static String email = null;
    public static String mobile = null;

    public static ArrayList<String> seats = new ArrayList<String>();
    public static int boardingid = 0;
    public static String name = null;
    public static String age = null;
    public static String gender = null;
    public static String name2 = null;
    public static String age2 = null;
    public static String gender2 = null;
    public static String name3 = null;
    public static String age3 = null;
    public static String gender3 = null;
    public static String name4 = null;
    public static String age4 = null;
    public static String gender4 = null;
    public static String name5 = null;
    public static String age5 = null;
    public static String gender5 = null;
    public static String name6 = null;
    public static String age6 = null;
    public static String gender6 = null;
    public static String pnr = null;
    public static String note = null;
    public static String travelsPhoneNbr = null;
    public static String cancellationDescList = null;

    public static String user_id = null;
    public static String user_phone = null;


}