package com.example.dell.cleancare;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ThingSpeakFetch2 extends AsyncTask<Void,Void,Void> {
    private List<ThingFetch> fetchList = new ArrayList<>();

    private String data ="";
    private String strParsedValue1 = null;
    private String created_at = null;
    private String entry_id = null;
    public static String gsensor = "";
    public static String counter = "";
    public static String id="";

    public String getSensor() {
        return this.gsensor;
    }

    public String getCounter() {
        return this.counter;
    }

    private MeterFragment updateReadings = new MeterFragment();

    public ThingSpeakFetch2() {}

    public ThingSpeakFetch2(MeterFragment updateReadings) {
        this.updateReadings = updateReadings;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        try{
            URL url  = new URL("https://api.thingspeak.com/channels/714434/feeds.json?api_key=L3V9ETEO056Y5L2I");
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line = "";
            while(line != null){
                line = bufferedReader.readLine();
                data = data + line;
            }
            JSONObject jsonObject = new JSONObject(data);
            JSONObject object = jsonObject.getJSONObject("channel");
            id =Integer.toString(object.getInt("id"));
            strParsedValue1="Name:"+ object.getString("name")+"\n"+
                    "Description:"+ object.getString("description")+"\n"+
                    "Latitude:"+ object.getString("latitude")+"\n"+
                    "Longitude:"+ object.getString("longitude")+"\n"+
                    "Gas Sensor:"+ object.getString("field1")+"\n"+
                    "People Counter:"+ object.getString("field2")+"\n"+
                    "Created At:"+ object.getString("created_at")+"\n"+
                    "LastEntryID"+ Integer.toString(object.getInt("last_entry_id"))+"\n";

            JSONArray subArray = jsonObject.getJSONArray("feeds");
            for(int i=0; i<subArray.length(); i++){
                created_at="Created At:"+subArray.getJSONObject(i).getString("created_at");
                entry_id="Entry ID:"+ Integer.toString(subArray.getJSONObject(i).getInt("entry_id"));
                gsensor=subArray.getJSONObject(i).getString("field1");
                counter=subArray.getJSONObject(i).getString("field2");
            }
            updateReadings.updateSensorReadings(id,gsensor, counter);


        }catch (IOException e1){
            e1.printStackTrace();
        }catch (NullPointerException ex) {
            ex.printStackTrace();
        }catch (JSONException ex) {
            ex.printStackTrace();
        }
        return null;
    }


    @Override
    public void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        fetchList.add( new ThingFetch(this.id,this.gsensor,this.counter) );
    }
}

