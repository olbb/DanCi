package com.readboy.learnwordg.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.readboy.learnwordg.R;
import com.readboy.learnwordg.util.User;
import com.readboy.learnwordg.util.Util;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by mao on 13-11-13.
 */
public class BoardAdapter extends BaseAdapter {

    List<User> list;
    Context context;
    LayoutInflater mInflater;
    Handler mh;

    public BoardAdapter(Context context, List<User> list) {


        this.context = context;
//        Log.i("LearnWord",list.size()+" ");


        this.list = new ArrayList<User>();
        for (User user : list) {
            this.list.add(user);
        }
        mInflater = LayoutInflater.from(context);
        mh = new Handler();
        if (this.list.size() == 0) {
            return;
        }

    }

//    public void updateAdapter(Context context,List<User> list){
//        this.context=context;
//        this.list.clear();
//        this.list.addAll(list);
//        mInflater=LayoutInflater.from(context);
//        if(list.size()==0){
//            return;
//        }
//    }

    @Override
    public int getCount() {
        return list.size();
//        if(list.size()==0){
//            return 0;
//        }else {
//            return 20;
//        }

    }

    @Override
    public Object getItem(int position) {
        return list.get(position);

//        if(position<list.size()){
//            return list.get(position);
//        }else {
//            return list.get(position%list.size());
//        }

    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return false;
//        return super.areAllItemsEnabled();
    }

    @Override
    public boolean isEnabled(int position) {
//        return super.isEnabled(position);
        return false;
    }

    ViewHolder viewHolder;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.board_item, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final User user;
        if (list.size() == 0) {
            return convertView;
        } else if (list.size() >= position) {
            user = list.get(position);
        } else {
            return convertView;
        }
        if (user.uid == Util.user.uid) {
            user.userimg = Util.user.userimg;
        }

        try {
            if (user.userimg != null) {
                viewHolder.img.setImageDrawable(user.userimg);
            } else {
                viewHolder.img.setImageDrawable(null);
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            URL url = new URL(user.imagePath);
                            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                            conn.connect();
                            InputStream is = conn.getInputStream();
                            user.userimg = Drawable.createFromStream(is, null);
                            is.close();
                            mh.post(new Runnable() {
                                @Override
                                public void run() {
//                                    viewHolder.img.setImageDrawable(user.userimg);
                                    notifyDataSetChanged();
                                }
                            });


                        } catch (FileNotFoundException e) {
//                            e.printStackTrace();
                            Resources r = context.getResources();
                            InputStream is = r.openRawResource(R.drawable.defaultimg);
                            user.userimg=Drawable.createFromStream(is,null);
                            try {
                                is.close();
                            }catch (Exception ee){
                                ee.printStackTrace();
                            }

                            mh.post(new Runnable() {
                                @Override
                                public void run() {
//                                    viewHolder.img.setImageDrawable(user.userimg);
                                    notifyDataSetChanged();
                                }
                            });
                        }catch (Exception e){
                            e.printStackTrace();
                        }

                    }
                });
                thread.start();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


        viewHolder.name.setText(user.realname);

        int minci = position + 1;

        if (position < 3 || position == 0) {
            viewHolder.stage.setText("0" + minci + "");
            viewHolder.stage.setTextColor(Color.rgb(255, 127, 2));
            viewHolder.stage.setTextSize(36);
        } else if (position < 9) {
            viewHolder.stage.setText("0" + minci + "");
            viewHolder.stage.setTextSize(30);
            viewHolder.stage.setTextColor(Color.rgb(99, 99, 99));
        } else {
            viewHolder.stage.setText(minci + "");
            viewHolder.stage.setTextSize(30);
            viewHolder.stage.setTextColor(Color.rgb(99, 99, 99));
        }

        viewHolder.time.setText(user.time);
//        viewHolder.qushi.setText(user.pre+"");
        int qs = user.pre - position - 1;
        if (qs > 0) {
            viewHolder.qushi.setText("+" + qs + " ↑");
            viewHolder.qushi.setTextColor(Color.GREEN);
        } else if (qs == 0) {
            viewHolder.qushi.setText("- -");
            viewHolder.qushi.setTextColor(Color.GRAY);
        } else {
            viewHolder.qushi.setText(qs + " ↓");
            viewHolder.qushi.setTextColor(Color.RED);
        }
//        Log.d("Tag","Add User "+user.toString());
        return convertView;
    }

    class ViewHolder {

        private TextView time, name, stage, qushi;
        private ImageView img;


        public ViewHolder(View view) {

            time = (TextView) view.findViewById(R.id.board_user_time);
            name = (TextView) view.findViewById(R.id.board_user_name);
            stage = (TextView) view.findViewById(R.id.board_user_stage);
            qushi = (TextView) view.findViewById(R.id.board_user_qushi);
            img = (ImageView) view.findViewById(R.id.board_user_img);
        }
    }


}
