package com.m1kes.expressscript;

import android.content.Context;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.widget.Toast;

import com.m1kes.expressscript.adapters.recyclerview.QuotesRecyclerAdapter;
import com.m1kes.expressscript.objects.Quote;
import com.m1kes.expressscript.objects.Product;
import com.m1kes.expressscript.objects.QuoteItem;
import com.m1kes.expressscript.sqlite.adapters.QuotesDBAdapter;
import com.m1kes.expressscript.sqlite.adapters.ProductsDBAdapter;
import com.m1kes.expressscript.storage.ClientIDManager;
import com.m1kes.expressscript.utils.CoreUtils;
import com.m1kes.expressscript.utils.EndPoints;
import com.m1kes.expressscript.utils.WebUtils;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;


import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Quotes extends AppCompatActivity {

    @BindView(R.id.quotesRecycler)RecyclerView quotesRecycler;

    private Context context;
    private QuotesRecyclerAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private List<Quote> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quotes);
        CoreUtils.setupActionBar("Quotes",this);
        ButterKnife.bind(this);
        context = this;

        List<Quote> quotes = QuotesDBAdapter.getAll(context);

        if(quotes == null || quotes.isEmpty())return;

        for(Quote quote : quotes){
            checkProductStatus(quote);
        }

        setupRecyclerView();
    }





    private void checkProductStatus(final Quote quote){

        WebUtils.SimpleHttpURLWebRequest request = WebUtils.getSimpleHttpRequest(new WebUtils.OnResponseCallback() {
            @Override
            public void onSuccess(String response) {

                System.out.println("Response to be parsed is : " + response);

                try {
                    Object jsonArrayy = new JSONParser().parse(response);

                    JSONArray jsonResponse = (JSONArray) jsonArrayy;

                    List<QuoteItem> items = new ArrayList<>();
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
                        items.add(new QuoteItem(quote.getId(),productId,description,unit_price,quantity,total_price));
                    }

                    quote.setContent(response);
                    quote.setSynced(true);
                    quote.setItems(items);

                    QuotesDBAdapter.update(quote,context);
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

        request.execute(EndPoints.API_URL + EndPoints.API_CHECK_QUOTE + ClientIDManager.getClientID(context) + "/" + quote.getId());

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

        adapter = new QuotesRecyclerAdapter(this,data);
        layoutManager = new LinearLayoutManager(this);
        quotesRecycler.setLayoutManager(layoutManager);
        quotesRecycler.setAdapter(adapter);
    }

    private void updateRecycler(){
        System.out.println("Updating Recyclerview");
        this.data = QuotesDBAdapter.getAll(context);
        adapter = new QuotesRecyclerAdapter(this,data);
        quotesRecycler.setAdapter(adapter);
    }

}
