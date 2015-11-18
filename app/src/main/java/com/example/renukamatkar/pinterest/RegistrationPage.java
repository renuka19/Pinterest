package com.example.renukamatkar.pinterest;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.app.ActionBar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class RegistrationPage extends AppCompatActivity {
// http://pintrestproj.net46.net//register.php

    EditText name,lastname,email,password;
    Button reg;
    int id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_page);
        name = (EditText)findViewById(R.id.FirstNameRegMessage);
        lastname = (EditText)findViewById(R.id.LastNameRegMessage);
        email = (EditText)findViewById(R.id.EmailRegMessage);
        password = (EditText)findViewById(R.id.PasswordRegMessage);
        reg = (Button)findViewById(R.id.regbutton);
        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!email.getText().toString().matches("[a-zA-Z0-9._-]+@[a-z]+.[a-z]+")|| TextUtils.isEmpty(email.getText().toString())==true || email.getText().toString().trim().equals(""))
                    {
                    email.setError("Please enter a valid email address");
                    }else{
                SendData sd = new SendData();
                sd.execute();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_registration_page, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    class SendData extends AsyncTask<String,Integer,String>{

    ProgressDialog pd ;


        HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost("http://pintrestproj.net46.net/register.php");
        HttpResponse response;
        String result;
        JSONObject jo;
        MultipartEntity entity = new MultipartEntity();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(RegistrationPage.this);
            pd.setMessage("Loading");
            pd.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                entity.addPart("name",name.getText().toString() +" "+lastname.getText().toString());
                entity.addPart("email",email.getText().toString());
                entity.addPart("pass",password.getText().toString());
                post.setEntity(entity);
                response = client.execute(post);
                result = EntityUtils.toString(response.getEntity());
                jo = new JSONObject(result);
                id = jo.getInt("id");
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }


        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            pd.dismiss();
            if(id>0)
            {

                
                    Toast.makeText(RegistrationPage.this, "Successfully registered with id : " + id, Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(RegistrationPage.this, Login_page.class));

                }else{
                Toast.makeText(RegistrationPage.this, "This email is already registered", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
