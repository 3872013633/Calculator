package com.example.calculator;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.HapticFeedbackConstants;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    private StringBuilder expressing;
    private Calculate calculator;
    private MaterialTextView textViewExpression;
    private MaterialTextView textViewResult;
    private HistoryDatabaseHelper dbHelper;
    private static Set<Character> hs = Set.of('+', '-', '×', '÷');

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main); // 布局加载后findViewById才开始找控件
        initialize(); // 初始化计算器
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        MaterialButton buttonHistory = findViewById(R.id.history);
        buttonHistory.setOnClickListener(v -> {

            HistoryFragment historyFragment = new HistoryFragment();
            historyFragment.show(getSupportFragmentManager(), "fragment_history");

        });

        MaterialButton buttonSettings = findViewById(R.id.settings);
        buttonSettings.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(intent);
        });

        MaterialButton button1 = findViewById(R.id.circleButton5);
        button1.setOnClickListener(v -> {
            v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
            expressing.append('1');
            showExpressing();
            showResult();
        });

        MaterialButton button2 = findViewById(R.id.circleButton6);
        button2.setOnClickListener(v -> {
            v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
            expressing.append('2');
            showExpressing();
            showResult();
        });

        MaterialButton button3 = findViewById(R.id.circleButton7);
        button3.setOnClickListener(v -> {
            v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
            expressing.append('3');
            showExpressing();
            showResult();
        });

        MaterialButton button4 = findViewById(R.id.circleButton9);
        button4.setOnClickListener(v -> {
            v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
            expressing.append('4');
            showExpressing();
            showResult();
        });

        MaterialButton button5 = findViewById(R.id.circleButton10);
        button5.setOnClickListener(v -> {
            v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
            expressing.append('5');
            showExpressing();
            showResult();
        });

        MaterialButton button6 = findViewById(R.id.circleButton11);
        button6.setOnClickListener(v -> {
            v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
            expressing.append('6');
            showExpressing();
            showResult();
        });

        MaterialButton button7 = findViewById(R.id.circleButton13);
        button7.setOnClickListener(v -> {
            v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
            expressing.append('7');
            showExpressing();
            showResult();
        });

        MaterialButton button8 = findViewById(R.id.circleButton14);
        button8.setOnClickListener(v -> {
            v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
            expressing.append('8');
            showExpressing();
            showResult();
        });

        MaterialButton button9 = findViewById(R.id.circleButton15);
        button9.setOnClickListener(v -> {
            v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
            expressing.append('9');
            showExpressing();
            showResult();
        });

        MaterialButton button0 = findViewById(R.id.circleButton18);
        button0.setOnClickListener(v -> {
            v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
            expressing.append('0');
            showExpressing();
            showResult();
        });

        MaterialButton buttonPoint = findViewById(R.id.circleButton19);
        buttonPoint.setOnClickListener(v -> {
            v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
            expressing.append('.');
            showExpressing();
            showResult();
        });

        MaterialButton buttonAllClear = findViewById(R.id.circleButton17);
        buttonAllClear.setOnClickListener(v -> {
            v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
            expressing.setLength(0);
            showExpressing();
            showResult();
        });

        MaterialButton buttonDEL = findViewById(R.id.circleButton3);
        buttonDEL.setOnClickListener(v -> {
            v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
            if(expressing.length() > 0) {
                expressing.deleteCharAt(expressing.length() - 1);
                showExpressing();
            }
            showResult();
        });

        MaterialButton buttonPlus = findViewById(R.id.circleButton4);
        buttonPlus.setOnClickListener(v -> {
            v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
            if(expressing.length() == 0 || !hs.contains(expressing.charAt(expressing.length() - 1))) {
                expressing.append('+');
            }else {
                expressing.setCharAt(expressing.length() - 1, '+');
            }
            showExpressing();
            showResult();
        });

        MaterialButton buttonMinus = findViewById(R.id.circleButton8);
        buttonMinus.setOnClickListener(v -> {
            v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
            if(expressing.length() == 0 || !hs.contains(expressing.charAt(expressing.length() - 1))) {
                expressing.append('-');
            }else {
                expressing.setCharAt(expressing.length() - 1, '-');
            }
            showExpressing();
            showResult();
        });

        MaterialButton buttonMultiply = findViewById(R.id.circleButton12);
        buttonMultiply.setOnClickListener(v -> {
            v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
            if(expressing.length() == 0 || !hs.contains(expressing.charAt(expressing.length() - 1))) {
                expressing.append('×');
            }else {
                expressing.setCharAt(expressing.length() - 1, '×');
            }
            showExpressing();
            showResult();
        });

        MaterialButton buttonDivide = findViewById(R.id.circleButton16);
        buttonDivide.setOnClickListener(v -> {
            v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
            if(expressing.length() == 0 || !hs.contains(expressing.charAt(expressing.length() - 1))) {
                expressing.append('÷');
            }else {
                expressing.setCharAt(expressing.length() - 1, '÷');
            }
            showExpressing();
            showResult();
        });

        MaterialButton buttonLeft = findViewById(R.id.circleButton1);
        buttonLeft.setOnClickListener(v -> {
            v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
            expressing.append('(');
            showExpressing();
            showResult();
        });

        MaterialButton buttonRight = findViewById(R.id.circleButton2);
        buttonRight.setOnClickListener(v -> {
            v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
            expressing.append(')');
            showExpressing();
            showResult();
        });

        MaterialButton buttonEquals = findViewById(R.id.circleButton20);
        buttonEquals.setOnClickListener(v -> {
            v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
            try {
                float startSize = textViewResult.getTextSize() / getResources().getDisplayMetrics().scaledDensity;
                calculator = new Calculate(expressing);
                if (calculator.isSecure()) {
                    String result = calculator.getResult().toString();
                    if(result.length() > 10) {
                        if(startSize < 36)
                            animateTextSize(textViewResult, startSize, 36, 180);
                    }
                    else {
                        if(startSize < 48)
                            animateTextSize(textViewResult, startSize, 48, 180);
                    }
                    textViewResult.setTextColor(Color.parseColor("#000000"));
                    textViewResult.setText(result);

                    SQLiteDatabase db = dbHelper.getWritableDatabase();
                    ContentValues values = new ContentValues();
                    values.put("time", new SimpleDateFormat("yyyy年MM月dd日", Locale.getDefault()).format(new Date()));
                    values.put("expression", expressing.toString());
                    values.put("result", calculator.getResult().toString());
                    db.insert("History", null, values);
                    values.clear();

                } else {
                    textViewResult.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24);
                    textViewResult.setTextColor(Color.parseColor("#878787"));
                    textViewResult.setText("错误");
                }
            } catch (ArithmeticException | NullPointerException | NumberFormatException e) {
                if(expressing.length() == 0) textViewResult.setText("");
                else {
                    textViewResult.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24);
                    textViewResult.setTextColor(Color.parseColor("#878787"));
                    textViewResult.setText("错误");
                }
            }
        });

        HistoryViewModel viewModel = new ViewModelProvider(this).get(HistoryViewModel.class);
        viewModel.getExpression().observe(this, item -> {
            expressing.append(item);
            showExpressing();
            showResult();
        });

        dbHelper = new HistoryDatabaseHelper(this);
        dbHelper.getWritableDatabase();
    }

    private void initialize(){
        expressing = new StringBuilder();
        textViewExpression = findViewById(R.id.materialTextView1);
        textViewResult = findViewById(R.id.materialTextView2);
    }

    @SuppressLint("SetTextI18n")
    private void showExpressing() {
        String expressingTemp = expressing.toString();
        textViewExpression.setText(expressingTemp);
        textViewResult.setText("");
    }

    private void showResult() {
        try {
            calculator = new Calculate(expressing);
            if (calculator.isSecure()) {
                String result = calculator.getResult().toString();
                textViewResult.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24);
                textViewResult.setTextColor(Color.parseColor("#878787"));
                textViewResult.setText(result);
            } else {
                textViewResult.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24);
                textViewResult.setTextColor(Color.parseColor("#878787"));
                textViewResult.setText("错误");
            }
        } catch (ArithmeticException | NullPointerException | NumberFormatException e) {
            if(expressing.length() == 0) textViewResult.setText("");
            else {
                textViewResult.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24);
                textViewResult.setTextColor(Color.parseColor("#878787"));
                textViewResult.setText("错误");
            }
        }
    }

    public static void animateTextSize(MaterialTextView textView, float startSize, float endSize, long duration) {
        ValueAnimator animator = ValueAnimator.ofFloat(startSize, endSize);
        animator.setDuration(duration);
        animator.addUpdateListener(animation -> {
            float size = (float) animation.getAnimatedValue();
            float pxSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, size, textView.getContext().getResources().getDisplayMetrics());
            textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, pxSize);
        });
        animator.start();
    }

}