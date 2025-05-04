package com.example.timesync.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.timesync.R;
import com.example.timesync.model.ScheduleItem;

import java.util.List;

public class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.ScheduleViewHolder> {

    private List<ScheduleItem> scheduleItems;
    private Context context;

    public ScheduleAdapter(Context context, List<ScheduleItem> scheduleItems) {
        this.context = context;
        this.scheduleItems = scheduleItems;
    }

    @NonNull
    @Override
    public ScheduleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_schedule, parent, false);
        return new ScheduleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ScheduleViewHolder holder, int position) {
        ScheduleItem item = scheduleItems.get(position);
        
        holder.tvStartTime.setText(item.getStartTime());
        holder.tvEndTime.setText(item.getEndTime());
        holder.tvSubjectName.setText(item.getTitle());
        holder.tvChapter.setText(item.getSubtitle());
        holder.tvTeacherName.setText(item.getTeacherName());
        holder.tvPlatform.setText(item.getPlatform());
        
        holder.imgTeacher.setImageResource(item.getTeacherImageResourceId());
        holder.imgPlatform.setImageResource(item.getPlatformImageResourceId());
        
        holder.cardScheduleItem.setCardBackgroundColor(item.getBackgroundColor());
        
        // Set the last timeline line invisible for the last item
        if (position == scheduleItems.size() - 1) {
            holder.timelineLine.setVisibility(View.INVISIBLE);
        } else {
            holder.timelineLine.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return scheduleItems.size();
    }

    public static class ScheduleViewHolder extends RecyclerView.ViewHolder {
        TextView tvStartTime, tvEndTime, tvSubjectName, tvChapter, tvTeacherName, tvPlatform;
        ImageView imgTeacher;
        ImageView imgPlatform;
        View timelineCircle, timelineLine, dotIndicator;
        CardView cardScheduleItem;

        public ScheduleViewHolder(@NonNull View itemView) {
            super(itemView);
            tvStartTime = itemView.findViewById(R.id.tvStartTime);
            tvEndTime = itemView.findViewById(R.id.tvEndTime);
            tvSubjectName = itemView.findViewById(R.id.tvSubjectName);
            tvChapter = itemView.findViewById(R.id.tvChapter);
            tvTeacherName = itemView.findViewById(R.id.tvTeacherName);
            tvPlatform = itemView.findViewById(R.id.tvPlatform);
            
            imgTeacher = itemView.findViewById(R.id.imgTeacher);
            imgPlatform = itemView.findViewById(R.id.imgPlatform);
            
            timelineCircle = itemView.findViewById(R.id.timelineCircle);
            timelineLine = itemView.findViewById(R.id.timelineLine);
            dotIndicator = itemView.findViewById(R.id.dotIndicator);
            
            cardScheduleItem = itemView.findViewById(R.id.cardScheduleItem);
        }
    }
} 