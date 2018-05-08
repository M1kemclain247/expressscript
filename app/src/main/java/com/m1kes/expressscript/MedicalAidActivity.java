package com.m1kes.expressscript;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.widget.BaseAdapter;
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

import java.util.ArrayList;
import java.util.List;

public class MedicalAidActivity extends AppCompatActivity {

    private Context context;

    private RecyclerView recyclerView;
    private MedicalAidAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private List<MedicalAid> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medical_aid);
        context = this;
        CoreUtils.setupActionBar("Medical Aid",this);
        recyclerView = findViewById(R.id.recylerMedicalAid);

        WebUtils.SimpleHttpURLWebRequest request = WebUtils.getSimpleHttpRequest(new WebUtils.OnResponseCallback() {
            @Override
            public void onSuccess(String response) {

                data = MedicalAidJsonParser.getMedicalAid(response);
                //Setup persistant storage
                MedicalAidDBAdapter.refill(data,context);

                setupRecyclerView();
                Toast.makeText(context,"Updated Successfully!",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailed() {
                Toast.makeText(context,"Unable to connect, check your Internet Connection!",Toast.LENGTH_LONG).show();
            }
        });

        request.execute(EndPoints.API_URL + EndPoints.URL_MEDICAL_AID + ClientIDManager.getClientID(context));

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
        adapter = new MedicalAidAdapter(this,data);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new RecyclerItemDivider(this));
    }

}
