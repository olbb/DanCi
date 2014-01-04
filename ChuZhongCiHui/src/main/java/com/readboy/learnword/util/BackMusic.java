package com.readboy.learnword.util;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;

import com.readboy.learnword.Config;

/**
 * Created by Mar on 13-10-10.
 */
public class BackMusic {

    Context context;
    MediaPlayer mp;
    MediaPlayer mp2;
    public boolean playflag = true;

    public BackMusic(Context context) {

        this.context = context;
        mp = new MediaPlayer();
        mp2 = new MediaPlayer();
    }

    public void play(String file) {
        // TODO Auto-generated method stub
        if (!playflag) {
            return;
        }

        if (!Config.music) {
            return;
        }
        if (mp.isPlaying()) {
            mp.stop();
            mp.reset();
        }
        mp.setLooping(false);
        try {
            mp.reset();
//			mp.setDataSource(context, Uri.fromFile(new File(path+"sound/"+file+".wav")));
            AssetFileDescriptor fd = context.getAssets().openFd("sound/" + file + ".wav");
//            System.out.println("sound/"+file+".wav");

            mp.setDataSource(fd.getFileDescriptor(), fd.getStartOffset(), fd.getLength());
            mp.prepare();
            mp.start();
        } catch (Exception e) {
            // TODO: handle exception
        }

    }

    public void play2(String file) {
        // TODO Auto-generated method stub
        if (!playflag) {
            return;
        }
        if (!Config.music) {
            return;
        }
        if (mp2.isPlaying()) {
            mp2.stop();
            mp2.reset();
        }
        mp2.setLooping(false);
        try {
            mp2.reset();
//			mp.setDataSource(context, Uri.fromFile(new File(path+"sound/"+file+".wav")));
            AssetFileDescriptor fd = context.getAssets().openFd("sound/" + file + ".wav");
//            System.out.println("sound/"+file+".wav");

            mp2.setDataSource(fd.getFileDescriptor(), fd.getStartOffset(), fd.getLength());
            mp2.prepare();
            mp2.start();
        } catch (Exception e) {
            // TODO: handle exception
        }

    }

    public void stop() {
        if (mp.isPlaying()) {
            mp.stop();
            mp.reset();

        }
        if (mp2.isPlaying()) {
            mp2.stop();
            mp2.reset();

        }
    }


}
