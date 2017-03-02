package com.matano.muxik;

import android.Manifest;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class MainActivity extends AppCompatActivity
{
    private ArrayList<Song> songArrayList;
    RecyclerView songRecyclerView;
    RecyclerView.LayoutManager recycleViewLayoutManager;
    SongAdapter recyclerSongAdapter;
    private final String TAG = MainActivity.class.getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        songRecyclerView = (RecyclerView) findViewById(R.id.song_RecycleView);
        recycleViewLayoutManager = new LinearLayoutManager(this);
        songRecyclerView.setLayoutManager(recycleViewLayoutManager);
        songArrayList = new ArrayList<>();

        getSongList();

        Collections.sort(songArrayList, new Comparator<Song>()
        {
            @Override
            public int compare(Song o1, Song o2)
            {
                return o1.getTitle().compareTo(o2.getTitle());
            }
        });


        recyclerSongAdapter = new SongAdapter(this, songArrayList);
        songRecyclerView.setAdapter(recyclerSongAdapter);
    }

    public void getSongList()
    {
        //retrieve song info
        askPermission();
        ContentResolver musicResolver = getContentResolver();
        Uri musicUri = MediaStore.Audio
                .Media.EXTERNAL_CONTENT_URI;

        Cursor musicCursor = musicResolver.query(musicUri, null, null, null, null);


        if (musicCursor != null && musicCursor.moveToFirst())
        {
            //get columns
            int titleColumn = musicCursor.getColumnIndex
                    (MediaStore.Audio.Media.TITLE);
            int idColumn = musicCursor.getColumnIndex
                    (MediaStore.Audio.Media._ID);
            int artistColumn = musicCursor.getColumnIndex
                    (MediaStore.Audio.Media.ARTIST);

            //add songs to list

            do
            {
                long thisId = musicCursor.getLong(idColumn);
                String thisTitle = musicCursor.getString(titleColumn);
                String thisArtist = musicCursor.getString(artistColumn);
                songArrayList.add(new Song(thisId, thisTitle, thisArtist));
            }
            while (musicCursor.moveToNext());
            musicCursor.close();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }

    public void askPermission()
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {

                if (shouldShowRequestPermissionRationale(
                        Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    // Explain to the user why we need to read the contacts
                }    // Should we show an explanation?


                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        1);

                // MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE is an
                // app-defined int constant that should be quite unique

                return;
            }
        }
    }
}
