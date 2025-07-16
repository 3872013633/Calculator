package com.example.calculator;

public class HistoryItem {
    private String time;
    private String expression;
    private String result;

    public HistoryItem(String time, String expression, String result) {
        this.time = time;
        this.expression = expression;
        this.result = result;
    }

    public String getExpression() {
        return expression;
    }

    public String getResult() {
        return result;
    }

    public String getTime() {
        return time;
    }

}
