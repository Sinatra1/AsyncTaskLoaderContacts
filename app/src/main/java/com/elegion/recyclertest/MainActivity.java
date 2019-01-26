package com.elegion.recyclertest;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements ContactsAdapter.OnItemClickListener, LoaderManager.LoaderCallbacks<String> {

    // добавить фрагмент с recyclerView ---
    // добавить адаптер, холдер и генератор заглушечных данных ---
    // добавить обновление данных и состояние ошибки ---
    // добавить загрузку данных с телефонной книги ---
    // добавить обработку нажатий ---
    // добавить декораторы ---

    private static final int LOADER_ID = 111;
    private static final int STOP_ID = 1;

    private ContactsAsyncTaskLoader mLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, RecyclerFragment.newInstance())
                    .commit();
        }

        initLoader();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        menu.add(0, STOP_ID, Menu.NONE, R.string.stop).setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == STOP_ID && mLoader.isStarted()) {
            mLoader.cancelLoad();
            onLoaderReset(mLoader);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(String id) {

        if (mLoader.isStarted()) {
            mLoader = (ContactsAsyncTaskLoader) getSupportLoaderManager().restartLoader(LOADER_ID, null, this);
        }

        mLoader.forceLoad(id);
    }

    @Override
    public Loader onCreateLoader(int id, Bundle args) {
        if (id != LOADER_ID) {
            return null;
        }

        return new ContactsAsyncTaskLoader(this);
    }

    @Override
    public void onLoadFinished(Loader<String> loader, String number) {
        if (number == null) {
            Toast.makeText(this, R.string.error_number, Toast.LENGTH_LONG).show();
            return;
        }

        startActivity(new Intent(Intent.ACTION_CALL).setData(Uri.parse("tel:" + number)));
    }

    @Override
    public void onLoaderReset(Loader loader) {
        Toast.makeText(this, R.string.get_number_stopped, Toast.LENGTH_LONG).show();
    }

    private void initLoader() {
        mLoader = (ContactsAsyncTaskLoader) getSupportLoaderManager().initLoader(LOADER_ID, null, this);
    }
}
