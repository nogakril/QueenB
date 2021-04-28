package queenb.app.y2021.queenb.ui.main;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import queenb.app.y2021.queenb.R;


public class MeetingsFragment extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    final int VIEW_TYPE_LOADING = 0;
    final int VIEW_TYPE_ITEM = 1;
    public List<String> meetings;

    public MeetingsFragment(List<String> meetings){
        this.meetings = meetings;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row, parent, false);
            return new ItemViewHolder(view);
        }
        else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loading, parent, false);
            return new LoadingHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        populateItemRows((ItemViewHolder) holder, position);
    }

    @Override
    public int getItemCount() {
        return this.meetings == null ? 0 : this.meetings.size();
    }

    @Override
    public int getItemViewType(int position) {
        return this.meetings.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    private class ItemViewHolder extends RecyclerView.ViewHolder{
        TextView tvItem;

        public ItemViewHolder(@NonNull View itemView){
            super(itemView);
            tvItem = itemView.findViewById(R.id.tvItem);
        }
    }

    private class LoadingHolder extends RecyclerView.ViewHolder{

        public LoadingHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
    private void populateItemRows(ItemViewHolder viewHolder, int position){
        String item = this.meetings.get(position);
        viewHolder.tvitem.setText(item);
    }

//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_meetings, container, false);
//    }
}