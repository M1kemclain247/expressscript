package com.m1kes.expressscript;

import android.content.Context;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.widget.Toast;

import com.m1kes.expressscript.adapters.recyclerview.MessageAdapter;
import com.m1kes.expressscript.adapters.recyclerview.OrdersRecyclerAdapter;
import com.m1kes.expressscript.objects.Message;
import com.m1kes.expressscript.objects.Order;
import com.m1kes.expressscript.objects.Product;
import com.m1kes.expressscript.sqlite.adapters.MessagesDBAdapter;
import com.m1kes.expressscript.sqlite.adapters.OrdersDBAdapter;
import com.m1kes.expressscript.sqlite.adapters.ProductsDBAdapter;
import com.m1kes.expressscript.storage.ClientIDManager;
import com.m1kes.expressscript.utils.CoreUtils;
import com.m1kes.expressscript.utils.EndPoints;
import com.m1kes.expressscript.utils.WebUtils;

import org.json.JSONException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Quotes extends AppCompatActivity {

    @BindView(R.id.quotesRecycler)RecyclerView quotesRecycler;

    private Context context;
    private OrdersRecyclerAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private List<Order> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quotes);
        CoreUtils.setupActionBar("Quotes",this);
        ButterKnife.bind(this);
        context = this;

        List<Order> orders = OrdersDBAdapter.getAll(context);

        if(orders == null || orders.isEmpty())return;

        for(Order order : orders){
            checkProductStatus(order);
        }

        setupRecyclerView();
    }

    private void checkProductStatus(final Order order){

        WebUtils.SimpleHttpURLWebRequest request = WebUtils.getSimpleHttpRequest(new WebUtils.OnResponseCallback() {
            @Override
            public void onSuccess(String response) {

                System.out.println("Response to be parsed is : " + response);

                try {
                    Object jsonArrayy = new JSONParser().parse(response);

                    JSONArray jsonResponse = (JSONArray) jsonArrayy;

                    for(int i = 0; i < jsonResponse.size(); i++){
                         JSONObject obj =  (JSONObject)jsonResponse.get(i);

                        int productId = ((Long) obj.get("ProductId")).intValue();
                        String description = ((String) obj.get("Description"));
                        double unit_price = ((Double) obj.get("UnitPrice"));
                        int quantity = ((Double) obj.get("Quantity")).intValue();
                        double total_price = ((Double) obj.get("Total"));

                        Product product = new Product();

                        product.setId(productId);
                        product.setDescription(description);
                        product.setUnit_price(unit_price);
                        product.setQuantity(quantity);
                        product.setTotal_price(total_price);

                        ProductsDBAdapter.update(product,context);
                        System.out.println("Updated a product");
                    }

                    order.setContent(response);
                    order.setSynced(true);

                    OrdersDBAdapter.update(order,context);

                    Toast.makeText(context,"Quote has been sent Successfully!",Toast.LENGTH_LONG).show();

                } catch (ParseException e) {
                    e.printStackTrace();
                }


                Toast.makeText(context,"Downloaded Using Order ID : " + order.getId() ,Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailed() {
                Toast.makeText(context,"Unable to connect, check your Internet Connection!", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCompleteTask() {

            }
        });

        request.execute(EndPoints.API_URL + EndPoints.API_CHECK_QUOTE + ClientIDManager.getClientID(context) + "/" + order.getId());

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
        quotesRecycler.setLayoutManager(layoutManager);
        quotesRecycler.setAdapter(adapter);
    }

    private void updateRecycler(){
        System.out.println("Updating Recyclerview");
        this.data = OrdersDBAdapter.getAll(context);
        adapter = new OrdersRecyclerAdapter(this,data);
        quotesRecycler.setAdapter(adapter);
    }

}
