package com.example.calculator;

import android.app.Dialog;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;
import java.util.List;

public class HistoryFragment extends DialogFragment {

    private List<HistoryItem> historyList = new ArrayList<>();
    private RecyclerView recyclerView;
    private HistoryViewAdapter historyViewAdapter;

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
        loadHistoryData();
        historyViewAdapter = new HistoryViewAdapter();
        recyclerView.setAdapter(historyViewAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext());
        recyclerView.setLayoutManager(layoutManager);

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
        for(int i = 1; i <= 10; ++i){
            historyList.add(new HistoryItem("2025.7." + i, "1+1", "=2"));
        }
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
