package com.fastx.assignment.viewmodels;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.fastx.assignment.repository.EmployeeRepository;
import com.fastx.assignment.viewmodels.EmployeesViewModel;

public class ViewModelFactory implements ViewModelProvider.Factory {
    EmployeeRepository employeeRepository;

    public ViewModelFactory(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new EmployeesViewModel(employeeRepository);
    }
}

