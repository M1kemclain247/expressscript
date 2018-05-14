package com.m1kes.expressscript;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.m1kes.expressscript.utils.CoreUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MessagesActivity extends AppCompatActivity {

    @BindView(R.id.btnCreateMessage)FloatingActionButton actionButton;

    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);
        CoreUtils.setupActionBar("Messages",this);
        ButterKnife.bind(this);
        context = this;

        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context,NewMessageActivity.class));
            }
        });

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

}
