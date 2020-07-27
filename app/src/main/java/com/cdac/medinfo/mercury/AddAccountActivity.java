package com.cdac.medinfo.mercury;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.cdac.medinfo.mercury.common.CommonHelper;
import com.cdac.medinfo.mercury.common.MercuryConstants;
import com.cdac.medinfo.mercury.model.Account;
import com.cdac.medinfo.mercury.model.LoginDetailsDTO;
import com.cdac.medinfo.mercury.volley.SingletonRequestQueue;
import com.google.gson.Gson;

import org.apache.http.conn.ConnectTimeoutException;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParserException;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.ConnectException;
import java.net.MalformedURLException;
import java.net.SocketException;
import java.net.SocketTimeoutException;

public class AddAccountActivity extends BaseActivity implements View.OnClickListener {

    EditText editTextMercuryServerURL, editTextUsername, editTextPassword, editTextCaptchaKey, editTextCaptchaValue;
    Button buttonGo, buttonSave, buttonCancel;
    ImageButton imageButtonRefresh;
    ImageView imageViewCaptcha;
    View viewProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_account);

        viewProgressBar = findViewById(R.id.includeProgressBarAddAccount);

        editTextMercuryServerURL = findViewById(R.id.editTextMercuryServerURL);
        editTextUsername = findViewById(R.id.editTextUsername);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextCaptchaKey = findViewById(R.id.editTextCaptchaKey);
        editTextCaptchaValue = findViewById(R.id.editTextCaptchaValue);

        buttonGo = findViewById(R.id.buttonGo);
        imageButtonRefresh= findViewById(R.id.imageButtonRefresh);
        buttonSave = findViewById(R.id.buttonSave);
        buttonCancel = findViewById(R.id.buttonCancel);
        buttonGo.setOnClickListener(this);
        imageButtonRefresh.setOnClickListener(this);
        buttonSave.setOnClickListener(this);
        buttonCancel.setOnClickListener(this);

        imageViewCaptcha = findViewById(R.id.imageViewCaptcha);

        buttonSave.setEnabled(false);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonGo:
            case R.id.imageButtonRefresh:
                getCaptcha();
                break;
            case R.id.buttonSave:
                onSave();
                break;
            case R.id.buttonCancel:
                finish();
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

        String serverURL = editTextMercuryServerURL.getText().toString().trim();

        if (serverURL.length() == 0) {
            editTextMercuryServerURL.setError(getString(R.string.error_server_url));
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

                                    buttonSave.setEnabled(true);
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

    private void onSave() {

        String serverURL = editTextMercuryServerURL.getText().toString().trim();
        String username = editTextUsername.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        String captchakey = editTextCaptchaKey.getText().toString().trim();
        String captchaValue = editTextCaptchaValue.getText().toString().trim();

        if (serverURL.length() == 0) {
            editTextMercuryServerURL.setError(getString(R.string.error_server_url));
        }else if (username.length() == 0) {
            editTextUsername.setError(getString(R.string.error_username));
        } else  if (password.length() == 0) {
            editTextPassword.setError(getString(R.string.error_password));
        } else  if (captchaValue.length() == 0) {
            editTextCaptchaValue.setError(getString(R.string.error_captcha));
        }else {

            LoginDetailsDTO loginDetailsDTO = new LoginDetailsDTO(username, password, captchakey, captchaValue);
            this.validateUser(serverURL, loginDetailsDTO);
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
                                    saveAccountIntoDatabase(serverURL, loginDetailsDTO);
                                    viewProgressBar.setVisibility(View.GONE);
                                }
                            },errorListener);

                    SingletonRequestQueue.getInstance(getApplicationContext()).getRequestQueue().add(jsonObjectRequest);
                }//end if
            }//end if
        }//end if
    }//end validateUser

    private void saveAccountIntoDatabase(String serverURL, LoginDetailsDTO loginDetailsDTO){

        if ( serverURL != null
                && loginDetailsDTO != null
                && loginDetailsDTO.getLoginId() != null
                && loginDetailsDTO.getPassword() != null){

            Account account = new Account(serverURL, loginDetailsDTO.getLoginId(), loginDetailsDTO.getPassword());
            account.createAccount(this);

            Toast.makeText(this, getString(R.string.add_account_success), Toast.LENGTH_SHORT).show();
            finish();
        }//end if
    }//end saveAccountIntoDatabase


}