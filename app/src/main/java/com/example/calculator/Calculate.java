package com.example.calculator;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiFunction;

class Calculate {
    private final String expression;
    private List<String> RPN;
    private static final Map<String, BiFunction<BigDecimal, BigDecimal, BigDecimal>> hm;
    private static final Map<Character, Integer> priority;

    static {
        hm = new HashMap<>();
        hm.put("+", BigDecimal::add);
        hm.put("-", BigDecimal::subtract);
        hm.put("×", BigDecimal::multiply);
        hm.put("÷", (a, b) -> {
            if(b.compareTo(BigDecimal.ZERO) == 0) return null;
            return a.divide(b, 8, RoundingMode.HALF_UP).stripTrailingZeros();
        });

        priority = new HashMap<>();
        priority.put('+', 1);
        priority.put('-', 1);
        priority.put('×', 2);
        priority.put('÷', 2);
    }

    Calculate(StringBuilder expressing) {
        expression = expressing.toString();
        RPN = getRPN();
    }

    private boolean isParenthesesEquals(){ // 括号是否安全嵌套
        Deque<Character> test = new ArrayDeque<>();
        for(char ch : expression.toCharArray()){
            if(ch == '(') test.offerLast('(');
            else if(ch == ')') {
                if(test.isEmpty()) return false;
                test.pollLast();
            }
        }
        return test.isEmpty();
    }

    private boolean isNotSymbolsAdjacent() { // 是否有相邻运算符
        for(int i = 0; i < expression.length() - 1; ++i){
            if(priority.containsKey(expression.charAt(i)) && priority.containsKey(expression.charAt(i + 1))) return false;
        }
        return true;
    }

    private boolean isNotMulOrDivBegin() { // 是否以乘号或除号开头
        return expression.isEmpty() ||
                !priority.containsKey(expression.charAt(0)) ||
                priority.get(expression.charAt(0)) != 2;
    }

    private boolean isNotMultiplePoints() { // 是否有多个小数点
        for(String str : RPN){
            int num = 0;
            for(char ch : str.toCharArray()){
                if(ch == '.') num++;
            }
            if(num > 1 || num == str.length()) return false;
        }
        return true;
    }

    public boolean isSecure() {
        return isParenthesesEquals() && isNotSymbolsAdjacent() && isNotMulOrDivBegin() && isNotMultiplePoints();
    }

    private List<String> getRPN() {
        RPN = new ArrayList<>();
        Deque<Character> dq = new ArrayDeque<>();
        char[] chs = expression.toCharArray();
        for(int i = 0; i < chs.length; ++i) {
            if(i + 1 < chs.length &&
                    (i == 0 || chs[i - 1] == '(') &&
                    (chs[i] == '+' || chs[i] == '-') &&
                    ((chs[i + 1] >= '0' && chs[i + 1] <= '9') || chs[i + 1] == '.')) { // 一元运算符数值
                int begin = i;
                do i++;
                while (i < chs.length && ((chs[i] >= '0' && chs[i] <= '9') || chs[i] == '.'));
                RPN.add(new String(chs, begin, i - begin));
                i--;
                continue;
            }
            if(priority.containsKey(chs[i])) { // 运算符
                while(!dq.isEmpty() && dq.peekLast() != '(' && priority.get(dq.peekLast()) >= priority.get(chs[i])) {
                    RPN.add(Character.toString(dq.pollLast()));
                }
                dq.offerLast(chs[i]);
            } else if(chs[i] == '(') { // 左括号
                dq.offerLast(chs[i]);
            } else if(chs[i] == ')') { // 右括号
                while(!dq.isEmpty() && dq.peekLast() != '(') {
                    RPN.add(Character.toString(dq.pollLast()));
                }
                dq.pollLast();
            } else if((chs[i] >= '0' && chs[i] <= '9') || chs[i] == '.') { // 数值
                int begin = i;
                while(i < chs.length && ((chs[i] >= '0' && chs[i] <= '9') || chs[i] == '.')) i++;
                RPN.add(new String(chs, begin, i - begin));
                i--;
            }
        }
        while(!dq.isEmpty()) {
            RPN.add(Character.toString(dq.pollLast()));
        }
        return RPN;
    }
    public BigDecimal getResult() throws ArithmeticException, NullPointerException, NumberFormatException{
        Deque<BigDecimal> dq = new ArrayDeque<>();
        boolean isNegative = false;
        for(String ele : RPN){
            if(hm.containsKey(ele)) {
                if(dq.isEmpty() && ele.equals("-")) {
                    isNegative = true;
                    continue;
                } else if(dq.size() < 2 && (ele.equals("+") || ele.equals("-"))) continue;
                BigDecimal b = dq.pollLast();
                BigDecimal a = dq.pollLast();
                BigDecimal result = Objects.requireNonNull(hm.get(ele)).apply(a, b);
                if(result == null) throw new ArithmeticException();
                dq.offerLast(result);
            } else {
                dq.offerLast(new BigDecimal(ele));
            }
        }
        if(dq.size() != 1) throw new NullPointerException();
        return isNegative ? Objects.requireNonNull(dq.pollLast()).negate() : dq.pollLast();
    }

}