package com.project.views.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.project.R;
import com.project.config.ConstValue;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class NewsFeedActivity extends AppCompatActivity {

    private Context context;
    private RecyclerView recyclerView;
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_feed);

        context = this;

        pDialog = new ProgressDialog(context);
        pDialog.setMessage("Loading...");
        pDialog.show();

        RequestQueue requestQueue;
        try {
            requestQueue = Volley.newRequestQueue(getApplicationContext());
            JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, ConstValue.IMAGE, null,
                    new Response.Listener<JSONObject>()
                    {
                        @Override
                        public void onResponse(JSONObject response) {
                            hidePDialog();
                            Log.i("VOLLEY-Img: Success", response.toString());
                            
                        }
                    },
                    new Response.ErrorListener()
                    {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d("Error.Response", "nope");
                        }
                    }
            );
            requestQueue.add(getRequest);

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void initWidgets(){
        recyclerView = findViewById(R.id.recycler_view);
    }
    private void hidePDialog() {
        if (pDialog != null) {
            pDialog.dismiss();
            pDialog = null;
        }
    }
}
