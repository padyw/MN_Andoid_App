package com.cdac.medinfo.mercury.common;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.cdac.medinfo.mercury.R;

import org.apache.http.conn.ConnectTimeoutException;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParserException;

import java.io.UnsupportedEncodingException;
import java.net.ConnectException;
import java.net.MalformedURLException;
import java.net.SocketException;
import java.net.SocketTimeoutException;

public class CommonHelper {

    public static void handleError(VolleyError error, Context applicationContext) {
/*
        Log.e("ErrorListener", "" + error.toString());
        if (error instanceof NetworkError) {
            Toast.makeText(applicationContext, "No network available", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(applicationContext, error.toString(), Toast.LENGTH_LONG).show();
        }

 */
        if (error != null) {
            if(error instanceof NoConnectionError){
                Toast.makeText(applicationContext, "Your device is not connected to internet.", Toast.LENGTH_SHORT).show();
            } else if (error instanceof NetworkError
                    || error.getCause() instanceof ConnectException){
                Toast.makeText(applicationContext, "Your device is not connected to internet.", Toast.LENGTH_SHORT).show();
            } else if (error.getCause() instanceof MalformedURLException){
                Toast.makeText(applicationContext, "Bad Request.", Toast.LENGTH_SHORT).show();
            } else if (error instanceof ParseError
                    || error.getCause() instanceof IllegalStateException
                    || error.getCause() instanceof JSONException
                    || error.getCause() instanceof XmlPullParserException){
                Toast.makeText(applicationContext, "Parse Error (because of invalid json or xml).", Toast.LENGTH_SHORT).show();
            } else if (error.getCause() instanceof OutOfMemoryError){
                Toast.makeText(applicationContext, "Out Of Memory Error.", Toast.LENGTH_SHORT).show();
            }else if (error instanceof AuthFailureError){
                Toast.makeText(applicationContext, "server couldn't find the authenticated request.", Toast.LENGTH_SHORT).show();
            } else if (error instanceof ServerError || error.getCause() instanceof ServerError) {
//                Toast.makeText(applicationContext, "Server is not responding.", Toast.LENGTH_SHORT).show();
                if (error.networkResponse != null){
                    Log.d("Error", "Code " + error.networkResponse.statusCode);

                    switch(error.networkResponse.statusCode) {
                        case 400:
                        {
                            if (error.networkResponse.data != null) {
                                try {
                                    String responseBody = new String(error.networkResponse.data, "utf-8");
                                    Log.d("Error", "Data " + responseBody);
                                    JSONObject data = new JSONObject(responseBody);
                                    Log.d("Error", "Data " +  data.getInt("errorcode"));
                                    int errorCode = data.getInt("errorcode");
                                    switch(errorCode) {
                                        case 600:
                                        case 629:
                                            ;
                                            Toast.makeText(applicationContext, R.string.captcha_timeout, Toast.LENGTH_LONG).show();
                                            break;
                                        case 626:
                                            Toast.makeText(applicationContext,  R.string.invalid_username_password, Toast.LENGTH_LONG).show();
                                            break;
                                        case 627:
                                            Toast.makeText(applicationContext,  R.string.unable_to_complete_request, Toast.LENGTH_LONG).show();
                                            break;
                                        case 628:
                                            Toast.makeText(applicationContext,  R.string.invalid_captcha_value, Toast.LENGTH_LONG).show();
                                            break;
                                        default:
                                            Toast.makeText(applicationContext, "An unknown error occurred.", Toast.LENGTH_LONG).show();
                                    }//end switch

                                } catch (UnsupportedEncodingException | JSONException e) {
                                    e.printStackTrace();
                                    Toast.makeText(applicationContext, "An unknown error occurred.", Toast.LENGTH_LONG).show();
                                }//end catch
                            }//end if
                        }
                        break;
                        case 404:
                            Toast.makeText(applicationContext,  "Connection timeout error", Toast.LENGTH_LONG).show();
                            break;
                        case 500:
                            Toast.makeText(applicationContext,  R.string.internal_error, Toast.LENGTH_LONG).show();
                            break;
                        default:
                            Toast.makeText(applicationContext, "An unknown error occurred.", Toast.LENGTH_LONG).show();
                    }//end switch

                } else{
                    Toast.makeText(applicationContext, "An unknown error occurred.", Toast.LENGTH_LONG).show();
                }
            }else if (error instanceof TimeoutError
                    || error.getCause() instanceof SocketTimeoutException
                    || error.getCause() instanceof ConnectTimeoutException
                    || error.getCause() instanceof SocketException) {
                Toast.makeText(applicationContext, "Connection timeout error", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(applicationContext, "An unknown error occurred.", Toast.LENGTH_SHORT).show();
            }//end else
        }//end if
    }
}
