package com.m1kes.expressscript;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.m1kes.expressscript.utils.CoreUtils;

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
                Toast.makeText(context,"Message Sent",Toast.LENGTH_SHORT).show();
                break;
        }
        return true;
    }

}
