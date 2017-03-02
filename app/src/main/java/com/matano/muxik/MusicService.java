package com.matano.muxik;

import android.app.Service;
import android.content.ContentUris;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.os.PowerManager;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by matano on 2/3/17.
 */

public class MusicService extends Service implements
        MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener,
        MediaPlayer.OnCompletionListener
{
    //Media Player
    private MediaPlayer player;

    //song list
    private ArrayList<Song> songArrayList;

    //current position
    private int songPosn;

    private final IBinder musicBind = new MusicBinder();

    @Override
    public void onCreate()
    {
        //create the service
        super.onCreate();

        //initialize position
        songPosn = 0;

        //create player
        player = new MediaPlayer();
        initMusicPlayer();
    }

    public void initMusicPlayer()
    {
        //set player properties

        player.setWakeMode(getApplicationContext(),
                PowerManager.PARTIAL_WAKE_LOCK);
        player.setAudioStreamType(AudioManager.STREAM_MUSIC);
        player.setOnPreparedListener(this);
        player.setOnCompletionListener(this);
        player.setOnErrorListener(this);

    }

    public void setSongArrayList(ArrayList<Song> songArrayList)
    {
        this.songArrayList = songArrayList;
    }

    public void setSongPosn(int songPosn)
    {
        this.songPosn = songPosn;
    }

    public class MusicBinder extends Binder
    {
        MusicService getService()
        {
            return MusicService.this;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent)
    {
        return musicBind;
    }

    @Override
    public boolean onUnbind(Intent intent)
    {
        player.stop();
        player.release();
        return false;
    }

    @Override
    public void onCompletion(MediaPlayer mp)
    {

    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra)
    {
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mp)
    {
        //start playback
        mp.start();
    }

    public void playSong()
    {
        //play a song

        player.reset();

        //get song
        Song playSong = songArrayList.get(songPosn);
        //get id
        long currSong = playSong.getId();
        //set uri
        Uri trackUri = ContentUris.withAppendedId(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, currSong
        );

        try
        {
            player.setDataSource(getApplicationContext(), trackUri);
        }
        catch (Exception e)
        {
            Log.e("MUSIC SERVICE" , "Error setting data source" , e);
        }

        player.prepareAsync();
    }
}