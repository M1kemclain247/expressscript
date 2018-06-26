package com.m1kes.expressscript;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.m1kes.expressscript.objects.MedicalAid;
import com.m1kes.expressscript.storage.ClientIDManager;
import com.m1kes.expressscript.utils.EndPoints;
import com.m1kes.expressscript.utils.WebUtils;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.net.NetworkInterface;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.BindView;

public class RegistrationActivity extends AppCompatActivity {

    private static final String TAG = "RegistrationActivity";

    @BindView(R.id.input_first_name) EditText _firstName;
    @BindView(R.id.input_last_name) EditText _lastName;
    @BindView(R.id.input_email) EditText _emailText;
    @BindView(R.id.input_cell_no) EditText _cellNo;
    @BindView(R.id.male) RadioButton _male;
    @BindView(R.id.female) RadioButton _female;
    @BindView(R.id.gender_radiogroup)RadioGroup genderRadioGroup;
    @BindView(R.id.btn_signup) Button _signupButton;
    @BindView(R.id.birthday_edittext)EditText birthday_edittext ;

    private DatePickerDialog datePickerDialog;
    private Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        ButterKnife.bind(this);
        context = this;

        _signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });

        prepareDatePickerDialog();

        birthday_edittext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog.show();
            }
        });
    }


    public void signup() {
        Log.d(TAG, "Signup");

        if (!validate()) {
            onSignupFailed();
            return;
        }

        _signupButton.setEnabled(false);

        final ProgressDialog dialog = new ProgressDialog(RegistrationActivity.this,
                R.style.AppTheme_Dark_Dialog);
        dialog.setIndeterminate(true);
        dialog.setMessage("Creating Account...");
        dialog.show();

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        WebUtils.JsonWebPost webPost = WebUtils.postJsonRequest(RegistrationActivity.this, new WebUtils.OnResponseCallback() {
                            @Override
                            public void onSuccess(String response) {
                                System.out.println("Response is : " + response);

                                try {
                                    Object obj = new JSONParser().parse(response);

                                    JSONObject jsonResponse = (JSONObject)obj;
                                    String status = (String)jsonResponse.get("Status");
                                    String id_Str = (String)jsonResponse.get("Message");

                                    if(status.equalsIgnoreCase("success")){

                                        int id = Integer.parseInt(id_Str);

                                        ClientIDManager.setClientId(context,id);

                                        //Successfully Registered
                                        System.out.println("Successfully Registered!");

                                        Toast.makeText(context,"Successfully Registered!",Toast.LENGTH_LONG).show();
                                        onSignupSuccess();
                                    }else{

                                        System.out.println("Failed to register!");
                                        Toast.makeText(context,"Failed to register!",Toast.LENGTH_LONG).show();
                                        onSignupFailed();
                                    }


                                } catch (ParseException e) {
                                    e.printStackTrace();
                                    System.out.println("Failed to register!");
                                    Toast.makeText(context,"Failed to register!",Toast.LENGTH_LONG).show();
                                    onSignupFailed();
                                }

                            }

                            @Override
                            public void onFailed() {
                                System.out.println("Failed to register!");
                                Toast.makeText(context,"Failed to register!",Toast.LENGTH_LONG).show();
                                onSignupFailed();
                            }

                            @Override
                            public void onCompleteTask() {

                            }
                        },dialog);

                        Map<String,String> params = new HashMap<>();

                        String first_name = _firstName.getText().toString();
                        String last_name = _lastName.getText().toString();
                        String email = _emailText.getText().toString();
                        String cellNo = _cellNo.getText().toString();
                        String birthday = birthday_edittext.getText().toString();
                        RadioButton selectedRadioButton = findViewById(genderRadioGroup.getCheckedRadioButtonId());
                        String gender= selectedRadioButton == null ? "":selectedRadioButton.getText().toString();

                        params.put("Firstname",first_name);
                        params.put("Surname",last_name);
                        params.put("MAC",getMacAddress());
                        params.put("Cell",cellNo);
                        params.put("Email",email);
                        params.put("DOB",birthday);
                        params.put("Gender",gender);

                        webPost.execute(EndPoints.API_URL + EndPoints.API_SIGNUP_URL ,params);

                    }
                }, 1000);
    }

    public static String getMacAddr() {
        try {
            List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface nif : all) {
                if (!nif.getName().equalsIgnoreCase("wlan0")) continue;

                byte[] macBytes = nif.getHardwareAddress();
                if (macBytes == null) {
                    return "";
                }

                StringBuilder res1 = new StringBuilder();
                for (byte b : macBytes) {
                    res1.append(Integer.toHexString(b & 0xFF) + ":");
                }

                if (res1.length() > 0) {
                    res1.deleteCharAt(res1.length() - 1);
                }
                return res1.toString();
            }
        } catch (Exception ex) {
            //handle exception
        }
        return "";
    }

    @SuppressLint("HardwareIds")
    public String getMacAddress(){
        String finalAddress = "";
        WifiManager manager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if(manager != null) {
            WifiInfo info = manager.getConnectionInfo();
            if(info != null) {
                finalAddress = info.getMacAddress();
            }
        }


        if(finalAddress == null || finalAddress.equals("")){
            return getMacAddr();
        }else{
            return finalAddress;
        }

    }

    private void prepareDatePickerDialog() {
        Calendar calendar=Calendar.getInstance();

        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                //When a date is selected, it comes here.
                //Change birthdayEdittext's text and dismiss dialog.
                birthday_edittext.setText(year+"-"+monthOfYear+"-"+dayOfMonth);
                datePickerDialog.dismiss();
            }
        },calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH));
    }


    public void onSignupSuccess() {

        _signupButton.setEnabled(true);
        setResult(RESULT_OK, null);
        finish();

        startActivity(new Intent(context,MenuActivity.class));
    }

    public void onSignupFailed() {
        Toast.makeText(context, "Registration failed!, Please try again", Toast.LENGTH_LONG).show();
        _signupButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String first_name = _firstName.getText().toString();
        String last_name = _lastName.getText().toString();
        String email = _emailText.getText().toString();

        if (first_name.isEmpty() || first_name.length() < 3) {
            _firstName.setError("at least 3 characters");
            valid = false;
        } else {
            _firstName.setError(null);
        }

        if (last_name.isEmpty() || last_name.length() < 3) {
            _lastName.setError("at least 3 characters");
            valid = false;
        } else {
            _lastName.setError(null);
        }

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("enter a valid email address");
            valid = false;
        } else {
            _emailText.setError(null);
        }
        return valid;
    }

}
