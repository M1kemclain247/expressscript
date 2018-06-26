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
import android.widget.RadioButton;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.m1kes.expressscript.objects.Message;
import com.m1kes.expressscript.objects.Product;
import com.m1kes.expressscript.sqlite.adapters.ProductsDBAdapter;
import com.m1kes.expressscript.storage.ClientIDManager;
import com.m1kes.expressscript.utils.CoreUtils;
import com.m1kes.expressscript.utils.EndPoints;
import com.m1kes.expressscript.utils.WebUtils;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SendQuotation extends AppCompatActivity {

    @BindView(R.id.imgQuotationSend) ImageView imgQuotationSend;
    private Context context;
    private Bitmap image;

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
            String file = getIntent().getStringExtra(CreateQuotation.KEY_INTENT_IMAGE);
            if(file == null ) finish();

            Bitmap image = CoreUtils.fetchFile(file,context);
            if(image == null)finish();

            this.image = image;

            Glide.with(context)
                    .load(image)
                    .into(imgQuotationSend);

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
                        postImage();
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

    public void postImage(){

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        WebUtils.JsonWebPost webPost = WebUtils.postJsonRequest(context, new WebUtils.OnResponseCallback() {
                            @Override
                            public void onSuccess(String response) {
                                System.out.println("Response is : " + response);

                                try {
                                    Object obj = new JSONParser().parse(response);

                                    JSONObject jsonResponse = (JSONObject) obj;
                                    int id = Integer.parseInt ((String) jsonResponse.get("Message"));


                                    ProductsDBAdapter.add(new Product(id),context);

                                    Toast.makeText(context,"Image has been sent Successfully!",Toast.LENGTH_LONG).show();


                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }


                            }

                            @Override
                            public void onFailed() {
                                System.out.println("Failed to register!");
                                Toast.makeText(context,"Failed to Send Image!",Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void onCompleteTask() {

                            }
                        });

                        Map<String,String> params = new HashMap<>();

                        String encoded = CoreUtils.toBase64(image,true);
                        if(encoded == null){
                            Toast.makeText(context,"Failed to send image is null",Toast.LENGTH_SHORT).show();
                            return;
                        }

                        String clientid = ClientIDManager.getClientID(context)+"";
                        params.put("ClientId",clientid);
                        params.put("ImageBytes",encoded);

                        System.out.println("Params :");
                        System.out.println("ClientId : "+ClientIDManager.getClientID(context)+"");
                        System.out.println("ImageBytes : " + encoded);

                        webPost.execute(EndPoints.API_URL + EndPoints.API_CREATE_QUOTE_IMAGE + "/" + clientid,params);

                    }
                }, 1000);
    }

}
