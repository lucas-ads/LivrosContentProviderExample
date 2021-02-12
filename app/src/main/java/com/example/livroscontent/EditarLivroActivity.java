package com.example.livroscontent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.example.livroscontent.R;

public class EditarLivroActivity extends AppCompatActivity {

    private static final int LOADER_ID = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_livro);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();

        if(intent.hasExtra("id")){
            LoaderManager loaderManager = LoaderManager.getInstance(this);
            loaderManager.initLoader(LOADER_ID, intent.getExtras(), loaderCallbacks);
        }else{
            finish();
        }
    }

    public void alterar(){
        EditText editTextId = findViewById(R.id.editTextId);
        EditText editTextTitulo = findViewById(R.id.editTextTitulo);
        EditText editTextAutor = findViewById(R.id.editTextAutor);

        String id = editTextId.getText().toString();
        String titulo = editTextTitulo.getText().toString();
        String autor = editTextAutor.getText().toString();

        ContentValues contentValues = new ContentValues();

        contentValues.put(LivrosDbHelper.C_TITULO, titulo);
        contentValues.put(LivrosDbHelper.C_AUTOR, autor);

        getContentResolver().update(LivrosProvider.CONTENT_URI, contentValues, LivrosDbHelper.C_ID+"=?",new String[]{id});
        startActivity(new Intent(this, MainActivity.class));
    }

    public void deletar(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(android.R.drawable.ic_menu_delete)
                .setTitle("Confirmação!")
                .setMessage("Tem certeza que deseja excluir este registro?")
                .setPositiveButton("Sim", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EditText editTextId = findViewById(R.id.editTextId);
                        String id = editTextId.getText().toString();
                        getContentResolver().delete(LivrosProvider.CONTENT_URI, LivrosDbHelper.C_ID+"=?", new String[]{id});

                        Toast.makeText(getApplicationContext(), "Livro excluído!", Toast.LENGTH_LONG).show();

                        startActivity(new Intent(getBaseContext(), MainActivity.class));
                    }
                })
                .setNegativeButton("Não", null);
        builder.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_activity_editar_livro, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.actionAlter:
                alterar();
                return true;
            case android.R.id.home:
                finish();
                return true;
            case R.id.actionDelete:
                deletar();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    LoaderManager.LoaderCallbacks<Cursor> loaderCallbacks = new LoaderManager.LoaderCallbacks<Cursor>() {
        @NonNull
        @Override
        public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
            if(id == LOADER_ID){
                String idLivro = "";
                idLivro = args.getString("id");

                Uri uri = Uri.withAppendedPath(LivrosProvider.CONTENT_URI, "id/"+idLivro);
                return new CursorLoader(getApplicationContext(), uri, null,null,null,null);
            }
            return null;
        }

        @Override
        public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
            if(loader.getId() == LOADER_ID){
                String id = "";
                String titulo = "";
                String autor = "";

                if(data.moveToNext()){
                    id = data.getString(0);
                    titulo = data.getString(1);
                    autor = data.getString(2);
                }

                EditText editTextId = findViewById(R.id.editTextId);
                EditText editTextTitulo = findViewById(R.id.editTextTitulo);
                EditText editTextAutor = findViewById(R.id.editTextAutor);

                editTextId.setText(id);
                editTextTitulo.setText(titulo);
                editTextAutor.setText(autor);
            }
        }

        @Override
        public void onLoaderReset(@NonNull Loader<Cursor> loader) {

        }
    };
}