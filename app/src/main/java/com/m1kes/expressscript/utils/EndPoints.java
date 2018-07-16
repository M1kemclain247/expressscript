package com.m1kes.expressscript.utils;

public class EndPoints {

    private final static String PUBLIC_URL = "http://196.27.115.46:9002/";

    private final static String TEST_URL = "http://m1kemclain247.ddns.net:9696/";

    public static final String API_URL = TEST_URL;
    public static final String API_CHECK_QUOTE = "api/Transaction/GetQuoteStatus/";
    public static final String URL_MEDICAL_AID = "api/MedicalAids/GetAll/";
    public static final String API_ASSIGN_MEDICAL_AID = "api/Client/AddMedicalAid/";
    public static final String API_SIGNUP_URL = "api/Client/Register";
    public static final String API_GET_ALL_MESSAGES = "api/Chat/GetMessages/";
    public static final String API_SEND_CHAT = "api/Chat/Send";
    public static final String API_CHECK_ORDER_STATUS = "api/Transaction/GetOrderStatus/";
    public static final String API_CRATE_ORDER = "api/Order/MakeOrder";
    public static final String API_CREATE_QUOTE_IMAGE = "api/Transaction/Quotefile";
    public static final String API_CREATE_QUOTE_TEXT = "api/Transaction/QuoteText";
    public static final String API_SEND_MESSAGE = "api/Chat/Send";

}
