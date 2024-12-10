package com.justice_e_foster.music_application;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class SongAdapter extends RecyclerView.Adapter<SongAdapter.SongViewHolder> implements Filterable {
    private List<String> originalSongList;
    private List<String> filteredSongList;
    private final OnItemClickListener onItemClickListener;

    public SongAdapter(List<String> songList, OnItemClickListener onItemClickListener) {
        this.originalSongList = songList;
        this.filteredSongList = new ArrayList<>(songList);
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public SongViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
        return new SongViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SongViewHolder holder, int position) {
        String songName = filteredSongList.get(position);
        holder.songTextView.setText(songName);
        holder.itemView.setOnClickListener(v -> onItemClickListener.onItemClick(songName));
    }

    @Override
    public int getItemCount() {
        return filteredSongList.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                List<String> filteredResults = new ArrayList<>();
                if (constraint == null || constraint.length() == 0) {
                    filteredResults.addAll(originalSongList);
                } else {
                    String filterPattern = constraint.toString().toLowerCase().trim();
                    for (String song : originalSongList) {
                        if (song.toLowerCase().contains(filterPattern)) {
                            filteredResults.add(song);
                        }
                    }
                }
                FilterResults results = new FilterResults();
                results.values = filteredResults;
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                filteredSongList.clear();
                if (results.values != null) {
                    filteredSongList.addAll((List) results.values);
                }
                notifyDataSetChanged();
            }
        };
    }

    // Interface to handle item click
    public interface OnItemClickListener {
        void onItemClick(String songName);
    }

    // ViewHolder for song list items
    public static class SongViewHolder extends RecyclerView.ViewHolder {
        TextView songTextView;

        public SongViewHolder(@NonNull View itemView) {
            super(itemView);
            songTextView = itemView.findViewById(android.R.id.text1);
        }
    }
}
