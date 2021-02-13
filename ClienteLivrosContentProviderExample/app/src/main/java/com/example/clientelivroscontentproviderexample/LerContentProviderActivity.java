package com.example.clientelivroscontentproviderexample;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import androidx.appcompat.app.AppCompatActivity;

public class LerContentProviderActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ler_content_provider);

        try{
            Uri uriLivros = Uri.parse("content://com.example.livros");
            Cursor cursor = getContentResolver().query(uriLivros, null, null, null, null);

            String[] from = {"titulo", "autor"};
            int[] to = {R.id.textViewTitulo, R.id.textViewAutor};

            SimpleCursorAdapter simpleCursorAdapter = new SimpleCursorAdapter(this, R.layout.ler_provider_itens, cursor, from, to);

            ListView listView = findViewById(R.id.listViewProviderLivros);
            listView.setAdapter(simpleCursorAdapter);

        }catch (Exception e){
            Log.e("Error: ", e.getMessage());
        }
    }
}