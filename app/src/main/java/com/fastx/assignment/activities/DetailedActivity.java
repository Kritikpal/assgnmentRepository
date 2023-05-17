package com.fastx.assignment.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.fastx.assignment.apputills.InternetConnectionCheck;
import com.fastx.assignment.data.Address;
import com.fastx.assignment.data.Employee;
import com.fastx.assignment.databinding.ActivityDetailedBinding;
import com.fastx.assignment.repository.EmployeeRepository;
import com.fastx.assignment.retrofit.ApiInterface;
import com.fastx.assignment.retrofit.RetrofitService;
import com.fastx.assignment.viewmodels.EmployeesViewModel;
import com.fastx.assignment.viewmodels.ViewModelFactory;
import com.google.android.material.snackbar.Snackbar;

import java.util.Locale;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DetailedActivity extends AppCompatActivity {

    ActivityDetailedBinding binding;

    EmployeesViewModel viewModel;
    EmployeeRepository employeeRepository;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailedBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        int id = getIntent().getIntExtra("id", 0);
        ApiInterface apiInterface = RetrofitService.getInterface();
        employeeRepository = new EmployeeRepository(apiInterface);
        viewModel = new ViewModelProvider(this,new ViewModelFactory(employeeRepository)).get(EmployeesViewModel.class);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Details");

        if (InternetConnectionCheck.isConnected(this)) {
            observeData(id);
        } else {
            Snackbar.make(binding.getRoot(), "No Internet", Snackbar.LENGTH_LONG)
                    .setAction("Settings", view -> startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS)))
                    .show();
        }

    }

    private void observeData(int id) {
        ProgressDialog dialog = new ProgressDialog(this);
        dialog.setCancelable(false);
        dialog.setTitle("Getting Information");
        dialog.setMessage("Please Wait...");
        dialog.show();
        viewModel.getEmployeeDetails(id).observe(this, employee -> {
            dialog.dismiss();
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
        });
        viewModel.getError().observe(this, error -> {
            Toast.makeText(this, "Error: "+error, Toast.LENGTH_SHORT).show();
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}