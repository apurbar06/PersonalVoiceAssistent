package com.personalvoiceassistent.actions;

import android.content.Context;
import android.os.StrictMode;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class WikipediaAction extends BaseAction {

    private static final String TAG = "WikipediaAction";
    ArrayList<String> list = new ArrayList<>();

    public WikipediaAction(Context context) {
        super(context);

        list.add("what is");
        list.add("who is");
        list.add("whois");
        this.makeListToGlobalScope(list);
    }

    @Override
    public String runCommand(String msg) {
        String raw = msg;
        for (String l :
                list) {
            raw = msg.replace(l, "").trim();
        }
        raw = raw.replace(" ", "+").trim();
        HttpHelper http = new HttpHelper();
        String resp = http.get(raw);
        Log.d(TAG, "runCommand: " + raw);
        Log.d(TAG, "runCommand res: " + resp);
        return resp;
    }

}


class HttpHelper {
    private static final String TAG = "HTTP";

    public String get(String kwd) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        String u = String.format("https://en.wikipedia.org/w/api.php?action=query&list=search&srsearch=%s&utf8=&format=json", kwd);
        HttpURLConnection conn = null;
        try {
            URL url = new URL(u);
            conn = (HttpURLConnection) url.openConnection();
            InputStream in = conn.getInputStream();
            InputStreamReader isr = new InputStreamReader(in);
            int data;
            String txt = "";
            while ((data = isr.read()) != -1) {
                txt += (char) data;
            }
            JSONObject jo = new JSONObject(txt);
            JSONObject result = jo.getJSONObject("query");
            JSONArray search = result.getJSONArray("search");
            JSONObject res = search.getJSONObject(0);
            String snippet = (String) res.get("snippet");
            String str = android.text.Html.fromHtml(snippet).toString();
            return str;
        } catch (Exception ex) {
            Log.e(TAG, "get: ", ex);
        }

        return "";
    }
}
