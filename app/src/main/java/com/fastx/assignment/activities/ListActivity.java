package com.fastx.assignment.activities;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import com.fastx.assignment.adapters.MyAdapter;
import com.fastx.assignment.data.EmployeeList;
import com.fastx.assignment.databinding.ActivityListBinding;
import com.fastx.assignment.repository.EmployeeRepository;
import com.fastx.assignment.retrofit.ApiInterface;
import com.fastx.assignment.retrofit.RetrofitService;
import com.fastx.assignment.viewmodels.EmployeesViewModel;
import com.fastx.assignment.viewmodels.ViewModelFactory;

public class ListActivity extends AppCompatActivity {

    ActivityListBinding binding;
    MyAdapter adapter;
    EmployeeList employees;
    EmployeesViewModel viewModel;
    EmployeeRepository employeeRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        employees = new EmployeeList();
        adapter = new MyAdapter(employees, this);
        binding.usersRV.setAdapter(adapter);
        ApiInterface apiInterface = RetrofitService.getInterface();

        employeeRepository = new EmployeeRepository(apiInterface);

        viewModel = new ViewModelProvider(this, new ViewModelFactory(employeeRepository)).get(EmployeesViewModel.class);

        binding.swiperefresh.setOnRefreshListener(this::observeData);

        observeData();
    }

    private void observeData() {
        ProgressDialog dialog = new ProgressDialog(this);
        dialog.setCancelable(false);
        dialog.setTitle("Loading Data");
        dialog.setMessage("Please Wait...");
        dialog.show();
        viewModel.getEmployees().observe(ListActivity.this, list -> {
            adapter = new MyAdapter(list,this);
            binding.usersRV.setAdapter(adapter);
            dialog.dismiss();
            binding.swiperefresh.setRefreshing(false);
        });

        viewModel.getError().observe(this, error -> {
            Toast.makeText(this, "Error: "+error, Toast.LENGTH_SHORT).show();
        });
    }

}