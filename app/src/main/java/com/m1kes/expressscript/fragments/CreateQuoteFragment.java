package com.m1kes.expressscript.fragments;


import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
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
import com.m1kes.expressscript.utils.CoreUtils;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.app.Activity.RESULT_OK;

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
        View view = inflater.inflate(R.layout.fragment_create_quote, container, false);

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


        return view;
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

    public void processImage(Bitmap image) {

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
