package com.example.gilang.latihan.ui.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gilang.latihan.R;
import com.example.gilang.latihan.domain.Peserta;
import com.example.gilang.latihan.service.HttpService;
import com.example.gilang.latihan.ui.MainActivity;
import com.example.gilang.latihan.ui.UpdateActivity;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

/**
 * Created by gilang on 13/05/16.
 */
public class ListPesertaAdapter extends BaseAdapter {

    private Context context;
    private List<Peserta> pesertas;
    HttpService service = new HttpService();
    private boolean hapus;

    public ListPesertaAdapter(Context context, List<Peserta> pesertas) {
        this.context = context;
        this.pesertas = pesertas;
        this.service = service;
    }

    @Override
    public int getCount() {
        if (!pesertas.isEmpty())
            return pesertas.size();
        else return 0;

    }

    @Override
    public Peserta getItem(int position) {
        return pesertas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null )
            convertView = LayoutInflater.from(context).inflate(R.layout.list_peserta_layout, parent, false);

        final Peserta peserta = getItem(position);

        TextView lblNama = (TextView) convertView.findViewById(R.id.lbl_peserta_nama);
        TextView lblAlamat = (TextView) convertView.findViewById(R.id.lbl_peserta_alamat);
        ImageButton edit = (ImageButton) convertView.findViewById(R.id.edit);
        ImageButton hapus = (ImageButton) convertView.findViewById(R.id.hapus);

        lblNama.setText(peserta.getNama());
        lblAlamat.setText(peserta.getAlamat());

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, UpdateActivity.class);
                i.putExtra("p_id", peserta.getId());
                i.putExtra("p_nama", peserta.getNama());
                i.putExtra("p_alamat", peserta.getAlamat());
                context.startActivity(i);

            }
        });

        hapus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               AlertDialog dialog = new AlertDialog.Builder(context).create();
                dialog.setCancelable(false);
                dialog.setMessage("Hapus Data.. Anda Yakin!!");
                dialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        new DeletePeserta(peserta.getId()).execute();
                    }
                });
                dialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });


        return convertView;
    }

    public class DeletePeserta extends AsyncTask<Void, Void, ResponseEntity>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            ((MainActivity) context).showLoadingDialog("Loading, Please Wait..");
        }

        private String id;

        public DeletePeserta(String id) {
            this.id = id;
        }

        @Override
        protected void onPostExecute(ResponseEntity responseEntity) {
            super.onPostExecute(responseEntity);
            ((MainActivity) context).dismissLoadingDialog();
            if (responseEntity.getStatusCode() == HttpStatus.OK){
                Toast.makeText(context, "Data Berhasil Di hapus", Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(context, "Data Gagal Dihapus", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected ResponseEntity doInBackground(Void... params) {

            return service.deletePeserta(id);

        }
    }
}
