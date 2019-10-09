package com.t4f.lc_helper.Adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.t4f.lc_helper.R;
import com.t4f.lc_helper.data.Info;

public class MyRecyclerViewAdapter extends
        CursorRecyclerViewAdapter<MyRecyclerViewAdapter.ViewHolder> {

    private MyRecyclerViewAdapter.Listener listener;

    public interface Listener {
        void onClick(String title);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private CardView cardView;

        public ViewHolder(CardView v) {
            super(v);
            cardView = v;
        }
    }

    public MyRecyclerViewAdapter(Context context, Cursor cursor) {
        super(context, cursor);
    }

    @Override
    public MyRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CardView itemView = (CardView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.history_list_item, parent, false);
        ViewHolder vh = new ViewHolder(itemView);
        return vh;
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, Cursor cursor) {
        CardView cardView = viewHolder.cardView;
        TextView textCmdName = cardView.findViewById(R.id.cmd_name);

        Info cmd = fromCursor(cursor);
        final String cmdName = cmd.getTitle();
        textCmdName.setText(cmdName);

        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onClick(cmdName);
                }
            }
        });
    }

    public Info fromCursor(Cursor cursor) {
        return new Info(cursor.getString(cursor.getColumnIndex("title")),
                        "", "",
                        cursor.getString(cursor.getColumnIndex("date")));
    }
}