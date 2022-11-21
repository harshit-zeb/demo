package com.example.myapplication2.network;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Chetan on 12/09/16.
 */
public interface VolleyCallback {
    void onSuccess(JSONObject result) throws JSONException;
    void onError(String result) throws Exception;
}