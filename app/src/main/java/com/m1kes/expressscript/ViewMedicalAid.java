package com.m1kes.expressscript;

import android.app.DialogFragment;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.m1kes.expressscript.adapters.recyclerview.MedicalAidAdapter;
import com.m1kes.expressscript.dialogs.fragments.AssignMedicalAidFragment;
import com.m1kes.expressscript.objects.MedicalAid;
import com.m1kes.expressscript.sqlite.adapters.MedicalAidDBAdapter;
import com.m1kes.expressscript.utils.CoreUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ViewMedicalAid extends AppCompatActivity implements AssignMedicalAidFragment.ClickListener{

    private MedicalAid aid;

    @BindView(R.id.txtAidHeader)TextView header;
    @BindView(R.id.imgAidLogo)ImageView logo;
    @BindView(R.id.btnAssignMedicalAid)Button button;

    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_medical_aid);
        this.context = this;
        ButterKnife.bind(this);
        initView();
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


    private void initView(){

        try {
            this.aid = (MedicalAid) getIntent().getSerializableExtra(MedicalAidAdapter.KEY_INTENT);
            if(aid == null)
                finish();

            CoreUtils.setupActionBar(aid.getName(),this);

            header.setText(aid.getName());
            logo.setImageDrawable(getDrawable(aid.getName()));

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDialog();
                }
            });

        }catch (Exception e){
            e.printStackTrace();
            finish();
        }

    }

    public void showDialog() {
        AssignMedicalAidFragment dialog = new AssignMedicalAidFragment();
        dialog.show(getFragmentManager(), "AssignMedicalAid");
    }


    private Drawable getDrawable(String name){
        Drawable drawable = null;
        if(name.equalsIgnoreCase("cimas")){
            drawable = getResources().getDrawable(R.drawable.cimass);
        }else if(name.equalsIgnoreCase("hmmas")){
            drawable = getResources().getDrawable(R.drawable.hmmas);
        }
        return drawable;
    }

    //Fragment for the medical number to be entered into

    @Override
    public void positiveClick(DialogFragment dialog, EditText editText) {

        String medicalAidNo = editText.getText().toString();
        Toast.makeText(context,"Medical Aid No :" + medicalAidNo,Toast.LENGTH_SHORT).show();
        dialog.dismiss();

        finish();
    }

    @Override
    public void negativeClick(DialogFragment dialog) {
        Toast.makeText(context,"Cancelled !",Toast.LENGTH_SHORT).show();
        dialog.dismiss();
    }

}
