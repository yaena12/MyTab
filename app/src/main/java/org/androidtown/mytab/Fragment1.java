package org.androidtown.mytab;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Yeo on 2017-08-04.
 */

public class Fragment1 extends Fragment {

    String myJSON;
    String time , temp;

    TextView temperaturetime; //텍스트뷰 변수
    TextView temperature;

    private static final String TAG_RESULTS = "result";
    private static final String TAG_TIME = "time";
    private static final String TAG_TEMP = "temp";

    JSONArray peoples = null;

    ArrayList<HashMap<String, String>> personList;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        personList = new ArrayList<HashMap<String, String>>();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment1,container,false);

        temperaturetime = rootview.findViewById(R.id.timetext);
        temperature = rootview.findViewById(R.id.temptext);

        return rootview;
    }


    public void onStart() {
        super.onStart();
        getData("http://211.253.10.200/getdata.php");
    }

    public void onResume(){
        super.onResume();
    }


    protected void showList() {
        try {
            JSONObject jsonObj = new JSONObject(myJSON);
            peoples = jsonObj.getJSONArray(TAG_RESULTS);

            for (int i = 0; i < peoples.length(); i++) {
                JSONObject c = peoples.getJSONObject(i);
                time = c.getString(TAG_TIME);
                temp = c.getString(TAG_TEMP);

                HashMap<String, String> persons = new HashMap<String, String>();

                persons.put(TAG_TIME, time);
                persons.put(TAG_TEMP, temp);

            }

            temperaturetime.setText(time);
            temperature.setText(temp);


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void getData(String url) {
        class GetDataJSON extends AsyncTask<String, Void, String> {

            @Override
            protected String doInBackground(String... params) {

                String uri = params[0];

                BufferedReader bufferedReader = null;
                try {
                    URL url = new URL(uri);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    StringBuilder sb = new StringBuilder();

                    bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));

                    String json;
                    while ((json = bufferedReader.readLine()) != null) {
                        sb.append(json + "\n");
                    }

                    return sb.toString().trim();

                } catch (Exception e) {
                    return null;
                }
            }

            @Override
            protected void onPostExecute(String result) {
                myJSON = result;
                showList();
            }
        }
        GetDataJSON g = new GetDataJSON();
        g.execute(url);
    }
}