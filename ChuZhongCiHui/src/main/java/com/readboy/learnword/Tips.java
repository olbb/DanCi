package com.readboy.learnword;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.readboy.learnword.util.Util;

/**
 * Created by Mar on 13-9-25.
 */
public class Tips implements View.OnClickListener {


    Context context;
    Dialog dialog;
    public Button exit, gobarrier;
    public Button wrongwords;
    public TextView title, result, spendtime;
    public ImageView resultimg;
    public RatingBar rate;
    AnimationDrawable ad;


    public Tips(Context c) {

        context = c;
        dialog = new Dialog(context, R.style.dialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.tips);

        exit = (Button) dialog.findViewById(R.id.tipsexit);
        gobarrier = (Button) dialog.findViewById(R.id.tips_goon);
        title = (TextView) dialog.findViewById(R.id.tipstitle);
        result = (TextView) dialog.findViewById(R.id.gamerestext);
        spendtime = (TextView) dialog.findViewById(R.id.gamespendtime);
        resultimg = (ImageView) dialog.findViewById(R.id.gameresult);
        rate = (RatingBar) dialog.findViewById(R.id.tips_rate);
        wrongwords = (Button) dialog.findViewById(R.id.tips_viewwrongwords);

        exit.setOnClickListener(this);
        gobarrier.setOnClickListener(this);
        wrongwords.setOnClickListener(this);

        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                try {
                    TestWords.instance.finish();
                } catch (Exception e) {

                }
                try {
                    SelectWord.instance.finish();
                } catch (Exception e) {

                }
                ad.stop();

            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tipsexit:
                dialog.cancel();
                Util.stage = -1;
                TestWords.instance.finish();
                break;

            case R.id.tips_goon:
                dialog.cancel();
                Util.finish();
                if (Util.stage < 36) {
                    Util.stage++;
//                    Barrier.instance.barad.notifyDataSetChanged();
//                SelectWord.instance.update();
//                SelectWord.instance.sd.notifyDataSetChanged();
                    Intent z = new Intent(Barrier.instance, SelectWord.class);
                    z.putExtra("stage", Util.stage);
                    Barrier.instance.startActivity(z);
                } else {
                    Toast.makeText(context, "恭喜你已经完成了全部关卡!", Toast.LENGTH_SHORT).show();

                }


                break;

            case R.id.tips_viewwrongwords:
                Intent i = new Intent(context, ErrorWord.class);
                context.startActivity(i);
                try {
                    TestWords.instance.finish();

                } catch (Exception e) {

                }
//                try{
//                    SelectWord.instance.finish();
//                }catch (Exception e){
//
//                }
                try {
                    LearnWord.instance.finish();
                } catch (Exception e) {

                }
//                Util.stage++;
                break;
        }

    }

    public void show(AnimationDrawable ad) {
        resultimg.setImageDrawable(ad);
        this.ad = ad;
        ad.start();
        dialog.show();
    }


}
