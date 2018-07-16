package com.m1kes.expressscript.utils;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONObject;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.PropertyResourceBundle;

public class WebUtils {

    public interface OnResponseCallback {
        void onSuccess(String response);
        void onFailed();
        void onCompleteTask();
    }

    public static SimpleHttpURLWebRequest getSimpleHttpRequest(OnResponseCallback callback) {
        return new SimpleHttpURLWebRequest(callback);
    }

    public static SimpleHttpURLWebRequest getSimpleHttpRequest(OnResponseCallback callback,ProgressDialog dialog) {
        return new SimpleHttpURLWebRequest(callback,dialog);
    }

    public static JsonWebPost postJsonRequest(Context context,OnResponseCallback callback) {
        return new JsonWebPost(context,callback);
    }

    public static JsonWebPost postJsonRequest(Context context,OnResponseCallback callback,ProgressDialog dialog) {
        return new JsonWebPost(context,callback,dialog);
    }

    public static class JsonWebPost extends AsyncTask<Object,String,Void>{

        private OnResponseCallback callback;
        @SuppressLint("StaticFieldLeak")
        private Context context;
        private ProgressDialog dialog;

        private JsonWebPost(Context context,OnResponseCallback callback) {
            this.callback = callback;
            this.context = context;
        }

        private JsonWebPost(Context context,OnResponseCallback callback,ProgressDialog dialog) {
            this.callback = callback;
            this.context = context;
            this.dialog = dialog;
        }

        @Override
        protected void onPreExecute() {
            if(dialog != null && !dialog.isShowing())
                dialog.show();
        }

        @Override
        protected Void doInBackground(Object... args) {

            String url = (String)args[0];
            System.out.println("Making Request to URL : " + url);
            Map<String,String> params = (Map<String,String>)args[1];

            post(context,url,params);

            return null;
        }

        private void post(Context context, String url, final Map<String,String> post_params){

            RequestQueue queue = Volley.newRequestQueue(context);
            StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>()
                    {
                        @Override
                        public void onResponse(String response) {
                            callback.onSuccess(response);
                            Log.d("Response", response);
                            if(dialog != null)dialog.dismiss();
                        }
                    },
                    new Response.ErrorListener()
                    {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            callback.onFailed();
                            if(dialog != null)dialog.dismiss();
                            Log.d("Error.Response", error.getLocalizedMessage() + "");
                        }
                    }
            ) {
                @Override
                protected Map<String, String> getParams()
                {
                    return post_params;
                }
            };
            queue.add(postRequest);

        }

    }


    public interface JsonResponse{
        void onSuccess(JSONObject response);
        void onFailed(String error);
    }

    public static AdvanceJsonPostRequest getPostRequest(Context context , String url,JSONObject params,JsonResponse callback, ProgressDialog dialog){
        return new AdvanceJsonPostRequest(callback,url,context,params,dialog);
    }


    public static class AdvanceJsonPostRequest extends AsyncTask<Object,String,String>{

        private JsonResponse callback;
        private String url;
        private Context context;
        private JSONObject params;
        private ProgressDialog dialog;

        public AdvanceJsonPostRequest(JsonResponse callback,String url,Context context,JSONObject params,ProgressDialog dialog) {
            this.callback = callback;
            this.url = url;
            this.context = context;
            this.params = params;
            this.dialog = dialog;
        }

        @Override
        protected String doInBackground(Object... objects) {
            post(context,url,params);
            return "";
        }

        private void post(Context context, String url,JSONObject params){

            RequestQueue queue = Volley.newRequestQueue(context);
            JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, url, params, new Response.Listener<org.json.JSONObject>() {
                @Override
                public void onResponse(org.json.JSONObject response) {
                    callback.onSuccess(response);
                    dialog.dismiss();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    callback.onFailed(error.getLocalizedMessage());
                    dialog.dismiss();
                }
            });
            queue.add(postRequest);

        }

    }



    public static class SimpleHttpURLWebRequest extends AsyncTask<String, String, String> {

        private OnResponseCallback callback;
        private ProgressDialog dialog;

        private SimpleHttpURLWebRequest(OnResponseCallback callback) {
            this.callback = callback;
        }

        private SimpleHttpURLWebRequest(OnResponseCallback callback,ProgressDialog dialog) {
            this.callback = callback;
            this.dialog = dialog;
        }

        @Override
        protected void onPreExecute() {
            if(dialog != null && !dialog.isShowing()) dialog.show();
        }

        @SuppressLint("NewApi")
        @Override
        protected String doInBackground(String... args) {

            String result;
            String url = args[0];
            System.out.println("Making Web Request....");
            System.out.println("Url : " + url);

            result = makeRequestHttp(url);

            System.out.println(result);
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            if (callback == null) return;

            if (result == null || result.equals("")) {
                callback.onFailed();
            } else {
                callback.onSuccess(result);
            }

            if(dialog != null)
            dialog.dismiss();
        }

        private String makeRequestHttp(String url) {

            InputStream inputStream = null;
            String result = null;

            try {

                HttpClient httpclient;
                final HttpParams httpParams = new BasicHttpParams();
                HttpConnectionParams.setConnectionTimeout(httpParams,  6 * 1000); //6 seconds timeout
                httpclient = new DefaultHttpClient(httpParams);

                HttpGet request = new HttpGet(url);

                // 8. Execute POST request to the given URL
                HttpResponse httpResponse = httpclient.execute(request);

                // 9. receive response as inputStream
                inputStream = httpResponse.getEntity().getContent();

                // 10. convert inputstream to string
                if (inputStream != null)
                    result = convertInputStreamToString(inputStream);

            } catch (Exception ex) {
                Log.d("InputStream", ex.getLocalizedMessage());
            }

            return result;
        }

        private static String convertInputStreamToString(InputStream inputStream) throws IOException {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line = "";
            StringBuilder result = new StringBuilder();
            while ((line = bufferedReader.readLine()) != null)
                result.append(line);

            inputStream.close();
            return result.toString();

        }

    }

}
