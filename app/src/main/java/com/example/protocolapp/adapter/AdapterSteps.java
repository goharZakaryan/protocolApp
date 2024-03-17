package com.example.protocolapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
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
import com.example.protocolapp.activity.StepActivity;
import com.example.protocolapp.model.Protocol;
import com.example.protocolapp.model.Step;
import com.example.protocolapp.retrofit.ApiInterface;
import com.example.protocolapp.retrofit.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdapterSteps extends RecyclerView.Adapter<AdapterSteps.ContactViewHolder> {

    private Context context;
    private List<Step> steps;
    private Long protocolId;


    public AdapterSteps(Context context, List<Step> steps, Long protocolId) {
        this.context = context;
        this.steps = steps;
        this.protocolId = protocolId;
    }

    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_step_item, parent, false);
        ContactViewHolder vh = new ContactViewHolder(view);
        return vh;
    }


    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder holder, int position) {
        int reversePosition = getItemCount() - position - 1;

        Step step = steps.get(reversePosition);
        //get data
        //we need only All data

        String name = step.getNumber();
        String description=step.getDescription();

        holder.description.setText(String.valueOf(description));
        holder.name.setText(String.valueOf(name));


        Log.d("AdapterContact", "Item or layout clicked!");
        final List<Step> finalSteps = steps; // Declare steps as final

        holder.go.setOnClickListener(view -> {
            Log.d("AdapterContact", "Item or layout clicked!");
            Intent intent = new Intent(context, StepActivity.class);
            intent.putParcelableArrayListExtra("stepList", (ArrayList<? extends Parcelable>) finalSteps); // Use finalSteps here
            intent.putExtra("index", 1); // Use finalSteps here
            intent.putExtra("protocolId", protocolId); // Use finalSteps here
            intent.putExtra("step", step); // Use finalSteps here
            context.startActivity(intent);


        });

    }

    @Override
    public int getItemCount() {
        return steps.size();
    }


    class ContactViewHolder extends RecyclerView.ViewHolder {
        TextView name, description, go;
        RelativeLayout relativeLayout;

        public ContactViewHolder(@NonNull View itemView) {

            super(itemView);
            description = itemView.findViewById(R.id.description);
            //init view
            name = itemView.findViewById(R.id.name);
            go = itemView.findViewById(R.id.go);
            relativeLayout = itemView.findViewById(R.id.my_relative_layout_bio);

        }

    }

}
