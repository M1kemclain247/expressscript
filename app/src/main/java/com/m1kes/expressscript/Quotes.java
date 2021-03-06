package com.m1kes.expressscript;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.m1kes.expressscript.objects.Product;
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

import butterknife.ButterKnife;

public class Quotes extends AppCompatActivity {

    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quotes);
        CoreUtils.setupActionBar("Quotes",this);
        ButterKnife.bind(this);
        context = this;

        List<Product> products = ProductsDBAdapter.getAll(context);

        if(products == null || products.isEmpty())return;

        for(Product p : products){
            checkProductStatus(p.getId());
        }

    }

    private void checkProductStatus(final int product_id){

        WebUtils.SimpleHttpURLWebRequest request = WebUtils.getSimpleHttpRequest(new WebUtils.OnResponseCallback() {
            @Override
            public void onSuccess(String response) {


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

                    Toast.makeText(context,"Quote has been sent Successfully!",Toast.LENGTH_LONG).show();

                } catch (ParseException e) {
                    e.printStackTrace();
                }

                System.out.println("Response to be parsed is : " + response);



                Toast.makeText(context,"Downloaded Using Product ID : " + product_id,Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailed() {
                Toast.makeText(context,"Unable to connect, check your Internet Connection!", Toast.LENGTH_LONG).show();
            }
        });

        request.execute(EndPoints.API_URL + EndPoints.API_CHECK_QUOTE + ClientIDManager.getClientID(context) + "/" + product_id);

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
