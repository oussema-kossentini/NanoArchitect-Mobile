package com.java.akrem.contrat;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.java.akrem.contrat.entity.Contrat;

import java.util.List;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {

    private Context context;
    private Activity activity;
    private List<Contrat> contratList;

    // Constructor
    CustomAdapter(Activity activity, Context context, List<Contrat> contratList) {
        this.activity = activity;
        this.context = context;
        this.contratList = contratList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.my_row, parent, false);
        return new MyViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        // Get the current contract from the list
        final Contrat currentContrat = contratList.get(position);

        // Bind data to the views
        holder.contrat_id_txt.setText(String.valueOf(currentContrat.getId()));
        holder.contrat_cin_txt.setText(currentContrat.getCin());
        holder.contrat_type_txt.setText(currentContrat.getType());
        holder.contrat_num_txt.setText(String.valueOf(currentContrat.getNum()));
        holder.contrat_datestart_txt.setText(currentContrat.getDatestart());
        holder.contrat_dateend_txt.setText(currentContrat.getDateend());
        holder.contrat_valeur_txt.setText(String.valueOf(currentContrat.getValeur()));

        // RecyclerView onClickListener
        holder.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, UpdateActivity.class);
                intent.putExtra("id", String.valueOf(currentContrat.getId()));
                intent.putExtra("cin", currentContrat.getCin());
                intent.putExtra("type", currentContrat.getType());
                intent.putExtra("num", String.valueOf(currentContrat.getNum()));
                intent.putExtra("datestart", currentContrat.getDatestart());
                intent.putExtra("dateend", currentContrat.getDateend());
                intent.putExtra("valeur", String.valueOf(currentContrat.getValeur()));
                activity.startActivityForResult(intent, 1);
            }
        });
    }

    @Override
    public int getItemCount() {
        return contratList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView contrat_id_txt, contrat_cin_txt, contrat_type_txt, contrat_num_txt, contrat_datestart_txt, contrat_dateend_txt, contrat_valeur_txt;
        LinearLayout mainLayout;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            contrat_id_txt = itemView.findViewById(R.id.contrat_id_txt);
            contrat_cin_txt = itemView.findViewById(R.id.contrat_cin_txt);
            contrat_type_txt = itemView.findViewById(R.id.contrat_type_txt);
            contrat_num_txt = itemView.findViewById(R.id.contrat_num_txt);
            contrat_datestart_txt = itemView.findViewById(R.id.contrat_datestart_txt);
            contrat_dateend_txt = itemView.findViewById(R.id.contrat_dateend_txt);
            contrat_valeur_txt = itemView.findViewById(R.id.contrat_valeur_txt);
            mainLayout = itemView.findViewById(R.id.mainLayout);
            // Animate RecyclerView
            Animation translate_anim = AnimationUtils.loadAnimation(context, R.anim.translate_anim);
            mainLayout.setAnimation(translate_anim);
        }
    }
}
