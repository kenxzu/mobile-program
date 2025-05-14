package com.myfirstapp.lct1;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class RedActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.merah);
    }

    public  void tekanTombol(View v){
        Toast.makeText(this, "prosesss", Toast.LENGTH_LONG).show();
        this.finish();
    }

}
