package com.fastx.assignment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.fastx.assignment.data.Address;
import com.fastx.assignment.data.Employee;
import com.fastx.assignment.databinding.ActivityDetailedBinding;
import com.google.android.material.snackbar.Snackbar;

import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DetailedActivity extends AppCompatActivity {

    ActivityDetailedBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailedBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        int id = getIntent().getIntExtra("id", 0);
        String baseUrl = "https://jsonplaceholder.typicode.com/";
        ApiInterface apiInterface = new Retrofit.Builder().baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create()).build()
                .create(ApiInterface.class);

        ProgressDialog dialog = new ProgressDialog(this);
        dialog.setCancelable(false);
        dialog.setTitle("Getting Information");
        dialog.setMessage("Please Wait...");

        if (InternetConnectionCheck.isConnected(this)) {
            dialog.show();
            apiInterface.getEmployee(id).enqueue(new Callback<Employee>() {
                @Override
                public void onResponse(@NonNull Call<Employee> call, @NonNull Response<Employee> response) {
                    dialog.dismiss();
                    if (response.isSuccessful()) {
                        Employee employee = response.body();
                        assert employee != null;
                        binding.employeeId.setText(String.valueOf(employee.getId()));
                        String[] words = employee.getName().split(" ");

                        String name = words[0].toLowerCase() + Character.toUpperCase(words[1].charAt(0))
                                + words[1].substring(1);

                        binding.employeeName.setText(name);
                        binding.employeeEmail.setText(employee.getEmail().toLowerCase(Locale.ROOT));
                        binding.employeePhoneNo.setText(employee.getPhone());
                        Address address = employee.getAddress();
                        String addressline = address.getStreet() + "," + address.getSuite() + "," + address.getCity() + ","
                                + address.getZipcode();
                        binding.employeeAddress.setText(addressline);

                        binding.companyName.setText(employee.getCompany().getName());
                        binding.website.setText(employee.getWebsite());
                    }
                }

                @Override
                public void onFailure(@NonNull Call<Employee> call, @NonNull Throwable t) {
                    dialog.dismiss();
                    Toast.makeText(DetailedActivity.this, "ERROR : " + t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
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