package com.fastx.assignment.repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.fastx.assignment.data.Employee;
import com.fastx.assignment.retrofit.ApiInterface;
import com.fastx.assignment.data.EmployeeList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EmployeeRepository {
    private final ApiInterface apiInterface;
    private final MutableLiveData<EmployeeList> employeeListLiveData = new MutableLiveData<>();
    private final MutableLiveData<Employee> employeeData = new MutableLiveData<>();
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();

    public LiveData<EmployeeList> getEmployeeList() {
        apiInterface.getEmployeeList().enqueue(new Callback<EmployeeList>() {
            @Override
            public void onResponse(@NonNull Call<EmployeeList> call, @NonNull Response<EmployeeList> response) {
                if (response.isSuccessful()) {
                    employeeListLiveData.postValue(response.body());
                }
            }

            @Override
            public void onFailure(@NonNull Call<EmployeeList> call, @NonNull Throwable t) {
                employeeListLiveData.postValue(null);
                errorMessage.postValue(t.getLocalizedMessage());
            }
        });
        return employeeListLiveData;
    }
    public LiveData<Employee> getEmployeeDetails(int id) {
        apiInterface.getEmployee(id).enqueue(new Callback<Employee>() {
            @Override
            public void onResponse(@NonNull Call<Employee> call, @NonNull Response<Employee> response) {
                if (response.isSuccessful()) {
                    employeeData.postValue(response.body());
                }
            }

            @Override
            public void onFailure(@NonNull Call<Employee> call, @NonNull Throwable t) {
                employeeData.postValue(null);
                errorMessage.postValue(t.getLocalizedMessage());
            }
        });
        return employeeData;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public EmployeeRepository(ApiInterface apiInterface) {
        this.apiInterface = apiInterface;
    }

}
