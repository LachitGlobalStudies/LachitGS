package com.adnan.lgstudies.Adapters;

import static com.adnan.lgstudies.R.color.card;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.adnan.lgstudies.Activity.ProgressQuestionSetActivity;
import com.adnan.lgstudies.R;

import java.util.ArrayList;


public class ProgressTestTopicAdapter extends RecyclerView.Adapter<ProgressTestTopicAdapter.TestTopicViewHolder> {
    Context context;
ArrayList<TestCategory> list;

    public ProgressTestTopicAdapter(Context context, ArrayList<TestCategory> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ProgressTestTopicAdapter.TestTopicViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.test_name_item_view,parent,false);

        return new TestTopicViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProgressTestTopicAdapter.TestTopicViewHolder holder, int position) {
      TestCategory testCategory=list.get(position);
      holder.test_category_title.setText(testCategory.getTest_category());
      holder.total_test.setText(testCategory.getTotal_test());
      holder.cardView.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
              Intent intent=new Intent(context, ProgressQuestionSetActivity.class);
              intent.putExtra("batch_name",testCategory.getTest_category());
              intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
              context.startActivity(intent);
          }
      });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class TestTopicViewHolder extends RecyclerView.ViewHolder {
        TextView test_category_title,total_test;
        CardView cardView;
        public TestTopicViewHolder(@NonNull View itemView) {
            super(itemView);
            test_category_title=itemView.findViewById(R.id.test_series_title);
            total_test=itemView.findViewById(R.id.total_test);
            cardView=itemView.findViewById(R.id.test_series_topic_card);
        }
    }
}
