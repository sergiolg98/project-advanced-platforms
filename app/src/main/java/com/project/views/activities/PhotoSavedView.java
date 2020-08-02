package com.project.views.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.project.R;
import com.project.config.ConstValue;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class PhotoSavedView extends AppCompatActivity {

    private Context context;
    private ImageView previewPhoto, buttonShare;
    private EditText inputDescription;
    private TextView txUsername;
    private SharedPreferences pref;
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_saved_view);

        context = this;
        initWidgets();

        pref = getSharedPreferences("userAuthenticated", Context.MODE_PRIVATE);
        txUsername.setText("@" + pref.getString("username_short", ""));
        Glide.with(this).load(getPhotoPath()).into(previewPhoto);


        buttonShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                pDialog = new ProgressDialog(context);
                pDialog.setMessage("Loading...");
                pDialog.show();

                //Transform image into Base64
                String imageEncode = "error";
                File imageFile = new File(getPhotoPath());
                if(imageFile.exists()) {
                    Bitmap bmInitial = BitmapFactory.decodeFile(getPhotoPath());
                    Bitmap bm = Bitmap.createScaledBitmap(bmInitial, 650, 400, false);
                    ByteArrayOutputStream bao = new ByteArrayOutputStream();
                    bm.compress(Bitmap.CompressFormat.JPEG, 90, bao);
                    byte[] ba = bao.toByteArray();
                    imageEncode = Base64.encodeToString(ba, Base64.DEFAULT);
                }

                JSONObject jsonobj = new JSONObject();
                try {
                    jsonobj.put("user_id", pref.getString("userId", ""));
                    jsonobj.put("user_name", pref.getString("username_short", ""));
                    jsonobj.put("description", inputDescription.getText().toString());
                    jsonobj.put("image", imageEncode);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.i("JSON for SEND: ", jsonobj.toString());

                RequestQueue requestQueue;
                try {
                    requestQueue = Volley.newRequestQueue(getApplicationContext());
                    JsonObjectRequest jsonOblect = new JsonObjectRequest(Request.Method.POST,
                            ConstValue.IMAGE, jsonobj, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            hidePDialog();
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
        });

    }

    private String getPhotoPath(){
        return Environment.getExternalStorageDirectory() + "/" + ConstValue.getPhotoPath();
    }

    private void initWidgets(){
        previewPhoto = findViewById(R.id.img_preview);
        inputDescription = findViewById(R.id.et_description);
        txUsername = findViewById(R.id.tx_username_preview);
        buttonShare = findViewById(R.id.bt_share);
    }

    private void hidePDialog() {
        if (pDialog != null) {
            pDialog.dismiss();
            pDialog = null;
        }
    }
}
