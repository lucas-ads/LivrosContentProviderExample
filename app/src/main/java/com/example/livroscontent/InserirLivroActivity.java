package com.example.livroscontent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.example.livroscontent.R;

public class InserirLivroActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inserir_livro);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_activity_inserir_livro, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public void save(){
        EditText editTextTitulo = findViewById(R.id.editTextTitulo);
        EditText editTextAutor = findViewById(R.id.editTextAutor);

        String titulo = editTextTitulo.getText().toString();
        String autor = editTextAutor.getText().toString();

        ContentValues values = new ContentValues();

        values.put(LivrosDbHelper.C_TITULO, titulo);
        values.put(LivrosDbHelper.C_AUTOR, autor);

        ContentResolver provedor = getContentResolver();
        provedor.insert(LivrosProvider.CONTENT_URI, values);

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.actionSave:
                save();
                return true;
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}