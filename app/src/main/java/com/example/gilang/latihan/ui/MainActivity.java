package com.example.gilang.latihan.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.example.gilang.latihan.R;
import com.example.gilang.latihan.dto.PesertaResponse;
import com.example.gilang.latihan.service.HttpService;
import com.example.gilang.latihan.ui.adapter.ListPesertaAdapter;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class MainActivity extends AppCompatActivity {

    SwipeRefreshLayout reload;
    ListView lvPeserta;
    HttpService service = new HttpService();

    public AlertDialog.Builder loadingBuilder, messageBuilder = null;
    public AlertDialog loadingDialog, messageDialog = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setTitle("Data Perserta");

        lvPeserta = (ListView) findViewById(R.id.lv_peserta);
        reload = (SwipeRefreshLayout) findViewById(R.id.refresh);

        reload.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new GetPesertaExcutor().execute();
            }
        });

        new GetPesertaExcutor().execute();

    }

    private class GetPesertaExcutor extends AsyncTask<Void, Void, ResponseEntity>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showLoadingDialog("Load Data Peserta");
        }

        @Override
        protected ResponseEntity doInBackground(Void... params) {
            return service.getAllPeserta();
        }

        @Override
        protected void onPostExecute(ResponseEntity responseEntity) {
            super.onPostExecute(responseEntity);
            dismissLoadingDialog();
            if (responseEntity != null){
                if (responseEntity.getStatusCode() == HttpStatus.OK){
                    PesertaResponse response = (PesertaResponse) responseEntity.getBody();
                    ListPesertaAdapter adapter = new ListPesertaAdapter(MainActivity.this, response.getContent());
                    lvPeserta.setAdapter(adapter);
                    reload.setRefreshing(false);
                }else {
                    showMessageDialog((String) responseEntity.getBody());
                }
            }else {
                showLoadingDialog("Server Not Respond");
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_proses) {
            Intent i = new Intent(getApplicationContext(), SaveActivity.class);
            startActivity(i);
           // finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void showLoadingDialog(String text) {
        View promptsView = LayoutInflater.from(this).inflate(R.layout.reload_dialog, null);
        TextView loadingText = (TextView) promptsView.findViewById(R.id.loading_text);
        loadingText.setText(text);
        if(loadingBuilder == null) {
            loadingBuilder = new AlertDialog.Builder(this);
        }

        loadingBuilder.setView(promptsView);
        loadingBuilder.setCancelable(false);

        loadingDialog = loadingBuilder.create();
        loadingDialog.show();
    }

    public void dismissLoadingDialog() {
        loadingDialog.dismiss();
    }

    public void showMessageDialog(String msg){
        if(messageBuilder == null) {
            messageBuilder = new AlertDialog.Builder(this);
        }
        messageBuilder.setMessage(msg);
        messageBuilder.setCancelable(false);
        messageBuilder.setPositiveButton("Close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        messageDialog = messageBuilder.create();
        messageDialog.show();
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }
}
