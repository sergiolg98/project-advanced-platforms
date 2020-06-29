package com.project.views.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.project.R;
import com.project.config.ConstValue;
import com.project.network.Provider;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    Context context;
    ProgressDialog dataDialog;
    SharedPreferences pref;

    Button btLogin, btRegister, btMakeLogin, btMakeRegister;
    EditText loginUsername, loginPassword, registerName, registerLastname,registerEmail, registerUsername, registerPassword;
    LinearLayout loginLayout, registerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        context = this;


        if(!checkLogin()){
            initWidgets();

            btLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(validateFields("L")){
                        userLogin();
                    }
                    else Toast.makeText(context, "Todos los campos deben ser completados", Toast.LENGTH_SHORT).show();
                }
            });

            btRegister.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    loginLayout.setVisibility(View.GONE);
                    registerLayout.setVisibility(View.VISIBLE);
                }
            });

            btMakeLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    loginLayout.setVisibility(View.VISIBLE);
                    registerLayout.setVisibility(View.GONE);
                }
            });

            btMakeRegister.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(validateFields("R")){
                        new RegisterTask().execute(
                                registerName.getText().toString(), registerLastname.getText().toString(),
                                registerUsername.getText().toString(), registerEmail.getText().toString(),
                                registerPassword.getText().toString());
                    }
                    else Toast.makeText(context, "Todos los campos deben ser completados", Toast.LENGTH_SHORT).show();
                }
            });
        }
        else{
            finish();
            startActivity(new Intent(this, DashboardActivity.class));
        }
    }

    private boolean checkLogin(){
        pref = getSharedPreferences("userAuthenticated", Context.MODE_PRIVATE);
        String state = pref.getString("userState", "");
        if(state.equals("activeUser")) return true;
        else return false; //Any other case, it will be ""
    }

    private void initWidgets(){
        loginLayout = findViewById(R.id.ly_login);
        registerLayout = findViewById(R.id.ly_register);

        btLogin = (Button) findViewById(R.id.bt_login);
        btRegister = (Button) findViewById(R.id.bt_register);

        btMakeLogin = (Button) findViewById(R.id.bt_make_login);
        btMakeRegister = (Button) findViewById(R.id.bt_make_register);

        loginUsername = (EditText) findViewById(R.id.et_username);
        loginPassword = (EditText) findViewById(R.id.et_password);

        registerName = (EditText) findViewById(R.id.et_input_names);
        registerLastname = (EditText) findViewById(R.id.et_input_lastnames);
        registerEmail = (EditText) findViewById(R.id.et_input_email);
        registerUsername = (EditText) findViewById(R.id.et_input_username);
        registerPassword = (EditText) findViewById(R.id.et_input_password);
    }

    private boolean validateFields(String type){
        if(type.equals("R")){
            //Register
            if(registerName.getText().toString().equals("")) return false;
            else if(registerLastname.getText().toString().equals("")) return false;
            else if(registerEmail.getText().toString().equals("")) return false;
            else if(registerUsername.getText().toString().equals("")) return false;
            else if(registerPassword.getText().toString().equals("")) return false;
        }
        else if(type.equals("L")){
            //Login
            if(loginUsername.getText().toString().equals("")) return false;
            else if(loginPassword.getText().toString().equals("")) return false;
        }
        return true;
    }

    /* Method using Volley Library which creates itself a background threat to perform a network operation */
    private void userLogin(){
        try {
            dataDialog = ProgressDialog.show(context, "Cargando", "Iniciando sesión ...", true);
            JSONObject jsonobj = new JSONObject();
            jsonobj.put("username", loginUsername.getText().toString());
            jsonobj.put("password", loginPassword.getText().toString());
            Log.i("JSON for SEND: ", jsonobj.toString());

            RequestQueue requestQueue;
            try {
                requestQueue = Volley.newRequestQueue(context);
                JsonObjectRequest jsonOblect = new JsonObjectRequest(Request.Method.POST,
                        ConstValue.LOGIN, jsonobj, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i("VOLLEY-Login: Success", response.toString());
                        if(response.has("response")){
                            dataDialog.dismiss();
                            try {
                                if(response.get("response").equals("success")){

                                    /* Store in SharedPreferences */
                                    SharedPreferences.Editor editor = pref.edit();
                                    editor.putString("userState", "activeUser");
                                    editor.putString("username_short", response.getJSONObject("data").getString("username"));
                                    editor.putString("userEmail", response.getJSONObject("data").getString("email"));
                                    editor.putString("userName", response.getJSONObject("data").getString("name"));
                                    editor.putString("userLastname", response.getJSONObject("data").getString("lastname"));
                                    editor.putString("userDateRegistered", response.getJSONObject("data").getString("createdAt"));
                                    editor.apply();

                                    finish();
                                    context.startActivity(new Intent(context, DashboardActivity.class));
                                    Toast.makeText(context, "Inicio de sesión exitoso.", Toast.LENGTH_SHORT).show();
                                }
                                else if(response.get("response").equals("error")){
                                    Toast.makeText(context, "Datos incorrectos.", Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(context, "Hubo un error: " + e.toString(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("VOLLEY-Login: Error", error.toString());
                        dataDialog.dismiss();
                        Toast.makeText(context, "Hubo un error.", Toast.LENGTH_SHORT).show();
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
                dataDialog.dismiss();
                Toast.makeText(context, "Error general: " + e.toString(), Toast.LENGTH_SHORT).show();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* AsyncTask for sending data */
    class RegisterTask extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dataDialog = ProgressDialog.show(context, "Cargando", "Registrando usuario ...", true);
        }
        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            dataDialog.dismiss();
            if(result.equals("")){
                Toast.makeText(context, "Registro exitoso! Ahora puedes iniciar sesión con nosotros.", Toast.LENGTH_LONG).show();
            }
            else {
                Toast.makeText(context, "Error: " + result, Toast.LENGTH_SHORT).show();
            }
        }
        @Override
        protected String doInBackground(String... params) {
            String responseString = "";
            try {
                JSONObject jsonobj = new JSONObject();
                jsonobj.put("name", params[0]);
                jsonobj.put("lastname", params[1]);
                jsonobj.put("username", params[2]);
                jsonobj.put("email", params[3]);
                jsonobj.put("password", params[4]);
                jsonobj.put("state", "active");

                Log.i("JSON for SEND: ", jsonobj.toString());
                Provider.sendPostRequest(context, jsonobj, ConstValue.USER);
            }
            catch (Exception e) {
                responseString = e.toString();
                e.printStackTrace();
            }
            return responseString;
        }
    }
}
