package com.fastx.assignment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.fastx.assignment.data.EmployeeList;
import com.fastx.assignment.databinding.ActivityListBinding;
import com.google.android.material.snackbar.Snackbar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ListActivity extends AppCompatActivity {

    ActivityListBinding binding;
    MyAdapter adapter;
    EmployeeList employees;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        employees = new EmployeeList();
        adapter = new MyAdapter(employees, this);
        binding.usersRV.setAdapter(adapter);
        String baseUrl = "https://jsonplaceholder.typicode.com/";
        ApiInterface apiInterface = new Retrofit.Builder().baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create()).build()
                .create(ApiInterface.class);
        ProgressDialog dialog = new ProgressDialog(this);
        dialog.setCancelable(false);
        dialog.setTitle("Loading Data");
        dialog.setMessage("Please Wait...");


        binding.swiperefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData(apiInterface, dialog);
            }
        });
        getData(apiInterface, dialog);
    }

    private void getData(ApiInterface apiInterface, ProgressDialog dialog) {
        if (InternetConnectionCheck.isConnected(this)) {
            dialog.show();
            apiInterface.getEmployeeList().enqueue(new Callback<EmployeeList>() {
                @Override
                public void onResponse(@NonNull Call<EmployeeList> call, @NonNull Response<EmployeeList> response) {
                    dialog.dismiss();
                    binding.swiperefresh.setRefreshing(false);
                    if (response.isSuccessful()) {
                        assert response.body() != null;
                        employees.clear();
                        employees.addAll(response.body());
                        adapter.notifyDataSetChanged();

                    }
                }

                @Override
                public void onFailure(@NonNull Call<EmployeeList> call, @NonNull Throwable t) {
                    dialog.dismiss();
                    binding.swiperefresh.setRefreshing(false);
                    Toast.makeText(ListActivity.this, "ERROR : " + t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        } else {
            Snackbar.make(binding.getRoot(), "No Internet", Snackbar.LENGTH_LONG)
                    .setAction("Settings", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            startActivity(new Intent(
                                    Settings.ACTION_WIFI_SETTINGS));
                        }
                    })
                    .show();

        }
    }
}