package com.m1kes.expressscript;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.m1kes.expressscript.adapters.recyclerview.MessageAdapter;
import com.m1kes.expressscript.objects.Message;
import com.m1kes.expressscript.objects.custom.CustomDate;
import com.m1kes.expressscript.recievers.CheckMessagesReciever;
import com.m1kes.expressscript.sqlite.adapters.MedicalAidDBAdapter;
import com.m1kes.expressscript.sqlite.adapters.MessagesDBAdapter;
import com.m1kes.expressscript.storage.ClientIDManager;
import com.m1kes.expressscript.utils.CoreUtils;
import com.m1kes.expressscript.utils.EndPoints;
import com.m1kes.expressscript.utils.WebUtils;
import com.m1kes.expressscript.utils.parsers.MedicalAidJsonParser;
import com.m1kes.expressscript.utils.parsers.MessageJsonParser;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MessagesActivity extends AppCompatActivity {


    @BindView(R.id.messagesRecycler)RecyclerView messagesRecycler;
    @BindView(R.id.txtChatBox)EditText txtChatBox;
    @BindView(R.id.btnSendChat)Button btnSendChat;

    private Context context;
    private MessageAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private List<Message> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);
        CoreUtils.setupActionBar("Messages",this);
        ButterKnife.bind(this);
        context = this;
        this.data = MessagesDBAdapter.getAll(context);

        btnSendChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });

        btnSendChat.setOnEditorActionListener(new EditText.OnEditorActionListener(){
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_NULL
                        && event.getAction() == KeyEvent.ACTION_DOWN) {
                    sendMessage();
                }
                return true;
            }
        });

        setupRecyclerView();


        checkMessages();
    }

    public void checkMessages(){

        WebUtils.SimpleHttpURLWebRequest request = WebUtils.getSimpleHttpRequest(new WebUtils.OnResponseCallback() {
            @Override
            public void onSuccess(String response) {


                List<Message> new_content = MessageJsonParser.getMessages(response);
                if(new_content == null ){
                    Toast.makeText(context,"Unable to connect, check your Internet Connection!", Toast.LENGTH_LONG).show();
                    return;
                }

                for(Message m : new_content){
                    boolean exist = false;
                    for(Message compareTo : data){
                        if(m.getContent().equalsIgnoreCase(compareTo.getContent())) exist = true;
                    }

                    if(!exist) {
                        MessagesDBAdapter.add(m, context);
                        data.add(m);
                    }
                }

                setupRecyclerView();
                Toast.makeText(context,"Updated Successfully!",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailed() {
                Toast.makeText(context,"Unable to connect, check your Internet Connection!", Toast.LENGTH_LONG).show();
            }
        });

        request.execute(EndPoints.API_URL + EndPoints.API_GET_ALL_MESSAGES + ClientIDManager.getClientID(context));



    }


    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onRestoreInstanceState(savedInstanceState, persistentState);
        updateRecycler();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateRecycler();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }

    private void setupRecyclerView(){

        adapter = new MessageAdapter(this,data);
        layoutManager = new LinearLayoutManager(this);
        messagesRecycler.setLayoutManager(layoutManager);
        messagesRecycler.setAdapter(adapter);
    }

    private void updateRecycler(){
        System.out.println("Updating Recyclerview");
        this.data = MessagesDBAdapter.getAll(context);
        adapter = new MessageAdapter(this,data);
        messagesRecycler.setAdapter(adapter);
    }

    private boolean validate(){
        return txtChatBox.getText().toString().length() > 0 && txtChatBox.getText().toString().length() > 0;
    }

    private void sendMessage() {
        if(!validate())return;

        postText();

        cleanUp();
    }

    private void cleanUp(){
        txtChatBox.setText("");

        // Check if no view has focus:
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            if(imm == null)return;
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }


    }

    public void postText(){
        final String message = txtChatBox.getText().toString();

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {

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
                                        updateRecycler();
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
                        });


                        Map<String,String> params = new HashMap<>();

                        params.put("ClientId", ClientIDManager.getClientID(context)+"");
                        params.put("Message",message);

                        System.out.println("Params :");
                        System.out.println("ClientId : "+ClientIDManager.getClientID(context)+"");
                        System.out.println("Message : " + message);

                        int id = CoreUtils.getRandomNumber(1,100000);
                        MessagesDBAdapter.add(new Message(id,message,"Me",new CustomDate()),context);

                        updateRecycler();

                        webPost.execute(EndPoints.API_URL + EndPoints.API_SEND_MESSAGE ,params);

                    }
                }, 1000);
    }

}
