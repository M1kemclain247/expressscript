package com.m1kes.expressscript.utils.extra;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.m1kes.expressscript.R;
import com.m1kes.expressscript.objects.BranchDistance;
import com.m1kes.expressscript.objects.BranchLocation;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;



public class Utils {

    public static final String WAIT_DIALOG = "wait_dialog";
    private static String dateTimeNow;

    public static TextView findTextView(View view, int resId) {

        return (TextView) view.findViewById(resId);

    }

    public static EditText findEditText(View view, int resId) {
        return (EditText) view.findViewById(resId);
    }

    public static ListView findListView(View view, int resId) {

        return (ListView) view.findViewById(resId);

    }

    public static ImageView findImageView(View view, int resId) {
        return (ImageView) view.findViewById(resId);
    }

    public static boolean isEmptyEditText(EditText editText) {
        return editText.getText().toString().trim().length() == 0;
    }

    public static String formatDate(long longDate) {

        Date date = new Date(longDate);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm a");
        return sdf.format(date);

    }



    public static void dismissWaitDialog(FragmentManager fragmentManager) {

        Fragment dialog = fragmentManager.findFragmentByTag(WAIT_DIALOG);
        if (fragmentManager != null && dialog != null) {
            fragmentManager.beginTransaction().remove(dialog).commit();
        }
    }


    public static void dismissWaitDialog(Fragment fragment) {
        dismissWaitDialog(fragment.getFragmentManager());
    }

    public static boolean isDateOlderMinutes(long datemillis, long period) {

        long now = System.currentTimeMillis();
        long diff = now - datemillis;

        return diff >= (period * 60 * 1000);
    }

    public static Button findButton(View rootView, int resId) {

        return (Button) rootView.findViewById(resId);
    }

    public static String formatAmount(double amount) {
        DecimalFormat format = new DecimalFormat("#,##0.00");
        return format.format(amount);
    }

    public static String formatMoneyAmount(double amount) {
        DecimalFormat format = new DecimalFormat("$#,##0.00;-$#,##0.00");
        return format.format(amount);
    }

    public static String formatMoneyAmount(BigDecimal amount) {
        if (amount == null) {
            return formatMoneyAmount(0);
        }
        return formatMoneyAmount(amount.doubleValue());
    }

    public static String formatAmount(BigDecimal amount) {

        if (amount == null) {
            return formatAmount(0);
        }

        return formatAmount(amount.doubleValue());
    }

    public static BigDecimal getDecimalFromEditText(EditText editText) {

        if (editText == null || editText.getText().toString().trim().isEmpty()) {
            return BigDecimal.ZERO;
        }

        String value = editText.getText().toString();
        return BigDecimal.valueOf(Double.parseDouble(value));
    }

    public static void showToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public static View createDottedSeperator(LayoutInflater inflater) {
        View dottedLayout = inflater.inflate(R.layout.dotted_seperator_view, null);
        dottedLayout.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        return dottedLayout;
    }

    public static View createDottedSeperator(Context context, LayoutInflater inflater, int dpSize) {

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        int margin = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, dpSize, context.getResources().getDisplayMetrics()
        );
        layoutParams.setMargins(0, margin, 0, 0);

        View seperator = createDottedSeperator(inflater);
        seperator.setLayoutParams(layoutParams);
        return seperator;
    }

    public static void addDottedSeperator(LayoutInflater layoutInflater, ViewGroup parentView) {
        View dottedSeperator = layoutInflater.inflate(R.layout.dotted_seperator_view, parentView);
        dottedSeperator.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
    }

    public static String formatMsisdnFromContacts(String number) {

        if (number == null) {
            return number;
        }

        String formattedNumber = number;

        //Replace all the possible friendly display chars
        formattedNumber = formattedNumber
                .replace("(", "").replace(")", "")
                .replace("-", "")
                .replace("+", "")
                .replace(" ", "").trim();

        if (formattedNumber.startsWith("0")) {
            formattedNumber = "263" + formattedNumber.substring(1);
        }
        return formattedNumber;
    }

    public static String getDateTimeNow() {

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        return sdf.format(new Date());
    }

    public static String formatMetersToKm(double value) {
        double kmValue = value / 1000;
        DecimalFormat format = new DecimalFormat("#####.00");
        return format.format(kmValue) + "km";
    }

    public static boolean checkPermission(Context context, String permission) {

        return ActivityCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED;
    }

    public static void requestPemrission(Activity activity, int requestCode, String... permissions) {
        ActivityCompat.requestPermissions(activity, permissions, requestCode);
    }

    public static <T extends BranchLocation> ArrayList<MapLocation> convertBranchToMapLocation(List<BranchDistance> branches) {
        if (branches == null) {
            return null;
        }
        ArrayList<MapLocation> locations = new ArrayList<>();
        for (BranchDistance b : branches) {
            locations.add(new MapLocation(b.getLatitude(), b.getLongitude(), b.getName(), b.getAddress()));
        }
        return locations;
    }
}