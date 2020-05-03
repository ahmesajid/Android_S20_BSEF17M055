package com.example.recyclerviewimplementation;
import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

import static androidx.core.content.ContextCompat.checkSelfPermission;


public class programmingAdpater extends RecyclerView.Adapter<programmingAdpater.programmingViewHolder> {
    private ArrayList<String> data;
    private ArrayList<String> level;
    private ArrayList<String> info;
    private ArrayList<String> url;


    public programmingAdpater(ArrayList<ArrayList<String>> data)
    {
        MainActivity.Variables.pos = -1;
        this.data=data.get(0);
        this.level=data.get(1);
        this.info=data.get(2);
        this.url = data.get(3);
    }
    @NonNull
    @Override
    public programmingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.list_item_layout , parent,false);
        return new programmingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final programmingViewHolder holder, final int position) {
        final String title = data.get(position);
        holder.txt1.setText(title);

        String lev = level.get(position);
        holder.txt2.setText(lev);

        String inf = info.get(position);
        holder.txt3.setText(inf);

        holder.btn1.setOnClickListener(new View.OnClickListener() {
            private Object mContext = new MainActivity();

            @Override
            public void onClick(View v) {
                MainActivity.Variables.pos = position;
                Toast.makeText(v.getContext(), "url: "+ url.get(position), Toast.LENGTH_SHORT).show();
                if(mContext instanceof MainActivity){
                    ((MainActivity)mContext).startDownloadingProcess();
                }

            }

        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class programmingViewHolder extends RecyclerView.ViewHolder{
        ImageView img;
        TextView txt1 , txt2 , txt3;
        Button btn1;
        public programmingViewHolder(@NonNull View itemView) {
            super(itemView);
            img = (ImageView) itemView.findViewById(R.id.img1);
            txt1 = (TextView) itemView.findViewById(R.id.txt1);
            txt2 = (TextView) itemView.findViewById(R.id.txt2);
            txt3 = (TextView) itemView.findViewById(R.id.txt3);
            btn1 = (Button) itemView.findViewById(R.id.btn1);

        }


    }
}
