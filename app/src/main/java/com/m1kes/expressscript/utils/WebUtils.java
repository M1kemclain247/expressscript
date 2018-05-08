package com.m1kes.expressscript.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class WebUtils {

    public interface OnResponseCallback {
        void onSuccess(String response);
        void onFailed();
    }

    public static SimpleHttpURLWebRequest getSimpleHttpRequest(OnResponseCallback callback) {
        return new SimpleHttpURLWebRequest(callback);
    }

    public static JsonWebPost postJsonRequest(Context context,OnResponseCallback callback) {
        return new JsonWebPost(context,callback);
    }

    public static class JsonWebPost extends AsyncTask<Object,String,Void>{

        private OnResponseCallback callback;
        @SuppressLint("StaticFieldLeak")
        private Context context;

        private JsonWebPost(Context context,OnResponseCallback callback) {
            this.callback = callback;
            this.context = context;
        }

        @Override
        protected Void doInBackground(Object... args) {

            String url = (String)args[0];
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
                        }
                    },
                    new Response.ErrorListener()
                    {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            callback.onFailed();
                            Log.d("Error.Response", error.getLocalizedMessage());
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



    public static class SimpleHttpURLWebRequest extends AsyncTask<String, String, String> {

        private OnResponseCallback callback;

        private SimpleHttpURLWebRequest(OnResponseCallback callback) {
            this.callback = callback;
        }

        @Override
        protected void onPreExecute() {

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

        }

        private String makeRequestHttp(String url) {

            InputStream inputStream = null;
            String result = null;

            try {

                HttpClient httpclient;
                final HttpParams httpParams = new BasicHttpParams();
                HttpConnectionParams.setConnectionTimeout(httpParams, 4000);
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
