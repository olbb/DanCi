package com.readboy.learnword;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.readboy.learnword.util.Util;

import java.lang.reflect.Field;


/**
 * Created by Mar on 13-12-4.
 */
public class ChengHao {

    ImageView chenghao;
    TextView name;
    Dialog dialog;

    public ChengHao(final TestWords context){

         dialog=new Dialog(context,R.style.dialog);
        dialog.setContentView(R.layout.jiangzhuang);
        chenghao=(ImageView)dialog.findViewById(R.id.chenghao);
        chenghao.setImageResource(getCHDrawable(Util.stage / 6))  ;
        name=(TextView)dialog.findViewById(R.id.chenghao_name);
        String username;
        if(Util.user!=null&&Util.user.realname!=null){
            username=Util.user.realname;
        }else{
            username="Dear";
        }
        name.setText(username);

        dialog.show();

        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                  context.getWord();
            }
        });
    }



     private int getCHDrawable(int id){
         String s="chenghao"+id;
         int d=0;
         try{
             Field field=R.drawable.class.getField(s);
             d=field.getInt(field.getName());


         }catch (Exception e){
             e.printStackTrace();
         }
        return d;

     }


}
