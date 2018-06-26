package com.m1kes.expressscript.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class CoreUtils {

    private static Random random = new Random();

    public static String getDeviceId(Context context) {
        return android.os.Build.SERIAL;
    }

    public static String getDeviceIMEI(Context context,Activity activity) {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            ActivityCompat.requestPermissions(activity,new String[]{Manifest.permission.READ_PHONE_STATE},1002);
        }
        final String deviceId = ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
        if (deviceId != null) {
            System.out.println("Device Serial/IMEI: " + deviceId);
            return deviceId;
        } else {
            System.out.println("Device Serial/IMEI: " + android.os.Build.SERIAL);
            return android.os.Build.SERIAL;
        }
    }

    /**
     * For now we are using the simcard phone number
     * **/
    public static String getTerminalNo(Context context,Activity activity) {
        TelephonyManager tMgr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_NUMBERS) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            ActivityCompat.requestPermissions(activity,new String[]{Manifest.permission.READ_SMS,Manifest.permission.READ_PHONE_NUMBERS,Manifest.permission.READ_PHONE_STATE},1003);
        }
        return tMgr.getLine1Number();
    }

    public static void setupActionBar(String title, AppCompatActivity appCompatActivity) {
        ActionBar actionBar = appCompatActivity.getSupportActionBar();
        if (actionBar != null) {
            // Show the Up button in the action bar.
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setTitle(title);
        }
    }


    public static void setupTitleOnlyActionBar(String title, AppCompatActivity appCompatActivity) {
        ActionBar actionBar = appCompatActivity.getSupportActionBar();
        if (actionBar != null) {
            // Show the Up button in the action bar.
            actionBar.setTitle(title);
        }
    }



    /**
     * Get a diff between two dates
     * @param date1 the oldest date
     * @param date2 the newest date
     * @param timeUnit the unit in which you want the diff
     * @return the diff value, in the provided unit
     */
    public static long getDateDiff(Date date1, Date date2, TimeUnit timeUnit) {
        long diffInMillies = date2.getTime() - date1.getTime();
        return timeUnit.convert(diffInMillies, TimeUnit.MILLISECONDS);
    }

    public static boolean isNowBetweenDateTime(final Date s, final Date e)
    {

        SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");

        Calendar calendar = Calendar.getInstance();
        Date now =  calendar.getTime();

        System.out.println("LastLogged: "+s.toString() +" Current: "+new SimpleDateFormat("yyyyMMddHHmmss").format(now) + "Expiry: "+e.toString());
        System.out.println("Now Object: "+now);

        String formattedStr = sf.format(now);
        Date newDate = null;
        try {
            newDate =  sf.parse(formattedStr);

        } catch (ParseException e1) {
            e1.printStackTrace();
        }

        return newDate.after(s) && newDate.before(e);
    }

    public static String saveFile(Bitmap bitmap,String filename,Context context) {

        try {
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, bytes);
            FileOutputStream fo = context.openFileOutput(filename, Context.MODE_PRIVATE);
            fo.write(bytes.toByteArray());
            // remember close file output
            fo.close();
        } catch (Exception e) {
            e.printStackTrace();
            filename = null;
        }
        return filename;
    }

    public static Bitmap fetchFile(String filename,Context context){

        try {
            return BitmapFactory.decodeStream(context
                    .openFileInput(filename));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }

    }

    public static byte[] bitmapToBytes(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    private static Bitmap resize(Bitmap image, int maxWidth, int maxHeight) {
        if (maxHeight > 0 && maxWidth > 0) {
            int width = image.getWidth();
            int height = image.getHeight();
            float ratioBitmap = (float) width / (float) height;
            float ratioMax = (float) maxWidth / (float) maxHeight;

            int finalWidth = maxWidth;
            int finalHeight = maxHeight;
            if (ratioMax > 1) {
                finalWidth = (int) ((float)maxHeight * ratioBitmap);
            } else {
                finalHeight = (int) ((float)maxWidth / ratioBitmap);
            }
            image = Bitmap.createScaledBitmap(image, finalWidth, finalHeight, true);
            return image;
        } else {
            return image;
        }
    }

    public static String toBase64(Bitmap bitmap,boolean resize){
        if(bitmap == null)return null;

        if(resize){
            bitmap = resize(bitmap,100,100);
        }

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        return Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);
    }

    public static int getRandomNumber(int low_bound, int upper_bound){
        return random.nextInt(upper_bound-low_bound) + low_bound;
    }

    public static int getRandomNumber(int length) {
        String chars = "0123456789";
        StringBuilder b = new StringBuilder(length);
        for (int j = 0; j < length; j++) {
            b.append(chars.charAt(random.nextInt(chars.length())));
        }
        return Integer.parseInt(b.toString());
    }

    /**
     * converts an intger value to a boolean value 1 = true  0 = false
     * @param intValue
     * @return
     */
    public static boolean toBoolean(int intValue){
        return intValue == 1;
    }

    /**
     * converts an boolean value to a int value 1 = true  0 = false
     * @param value
     * @return
     */

    public static int toInt(boolean value) {
        // Convert true to 1 and false to 0.
        return value ? 1 : 0;
    }

    public static void hideKeyboard(final Activity context) {
        InputMethodManager inputManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);

        // check if no view has focus:
        View v = context.getCurrentFocus();
        if (v == null)
            return;

        if(inputManager != null)
        inputManager.hideSoftInputFromWindow(v.getWindowToken(), 0);

    }


    /**
     * Scales the provided bitmap to have the height and width provided.
     * (Alternative method for scaling bitmaps
     * since Bitmap.createScaledBitmap(...) produces bad (blocky) quality bitmaps.)
     *
     * @param bitmap is the bitmap to scale.
     * @param newWidth is the desired width of the scaled bitmap.
     * @param newHeight is the desired height of the scaled bitmap.
     * @return the scaled bitmap.
     */
    public static Bitmap scaleBitmap(Bitmap bitmap, int newWidth, int newHeight) {
        Bitmap scaledBitmap = Bitmap.createBitmap(newWidth, newHeight, Bitmap.Config.ARGB_8888);

        float scaleX = newWidth / (float) bitmap.getWidth();
        float scaleY = newHeight / (float) bitmap.getHeight();
        float pivotX = 0;
        float pivotY = 0;

        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(scaleX, scaleY, pivotX, pivotY);

        Canvas canvas = new Canvas(scaledBitmap);
        canvas.setMatrix(scaleMatrix);
        canvas.drawBitmap(bitmap, 0, 0, new Paint(Paint.FILTER_BITMAP_FLAG));

        return scaledBitmap;
    }


}

