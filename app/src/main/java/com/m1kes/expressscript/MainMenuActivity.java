package com.m1kes.expressscript;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.m1kes.expressscript.adapters.gridview.CustomMenuAdapter;
import com.m1kes.expressscript.utils.CoreUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainMenuActivity extends AppCompatActivity {

    Integer imageIds[] = {R.drawable.ic_progress_upload_black_48dp,R.drawable.ic_file_plus_black_48dp,R.drawable.ic_information_black_48dp,R.drawable.ic_forum_black_48dp,
            R.drawable.ic_book_open_variant_black_48dp};
    GridView gridView;
    String []keyWords ={"Create Quote","Medical Aid","Order Status","Messages","Contact Us"};
    private Context context = this;
    private RelativeLayout rootView;

    @BindView(R.id.txtVersionNoMain)TextView txtVersionNoMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main_menu);
        CoreUtils.setupTitleOnlyActionBar("Home",this);
        ButterKnife.bind(this);


        String versionName = BuildConfig.VERSION_NAME;
        txtVersionNoMain.setText("Version: "+versionName);

        rootView = findViewById(R.id.activity_main_root);
        gridView = findViewById(R.id.gridview);
        gridView.setAdapter(new CustomMenuAdapter(MainMenuActivity.this,imageIds,keyWords));
        gridView.setNumColumns(2);
        gridView.setPadding(0,0,0,0);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                switch (position){

                    case 0://Create Quote
                        startActivity(new Intent(MainMenuActivity.this,UploadScreen.class));
                        break;
                    case 1://Medical Aid
                        startActivity(new Intent(MainMenuActivity.this,MedicalAidActivity.class));
                        break;
                    case 2: //OrderStatus
                        startActivity(new Intent(MainMenuActivity.this,OrderStatus.class));
                        break;
                    case 3: //Messages
                        startActivity(new Intent(MainMenuActivity.this,MessagesActivity.class));
                        break;
                    case 4://Contact Us
                        showContactUsDialog();
                        break;
                    default:

                }

            }
        });

    }

    private void logout(){

        final ProgressDialog progressDialog = new ProgressDialog(MainMenuActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Loggging out...");
        progressDialog.show();

        // TODO: Implement your own authentication logic here.

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onLoginSuccess or onLoginFailed
                        onLogoutSuccess();
                        // onLoginFailed();
                        progressDialog.dismiss();
                    }
                }, 3000);

    }

    private void onLogoutSuccess(){
        finish();
    }


    private void showContactUsDialog() {
        AlertDialog alertDialog = new AlertDialog.Builder(MainMenuActivity.this).create();
        alertDialog.setTitle("Contact Us");

        alertDialog.setMessage("Express Script. \n\n"+
                "Address: 21 Milton Park, Harare\n"+
                "Tel: 077532432423 \n"+
                "Mobile: 0775432322 \n"+
                "Email: expresscript@email.com");
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();

    }


    private void showAboutDialog() {
        AlertDialog alertDialog = new AlertDialog.Builder(MainMenuActivity.this).create();
        alertDialog.setTitle("About");

        String versionName = BuildConfig.VERSION_NAME;
        alertDialog.setMessage("Express Script. \n\n"+
                "Version: "+versionName+" \n"+
                "Release Date: 02-05-2018 \n"+
                "Checksum: HA2882SJSHDWQ2 \n"+
                " \n \nCreated by : Michael Mclean (SOJ))");
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();

    }
}
