package com.m1kes.expressscript;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import com.m1kes.expressscript.objects.Message;
import com.m1kes.expressscript.utils.CoreUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SendQuotation extends AppCompatActivity {

    @BindView(R.id.imgQuotationSend) ImageView imgQuotationSend;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_quotation);
        CoreUtils.setupActionBar("Send Quotation",this);
        ButterKnife.bind(this);
        context = this;


        initView();
    }

    private void initView(){

        try {
            String file = (String) getIntent().getStringExtra(CreateQuotation.KEY_INTENT_IMAGE);
            if(file == null ) finish();

            Bitmap image = CoreUtils.fetchFile(file,context);
            if(image == null)finish();

            imgQuotationSend.setImageBitmap(image);

        }catch (Exception e){
            e.printStackTrace();
            finish();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.quotation_send_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.action_send_quotation:
                showDialog();
                break;
        }
        return true;
    }

    private void showDialog() {

        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle("Confirm");
        alertDialog.setMessage("Are you sure you would like to send this quotation?");
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        //TODO CODE TO SEND THE IMAGE
                        Toast.makeText(context,"Image Quotation has been sent!",Toast.LENGTH_SHORT).show();
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

}
