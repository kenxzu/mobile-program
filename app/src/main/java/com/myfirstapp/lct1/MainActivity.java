package com.myfirstapp.lct1;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ListView listView;
    DatabaseHelper db;
    List<Employee> employeeList;
    ArrayAdapter<String> adapter;

    @Override
    protected void onResume() {
        super.onResume();
        loadData();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db = new DatabaseHelper(this);
        listView = findViewById(R.id.listView);
        findViewById(R.id.btnAdd).setOnClickListener(v ->
                startActivity(new Intent(this, AddEditEmployeeActivity.class))
        );
        loadData();

        listView.setOnItemClickListener((parent, view, position, id) -> {
            int empId = employeeList.get(position).getId();
            Intent intent = new Intent(this, EmployeeDetailActivity.class);
            intent.putExtra("EMP_ID", empId);
            startActivity(intent);
        });
    }

    private void loadData() {
        employeeList = db.getAllEmployees();
        List<String> displayList = new ArrayList<>();
        for (Employee e : employeeList) {
            displayList.add(e.getName() + " - " + e.getPosition());
        }
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, displayList);
        listView.setAdapter(adapter);
    }
}
