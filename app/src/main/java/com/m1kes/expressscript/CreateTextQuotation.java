package com.m1kes.expressscript;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.m1kes.expressscript.storage.ClientIDManager;
import com.m1kes.expressscript.utils.CoreUtils;
import com.m1kes.expressscript.utils.EndPoints;
import com.m1kes.expressscript.utils.WebUtils;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CreateTextQuotation extends AppCompatActivity {

    private Context context;
    @BindView(R.id.txtQuotationText)EditText txtQuotationText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_text_quotation);
        CoreUtils.setupActionBar("Send Quotation",this);
        ButterKnife.bind(this);
        context = this;


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.quotation_text_send, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.action_send_text_quotation:
                showDialog();
                break;
        }
        return true;
    }

    private boolean validate(){
        if(txtQuotationText.getText().toString().length() <= 0)return false;
        else return true;
    }

    private void showDialog() {
        if(!validate())return;

        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle("Confirm");
        alertDialog.setMessage("Are you sure you would like to send this quotation?");
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        postText();
                        dialog.dismiss();
                        finish();
                    }
                });
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(context,"Cancelled",Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                        finish();
                    }
                });
        alertDialog.show();

    }

    public void postText(){

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        WebUtils.JsonWebPost webPost = WebUtils.postJsonRequest(context, new WebUtils.OnResponseCallback() {
                            @Override
                            public void onSuccess(String response) {
                                System.out.println("Response is : " + response);
                                Toast.makeText(context,"Quote has been sent Successfully!",Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void onFailed() {
                                System.out.println("Failed to register!");
                                Toast.makeText(context,"Failed to Send Quote!",Toast.LENGTH_LONG).show();
                            }
                        });

                        Map<String,String> params = new HashMap<>();

                        String message = txtQuotationText.getText().toString();

                        params.put("ClientId", ClientIDManager.getClientID(context)+"");
                        params.put("Message",message);

                        System.out.println("Params :");
                        System.out.println("ClientId : "+ClientIDManager.getClientID(context)+"");
                        System.out.println("Message : " + message);

                        webPost.execute(EndPoints.API_URL + EndPoints.API_CREATE_QUOTE_TEXT ,params);

                    }
                }, 1000);
    }





}
