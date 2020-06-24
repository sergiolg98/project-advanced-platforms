package com.project.views.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.project.R;
import com.project.config.ConstValue;
import com.project.network.Provider;

import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    Context context;
    ProgressDialog dataDialog;

    Button btLogin, btRegister, btMakeLogin, btMakeRegister;
    EditText loginUsername, loginPassword, registerName, registerLastname,registerEmail, registerUsername, registerPassword;
    LinearLayout loginLayout, registerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        context = this;
        initWidgets();

        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Simular Login ...", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(LoginActivity.this, DashboardActivity.class);
                startActivity(i);
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
        }
        return true;
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
                finish();
                context.startActivity(new Intent(context, DashboardActivity.class));
                Toast.makeText(context, "Registro exitoso", Toast.LENGTH_SHORT).show();
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
