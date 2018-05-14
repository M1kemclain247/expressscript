package com.m1kes.expressscript;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.m1kes.expressscript.utils.CoreUtils;

import butterknife.ButterKnife;

public class Quotes extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quotes);
        CoreUtils.setupActionBar("Quotes",this);
        ButterKnife.bind(this);




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

}
