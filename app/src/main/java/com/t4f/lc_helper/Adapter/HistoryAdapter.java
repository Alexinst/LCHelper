package com.t4f.lc_helper.Adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.t4f.lc_helper.R;
import com.t4f.lc_helper.data.Info;

import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {
    private List<Info> histories;
    private Listener listener;

    public interface Listener {
        void onClick(int position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private CardView cardView;
        TextView cmdName;

        public ViewHolder(CardView v) {
            super(v);
            cardView = v;
            cmdName = v.findViewById(R.id.cmd_name);
        }
    }

    public HistoryAdapter(List<Info> histories) {
        this.histories = histories;
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        CardView v = (CardView) LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.history_list_item,
                viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {
        CardView cardView = viewHolder.cardView;

        Info item = histories.get(i);
        viewHolder.cmdName.setText(item.getTitle());

        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onClick(i);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return histories.size();
    }
}
