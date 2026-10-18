package com.adnan.lgstudies.Adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.adnan.lgstudies.R;
import java.util.ArrayList;

public class DocumentAdapter extends RecyclerView.Adapter<DocumentAdapter.ViewHolder> {

    private final OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    Context context;
    ArrayList<Notes> notesItem;

    public DocumentAdapter(OnItemClickListener listener, Context context, ArrayList<Notes> notesItem) {
        this.listener = listener;
        this.context = context;
        this.notesItem = notesItem;
    }

    @NonNull
    @Override
    public DocumentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(context).inflate(R.layout.pdf_list_item,parent,false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull DocumentAdapter.ViewHolder holder, int position) {

        holder.bind(position);
    }


    @Override
    public int getItemCount() {
        return notesItem.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView pdf_name;
        ImageView pdf_lock;
        CardView cardView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            pdf_name=itemView.findViewById(R.id.pdf_file_name);
            pdf_lock=(ImageView) itemView.findViewById(R.id.download_pdf_lock);
            cardView=(CardView) itemView.findViewById(R.id.card);
        }

       public void bind(final int position) {
            Notes notes=notesItem.get(position);
           if (notes.isPremium) {
               pdf_name.setText(notesItem.get(position).getName());
               pdf_lock.setImageResource(R.drawable.baseline_file_download_done_24);

           }else  {
               pdf_name.setText(notesItem.get(position).getName());


       }


            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(position);
                }
            });
        }

    }
}
