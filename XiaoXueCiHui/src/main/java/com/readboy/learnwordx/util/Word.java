package com.readboy.learnwordx.util;

import android.media.MediaPlayer;
import android.os.Handler;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Arrays;

public class Word {

    public int index;//单词序号
    public String word;//单词
    public String phon;//音标
    public String expl;//解释
    public int vocadd;//发音数据地址
    public int voclen;//发音数据长度
    public int[] letaddr;//每个字母发音数据地址
    public int[] letlen;//每个字母发音数据长度
    public String exampen, exampcn;//例句
    public boolean checked = false;//是否选中
    Handler handler;
    public int errorcount = 0;

    RandomAccessFile data;

    static MediaPlayer mp;

    public Word(int index) {
        // TODO Auto-generated constructor stub
        this.index = index;
        this.data = Data.datafile;
        handler = new Handler();
        if (mp == null) {
            mp = new MediaPlayer();
        }
    }

    @Override
    public String toString() {
        return "Word [index=" + index + ", word=" + word + ", phon=" + phon
                + ", expl=" + expl + ", vocadd=" + vocadd + ", voclen="
                + voclen + ", letaddr=" + Arrays.toString(letaddr)
                + ", letlen=" + Arrays.toString(letlen) + ", exampen="
                + exampen + ", exampcn=" + exampcn + ", data=" + data + "]";
    }

    public void readword() {
        // TODO Auto-generated method stub
        if (mp.isPlaying()) {
            mp.stop();
        }
        mp.reset();
        try {
            mp.setDataSource(data.getFD(), vocadd, voclen);
            mp.prepare();
            mp.start();
        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalStateException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    int i = 0;
    int j;
    final Runnable r = new Runnable() {

        @Override
        public void run() {
            // TODO Auto-generated method stub
            readlettn(i);

            if (i < j - 1) {
                handler.postDelayed(r, 1000);
            }
            i++;
        }
    };

    public void readlett() {
        j = word.length();

        handler.postDelayed(r, 1000);

    }

    private void readlettn(int n) {
        if (mp.isPlaying()) {
            mp.stop();
        }
        mp.reset();
        try {
            mp.setDataSource(Data.letterfile.getFD(), letaddr[n], letlen[n]);
            mp.prepare();
            mp.start();
        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalStateException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


}
