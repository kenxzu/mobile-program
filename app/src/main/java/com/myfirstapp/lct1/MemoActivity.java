package com.myfirstapp.lct1;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MemoActivity extends AppCompatActivity {
    private DBHelperMemo mydb;
    TextView txtJudul;
    TextView txtCatatan;
    int id_To_Update = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memo);

        txtJudul = findViewById(R.id.txtJudul);
        txtCatatan = findViewById(R.id.txtCatatan);
        mydb = new DBHelperMemo(this);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            int paramId = extras.getInt("id");
            if (paramId > 0) {
                Cursor rs = mydb.getData(paramId);
                id_To_Update = paramId;
                rs.moveToFirst();

                @SuppressLint("Range") String judul = rs.getString(rs.getColumnIndex(DBHelperMemo.COLUMN_NAME));
                @SuppressLint("Range") String catatan = rs.getString(rs.getColumnIndex(DBHelperMemo.COLUMN_CONTENT));

                if (!rs.isClosed()) rs.close();

                txtJudul.setText(judul);
                txtCatatan.setText(catatan);
                Toast.makeText(this, "proses ubah data " + paramId, Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "proses tambah data", Toast.LENGTH_LONG).show();
            }
        }
    }

    public void btnClicked(View v) {
        if (v.getId() == R.id.btnSimpan) {
            int s = simpan(v);
            if (s == 1) {
                setResult(RESULT_OK);
            } else {
                setResult(RESULT_CANCELED);
            }
            finish();

        } else if (v.getId() == R.id.btnHapus) {
            hapus(v);

        } else if (v.getId() == R.id.btnKembali) {
            setResult(RESULT_CANCELED);
            finish();
        }
    }

    private int simpan(View v) {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            int id = extras.getInt("id");

            try {
                if (id == 0) {
                    // Insert new
                    if (mydb.insertData(txtJudul.getText().toString(), txtCatatan.getText().toString())) {
                        Toast.makeText(getApplicationContext(), "data tercatat", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "ada kesalahan", Toast.LENGTH_SHORT).show();
                        return 0;
                    }
                } else {
                    // Update existing
                    if (mydb.updateData(id, txtJudul.getText().toString(), txtCatatan.getText().toString())) {
                        Toast.makeText(getApplicationContext(), "data " + id + " tersimpan", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "ada kesalahan", Toast.LENGTH_SHORT).show();
                        return 0;
                    }
                }
                return 1;
            } catch (Exception e) {
                Log.d("MemoActivity", e.getMessage());
                return 0;
            }
        }
        return 0;
    }

    private void hapus(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.deleteContact)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        mydb.deleteData(id_To_Update);
                        Toast.makeText(getApplicationContext(), "data terhapus", Toast.LENGTH_SHORT).show();
                        setResult(RESULT_OK);
                        finish();
                    }
                })
                .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Toast.makeText(getApplicationContext(), "batal hapus", Toast.LENGTH_SHORT).show();
                        dialog.cancel();
                    }
                });
        AlertDialog d = builder.create();
        d.setTitle("Konfirmasi");
        d.show();
    }
}
