package com.m1kes.expressscript;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.m1kes.expressscript.adapters.recyclerview.decoration.RecyclerItemDivider;
import com.m1kes.expressscript.adapters.recyclerview.quoteitems.SelectableQuoteItem;
import com.m1kes.expressscript.adapters.recyclerview.quoteitems.adapters.SelectableQuoteItemRecyclerAdapter;
import com.m1kes.expressscript.adapters.recyclerview.quoteitems.objects.SelectableQuoteItemViewHolder;
import com.m1kes.expressscript.objects.Order;
import com.m1kes.expressscript.objects.OrderWrapper;
import com.m1kes.expressscript.objects.PaymentMode;
import com.m1kes.expressscript.objects.Quote;
import com.m1kes.expressscript.objects.QuoteItem;
import com.m1kes.expressscript.objects.custom.CustomDate;
import com.m1kes.expressscript.sqlite.adapters.OrdersDBAdapter;
import com.m1kes.expressscript.storage.ClientIDManager;
import com.m1kes.expressscript.storage.OrderRefGenerator;
import com.m1kes.expressscript.utils.CoreUtils;
import com.m1kes.expressscript.utils.EndPoints;
import com.m1kes.expressscript.utils.WebUtils;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class QuoteDetails extends AppCompatActivity implements SelectableQuoteItemViewHolder.OnItemSelectedListener {

    @BindView(R.id.quotesRecycler)
    RecyclerView quotesRecycler;
    @BindView(R.id.btnMakeQuote)
    Button btnMakeQuote;

    private Context context;
    private SelectableQuoteItemRecyclerAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private List<QuoteItem> data;

    private Quote quote = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quote_details);

        CoreUtils.setupActionBar("Quote Details", this);
        ButterKnife.bind(this);
        context = this;

        data = new ArrayList<>();

        getQuoteDetails();

        btnMakeQuote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (getSelectedItems() == null || getSelectedItems().isEmpty()) {
                    Snackbar.make(quotesRecycler, "Please select at least 1 item to continue!", Snackbar.LENGTH_LONG).show();
                    return;
                }


                showDialog();
            }
        });

        setupRecyclerView();

    }

    public void getQuoteDetails() {

        Quote quote = getIntent().getExtras().getParcelable("quote");
        if (quote == null) {
            finish();
            return;
        }


        this.quote = quote;
        System.out.println("Loading QuoteItems to viewed : " + quote.getItems().size());
        data.addAll(quote.getItems());
    }

    public void buildOrderItem() {

        //TODO Collect all select items and post json to the API'

        final ProgressDialog dialog = new ProgressDialog(context,
                R.style.AppTheme_Dark_Dialog);
        dialog.setIndeterminate(true);
        dialog.setMessage("Making Order....");
        dialog.show();

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {

                        final Order order = new Order();

                        order.setClientID(ClientIDManager.getClientID(context));
                        order.setDeviceRef("" + OrderRefGenerator.getNextOrderNo(context));
                        order.setDescription(quote.getContent());
                        order.setTransaction_date(new CustomDate(System.currentTimeMillis()));
                        order.setAddress("Harare , Zimbabwe");
                        order.setTotal(getTotalPrice());
                        order.setPaymentMode(PaymentMode.BANK_TRANSFER);
                        order.setDrugs(getSelectedItems());

                        try {

                        JSONObject object = new JSONObject();

                        object.put("ClientId", "" + order.getClientID());
                        object.put("DeviceRef", order.getDeviceRef());
                        object.put("Description", "");
                        object.put("TransactionDate", "" + order.getTransaction_date().getFormattedTime("yyyy-MM-dd hh:mm:ss"));
                        object.put("Address", order.getAddress());
                        object.put("Total", "" + order.getTotal());
                        object.put("PaymentMode", order.getPaymentMode().toString());

                        JSONArray jArray = new JSONArray();
                        for (QuoteItem drug : getSelectedItems()) {
                            JSONObject json = new JSONObject();
                            json.put("ProductId", drug.getProductID());
                            json.put("Quantity", drug.getQuantity());
                            json.put("UnitPrice", drug.getUnitPrice());
                            json.put("Total", drug.getTotal());
                            jArray.put(json);
                        }
                        object.put("Drugs", jArray.toString());

                            System.out.println("Object : " + object.toString(2));

                        WebUtils.AdvanceJsonPostRequest request =  WebUtils.getPostRequest(context, EndPoints.API_URL + EndPoints.API_CRATE_ORDER, object, new WebUtils.JsonResponse() {
                            @Override
                            public void onSuccess(org.json.JSONObject response) {
                                System.out.println("Response is : " + response);
                                try {
                                    String message = (String) response.get("Message");
                                    System.out.println("Message Response is : " + message);

                                    int id = Integer.parseInt(message);

                                    OrderWrapper wrapper = new OrderWrapper();
                                    wrapper.setId(id);
                                    wrapper.setOrder(order);

                                    OrdersDBAdapter.add(wrapper,context);

                                    Toast.makeText(context, "Order has been successfully created!", Toast.LENGTH_LONG).show();

                                } catch (Exception e) {
                                    e.printStackTrace();
                                    Toast.makeText(context, "Failed to create order!", Toast.LENGTH_LONG).show();
                                }

                                finish();


                                Intent i = new Intent(context, Orders.class);
                                startActivity(i);
                            }

                            @Override
                            public void onFailed(String error) {
                                System.out.println("Failed to register!");
                                Toast.makeText(context, "Failed to create order!", Toast.LENGTH_LONG).show();
                            }
                        },dialog);

                        request.execute();



                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, 1000);

    }

    private void showDialog() {

        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle("Confirm");

        StringBuilder sb = new StringBuilder();
        sb.append("Selection Breakdown \n\n");

        for (QuoteItem item : getSelectedItems()) {
            sb.append(" ").append(item.getDescription()).append("    ").append("$").append(df.format(item.getTotal())).append("\n");
        }

        sb.append("\n\n");
        sb.append("Total Cost : $").append(df.format(getTotalPrice()));

        alertDialog.setMessage(sb.toString());
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Make Order",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        buildOrderItem();
                    }
                });
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(context, "Cancelled", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                });
        alertDialog.show();

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

    private List<QuoteItem> getSelectedItems() {
        return adapter.getSelectedItems();
    }

    private void setupRecyclerView() {

        adapter = new SelectableQuoteItemRecyclerAdapter(this, data, true, context);
        layoutManager = new LinearLayoutManager(this);
        quotesRecycler.setHasFixedSize(true);
        quotesRecycler.setLayoutManager(layoutManager);
        quotesRecycler.setAdapter(adapter);
        quotesRecycler.addItemDecoration(new RecyclerItemDivider(this));
    }


    private void updateRecycler() {
        System.out.println("Updating Recyclerview");
        adapter = new SelectableQuoteItemRecyclerAdapter(this, data, true, context);
        quotesRecycler.setAdapter(adapter);
    }

    private DecimalFormat df = new DecimalFormat("#.##");

    @Override
    public void onItemSelected(SelectableQuoteItem item) {
        Snackbar.make(quotesRecycler, "Total Cost :  $" + df.format(getTotalPrice()), Snackbar.LENGTH_LONG).show();
    }


    public double getTotalPrice() {
        double total = 0.0D;
        for (QuoteItem item : getSelectedItems()) {
            double price = item.getTotal();
            total += price;
        }
        return total;
    }
}
