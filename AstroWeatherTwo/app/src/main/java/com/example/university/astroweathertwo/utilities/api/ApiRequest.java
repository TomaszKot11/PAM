package com.example.university.astroweathertwo.utilities.api;

import android.os.Build;
import android.util.Log;
import com.android.volley.*;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonRequest;
import com.example.university.astroweathertwo.BuildConfig;
import com.example.university.astroweathertwo.utilities.ProjectConstants;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import com.google.gson.reflect.TypeToken;
import net.oauth.OAuth;
import net.oauth.OAuthAccessor;
import net.oauth.OAuthConsumer;
import net.oauth.OAuthException;
import net.oauth.OAuthMessage;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


public class ApiRequest<T> extends JsonRequest<T>  {
    // https://medium.com/code-better/hiding-api-keys-from-your-android-repository-b23f5598b906
    private final String appId =  BuildConfig.AppId;
    private final String CONSUMER_KEY = BuildConfig.ConsumerKey;
    private final String CONSUMER_SECRET = BuildConfig.ConsumerSecret;
    private final String baseUrl = BuildConfig.BaseUrl;
    private String location = "lodz,pl";
    private String longitude;
    private String latitude;
    private boolean isCoordinateRequest = false;

    // constructor usin location
    public ApiRequest(int method, String url, String requestBody, String location, Response.Listener<T> listener, Response.ErrorListener errorListener) {
        super(method, url, requestBody, listener, errorListener);
        if(location != null)
            this.location = location;
    }

    // constructor using coordinates
    public ApiRequest(int method, String url, boolean isCoordinateRequest, String requestBody, String longitude, String latitude, Response.Listener<T> listener, Response.ErrorListener errorListener) {
        super(method, url, requestBody, listener, errorListener);
        if(longitude != null)
            this.longitude = longitude;

        if(latitude != null)
            this.latitude = latitude;

        this.isCoordinateRequest = true;
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        Map<String, String> headers = new HashMap<>();
        OAuthConsumer consumer = new OAuthConsumer(null, CONSUMER_KEY, CONSUMER_SECRET, null);
        consumer.setProperty(OAuth.OAUTH_SIGNATURE_METHOD, OAuth.HMAC_SHA1);
        OAuthAccessor accessor = new OAuthAccessor(consumer);
        try {
            OAuthMessage request = accessor.newRequestMessage(OAuthMessage.GET, getUrl(), null);
            String authorization = request.getAuthorizationHeader(null);
            headers.put("Authorization", authorization);
        } catch (OAuthException | IOException | URISyntaxException e) {
            throw new AuthFailureError(e.getMessage());
        }

        headers.put("X-Yahoo-App-Id", appId);
        headers.put("Content-Type", "application/json");
        return headers;
    }


    public void setLocation(String location) {
        this.location = location;
    }


    @Override
    public String getUrl() {
        String url =  this.isCoordinateRequest ? getCoordinateUrl() : getLocalizationUrl();
        return url;
    }

    private String getCoordinateUrl() {
        return baseUrl + "?lat=" + this.latitude +  "&lon=" + this.longitude + "&format=json";
    }

    private String getLocalizationUrl() {
        return baseUrl + "?location="+ location + "&format=json";
    }

    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {
        try {
            String json = new String(
                    response.data,
                    HttpHeaderParser.parseCharset(response.headers));

            T parsedResponse = parseResponse(json);

            return Response.success(
                    parsedResponse,
                    HttpHeaderParser.parseCacheHeaders(response));

        } catch (UnsupportedEncodingException | JsonSyntaxException | JSONException e) {
            return Response.error(new ParseError(e));
        }
    }


    //TODO: here we are doing json parsing
    private T parseResponse(String jsonObject) throws JSONException {

        JSONObject jsonObject1 = new JSONObject(jsonObject);

        //getJSONObject() gwtJSONArray() get()

        return (T)jsonObject1;
    }
}
