package com.m1kes.expressscript.utils;

public class EndPoints {
    private static final String ROOT_URL = "http://192.168.100.3/MyApi/Api.php?apicall=";
    public static final String UPLOAD_URL = ROOT_URL + "uploadpic";
    public static final String GET_PICS_URL = ROOT_URL + "getpics";

    public static final String API_URL = "http://196.27.115.46:9002/";

    public static final String URL_MEDICAL_AID = "api/MedicalAids/GetAll/";
    public static final String API_ASSIGN_MEDICAL_AID = "api/Client/AddMedicalAid/";
    public static final String API_SIGNUP_URL = "api/Client/Register";
    public static final String API_GET_ALL_MESSAGES = "api/Chat/GetMessages/";
    public static final String API_SEND_CHAT = "api/Chat/Send";
    public static final String API_CREATE_QUOTE_IMAGE = "api/Transaction/Quotefile";
    public static final String API_CREATE_QUOTE_TEXT = "api/Transaction/QuoteText";
    public static final String API_SEND_MESSAGE = "api/Chat/Send";

}
