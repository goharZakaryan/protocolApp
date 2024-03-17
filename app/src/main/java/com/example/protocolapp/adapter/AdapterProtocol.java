package com.example.protocolapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.protocolapp.R;
import com.example.protocolapp.activity.AddProtocolActivity;
import com.example.protocolapp.activity.LoginActivity;
import com.example.protocolapp.activity.UserPageActivity;
import com.example.protocolapp.model.Protocol;
import com.example.protocolapp.retrofit.ApiInterface;
import com.example.protocolapp.retrofit.RetrofitClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
        int reversePosition = getItemCount() - position - 1;

        Protocol modelContact = protocols.get(reversePosition);
        //get data
        //we need only All data

        String name = modelContact.getName();
        String taskListAuthor = modelContact.getTaskListAuthor();

        holder.tvIndex.setText(String.valueOf(reversePosition -1));
        holder.name.setText(String.valueOf(name));
        holder.taskListAuthor.setText(String.valueOf(taskListAuthor));


        Log.d("AdapterContact", "Item or layout clicked!");

        holder.remove.setOnClickListener(view -> {
            Log.d("AdapterContact", "Item or layout clicked!");
            removeProtocol(modelContact, position);

        });
        holder.edit.setOnClickListener(view -> {
            Log.d("AdapterContact", "Item or layout clicked!");
            Intent intent = new Intent(context, AddProtocolActivity.class);
            intent.putExtra("id", modelContact.getId());
            context.startActivity(intent); // now get data from details Activity



        });

    }

    @Override
    public int getItemCount() {
        return protocols.size();
    }


    class ContactViewHolder extends RecyclerView.ViewHolder {
        TextView name, taskListAuthor, remove, edit;
        RelativeLayout relativeLayout;
        TextView tvIndex;

        public ContactViewHolder(@NonNull View itemView) {

            super(itemView);
            tvIndex = itemView.findViewById(R.id.tv_index);
            //init view
            name = itemView.findViewById(R.id.name);
            taskListAuthor = itemView.findViewById(R.id.taskListAuthor);
            remove = itemView.findViewById(R.id.remove);
            edit = itemView.findViewById(R.id.edit);
            relativeLayout = itemView.findViewById(R.id.my_relative_layout_bio);

        }

    }

    private void removeProtocol(Protocol protocol, int position) {
        ApiInterface apiInterface = RetrofitClient.getRetrofitInstance().create(ApiInterface.class);
        Call<Void> call = apiInterface.remove(protocol.getId());
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    protocols.remove(protocol);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, protocols.size());
                    notifyDataSetChanged();
                    Toast.makeText(context, "Protocol successfully removed...", Toast.LENGTH_SHORT).show();

                } else {
                    // Handle unsuccessful response
                    Toast.makeText(context, "Request unsuccessful", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                // Handle request failure
                t.printStackTrace();
                Toast.makeText(context, "Request failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
