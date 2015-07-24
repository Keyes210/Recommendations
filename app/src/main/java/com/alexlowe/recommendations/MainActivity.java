package com.alexlowe.recommendations;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.alexlowe.recommendations.api.Etsy;
import com.alexlowe.recommendations.google.GoogleServicesHelper;
import com.alexlowe.recommendations.model.ActiveListings;
import com.google.android.gms.plus.PlusOneButton;

//APP NAMERecommendations KEYSTRINGv37j89morzzh57ert0kz4vh6 SHARED SECRET8vysim3s03

public class MainActivity extends ActionBarActivity {

    private static final String STATE_ACTIVE_LISTINGS = "StateActiveListings";

    private RecyclerView recyclerView;
    private View progressBar;
    private TextView error;

    private GoogleServicesHelper helper;

    private ListingAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        progressBar = findViewById(R.id.progress_bar);
        error = (TextView) findViewById(R.id.error_view);

        //setup recyclerview
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));
        adapter = new ListingAdapter(this);
        recyclerView.setAdapter(adapter);   // GSH has to be after adapter, b/c that is our listener for GSH

        helper = new GoogleServicesHelper(this, adapter);
        showLoading();

        if(savedInstanceState != null){
            if (savedInstanceState.containsKey(STATE_ACTIVE_LISTINGS)) {
                adapter.success((ActiveListings) savedInstanceState.getParcelable(STATE_ACTIVE_LISTINGS), null);
            }
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        helper.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        helper.disconnect();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        helper.handleActivityResult(requestCode, resultCode, data);

        if (requestCode == ListingAdapter.REQUEST_CODE_PLUS_ONE) {
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        ActiveListings activeListings = adapter.getActiveListings();
        if(activeListings != null ){
            outState.putParcelable(STATE_ACTIVE_LISTINGS, activeListings);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void showLoading(){
        progressBar.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
        error.setVisibility(View.GONE);
    }

    public void showList(){
        progressBar.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
        error.setVisibility(View.GONE);
    }

    public void showError(){
        progressBar.setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);
        error.setVisibility(View.VISIBLE);
    }
}
