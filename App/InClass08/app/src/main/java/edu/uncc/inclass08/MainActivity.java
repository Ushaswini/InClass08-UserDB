package edu.uncc.inclass08;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {


    ListView lv;
    ArrayList<User> userArrayList;
    ProgressDialog pDialog;
    ListViewAdapter adapter;

    private String BASE_URL = "http://inclass09.azurewebsites.net/api/UserData";
    private static String DOWNWARDS = "DOWN";
    private static String UPWARDS = "UP";
    private static String SORT = "SORT CHANGED";
    String scrollDirection;
    private static String TAG = "DEMO";
    Boolean scrollStateChanged = false;
    private String sortOption = "0";


    // Flag for current page
    int current_page = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lv = (ListView) findViewById(R.id.usersListview);
        userArrayList = new ArrayList<>();

        // LoadMore button
        Button btnLoadMore = new Button(this);
        btnLoadMore.setText("Load More...");

        lv.addFooterView(btnLoadMore);

        // Getting adapter
        adapter = new ListViewAdapter(this, userArrayList);
        lv.setAdapter(adapter);

        btnLoadMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // Starting a new async task
                if(isConnected()){
                    new loadMoreListView().execute(DOWNWARDS);
                }
                else {
                    Toast.makeText(MainActivity.this,"No Internet Connection", Toast.LENGTH_LONG).show();
                }

            }
        });


        if(isConnected()){
            new loadMoreListView().execute(DOWNWARDS);
        }
        else {
            Toast.makeText(MainActivity.this,"No Internet Connection", Toast.LENGTH_LONG).show();
        }

        lv.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                scrollStateChanged = true;
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem == 0) {
                    // check if we reached the top or bottom of the list
                    View v = lv.getChildAt(0);
                    int offset = (v == null) ? 0 : v.getTop();
                    if (offset == 0) {
                        if(current_page>2 && scrollStateChanged) {
                            if(isConnected()){
                                new loadMoreListView().execute(UPWARDS);
                            }
                            else {
                                Toast.makeText(MainActivity.this,"No Internet Connection", Toast.LENGTH_LONG).show();
                            }
                        }
                        return;
                    }
                } else if (totalItemCount - visibleItemCount == firstVisibleItem){
                    View v =  lv.getChildAt(totalItemCount-1);
                    int offset = (v == null) ? 0 : v.getTop();
                    if (offset == 0) {
                        // reached the bottom:
                        return;
                    }
                }
            }
        });
    }

    private boolean isConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo == null || !networkInfo.isConnected() ||
                (networkInfo.getType() != ConnectivityManager.TYPE_WIFI
                        && networkInfo.getType() != ConnectivityManager.TYPE_MOBILE)) {
            return false;
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_items, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.sort_by_firstName:
                sortOption = "0";
                if(isConnected()){
                    new loadMoreListView().execute(SORT);
                }
                else {
                    Toast.makeText(MainActivity.this,"No Internet Connection", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.sort_by_lastName:
                sortOption = "1";if(isConnected()){
                new loadMoreListView().execute(SORT);
            }
            else {
                Toast.makeText(MainActivity.this,"No Internet Connection", Toast.LENGTH_LONG).show();
            }
                break;
            case R.id.sort_by_gender:
                sortOption = "2";if(isConnected()){
                new loadMoreListView().execute(SORT);
            }
            else {
                Toast.makeText(MainActivity.this,"No Internet Connection", Toast.LENGTH_LONG).show();
            }
                break;
            default:
                return super.onOptionsItemSelected(item);
        }

        return true;
    }

    private class loadMoreListView extends AsyncTask<String, Void, ArrayList<User>> {

        @Override
        protected ArrayList<User> doInBackground(String... params) {
            StringBuilder stringBuilder = new StringBuilder();

            HttpURLConnection connection = null;
            BufferedReader reader = null;
            String result = null;
            scrollDirection = params[0];
            ArrayList<User> currentList = null;
            int page=0;

            try {
                if(scrollDirection.equals(DOWNWARDS)){
                    current_page++;
                    page = current_page;
                    Log.d(TAG, "doInBackground: "+ DOWNWARDS+" "+current_page);
                }
                else if(scrollDirection.equals(UPWARDS)) {
                    page = current_page-2;
                    current_page--;

                    Log.d(TAG, "doInBackground: "+ UPWARDS+" "+current_page);
                    if(page<1) {
                        current_page =0;
                        return currentList;
                    }
                }
                else {
                    current_page = 1;
                    page = 1;
                }

                String url_string = BASE_URL+"?index=" +page +"&sort="+sortOption;

                Log.d(TAG, "doInBackground: "+url_string);


                URL url = new URL(url_string);

                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {

                    currentList = new ArrayList<>();

                    reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    String line = "";
                    while ((line = reader.readLine()) != null) {
                        stringBuilder.append(line);
                    }
                    result = stringBuilder.toString();

                    JSONArray root = new JSONArray(result);

                    for (int i = 0; i < root.length(); i++) {

                        JSONObject userJson = root.getJSONObject(i);

                        User user = new User();
                        user.setFirstName(userJson.getString("first_name"));

                        user.setLastName(userJson.getString("last_name"));

                        user.setGender(userJson.getString("gender"));

                        user.setEmail(userJson.getString("email"));

                        user.setIpAddress(userJson.getString("ip_address"));

                        currentList.add(user);


                    }
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e1) {
                e1.printStackTrace();
            } finally {
                //Close open connections and reader
                if (connection != null) {
                    connection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return currentList;

        }

            @Override
        protected void onPreExecute() {
            // Showing progress dialog before sending http request
            pDialog = new ProgressDialog(
                    MainActivity.this);
            pDialog.setMessage("Please wait..");
            pDialog.setIndeterminate(true);
            pDialog.setCancelable(false);
            pDialog.show();
        }


        protected void onPostExecute(ArrayList<User> users) {

            if(users != null && users.size()>0) {
                Log.d(TAG, "onPostExecute: "+users.size());

                if (scrollDirection.equals(DOWNWARDS)) {

                    userArrayList.addAll(users);

                    if (userArrayList.size() > 100) {
                        userArrayList.subList(0, userArrayList.size()-100).clear();
                    }
                } else if (scrollDirection.equals(UPWARDS)) {

                    userArrayList.addAll(0, users);

                    if (userArrayList.size() > 100) {
                        userArrayList.subList(101, userArrayList.size() - 1).clear();
                    }
                    scrollStateChanged = false;

                }
                else {
                    userArrayList.clear();
                    userArrayList.addAll(users);

                }


                adapter.notifyDataSetChanged();
            }
            else if (users != null && users.size()==0 && scrollDirection.equals(DOWNWARDS)){

                Toast.makeText(MainActivity.this,"No More Results to load", Toast.LENGTH_LONG).show();
            }

            pDialog.dismiss();
        }
    }

}
