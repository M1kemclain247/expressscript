package com.m1kes.expressscript;

import android.app.DialogFragment;
import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.m1kes.expressscript.adapters.recyclerview.MedicalAidAdapter;
import com.m1kes.expressscript.adapters.recyclerview.decoration.RecyclerItemDivider;
import com.m1kes.expressscript.adapters.recyclerview.medicalaids.SelectableMedicalAid;
import com.m1kes.expressscript.adapters.recyclerview.medicalaids.adapters.SelectableMedicalAidAdapter;
import com.m1kes.expressscript.adapters.recyclerview.medicalaids.objects.SelectableMedicalAidViewHolder;
import com.m1kes.expressscript.dialogs.fragments.AssignMedicalAidFragment;
import com.m1kes.expressscript.objects.MedicalAid;
import com.m1kes.expressscript.sqlite.adapters.MedicalAidDBAdapter;
import com.m1kes.expressscript.sqlite.adapters.UserMedicalAidDBAdapter;
import com.m1kes.expressscript.sqlite.tables.UserMedicalAid;
import com.m1kes.expressscript.storage.ClientIDManager;
import com.m1kes.expressscript.utils.CoreUtils;
import com.m1kes.expressscript.utils.EndPoints;
import com.m1kes.expressscript.utils.WebUtils;
import com.m1kes.expressscript.utils.parsers.MedicalAidJsonParser;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MedicalAidSelection extends AppCompatActivity implements SelectableMedicalAidViewHolder.OnItemSelectedListener , AssignMedicalAidFragment.ClickListener {

    private Context context;

    @BindView(R.id.recyclerAssignMedicalAid) RecyclerView recyclerView;

    private SelectableMedicalAidAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private List<MedicalAid> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medical_aid_selection);
        ButterKnife.bind(this);
        this.context = this;
        CoreUtils.setupActionBar("Assign Medical Aid",this);

        this.data = MedicalAidDBAdapter.getAll(context);

        WebUtils.SimpleHttpURLWebRequest request = WebUtils.getSimpleHttpRequest(new WebUtils.OnResponseCallback() {
            @Override
            public void onSuccess(String response) {
               List<MedicalAid> items = MedicalAidJsonParser.getMedicalAid(response);
                if(items == null || items.isEmpty()){
                    Toast.makeText(context,"Unable to connect, check your Internet Connection!", Toast.LENGTH_LONG).show();
                    return;
                }

                if(data.isEmpty()) {
                    //Setup persistant storage
                    MedicalAidDBAdapter.refill(items, context);
                }else{
                    //Just update the new records.
                    for(MedicalAid aid : data){
                        MedicalAidDBAdapter.update(aid,context);
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

        request.execute(EndPoints.API_URL + EndPoints.URL_MEDICAL_AID + ClientIDManager.getClientID(context));
        setupRecyclerView();
    }


    private void setupRecyclerView(){

        adapter = new SelectableMedicalAidAdapter(this,data,true,context);
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
                assignMedicalAid();
                break;
        }
        return true;
    }

    public void assignMedicalAid(){
        AssignMedicalAidFragment dialog = new AssignMedicalAidFragment();
        dialog.show(getFragmentManager(), "AssignMedicalAid");
    }


    @Override
    public void positiveClick(DialogFragment dialog, EditText editText) {

        if(current == null){
            Toast.makeText(context,"An error occured!", Toast.LENGTH_LONG).show();
            return;
        }

        String medicalAidNo = editText.getText().toString();
        Toast.makeText(context,"Medical Aid No :" + medicalAidNo,Toast.LENGTH_SHORT).show();
        dialog.dismiss();

        MedicalAidDBAdapter.update(new MedicalAid(current.getId(),current.getName(),true),context);

        WebUtils.SimpleHttpURLWebRequest request = WebUtils.getSimpleHttpRequest(new WebUtils.OnResponseCallback() {
            @Override
            public void onSuccess(String response) {
                Toast.makeText(context,"Assigned Medical Aid Successfully!",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailed() {
                Toast.makeText(context,"Unable to connect, check your Internet Connection!", Toast.LENGTH_LONG).show();
            }
        });

        request.execute(EndPoints.API_URL + EndPoints.API_ASSIGN_MEDICAL_AID + ClientIDManager.getClientID(context) + "/" + current.getId() + "/" + medicalAidNo);

        System.out.println("Finishing Activity!");
        finishActivity(MedicalAidActivity.REQUEST_ASSIGNED);
        finish();

    }

    @Override
    public void negativeClick(DialogFragment dialog) {
        Toast.makeText(context,"Cancelled !",Toast.LENGTH_SHORT).show();
        dialog.dismiss();
    }


    private SelectableMedicalAid current;

    @Override
    public void onItemSelected(SelectableMedicalAid item) {
        current = item;
        Snackbar.make(recyclerView,"Selected " + item.getName() ,Snackbar.LENGTH_LONG).show();
    }
}
