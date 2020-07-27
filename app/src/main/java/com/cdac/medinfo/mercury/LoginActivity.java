package com.cdac.medinfo.mercury;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.cdac.medinfo.mercury.common.CommonHelper;
import com.cdac.medinfo.mercury.common.MercuryConstants;
import com.cdac.medinfo.mercury.model.Account;
import com.cdac.medinfo.mercury.model.LoginDetailsDTO;
import com.cdac.medinfo.mercury.volley.SingletonRequestQueue;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;

public class LoginActivity extends BaseActivity implements View.OnClickListener{

    EditText editTextUsername, editTextPassword, editTextCaptchaKey, editTextCaptchaValue;
    Button buttonLogin;
    ImageView imageViewCaptcha;
    ImageButton imageButtonRefresh;
    View viewProgressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        viewProgressBar = findViewById(R.id.includeProgressBarAddAccount);

        editTextUsername = findViewById(R.id.editTextUsername);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextCaptchaKey = findViewById(R.id.editTextCaptchaKey);
        editTextCaptchaValue = findViewById(R.id.editTextCaptchaValue);

        imageViewCaptcha = findViewById(R.id.imageViewCaptcha);
        imageButtonRefresh= findViewById(R.id.imageButtonRefresh);
        imageButtonRefresh.setOnClickListener(this);

        buttonLogin = findViewById(R.id.buttonLogin);
        buttonLogin.setOnClickListener(this);

        Intent intent = getIntent();
        Account account = (Account) intent.getSerializableExtra("account");
        if (account != null){
            Log.e("LoginActivity:onCreate", "" + account.toString());

            editTextUsername.setText(account.getUsername());
            editTextUsername.setEnabled(false);
            editTextPassword.setText(account.getPassword());
            editTextPassword.setEnabled(false);

            getCaptcha();
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imageButtonRefresh:
                getCaptcha();
                break;
            case R.id.buttonLogin:
                onLogin();
                break;
        }
    }

    Response.ErrorListener errorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            viewProgressBar.setVisibility(View.GONE);
            CommonHelper.handleError(error, getApplicationContext());
        }//end onErrorResponse
    };

    private void getCaptcha() {

        String serverURL = null;
        Intent intent = getIntent();
        Account account = (Account) intent.getSerializableExtra("account");
        if (account != null){
            serverURL = account.getServerURL();
        }

        if (serverURL == null || serverURL.length() == 0) {
            Toast.makeText(this, getString(R.string.internal_error), Toast.LENGTH_LONG).show();
        }else {
            String url = serverURL + MercuryConstants.CAPTCHA_GET_URI;

            viewProgressBar.setVisibility(View.VISIBLE);
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                    (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {

                            Log.e("getCaptcha", "" + response.toString());
                            try {
                                String captchaKey = response.getString("captchaKey");
                                String imageString = response.getString("captchaImage");
                                if (imageString != null && captchaKey != null) {

                                    editTextCaptchaKey.setText(captchaKey);

                                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                    byte[] imageBytes = baos.toByteArray();
                                    imageBytes = Base64.decode(imageString, Base64.DEFAULT);
                                    Bitmap decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                                    imageViewCaptcha.setImageBitmap(decodedImage);

                                }//end if

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }//end catch
                            viewProgressBar.setVisibility(View.GONE);
                        }
                    },errorListener);

            SingletonRequestQueue.getInstance(getApplicationContext()).getRequestQueue().add(jsonObjectRequest);

        }//end else

    }//end getCaptcha

    private void onLogin() {

        String username = editTextUsername.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        String captchakey = editTextCaptchaKey.getText().toString().trim();
        String captchaValue = editTextCaptchaValue.getText().toString().trim();

        if (username.length() == 0) {
            editTextUsername.setError(getString(R.string.error_username));
        } else  if (password.length() == 0) {
            editTextPassword.setError(getString(R.string.error_password));
        } else  if (captchaValue.length() == 0) {
            editTextCaptchaValue.setError(getString(R.string.error_captcha));
        }else {

            Intent intent = getIntent();
            Account account = (Account) intent.getSerializableExtra("account");

            LoginDetailsDTO loginDetailsDTO = new LoginDetailsDTO(username, password, captchakey, captchaValue);
            this.validateUser(account.getServerURL(), loginDetailsDTO);
        }
    }

    private void validateUser(final String serverURL, final LoginDetailsDTO loginDetailsDTO) {

        if (loginDetailsDTO != null && loginDetailsDTO.getLoginId() != null
                && loginDetailsDTO.getPassword() != null && loginDetailsDTO.getCaptchaKey() != null
                && loginDetailsDTO.getCaptchaValue() != null && serverURL != null){

            if (loginDetailsDTO.getLoginId().length() != 0
                    && loginDetailsDTO.getPassword().length() != 0
                    && loginDetailsDTO.getCaptchaKey().length() != 0
                    && loginDetailsDTO.getCaptchaValue().length() != 0
                    && serverURL.length() != 0){

                String url = serverURL + MercuryConstants.LOGIN_POST_URI;

                Gson gson = new Gson();
                String jsonString = gson.toJson(loginDetailsDTO);
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(jsonString);
                    jsonObject.toString();
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(this,  R.string.internal_error, Toast.LENGTH_LONG).show();
                }
                if (jsonObject != null) {
                    viewProgressBar.setVisibility(View.VISIBLE);
                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                            (Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {

                                @Override
                                public void onResponse(JSONObject response) {
                                    Log.e("login", "" + response.toString());
//                                    saveAccountIntoDatabase(serverURL, loginDetailsDTO);
                                    viewProgressBar.setVisibility(View.GONE);

                                    Toast.makeText(LoginActivity.this, "Login Successfully", Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();

                                }
                            },errorListener);

                    SingletonRequestQueue.getInstance(getApplicationContext()).getRequestQueue().add(jsonObjectRequest);
                }//end if
            }//end if
        }//end if
    }//end validateUser

}