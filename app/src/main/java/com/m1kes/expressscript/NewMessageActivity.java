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

import com.m1kes.expressscript.objects.Message;
import com.m1kes.expressscript.sqlite.adapters.MessagesDBAdapter;
import com.m1kes.expressscript.storage.ClientIDManager;
import com.m1kes.expressscript.utils.CoreUtils;
import com.m1kes.expressscript.utils.EndPoints;
import com.m1kes.expressscript.utils.WebUtils;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NewMessageActivity extends AppCompatActivity {

    private Context context;
    @BindView(R.id.input_message_content)EditText messageContent;
    @BindView(R.id.input_topic)EditText messageTopic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_message);
        CoreUtils.setupActionBar("New Message",this);
        ButterKnife.bind(this);
        context = this;



    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.message_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.action_send_message :
                showDialog();
                break;
        }
        return true;
    }

    private boolean validate(){
        return messageContent.getText().toString().length() > 0 && messageTopic.getText().toString().length() > 0;
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
                        final String message = messageContent.getText().toString();

                        WebUtils.JsonWebPost webPost = WebUtils.postJsonRequest(context, new WebUtils.OnResponseCallback() {
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
                                        MessagesDBAdapter.add(new Message(id,message),context);

                                        Toast.makeText(context,"Message has been sent!",Toast.LENGTH_LONG).show();
                                    }else{
                                        //Failed
                                        System.out.println("Response is failed ");
                                        Toast.makeText(context,"Failed to send the message!!",Toast.LENGTH_LONG).show();
                                    }


                                } catch (ParseException e) {
                                    System.out.println("Parsing error");
                                    e.printStackTrace();
                                    Toast.makeText(context,"Failed to send the message!!",Toast.LENGTH_LONG).show();
                                }

                            }

                            @Override
                            public void onFailed() {
                                System.out.println("Failed to send Message!");
                                Toast.makeText(context,"Failed to Send Message!",Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void onCompleteTask() {

                            }
                        });


                        Map<String,String> params = new HashMap<>();

                        params.put("ClientId", ClientIDManager.getClientID(context)+"");
                        params.put("Message",message);

                        System.out.println("Params :");
                        System.out.println("ClientId : "+ClientIDManager.getClientID(context)+"");
                        System.out.println("Message : " + message);

                        webPost.execute(EndPoints.API_URL + EndPoints.API_SEND_MESSAGE ,params);

                    }
                }, 1000);
    }

}
