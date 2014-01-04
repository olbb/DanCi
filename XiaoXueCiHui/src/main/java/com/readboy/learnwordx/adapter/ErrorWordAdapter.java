package com.readboy.learnwordx.adapter;

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
import android.widget.TextView;

import com.readboy.learnwordx.ErrorWord;
import com.readboy.learnwordx.R;
import com.readboy.learnwordx.util.Word;

public class ErrorWordAdapter extends BaseAdapter {

    LayoutInflater mInflater;
    Context context;


    public ErrorWordAdapter(Context context) {
        // TODO Auto-generated constructor stub
        mInflater = ((Activity) context).getLayoutInflater();
        this.context = context;

        Handler mh = new Handler();
        mh.postDelayed(new Runnable() {
            @Override
            public void run() {
                ErrorWord.instance.checkednum.setText("已选 " + getchecknum() + " 个/总共" + ErrorWord.words.size() + "个  ");
            }
        }, 100);


    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return ErrorWord.words.size();
    }

    @Override
    public Word getItem(int position) {
        // TODO Auto-generated method stub
        return ErrorWord.words.get(position);
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
        viewHolder.box.setVisibility(View.GONE);
        viewHolder.read.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                word.readword();
            }
        });
        viewHolder.checkd.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // TODO Auto-generated method stub
                word.checked = isChecked;
                if (!isChecked && ((ErrorWord) context).selectall.isChecked()) {
                    ((ErrorWord) context).selectall.setChecked(false);
                }
                ErrorWord.instance.checkednum.setText("已选 " + getchecknum() + " 个/总共" + ErrorWord.words.size() + "个  ");
                if (getchecknum() == ErrorWord.words.size()) {
                    ErrorWord.instance.selectall.setChecked(true);
                }
            }
        });
        if (position % 2 != 0) {
            convertView.setBackgroundColor(Color.argb(100, 240, 240, 240));
        } else {
            convertView.setBackgroundColor(Color.argb(100, 255, 255, 255));
        }

        return convertView;

    }

    class ViewHolder {

        private CheckBox checkd, box;
        private TextView wordname;
        private TextView wordexpl;
        private ImageButton read;


        public ViewHolder(View view) {
            checkd = (CheckBox) view.findViewById(R.id.selected);
            box = (CheckBox) view.findViewById(R.id.qianghua);
            wordname = (TextView) view.findViewById(R.id.wordname);
            wordexpl = (TextView) view.findViewById(R.id.wordexpl);
            read = (ImageButton) view.findViewById(R.id.read);
        }
    }

    public int getchecknum() {
        int num = 0;
        for (Word word : ErrorWord.words) {
            if (word.checked) {
                num++;
            }
        }
        return num;
    }
}
