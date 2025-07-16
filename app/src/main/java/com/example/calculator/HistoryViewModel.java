package com.example.calculator;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class HistoryViewModel extends ViewModel {

    private final MutableLiveData<String> expression = new MutableLiveData<>();

    public MutableLiveData<String> getExpression() {
        return expression;
    }

    public void setExpression(String item) {
        expression.setValue(item);
    }
}
