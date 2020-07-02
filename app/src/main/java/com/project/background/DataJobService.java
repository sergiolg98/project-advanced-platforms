package com.project.background;

import android.annotation.SuppressLint;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.project.config.ConstValue;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataJobService extends JobService {
    private static final String TAG = "DataJobService";
    private boolean jobCancelled = false;

    @Override
    public boolean onStartJob(JobParameters params) {
        Log.d(TAG, "Job started");
        doBackgroundWork(params);
        return true;
    }

    private void doBackgroundWork(JobParameters params){
        if(jobCancelled) return;

        /* Anadir el envio de coordenadas GPS */
        LocationManager mLocationManager = (LocationManager)getApplicationContext().getSystemService(LOCATION_SERVICE);
        List<String> providers = mLocationManager.getProviders(true);
        Location bestLocation = null;
        for (String provider : providers) {
            @SuppressLint("MissingPermission") Location l = mLocationManager.getLastKnownLocation(provider);
            if (l == null) {
                continue;
            }
            if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
                bestLocation = l;
            }
        }

        Location location = bestLocation;

        try {
            SharedPreferences pref = getSharedPreferences("userAuthenticated", Context.MODE_PRIVATE);

            JSONObject jsonobj = new JSONObject();
            jsonobj.put("latitude", String.valueOf(location.getLatitude()));
            jsonobj.put("longitude", String.valueOf(location.getLongitude()));
            jsonobj.put("user_id", pref.getString("userId", ""));
            jsonobj.put("timestamp", String.valueOf(new Date()));
            jsonobj.put("temperature", "-");
            Log.i("JSON for SEND: ", jsonobj.toString());

            RequestQueue requestQueue;
            try {
                requestQueue = Volley.newRequestQueue(getApplicationContext());
                JsonObjectRequest jsonOblect = new JsonObjectRequest(Request.Method.POST,
                        ConstValue.INFORMATION, jsonobj, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i("VOLLEY-Login: Success", response.toString());
                        if(response.has("response")){
                            try {
                                if(response.get("response").equals("success")){
                                    Log.i("InfoService", "Information Sent and received");
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("VOLLEY-Login: Error", error.toString());
                    }
                }) {
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        final Map<String, String> headers = new HashMap<>();
                        return headers;
                    }
                };
                requestQueue.add(jsonOblect);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        /* End data sending */
        //Toast.makeText(getApplicationContext(), "Doing background work", Toast.LENGTH_SHORT).show(); //No tiene que ser visible al usuario
        Log.i(TAG, "Job finished");
        jobFinished(params, false);
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        Log.i(TAG, "Job cancelled before completition");
        jobCancelled = true;
        return true;
    }
}
