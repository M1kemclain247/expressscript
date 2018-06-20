package com.m1kes.expressscript;

import android.content.Context;
import android.os.PersistableBundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.m1kes.expressscript.adapters.recyclerview.OrdersRecyclerAdapter;
import com.m1kes.expressscript.adapters.recyclerview.QuoteItemsRecyclerAdapter;
import com.m1kes.expressscript.adapters.recyclerview.decoration.RecyclerItemDivider;
import com.m1kes.expressscript.adapters.recyclerview.quoteitems.SelectableQuoteItem;
import com.m1kes.expressscript.adapters.recyclerview.quoteitems.adapters.SelectableQuoteItemRecyclerAdapter;
import com.m1kes.expressscript.adapters.recyclerview.quoteitems.objects.SelectableQuoteItemViewHolder;
import com.m1kes.expressscript.objects.Order;
import com.m1kes.expressscript.objects.QuoteItem;
import com.m1kes.expressscript.sqlite.adapters.OrdersDBAdapter;
import com.m1kes.expressscript.utils.CoreUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class QuoteDetails extends AppCompatActivity implements SelectableQuoteItemViewHolder.OnItemSelectedListener{

    @BindView(R.id.quotesRecycler)RecyclerView quotesRecycler;
    @BindView(R.id.btnMakeQuote)Button btnMakeQuote;

    private Context context;
    private SelectableQuoteItemRecyclerAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private List<QuoteItem> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quote_details);

        CoreUtils.setupActionBar("Quote Details",this);
        ButterKnife.bind(this);
        context = this;

        data = new ArrayList<>();

        data.add(new QuoteItem("x1 Besemax 250mg","$23.00"));
        data.add(new QuoteItem("x1 iBrufen 500mg","$10.00"));
        data.add(new QuoteItem("x1 Paracetemol 150mg","$2.00"));


        btnMakeQuote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        setupRecyclerView();

    }


    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onRestoreInstanceState(savedInstanceState, persistentState);
        updateRecycler();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateRecycler();
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

    private void setupRecyclerView(){

        adapter = new SelectableQuoteItemRecyclerAdapter(this,data,true,context);
        layoutManager = new LinearLayoutManager(this);
        quotesRecycler.setHasFixedSize(true);
        quotesRecycler.setLayoutManager(layoutManager);
        quotesRecycler.setAdapter(adapter);
        quotesRecycler.addItemDecoration(new RecyclerItemDivider(this));
    }


    private void updateRecycler(){
        System.out.println("Updating Recyclerview");
        adapter = new SelectableQuoteItemRecyclerAdapter(this,data,true,context);
        quotesRecycler.setAdapter(adapter);
    }

    @Override
    public void onItemSelected(SelectableQuoteItem item) {
        Snackbar.make(quotesRecycler,"Total Cost :  " + item.getPrice() ,Snackbar.LENGTH_LONG).show();
    }
}
