package com.m1kes.expressscript.utils.parsers;

import com.m1kes.expressscript.objects.MedicalAid;
import com.m1kes.expressscript.objects.Message;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.ArrayList;
import java.util.List;

public class MessageJsonParser {

    public static List<Message> getMessages(String data){
        List<Message> messages = new ArrayList<>();
        try {
            Object obj = new JSONParser().parse(data);

            JSONArray jsonArray = (JSONArray)obj;
            for (Object o : jsonArray) {
                Message medicalAid = Message.fromJsonObject((JSONObject) o);

                messages.add(medicalAid);
            }

        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
        return messages;
    }


}
