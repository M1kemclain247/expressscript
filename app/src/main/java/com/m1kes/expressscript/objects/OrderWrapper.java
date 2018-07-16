package com.m1kes.expressscript.objects;

import com.m1kes.expressscript.objects.custom.CustomDate;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class OrderWrapper {

    private int id;
    private Order order;
    private String status;


    public OrderWrapper() {
    }

    public OrderWrapper(int id, Order order) {
        this.id = id;
        this.order = order;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String toJson(OrderWrapper item) {

        JSONObject object = new JSONObject();

        try {

            Order order = item.getOrder();

            object.put("ClientId", "" + order.getClientID());
            object.put("DeviceRef", order.getDeviceRef());
            object.put("Description", "");
            object.put("TransactionDate", "" + order.getTransaction_date().getFormattedTime("yyyy-MM-dd hh:mm:ss"));
            object.put("Address", order.getAddress());
            object.put("Total", "" + order.getTotal());
            object.put("PaymentMode", order.getPaymentMode().toString());

            JSONArray jArray = new JSONArray();
            for (QuoteItem drug : order.getDrugs()) {
                JSONObject json = new JSONObject();
                json.put("ProductId", drug.getProductID());
                json.put("Quantity", drug.getQuantity());
                json.put("UnitPrice", drug.getUnitPrice());
                json.put("Total", drug.getTotal());
                jArray.put(json);
            }
            object.put("Drugs", jArray.toString());


        } catch (JSONException e) {
            e.printStackTrace();
        }
        return object.toString();
    }

    public static OrderWrapper fromJson(String json){

        OrderWrapper wrapper = new OrderWrapper();

        try {
            JSONObject object = new JSONObject(json);
            Order order = new Order();

            int clientId =  Integer.parseInt( (String) object.get("ClientId"));
            String deviceRef = (String)object.get("DeviceRef");
            String description = (String)object.get("Description");
            String transactionDate = (String)object.get("TransactionDate");
            String address = (String)object.get("Address");
            double total = Double.parseDouble( (String) object.get("Total"));
            PaymentMode paymentMode = PaymentMode.valueOf ((String)object.get("PaymentMode"));


            JSONArray items = new JSONArray((String)object.get("Drugs"));
            List<QuoteItem> drugs = new ArrayList<>();

            for(int i = 0 ; i < items.length() ; i++){

                QuoteItem drug = new QuoteItem();

                JSONObject subObj = (JSONObject)items.get(i);
                int productID = (Integer) subObj.get("ProductId");
                double unitPrice = ((Integer) subObj.get("UnitPrice")).doubleValue();
                int quantity = subObj.getInt("Quantity");
                int total_price = subObj.getInt("Total");

                drug.setProductID(productID);
                drug.setUnitPrice(unitPrice);
                drug.setQuantity(quantity);
                drug.setTotal(total_price);

                drugs.add(drug);
            }

            order.setClientID(clientId);
            order.setDeviceRef(deviceRef);
            order.setDescription(description);
            order.setTransaction_date(CustomDate.fromString(transactionDate));
            order.setAddress(address);
            order.setTotal(total);
            order.setPaymentMode(paymentMode);
            order.setDrugs(drugs);

            wrapper.setOrder(order);
            wrapper.setId(Integer.parseInt(deviceRef));


        } catch (JSONException e) {
            e.printStackTrace();
        }

        return wrapper;
    }


    @Override
    public String toString() {
        return "OrderWrapper{" +
                "id=" + id +
                ", order=" + order +
                '}';
    }
}
