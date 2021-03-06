package com.example.among.function;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.among.R;

import java.util.ArrayList;



public class PolicyViewAdapter
        extends RecyclerView.Adapter<PolicyViewAdapter.ViewHolder>{
    PolicyFragment context;
    ArrayList<PolicyViewItem> data;
    int res_id;

    public PolicyViewAdapter(PolicyFragment context, int res_id,ArrayList<PolicyViewItem> data) {
        this.context = context;
        this.data = data;
        this.res_id=res_id;
    }
    //클릭 이벤트를 위한 메소드 작성 ---------------------------------------
    public interface MyOnItemClickListener{
        void onItemClick(View v, int pos);
    }

    private MyOnItemClickListener myListener = null;

    public void setOnItemClickListener(MyOnItemClickListener listener){
        this.myListener = listener;
    }
    //------------------------------------------------------------------


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final PolicyViewItem items = data.get(position);
        TextView txt = holder.name;
        txt.setText(items.getName());

    }


    @Override
    public int getItemCount() {
        return data.size();
    }
    class ViewHolder extends RecyclerView.ViewHolder{
        TextView name;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    if(pos != RecyclerView.NO_POSITION){
                        PolicyViewItem item = data.get(pos);
                        if (myListener != null){
                            myListener.onItemClick(v, pos);
                        }
                    }
                }
            });
        }
    }
}
