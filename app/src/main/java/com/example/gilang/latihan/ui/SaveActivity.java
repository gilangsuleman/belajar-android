package com.example.gilang.latihan.ui;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.gilang.latihan.R;
import com.example.gilang.latihan.domain.Peserta;
import com.example.gilang.latihan.service.HttpService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class SaveActivity extends AppCompatActivity {

    EditText nama, alamat;
    Button save;
    HttpService service = new HttpService();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save);

        getSupportActionBar().setTitle("Tambah Data");

        nama = (EditText) findViewById(R.id.nama);
        alamat = (EditText) findViewById(R.id.alamat);
        save = (Button) findViewById(R.id.save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String namaP = nama.getText().toString();
                String alamatP = alamat.getText().toString();

                if (!validasiNama(namaP)){
                    nama.setError("Required");
                }else if (!validasiAlamat(alamatP)){
                    alamat.setError("Required");
                }else {
                    simpanData();
                }
            }
        });
    }

    public void simpanData(){

        Peserta peserta = new Peserta();
        peserta.setNama(nama.getText().toString());
        peserta.setAlamat(alamat.getText().toString());

        new SavePesertaExecute(peserta).execute();
        finish();
        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(i);

    }

    private class SavePesertaExecute extends AsyncTask<Void, Void, ResponseEntity>{

        private Peserta peserta;

        public SavePesertaExecute(Peserta peserta) {
            this.peserta = peserta;
        }

        @Override
        protected ResponseEntity doInBackground(Void... params) {

            return service.savePeserta(peserta);

        }

        @Override
        protected void onPostExecute(ResponseEntity responseEntity) {
            super.onPostExecute(responseEntity);

            if (responseEntity.getStatusCode() == HttpStatus.OK)
                Toast.makeText(getApplicationContext(), "Data Berhasil Disimpan", Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(getApplicationContext(), "Data Gagal Disimpan", Toast.LENGTH_SHORT).show();
        }
    }

    public boolean validasiNama(String namaPeserta){
        return namaPeserta.length() > 0;
    }
    public boolean validasiAlamat(String alamatPeserta){
        return alamatPeserta.length() > 0;
    }
}
