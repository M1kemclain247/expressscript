package com.m1kes.expressscript;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.m1kes.expressscript.adapters.recyclerview.MedicalAidAdapter;
import com.m1kes.expressscript.adapters.recyclerview.decoration.RecyclerItemDivider;
import com.m1kes.expressscript.objects.MedicalAid;
import com.m1kes.expressscript.sqlite.adapters.MedicalAidDBAdapter;
import com.m1kes.expressscript.utils.CoreUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MedicalAidSelection extends AppCompatActivity {

    private Context context;

    @BindView(R.id.recyclerAssignMedicalAid) RecyclerView recyclerView;

    private MedicalAidAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private List<MedicalAid> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medical_aid_selection);
        ButterKnife.bind(this);
        this.context = this;
        CoreUtils.setupActionBar("Assign Medical Aid",this);

        this.data = MedicalAidDBAdapter.getAssigned(false,context);

        setupRecyclerView();
    }



    private void setupRecyclerView(){
        adapter = new MedicalAidAdapter(this,data);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new RecyclerItemDivider(this));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_selection_medical_aid, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.action_assign_aid :

                break;
        }
        return true;
    }

}
