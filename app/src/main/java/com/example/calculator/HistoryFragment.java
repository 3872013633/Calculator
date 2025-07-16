package com.example.calculator;

import android.app.Dialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;
import java.util.List;

public class HistoryFragment extends DialogFragment {

    private List<HistoryItem> historyList = new ArrayList<>();
    private RecyclerView recyclerView;

    private MaterialButton clearButton;
    private HistoryViewAdapter historyViewAdapter;

    private HistoryDatabaseHelper dbHelper;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        Window window = dialog.getWindow();
        if (window != null) {
            window.requestFeature(Window.FEATURE_NO_TITLE);
        }

        return dialog;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_history, container, false);

        recyclerView = rootView.findViewById(R.id.recycler_view);

        dbHelper = new HistoryDatabaseHelper(requireContext());
        loadHistoryData();

        historyViewAdapter = new HistoryViewAdapter();
        recyclerView.setAdapter(historyViewAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext());
        recyclerView.setLayoutManager(layoutManager);

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        clearButton = rootView.findViewById(R.id.clear);
        clearButton.setOnClickListener(v -> {
            historyList.clear();
            historyViewAdapter.notifyDataSetChanged();
            db.execSQL("DELETE FROM History;");
            db.execSQL("VACUUM;");
        });

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            Window window = dialog.getWindow();
            if (window != null) {
                WindowManager.LayoutParams params = window.getAttributes();
                params.gravity = Gravity.BOTTOM | Gravity.START;
                params.width = (int) (getResources().getDisplayMetrics().widthPixels * 0.75);
                params.height = (int) (380 * getResources().getDisplayMetrics().density + 0.5f);;
                params.dimAmount = 0f;
                window.setBackgroundDrawableResource(android.R.color.white);
                window.setAttributes(params);
            }
        }
    }

    private void loadHistoryData() {
        historyList.clear();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM History ORDER BY id DESC", null);

        while (cursor.moveToNext()) {
            String time = cursor.getString(cursor.getColumnIndexOrThrow("time"));
            String expression = cursor.getString(cursor.getColumnIndexOrThrow("expression"));
            String result = cursor.getString(cursor.getColumnIndexOrThrow("result"));
            historyList.add(new HistoryItem(time, expression, result));
        }

        if(cursor != null) cursor.close();
    }

    class HistoryViewAdapter extends RecyclerView.Adapter<HistoryViewHolder> {

        @NonNull
        @Override
        public HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.history_item, parent, false);
            HistoryViewHolder historyViewHolder = new HistoryViewHolder(view);
            return historyViewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull HistoryViewHolder holder, int position) {
            HistoryItem historyItem = historyList.get(position);
            holder.time.setText(historyItem.getTime());
            holder.expression.setText(historyItem.getExpression());
            holder.result.setText(historyItem.getResult());

            holder.expression.setOnClickListener(v -> {
                HistoryViewModel viewModel = new ViewModelProvider(requireActivity())
                        .get(HistoryViewModel.class);
                viewModel.setExpression(historyItem.getExpression());
            });

            holder.result.setOnClickListener(v -> {
                HistoryViewModel viewModel = new ViewModelProvider(requireActivity())
                        .get(HistoryViewModel.class);
                viewModel.setExpression(historyItem.getRealResult());
            });
        }

        @Override
        public int getItemCount() {
            return historyList.size();
        }
    }

    class HistoryViewHolder extends RecyclerView.ViewHolder {
        MaterialTextView time;
        MaterialTextView expression;
        MaterialTextView result;

        public HistoryViewHolder(@NonNull View itemView){
            super(itemView);
            time = itemView.findViewById(R.id.time);
            expression = itemView.findViewById(R.id.expression);
            result = itemView.findViewById(R.id.result);
        }
    }

}
