package org.androidtown.mytab;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

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
 * Created by Lynn on 2017-08-04.
 */

public class Fragment1 extends Fragment {

    String myJSON;
    String time , temp;

    TextView temperaturetime; //텍스트뷰 변수
    TextView temperature;

    Button button;
    ToggleButton toggle;

    ImageView imageview1 = null, imageview2 = null, imageview = null;


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
    private void showMessage(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("환수");
        builder.setMessage("환수를 하시겠습니까?");
        builder.setIcon(android.R.drawable.ic_dialog_alert);
        builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.dismiss();
                //String message = "예 버튼이 눌렸습니다.";
               // textView.setText(message);
            }
        });

        builder.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //Toast.makeText(getApplicationContext(), "아니오 버튼이 눌렸습니다.",Toast.LENGTH_LONG).show();
                //String message = "아니오 버튼이 눌렸습니다.";
                //textView.setText(message);
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }




 /*   private void displayTemperature(int temperature) {
        TextView teperatureTextView = (TextView) findViewById(R.id.temperature_text_view);
        temperatureTextView.setText("" + temperature);
    }


    private void displayWater(int waterLevel) {
        TextView waterTextView = (TextView) findViewById(R.id.water_text_view);
        temperatureTextView.setText("" + waterLevel);
    }*/

//test

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment1,container,false);

        temperaturetime = rootview.findViewById(R.id.timetext);
        temperature = rootview.findViewById(R.id.temptext);

        imageview = rootview.findViewById(R.id.imageView);


        button = rootview.findViewById(R.id.waterButton);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                showMessage();
            }
        });


        toggle = rootview.findViewById(R.id.lightButton);
        toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    imageview.setImageResource(R.drawable.fishtankdefault);


                    // The toggle is enabled
                } else {
                    imageview.setImageResource(R.drawable.fishtanklampon);

                    // The toggle is disabled
                }
            }
        });



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