package com.example.v_mahich.blendnet;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.v_mahich.blendnet.Models.Resource;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ResourceListActivity extends AppCompatActivity {

    List<Resource> resources;
    final String TAG = "BLENDNETv0:ResActivity";
    ArrayAdapter adp;
    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resource_list);

        listView = (ListView)findViewById(R.id.resource_list_view);
      final RetrofitInterface retrofitInterface = RetrofitBuilder.createService(null);


        Call<List<Resource>> call = retrofitInterface.getResources();

         call.enqueue(new Callback<List<Resource>>() {
            @Override
            public void onResponse(Call<List<Resource>> call, Response<List<Resource>> response) {
                if(response.isSuccessful()){
                    resources = response.body();
                    Log.i(TAG, resources.toString());
                    adp = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_1, resources);
                    listView.setAdapter(adp);

                }
            }

            @Override
            public void onFailure(Call<List<Resource>> call, Throwable t) {

            }
        });




        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(),resources.get(position).link,Toast.LENGTH_SHORT).show();


                SharedPreferences sharedPreferences = getSharedPreferences("BLENDNET",Context.MODE_PRIVATE);
                Set<String> requests = sharedPreferences.getStringSet("REQUESTS",null);
                if(requests == null){
                    requests = new HashSet<String>();
                }

                String fileName = resources.get(position).link;
                fileName = fileName.substring(fileName.lastIndexOf('/')+1);

                requests.add(fileName);
                SharedPreferences.Editor edit =sharedPreferences.edit();
                edit.putStringSet("REQUESTS",requests);
                edit.commit();

                Call<String> call2 = retrofitInterface.createRequest(resources.get(position).Id);
                Toast.makeText(getApplicationContext(),call2.request().url().toString(),Toast.LENGTH_SHORT);
                call2.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        Toast.makeText(getApplicationContext(),response.body(),Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {

                    }
                });


            }
        });

    }



}
