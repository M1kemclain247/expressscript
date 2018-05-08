package com.m1kes.expressscript.adapters.gridview;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.m1kes.expressscript.R;

public class CustomMenuAdapter extends BaseAdapter {

    Context context;
    Integer []imageIds;
    String[]keyWords;

    public CustomMenuAdapter(Context context,Integer []imageIds,String[] keyWords){

        this.context = context;
        this.imageIds = imageIds;
        this.keyWords = keyWords;
    }


    @Override
    public int getCount() {
        return imageIds.length;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView= inflater.inflate(R.layout.custom_menu_item, null, true);

        ImageView imageView = (ImageView)rowView.findViewById(R.id.imageView);
        TextView txtview = (TextView)rowView.findViewById(R.id.txt_GridItem);
        imageView.setImageResource(imageIds[position]);
        // imageView.setBackgroundResource(R.drawable.edit_states);
        txtview.setText(keyWords[position]);
        Typeface face = Typeface.createFromAsset(context.getAssets(),
                "RobotoCondensed-Regular.ttf");
        txtview.setTypeface(face);
        //rowView.setPadding(2, 12, 2, 12);
        //LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT);
        // rowView.setLayoutParams(params);
        // rowView.setLayoutParams(new GridView.LayoutParams(GridView.AUTO_FIT, 500));
        return rowView;
    }




}
