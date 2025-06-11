package com.myfirstapp.lct1;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class EmployeeDetailActivity extends AppCompatActivity {
    TextView txtDetail;
    int empId;
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        txtDetail = findViewById(R.id.txtDetail);
        db = new DatabaseHelper(this);
        empId = getIntent().getIntExtra("EMP_ID", -1);

        Employee e = db.getEmployee(empId);
        txtDetail.setText("Nama: " + e.getName() + "\nPosisi: " + e.getPosition() +
                "\nDept: " + e.getDepartment() + "\nEmail: " + e.getEmail() + "\nPhone: " + e.getPhone());

        findViewById(R.id.btnEdit).setOnClickListener(v -> {
            Intent i = new Intent(this, AddEditEmployeeActivity.class);
            i.putExtra("EMP_ID", empId);
            startActivity(i);
        });

        findViewById(R.id.btnDelete).setOnClickListener(v -> {
            db.deleteEmployee(empId);
            finish();
        });
    }
}
