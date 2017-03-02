package com.matano.muxik;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by matano on 2/3/17.
 */

public class SongAdapter extends RecyclerView.Adapter<SongAdapter.ViewHolder>
{
    private ArrayList<Song> songs;
    private Context context;
    private final String TAG = SongAdapter.class.getSimpleName();

    SongAdapter(Context context, ArrayList<Song> songs)
    {
        this.songs = songs;
        this.context = context;

        Log.d(TAG, "In SongAdapter Constructor");
    }

    @Override
    public SongAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        Log.d(TAG, "In onCreateView");
        View v = LayoutInflater.from(context)
                .inflate(R.layout.song, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public int getItemCount()
    {
        return songs.size();
    }

    @Override
    public void onBindViewHolder(SongAdapter.ViewHolder holder, int position)
    {

        Log.d(TAG, "In onBindViewHolder");
        holder.artistView.setText(songs.get(position).getArtist());
        holder.songView.setText(songs.get(position).getTitle());
        holder.itemView.setTag(position);
    }

    class ViewHolder extends RecyclerView.ViewHolder
    {

        TextView songView;
        TextView artistView;
        ViewHolder(View itemView)
        {
            super(itemView);
            songView = (TextView) itemView.findViewById(R.id.song_title);
            artistView = (TextView) itemView.findViewById(R.id.song_artist);
        }
    }


}


























