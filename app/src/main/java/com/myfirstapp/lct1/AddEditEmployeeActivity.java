package com.myfirstapp.lct1;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class AddEditEmployeeActivity extends AppCompatActivity {
    EditText etName, etPos, etDept, etEmail, etPhone;
    Button btnSave;
    DatabaseHelper db;
    int empId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit);

        etName = findViewById(R.id.etName);
        etPos = findViewById(R.id.etPosition);
        etDept = findViewById(R.id.etDepartment);
        etEmail = findViewById(R.id.etEmail);
        etPhone = findViewById(R.id.etPhone);
        btnSave = findViewById(R.id.btnSave);

        db = new DatabaseHelper(this);
        empId = getIntent().getIntExtra("EMP_ID", -1);

        if (empId != -1) {
            Employee e = db.getEmployee(empId);
            etName.setText(e.getName());
            etPos.setText(e.getPosition());
            etDept.setText(e.getDepartment());
            etEmail.setText(e.getEmail());
            etPhone.setText(e.getPhone());
        }

        btnSave.setOnClickListener(v -> {
            Employee emp = new Employee(empId,
                    etName.getText().toString(),
                    etPos.getText().toString(),
                    etDept.getText().toString(),
                    etEmail.getText().toString(),
                    etPhone.getText().toString());

            if (empId == -1) {
                db.addEmployee(emp);
            } else {
                db.updateEmployee(emp);
            }
            finish();
        });
    }
}
