package com.readboy.learnword.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.readboy.learnword.R;
import com.readboy.learnword.SelectWord;
import com.readboy.learnword.util.Word;

public class SelWordsAdapter extends BaseAdapter {

    LayoutInflater mInflater;
    Context context;


    public SelWordsAdapter(Context context) {
        // TODO Auto-generated constructor stub
        mInflater = ((Activity) context).getLayoutInflater();
        this.context = context;
        Handler mh = new Handler();
        mh.postDelayed(new Runnable() {
            @Override
            public void run() {
                SelectWord.instance.checkednum.setText("已选 " + getchecknum() + " 个/总共" + SelectWord.words.size() + "个  ");
            }
        }, 100);


    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return SelectWord.words.size();
    }

    @Override
    public Word getItem(int position) {
        // TODO Auto-generated method stub
        return SelectWord.words.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        ViewHolder viewHolder = null;

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.previewword, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.checkd.setOnCheckedChangeListener(null);
        final Word word = getItem(position);
        viewHolder.wordname.setText(word.word);
        viewHolder.wordexpl.setText(word.expl);
        viewHolder.checkd.setChecked(word.checked);
        viewHolder.qianghua.setChecked(word.checked);
        viewHolder.read.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                word.readword();
            }
        });
        final CheckBox qianghua = viewHolder.qianghua;
        viewHolder.checkd.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // TODO Auto-generated method stub
                word.checked = isChecked;
                if (!isChecked && ((SelectWord) context).selectall.isChecked()) {
                    ((SelectWord) context).selectall.setChecked(false);
                }
                SelectWord.instance.checkednum.setText("已选 " + getchecknum() + " 个/总共" + SelectWord.words.size() + "个  ");
                qianghua.setChecked(isChecked);
                if (getchecknum() == SelectWord.words.size()) {
                    SelectWord.instance.selectall.setChecked(true);
                }
            }
        });
        if (position % 2 != 0) {
            viewHolder.listitem.setBackgroundColor(Color.argb(100, 240, 240, 240));
        } else {
            viewHolder.listitem.setBackgroundColor(Color.argb(100, 255, 255, 255));
        }

        return convertView;
    }

    class ViewHolder {

        private CheckBox checkd, qianghua;
        private TextView wordname;
        private TextView wordexpl;
        private ImageButton read;
        private LinearLayout listitem;


        public ViewHolder(View view) {
            checkd = (CheckBox) view.findViewById(R.id.selected);
            qianghua = (CheckBox) view.findViewById(R.id.qianghua);
            wordname = (TextView) view.findViewById(R.id.wordname);
            wordexpl = (TextView) view.findViewById(R.id.wordexpl);
            read = (ImageButton) view.findViewById(R.id.read);
            listitem = (LinearLayout) view.findViewById(R.id.listitem);
        }
    }

    public int getchecknum() {
        int num = 0;
        for (Word word : SelectWord.words) {
            if (word.checked) {
                num++;
            }
        }
        return num;
    }


}
