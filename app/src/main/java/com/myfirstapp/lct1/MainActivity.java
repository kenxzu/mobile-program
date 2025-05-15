package com.myfirstapp.lct1;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import android.view.View;
import androidx.appcompat .app.AppCompatActivity;
import com.myfirstapp.lct1.R;
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void pindahMerah(View v) {
        switch (v.getId()) {
            case R.id.btnred:
                startActivity(new Intent(this, RedActivity.class));
                break;
            case R.id.btngreen:
                startActivity(new Intent(this, GreenActivity.class));
                break;
        }
    }


    // Inflate the menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.top_menu, menu);
        return true;
    }

    // Handle menu item clicks
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menu_settings) {
            Toast.makeText(this, "Settings clicked", Toast.LENGTH_SHORT).show();
            return true;
        } else if (id == R.id.menu_help) {
            Toast.makeText(this, "Help clicked", Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
