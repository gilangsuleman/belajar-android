package com.example.gilang.latihan.ui;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.gilang.latihan.R;
import com.example.gilang.latihan.domain.Peserta;
import com.example.gilang.latihan.service.HttpService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * Created by gilang on 13/05/16.
 */

public class UpdateActivity extends AppCompatActivity {

    HttpService service = new HttpService();
    Button simpan;
    EditText nama, alamat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save);

        getSupportActionBar().setTitle("Update Data");

        nama = (EditText) findViewById(R.id.nama);
        alamat = (EditText) findViewById(R.id.alamat);
        simpan = (Button) findViewById(R.id.save);

        final Bundle b = getIntent().getExtras();
        nama.setText(b.getString("p_nama"));
        alamat.setText(b.getString("p_alamat"));

        simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Peserta item = new Peserta();
                item.setNama(nama.getText().toString());
                item.setAlamat(alamat.getText().toString());

                new UpdatePesertaExecute(b.getString("p_id"), item).execute();
                finish();
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);
            }
        });

    }

    public class UpdatePesertaExecute extends AsyncTask<Void, Void, ResponseEntity>{

        private Peserta peserta;
        private String id;

        public UpdatePesertaExecute(String id,  Peserta peserta) {
            this.peserta = peserta;
            this.id = id;
        }

        @Override
        protected ResponseEntity doInBackground(Void... params) {
            return service.updatePeserta(id,peserta);
        }

        @Override
        protected void onPostExecute(ResponseEntity responseEntity) {
            super.onPostExecute(responseEntity);

            if (responseEntity.getStatusCode() == HttpStatus.OK)
                Toast.makeText(getApplicationContext(), "Data Berhasil Diupdate", Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(getApplicationContext(), "Data Gagal Diupdate", Toast.LENGTH_SHORT).show();
        }
    }
}
