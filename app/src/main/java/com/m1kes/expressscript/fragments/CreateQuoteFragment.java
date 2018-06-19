package com.m1kes.expressscript.fragments;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.format.Time;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.m1kes.expressscript.CreateTextQuotation;
import com.m1kes.expressscript.R;
import com.m1kes.expressscript.RegistrationActivity;
import com.m1kes.expressscript.adapters.menu.DefaultListMenuFragment;
import com.m1kes.expressscript.objects.MenuId;
import com.m1kes.expressscript.objects.Product;
import com.m1kes.expressscript.sqlite.adapters.ProductsDBAdapter;
import com.m1kes.expressscript.storage.ClientIDManager;
import com.m1kes.expressscript.utils.CoreUtils;
import com.m1kes.expressscript.utils.EndPoints;
import com.m1kes.expressscript.utils.WebUtils;
import com.m1kes.expressscript.utils.extra.AbstractListMenuFragment;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static android.app.Activity.RESULT_OK;
import static com.m1kes.expressscript.MenuActivity.LANDING_FRAGMENT_TAG;

/**
 * A simple {@link Fragment} subclass.
 */
public class CreateQuoteFragment extends Fragment {

    private final int REQUEST_TAKE_PHOTO = 10021;
    private final int REQUEST_BROWSE_GALLERY = 10022;
    public final static String KEY_INTENT_IMAGE = "SendQuotation-Image";
    private static SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");

    private ImageView imgResultQuotation;
    private Button camera,gallery,text,btnSendQuotation,btnRotateImg;
    private TextView txtHintQuote,txtExampleQuote,txtHeaderQuote,txtTitleQuote,txtMainTitleQuote;

    public CreateQuoteFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_create_quote, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // Setup any handles to view objects here
        camera = view.findViewById(R.id.btnCreateQuoteCamera);
        gallery  = view.findViewById(R.id.btnCreateQuoteGallery);
        text  = view.findViewById(R.id.btnCreateQuoteText);
        btnSendQuotation = view.findViewById(R.id.btnSendQuotation);
        btnRotateImg = view.findViewById(R.id.btnRotateImg);

        txtHintQuote = view.findViewById(R.id.txtHintQuote);
        txtExampleQuote = view.findViewById(R.id.txtExampleQuote);
        txtHeaderQuote = view.findViewById(R.id.txtHeaderQuote);
        txtTitleQuote = view.findViewById(R.id.txtTitleQuote);
        txtMainTitleQuote = view.findViewById(R.id.txtMainTitleQuote);


        imgResultQuotation = view.findViewById(R.id.imgResultQuotation);

        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
                StrictMode.setVmPolicy(builder.build());

                mHighQualityImageUri = generateTimeStampPhotoFileUri();
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, mHighQualityImageUri);
                startActivityForResult(intent, REQUEST_TAKE_PHOTO);
            }
        });

        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //if everything is ok we will open image chooser
                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, REQUEST_BROWSE_GALLERY);
            }
        });

        text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(),CreateTextQuotation.class));
            }
        });

    }

    public void replaceFragment(Fragment fragment, String tag) {
        FragmentManager fragmentManager = getFragmentManager();
        android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.addToBackStack(tag);

        fragmentTransaction.commit();
    }

    public void postImage(final Context context,final Bitmap image){

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

        final ProgressDialog progressDialog = new ProgressDialog(getContext(),
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Sending quotation...");
        progressDialog.show();

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        progressDialog.dismiss();
                        replaceFragment(createLandingListMenu(), LANDING_FRAGMENT_TAG);
                    }
                }, 3 * 1000);

    }

    protected AbstractListMenuFragment createLandingListMenu() {
        return new DefaultListMenuFragment().createMenus().showCabsHeading(true)
                .addMenu(MenuId.CREATE_QUOTE_MENU,"Request a Quote",
                        "Capture prescription details using Camera, Picture or Text", R.drawable.cart_plus)
                .addMenu(MenuId.QUOTES_MENU, "Quotes",
                        "View all your Quotes", R.drawable.file_compare)
                .addMenu(MenuId.ORDERS_MENU, "Orders",
                        "View all your Orders", R.drawable.icon_cart)
                .addMenu(MenuId.MEDICAL_AID, "Medical Aid",
                        "Manage linked Medical Aids", R.drawable.credit_card)
                .addMenu(MenuId.BRANCH_LOCATOR, "Branch Locator",
                        "Find a branch near you", R.drawable.icon_locator)
                .addMenu(MenuId.HEALTH_TIPS_MENU, "Health Tips",
                        "Health Tips keeping you in shape", R.drawable.book_open_page_variant)
                .addMenu(MenuId.MY_PROFILE, "My Profile",
                        "View your profile", R.drawable.face_profile)
                .addMenu(MenuId.CONTACT_US_MENU, "Contact Us",
                        "Connect with us now!", R.drawable.icon_connect);
    }

    private Uri mHighQualityImageUri = null;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK && data == null) return;

        if (requestCode == REQUEST_BROWSE_GALLERY) {

            Uri imageUri = data.getData();
            try {

                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), imageUri);
                processImage(bitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }

        } else if (requestCode == REQUEST_TAKE_PHOTO) {

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), mHighQualityImageUri);
                processImage(bitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }

    public void processImage(final Bitmap image) {

        String generatedName = "img-" + df.format(new Date());
        String file = CoreUtils.saveFile(image, generatedName, getContext());

        if (file == null) {
            Toast.makeText(getContext(), "Error occured while saving image!", Toast.LENGTH_SHORT).show();
            return;
        }

        Toast.makeText(getContext(), "Loading Image ...", Toast.LENGTH_SHORT).show();

        Glide.with(getContext())
                .load(image)
                .into(imgResultQuotation);

        System.out.println("File Path to img : " + file);

        btnSendQuotation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postImage(getContext(),image);
            }
        });

        imgResultQuotation.setVisibility(View.VISIBLE);
        btnSendQuotation.setVisibility(View.VISIBLE);
        btnRotateImg.setVisibility(View.VISIBLE);

        camera.setVisibility(View.INVISIBLE);
        gallery.setVisibility(View.INVISIBLE);
        text.setVisibility(View.INVISIBLE);

        txtHeaderQuote.setVisibility(View.INVISIBLE);
        txtHintQuote.setVisibility(View.INVISIBLE);
        txtExampleQuote.setVisibility(View.INVISIBLE);
        txtTitleQuote.setVisibility(View.INVISIBLE);

        txtMainTitleQuote.setText("Send Quotation");
    }



    private Uri generateTimeStampPhotoFileUri() {

        Uri photoFileUri = null;
        File outputDir = getPhotoDirectory();
        if (outputDir != null) {
            Time t = new Time();
            t.setToNow();
            File photoFile = new File(outputDir, System.currentTimeMillis()
                    + ".jpg");
            photoFileUri = Uri.fromFile(photoFile);
        }
        return photoFileUri;
    }

    private File getPhotoDirectory() {
        File outputDir = null;
        String externalStorageStagte = Environment.getExternalStorageState();
        if (externalStorageStagte.equals(Environment.MEDIA_MOUNTED)) {
            File photoDir = Environment
                    .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
            outputDir = new File(photoDir, getString(R.string.app_name));
            if (!outputDir.exists())
                if (!outputDir.mkdirs()) {
                    Toast.makeText(
                            getContext(),
                            "Failed to create directory "
                                    + outputDir.getAbsolutePath(),
                            Toast.LENGTH_SHORT).show();
                    outputDir = null;
                }
        }
        return outputDir;
    }

}
