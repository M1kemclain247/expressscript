package com.m1kes.expressscript;

import android.content.Context;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.widget.Toast;

import com.m1kes.expressscript.adapters.recyclerview.OrdersRecyclerAdapter;
import com.m1kes.expressscript.objects.OrderWrapper;
import com.m1kes.expressscript.objects.Product;
import com.m1kes.expressscript.objects.Quote;
import com.m1kes.expressscript.objects.QuoteItem;
import com.m1kes.expressscript.sqlite.adapters.OrdersDBAdapter;
import com.m1kes.expressscript.sqlite.adapters.ProductsDBAdapter;
import com.m1kes.expressscript.sqlite.adapters.QuotesDBAdapter;
import com.m1kes.expressscript.storage.ClientIDManager;
import com.m1kes.expressscript.utils.CoreUtils;
import com.m1kes.expressscript.utils.EndPoints;
import com.m1kes.expressscript.utils.WebUtils;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.util.ArrayList;
import java.util.List;
import java.util.PropertyResourceBundle;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Orders extends AppCompatActivity {

    private Context context;
    @BindView(R.id.ordersRecyclerview) RecyclerView ordersRecyclerview;
    private OrdersRecyclerAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private List<OrderWrapper> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);
        CoreUtils.setupActionBar("Orders",this);
        ButterKnife.bind(this);
        context = this;

        List<OrderWrapper> quotes = OrdersDBAdapter.getAll(context);

        if(quotes == null || quotes.isEmpty())return;

        for(OrderWrapper wrapper : quotes){
            update(wrapper);
        }

        setupRecyclerView();
    }

    private void update(final OrderWrapper wrapper){

        WebUtils.SimpleHttpURLWebRequest request = WebUtils.getSimpleHttpRequest(new WebUtils.OnResponseCallback() {
            @Override
            public void onSuccess(String response) {

                System.out.println("Response to be parsed is : " + response);

                try {
                    org.json.JSONObject object = new org.json.JSONObject(response);

                    String status = object.getString("Message");
                    wrapper.setStatus(status);

                    System.out.println("Status is : "+ status);

                    OrdersDBAdapter.update(wrapper,context);
                    updateRecycler();

                    Toast.makeText(context,"Quote status updated!",Toast.LENGTH_LONG).show();

                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(context,"Unable to connect, check your Internet Connection!", Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailed() {
                Toast.makeText(context,"Unable to connect, check your Internet Connection!", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCompleteTask() {

            }
        });

        request.execute(EndPoints.API_URL + EndPoints.API_CHECK_ORDER_STATUS + ClientIDManager.getClientID(context) + "/" + wrapper.getId());

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

        adapter = new OrdersRecyclerAdapter(this,data);
        layoutManager = new LinearLayoutManager(this);
        ordersRecyclerview.setLayoutManager(layoutManager);
        ordersRecyclerview.setAdapter(adapter);
    }

    private void updateRecycler(){
        System.out.println("Updating Recyclerview");
        this.data = OrdersDBAdapter.getAll(context);
        adapter = new OrdersRecyclerAdapter(this,data);
        ordersRecyclerview.setAdapter(adapter);
    }

}
