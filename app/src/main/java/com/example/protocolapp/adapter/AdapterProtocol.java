package com.example.protocolapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.protocolapp.R;
import com.example.protocolapp.model.Protocol;

import java.util.List;

public class AdapterProtocol extends RecyclerView.Adapter<AdapterProtocol.ContactViewHolder> {

    private Context context;
    private List<Protocol> protocols;


    public AdapterProtocol(Context context, List<Protocol> protocols) {
        this.context = context;
        this.protocols = protocols;
    }

    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_protocol_item, parent, false);
        ContactViewHolder vh = new ContactViewHolder(view);
        return vh;
    }



    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder holder, int position) {
        Protocol modelContact = protocols.get(position);
        //get data
        //we need only All data

        String name =modelContact.getName();
        String taskListAuthor=modelContact.getTaskListAuthor();

        holder.tvIndex.setText(String.valueOf(position + 1));
        holder.name.setText(String.valueOf(name));
        holder.taskListAuthor.setText(String.valueOf(taskListAuthor));


        Log.d("AdapterContact", "Item or layout clicked!");

//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View view) {
//                Log.d("AdapterContact", "Item or layout clicked!");
//
//                Intent intent = new Intent(context, ContactRecordDetails.class);
//                intent.putExtra("genAutoId", generatedId);
//                intent.putExtra("id", id);
//                context.startActivity(intent); // now get data from details Activity
//
//            }
//        });

    }
    @Override
    public int getItemCount() {
        return protocols.size();
    }


    class ContactViewHolder extends RecyclerView.ViewHolder {
        TextView name,  taskListAuthor;
        RelativeLayout relativeLayout;
        TextView tvIndex;

        public ContactViewHolder(@NonNull View itemView) {

            super(itemView);
            tvIndex = itemView.findViewById(R.id.tv_index);
            //init view
            name = itemView.findViewById(R.id.name);
            taskListAuthor = itemView.findViewById(R.id.taskListAuthor);
            relativeLayout = itemView.findViewById(R.id.my_relative_layout_bio);

        }

    }
}
