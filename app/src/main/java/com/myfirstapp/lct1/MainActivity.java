package com.myfirstapp.lct1;


import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private Button btnSubmit;
    private String path = "https://gist.githubusercontent.com/keeguon/2310008/raw/bdc2ce1c1e3f28f9cab5b4393c7549f38361be4e/countries.json";

    private Activity activity;
    private ArrayList<Country> countries = new ArrayList<>();
    private ProgressDialog progressDialog;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        activity = this;
        btnSubmit = findViewById(R.id.tombol);
        listView = findViewById(R.id.daftar);

        btnSubmit.setOnClickListener(v -> {
            countries.clear();
            new GetServerData().execute();
        });
    }

    @SuppressWarnings("deprecation")
    private class GetServerData extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setMessage("Membaca data...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(Void... voids) {
            return fetchCountriesFromWeb();
        }

        @Override
        protected void onPostExecute(String jsonResponse) {
            super.onPostExecute(jsonResponse);
            if (progressDialog.isShowing()) progressDialog.dismiss();

            if (jsonResponse == null || jsonResponse.isEmpty()) {
                Toast.makeText(activity, "Gagal mengambil data", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                JSONArray jsonArray = new JSONArray(jsonResponse);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonobject = jsonArray.getJSONObject(i);
                    String code = jsonobject.getString("code");
                    String name = jsonobject.getString("name");
                    countries.add(new Country(code, name));
                }

                CustomCountryList adapter = new CustomCountryList(activity, countries);
                listView.setAdapter(adapter);

                listView.setOnItemClickListener((parent, view, position, id) -> {
                    String selected = countries.get(position).getCountryName();
                    Toast.makeText(getApplicationContext(), "You selected " + selected, Toast.LENGTH_SHORT).show();
                });

            } catch (JSONException e) {
                Toast.makeText(activity, "Error parsing data", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }

        private String fetchCountriesFromWeb() {
            StringBuilder response = new StringBuilder();
            try {
                URL url = new URL(path);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(15000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("GET");

                int responseCode = conn.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    reader.close();
                } else {
                    return null;
                }
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }

            return response.toString();
        }
    }
}
