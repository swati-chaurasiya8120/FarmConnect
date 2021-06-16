package com.example.farm_connect;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class BlogAdapter extends RecyclerView.Adapter<BlogAdapter.viewHolder>
{
    private String[] links;
    private ArrayList<String> list=new ArrayList<>();
    private Context context;

    public BlogAdapter(Context context,ArrayList<String> list,String[] links) {
        this.links=links;
        this.context = context;
        this.list=list;
    }

    public  void  refresh(ArrayList<String> list){
        this.list=list;
        notifyItemRangeChanged(0,list.size());
    }
    @Override
    public viewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category1,parent, false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(viewHolder holder, final int position) {
        holder.textView.setText(list.get(position));


        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browser =new Intent(context, WebActivity.class);
                browser.putExtra("link",links[position]);
                context.startActivity(browser);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class viewHolder extends RecyclerView.ViewHolder{
        TextView textView;
        CardView card;
        public viewHolder(View itemView) {
            super(itemView);
            textView= (TextView) itemView.findViewById(R.id.title);
            card= (CardView) itemView.findViewById(R.id.card);
        }
    }
}

