package com.m1kes.expressscript;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.text.format.Time;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.m1kes.expressscript.objects.Message;
import com.m1kes.expressscript.utils.CoreUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;


public class CreateQuotation extends AppCompatActivity {

    @BindView(R.id.btnBrowseImage)
    Button btnBrowseImg;
    @BindView(R.id.btnTakePhoto)
    Button btnTakePhoto;
    @BindView(R.id.btnCreateTxtQuote)
    Button btnCreateTxtQuote;

    private final int REQUEST_TAKE_PHOTO = 10021;
    private final int REQUEST_BROWSE_GALLERY = 10022;
    private Context context;
    public final static String KEY_INTENT_IMAGE = "SendQuotation-Image";
    private static SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_quotation);
        CoreUtils.setupActionBar("Create Quote", this);
        ButterKnife.bind(this);
        context = this;

        btnBrowseImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //if everything is ok we will open image chooser
                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, REQUEST_BROWSE_GALLERY);
            }
        });

        btnTakePhoto.setOnClickListener(new View.OnClickListener() {
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

        btnCreateTxtQuote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context,CreateTextQuotation.class));
            }
        });


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


    private Uri mHighQualityImageUri = null;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK && data == null) return;

        if (requestCode == REQUEST_BROWSE_GALLERY) {

            Uri imageUri = data.getData();
            try {

                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                processImage(bitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }

        } else if (requestCode == REQUEST_TAKE_PHOTO) {

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), mHighQualityImageUri);
                processImage(bitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }

    public void processImage(Bitmap image) {

        String generatedName = "img-" + df.format(new Date());
        String file = CoreUtils.saveFile(image, generatedName, context);

        if (file == null) {
            Toast.makeText(context, "Error occured while saving image!", Toast.LENGTH_SHORT).show();
            return;
        }

        Toast.makeText(context, "Processing Image", Toast.LENGTH_SHORT).show();
        Intent i = new Intent(context, SendQuotation.class);
        i.putExtra(KEY_INTENT_IMAGE, file);
        startActivity(i);
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
                            this,
                            "Failed to create directory "
                                    + outputDir.getAbsolutePath(),
                            Toast.LENGTH_SHORT).show();
                    outputDir = null;
                }
        }
        return outputDir;
    }
}
