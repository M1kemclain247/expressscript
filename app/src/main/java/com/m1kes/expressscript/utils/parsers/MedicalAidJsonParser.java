package com.m1kes.expressscript.utils.parsers;

import com.m1kes.expressscript.objects.MedicalAid;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.ArrayList;
import java.util.List;

public class MedicalAidJsonParser {


    public static List<MedicalAid> getMedicalAid(String data){
        List<MedicalAid> medicalAids = new ArrayList<>();
        try {
            Object obj = new JSONParser().parse(data);

            JSONArray jsonArray = (JSONArray)obj;
            for (Object o : jsonArray) {
                MedicalAid medicalAid = MedicalAid.fromJsonObject((JSONObject) o);

                medicalAids.add(medicalAid);
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return medicalAids;
    }

}
