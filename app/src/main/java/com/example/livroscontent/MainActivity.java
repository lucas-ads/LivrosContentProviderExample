package com.example.livroscontent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.example.livroscontent.R;

public class MainActivity extends AppCompatActivity {

    private static final int LOADER_ID = 1;
    private SimpleCursorAdapter simpleCursorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onStart() {
        super.onStart();

        String[] from = {LivrosDbHelper.C_ID, LivrosDbHelper.C_TITULO, LivrosDbHelper.C_AUTOR};
        int[] to = {R.id.textViewId, R.id.textViewTitulo, R.id.textViewAutor};

        simpleCursorAdapter = new SimpleCursorAdapter(this, R.layout.activity_main_items,null,from, to, 0);
        ListView list = findViewById(R.id.listViewLivros);
        list.setAdapter(simpleCursorAdapter);
        list.setOnItemClickListener(itemClickListener);

        LoaderManager loaderManager = LoaderManager.getInstance(this);
        loaderManager.initLoader(LOADER_ID, null, cursorLoaderCallbacks);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_activity_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.actionAdd:
                startActivity(new Intent(this, InserirLivroActivity.class));
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private LoaderManager.LoaderCallbacks<Cursor> cursorLoaderCallbacks = new LoaderManager.LoaderCallbacks<Cursor>() {
        @NonNull
        @Override
        public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
            return new CursorLoader(getApplicationContext(), LivrosProvider.CONTENT_URI,null,null,null,null);
        }

        @Override
        public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
            if(loader.getId() == LOADER_ID){
                simpleCursorAdapter.swapCursor(data);
            }
        }

        @Override
        public void onLoaderReset(@NonNull Loader<Cursor> loader) {
            simpleCursorAdapter.swapCursor(null);
        }
    };

    private AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent intent = new Intent(getBaseContext(), EditarLivroActivity.class);
            intent.putExtra("id", String.valueOf(id));
            startActivity(intent);
        }
    };
}