package com.m1kes.expressscript;

import android.content.Context;
import android.content.Intent;
import android.os.PersistableBundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;

import com.m1kes.expressscript.adapters.recyclerview.MedicalAidAdapter;
import com.m1kes.expressscript.adapters.recyclerview.MessageAdapter;
import com.m1kes.expressscript.adapters.recyclerview.decoration.RecyclerItemDivider;
import com.m1kes.expressscript.objects.MedicalAid;
import com.m1kes.expressscript.objects.Message;
import com.m1kes.expressscript.sqlite.adapters.MessagesDBAdapter;
import com.m1kes.expressscript.utils.CoreUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MessagesActivity extends AppCompatActivity {

    @BindView(R.id.btnCreateMessage)FloatingActionButton actionButton;
    @BindView(R.id.messagesRecycler)RecyclerView messagesRecycler;

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

        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context,NewMessageActivity.class));
            }
        });

        this.data = MessagesDBAdapter.getAll(context);

        setupRecyclerView();

    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onRestoreInstanceState(savedInstanceState, persistentState);
        setupRecyclerView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setupRecyclerView();
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
        messagesRecycler.setHasFixedSize(true);
        messagesRecycler.setAdapter(adapter);
        messagesRecycler.addItemDecoration(new RecyclerItemDivider(this));
    }

}
