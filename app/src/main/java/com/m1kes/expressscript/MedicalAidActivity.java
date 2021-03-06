package com.m1kes.expressscript;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.m1kes.expressscript.adapters.recyclerview.MedicalAidAdapter;
import com.m1kes.expressscript.adapters.recyclerview.decoration.RecyclerItemDivider;
import com.m1kes.expressscript.objects.MedicalAid;
import com.m1kes.expressscript.sqlite.adapters.MedicalAidDBAdapter;
import com.m1kes.expressscript.storage.ClientIDManager;
import com.m1kes.expressscript.utils.CoreUtils;
import com.m1kes.expressscript.utils.EndPoints;
import com.m1kes.expressscript.utils.WebUtils;
import com.m1kes.expressscript.utils.parsers.MedicalAidJsonParser;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MedicalAidActivity extends AppCompatActivity {

    private Context context;

    @BindView(R.id.recylerMedicalAid) RecyclerView recyclerView;

    private MedicalAidAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private List<MedicalAid> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medical_aid);
        ButterKnife.bind(this);
        context = this;
        CoreUtils.setupActionBar("Medical Aid",this);
        setupRecyclerView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_medical_aid, menu);
        return true;
    }

    public static final int REQUEST_ASSIGNED = 45;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.action_add_medical_aid:
                //TODO Code to add medical aid
                Intent i = new Intent(context,MedicalAidSelection.class);
                startActivityForResult(i,REQUEST_ASSIGNED);
                break;
        }
        return true;
    }

    private void setupRecyclerView(){
        List<MedicalAid> aids = MedicalAidDBAdapter.getAssigned(true,context);
        if(aids == null || aids.isEmpty())return;

        System.out.println("Size of Assigned Medical Aids : " + aids.size());

        adapter = new MedicalAidAdapter(this,aids);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new RecyclerItemDivider(this));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        System.out.println("Just picked up activity finished");
        // Check which request we're responding to
        if (requestCode == REQUEST_ASSIGNED) {
            System.out.println("This is the correct Request code");
                    setupRecyclerView();

        }
    }

}
