package com.example.myapplication2;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;



public class VolleyRequest extends Request {


    private final Response.Listener mListener;
    private final String TAG = VolleyRequest.class.getSimpleName();
    public Context context;
    private final String mBody;
    private String mContentType;
    private final String url;
    private final HashMap<String, String> mCustomHeaders;
    private boolean isNewApiServer = false;


    private final String CACHE_HEADER = "X-HCACHE";

    VolleyRequest(int method, String url, HashMap<String, String> customHeaders, String
            body, Response.Listener listener, Response.ErrorListener errorListener,
                  boolean isNewApiServer) {
        super(method, url, errorListener);
        mCustomHeaders = customHeaders;
        this.url = url;
        mBody = body;
        this.isNewApiServer = isNewApiServer;
        mListener = listener;
        if (isNewApiServer) {
            mContentType = "application/json";
        } else {
            mContentType = "application/x-www-form-urlencoded";
        }
    }


    @Override
    protected String getParamsEncoding() {
        return "utf-8";
    }

    @Override
    public HashMap<String, String> getHeaders() throws AuthFailureError {
        if (mCustomHeaders != null) {
            return mCustomHeaders;
        }
        return (HashMap<String, String>) super.getHeaders();
    }

    @Override
    public byte[] getBody() {
        return mBody == null ? null : mBody.getBytes(StandardCharsets.UTF_8);
    }

    @Override
    public String getBodyContentType() {
        if (isNewApiServer) {
            return "application/json; charset=" + getParamsEncoding();
        }
        return mContentType + "charset=" + getParamsEncoding();
    }

    public void setContentType(String mContentType) {
        this.mContentType = mContentType;
    }

    @Override
    protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
        try {

            String jsonString = new String(response.data, HttpHeaderParser.parseCharset(response
                    .headers));
            return Response.success(new JSONObject(jsonString), HttpHeaderParser
                    .parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (JSONException je) {
            return Response.error(new ParseError(je));
        }
    }

    @Override
    protected VolleyError parseNetworkError(VolleyError volleyError) {
        return volleyError;
    }

    @Override
    protected void deliverResponse(Object response) {
        mListener.onResponse(response);
    }

    @Override
    public int compareTo(Object another) {
        return 0;
    }
}
