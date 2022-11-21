package com.example.myapplication2.network;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.HttpHeaderParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class CustomJSONObjectRequest extends Request<JSONObject> {

    private Listener<JSONObject> listener;
    Priority mPriority;
    private final HashMap<String, String> mCustomHeaders;
    private final String mBody;
    private String mContentType;

    public CustomJSONObjectRequest(int method,HashMap<String, String> customHeaders, String url, String
                                    body,
                                   Listener<JSONObject> responseListener, ErrorListener errorListener) {
        super(method, url, errorListener);
        mCustomHeaders = customHeaders;
        this.listener = responseListener;
        this.mBody = body;
    }
    @Override
    protected String getParamsEncoding() {
        return "utf-8";
    }

    @Override
    public String getBodyContentType() {
        return mContentType + "charset=" + getParamsEncoding();
    }

    @Override
    public byte[] getBody() {
        return mBody == null ? null : mBody.getBytes(StandardCharsets.UTF_8);
    }

    @Override
    protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
        try {
            String jsonString = new String(response.data,
                    HttpHeaderParser.parseCharset(response.headers));
            return Response.success(new JSONObject(jsonString),
                    HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (JSONException je) {
            return Response.error(new ParseError(je));
        }
    }

    @Override
    protected void deliverResponse(JSONObject response) {
        listener.onResponse(response);
    }
    @Override
    public HashMap<String, String> getHeaders() throws AuthFailureError {
        if (mCustomHeaders != null) {
            return mCustomHeaders;
        }
        return (HashMap<String, String>) super.getHeaders();
    }
    public void setPriority(Priority priority) {
        mPriority = priority;
    }

    @Override
    public Priority getPriority() {
        // If we didn't use the setPriority method,
        // the priority is automatically set to NORMAL
        return mPriority != null ? mPriority : Priority.NORMAL;
    }

}