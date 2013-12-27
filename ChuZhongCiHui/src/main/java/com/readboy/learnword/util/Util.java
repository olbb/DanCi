package com.readboy.learnword.util;
import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.readboy.learnword.Board;
import com.readboy.learnword.ChengHao;
import com.readboy.learnword.Config;
import com.readboy.learnword.ErrorWord;
import com.readboy.learnword.LearnErrorWord;
import com.readboy.learnword.LearnWord;
import com.readboy.learnword.R;
import com.readboy.learnword.Rate;
import com.readboy.learnword.SelectWord;
import com.readboy.learnword.TestWords;
import com.readboy.learnword.Warn;
import com.readboy.learnword.WarnZhuCe;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Util{

    public static int curstage;//解锁关卡数
    public static int stage;//当前关卡数
//    public static RecordEvent re;
    public static String curstate;
    public static String dir;
    public static User user;
//    public static String url="http://192.168.16.133/API/word.php/";
//    public static String url="http://42.96.131.4/API/word.php/";
    public static String url="http://wkt.hgclass.com/MacAPI/word.php/";

    public static int min_shift=100;//最小移动距离

    //排行榜相关接口
    //post
    public static String ADD_USER="addUser";
    public static String UPDATE_USER="updateUser";
    public static String sendtime="diligent";//提交用户在线时常
    public static String sendstage="level";//提交用户通关信息
    public static String userinfo="rank";//获取用户信息
    //get
//    public static String getTotal ="diligentDay";//获取勤奋日榜
    public static String getTotal ="ranking";//获取勤奋总榜
    public static String getWeek="diligentWeek";//获取勤奋周榜
    public static String getstage="levelWeek";//获取通关效率榜
    public static String upload="upload";//上传图片


    public int getcurstage(){
        return curstage;
    }

    public  static int[]spendtime=new int[37];

    //阿拉伯数字转化为汉字数字
	public static String toCHS(int i){
		
		String sb = null;
		
		if (i<10){
			switch (i) {
//			case 0:
//				sb="零";
//				break;
			
			case 1:
				sb="一";
				break;
				
			case 2:
				sb="二";
				break;
				
			case 3:
				sb="三";
				break;
				
			case 4:
				sb="四";
				break;
				
			case 5:
				sb="五";
				break;
				
			case 6:
				sb="六";
				break;
				
			case 7:
				sb="七";
				break;
				
			case 8:
				sb="八";
				break;
				
			case 9:
				sb="九";
				break;

			default:
				sb="";
				break;
			}
		}else if (i<100){
			if ((int)i/10==1){
				return "十"+toCHS(i%10);
			}else{
				return toCHS((int)i/10)+"十"+toCHS(i%10);
			}
			
			
		}else if (i<1000){
			if ((int)i%100/10==0&&i%10!=0){
				return toCHS((int)i/100)+"百零"+toCHS((int)i%100);
			}else{
				return toCHS((int)i/100)+"百"+toCHS((int)i%100);
			}
			
		}
		
		
		return sb;
	}

    public static void copyfile(Context context,String filename){

//        File fdr = context.getExternalFilesDir(null);
        File fdr = context.getFilesDir();
        if(fdr==null){
//            Log.e("LearnWord","getExternalFilesDir return null");
            return;
        }
        dir=fdr.toString()+"/";
        File file=new File(fdr,filename);
//        File targetDir = new File(filesDir, filename);


//        File file = new File("/data/data/" + getPackageName() + "/databases/",
//                Utils.GLOBALDB+".db");
//        file.getParentFile().mkdirs();
        if(file.exists()){
//            Log.d("LearnWord","file"+filename+"is exists");

            return;
        }


        InputStream in;
        OutputStream out;
        try{
            in = context.getAssets().open(filename);
            out = new FileOutputStream(file);
            byte[] buff = new byte[1024];
            int len;
            while ((len = in.read(buff)) > 0) {
                out.write(buff, 0, len);
            }

            out.close();
            in.close();
//            Log.i("LearnWord","文件"+filename+"复制成功");
        }catch (IOException e){

        }



    }

    public static void finish(){
        try{
            LearnErrorWord.instance.finish();
        }catch (Exception e){

        }
        try{
            LearnWord.instance.finish();
        }catch (Exception e){

        }

        try{
            ErrorWord.instance.finish();
        }catch (Exception e){

        }

        try{
            TestWords.instance.finish();
        }catch (Exception e){

        }
        try{
            SelectWord.instance.finish();
        }catch (Exception e){

        }
        try{
            Rate.instance.finish();
        }catch (Exception e){

        }
        try{
            Board.instance.finish();
        }catch (Exception e){

        }
        try{
            Config.instance.finish();
        }catch (Exception e){

        }
        try{
            Warn.instance.finish();
        }catch (Exception e){

        }
        try{
            WarnZhuCe.instance.finish();
        }catch (Exception e){

        }
    }

    public static void httpPost(final String TYPE,final List<NameValuePair> pairs){

        if (user==null||user.uid==0){
            return;
        }
               Thread thread=new Thread(new Runnable() {
           @Override
           public void run() {


               HttpClient httpClient=new DefaultHttpClient();
               HttpPost httpPost=new HttpPost(url+TYPE);
//               httpPost.addHeader("id",user.uid+"");
               httpPost.addHeader("uuid",user.uid+"");
               httpPost.addHeader("charset", HTTP.UTF_8);

               HttpResponse httpResponse;

               try{
//                   List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
//                   nameValuePairs.add(new BasicNameValuePair("username", user.realname));

                    httpPost.setEntity(new UrlEncodedFormEntity(pairs,HTTP.UTF_8));


//                   System.out.println();
//                   Log.d("LearnWord",jb.toString());
                   httpResponse=httpClient.execute(httpPost);

//                   Log.d("LearnWord", EntityUtils.toString(httpResponse.getEntity()));
               }catch (Exception e){
                   e.printStackTrace();
               }
           }
       });
        thread.start();
//        return result;
    }


    static Thread getthread;

    public static  void httpget(final String TYPE,final int id){

        if (user.uid==0){
            return;
        }



       getthread=new Thread(new Runnable() {
           @Override
           public void run() {
               HttpClient httpClient=new DefaultHttpClient();
               HttpGet httpGet=new HttpGet(url+TYPE);
//               httpGet.addHeader("id",user.uid+"");
               httpGet.addHeader("uuid",user.uid+"");
               HttpResponse httpResponse;
               String result="";

               try{
//                   httpGet.set(new UrlEncodedFormEntity(pairs, HTTP.UTF_8));
                   httpResponse=httpClient.execute(httpGet);
//            System.out.println(httpResponse.toString());
//            System.out.println(httpResponse.getStatusLine().toString());

                   if(httpResponse.getStatusLine().getStatusCode()==200){
                       result=EntityUtils.toString(httpResponse.getEntity());
                   }else{
//                       System.out.println(httpResponse.getStatusLine().toString());
                   }
               }catch (Exception e){
                    e.printStackTrace();
               }


//               System.out.println(result);

               if(id!=Board.id){
                   getthread.interrupt();
                   return;
               }

               if(TYPE==userinfo){
                   try{
                       JSONObject jb=new JSONObject(result);
                       Board.totaltimestr=gettime(jb.getInt("totalTime"));
                       Board.totalnum =jb.getInt("totalRank");
                       Board.weeknum=jb.getInt("diligentWeek");
//                       Board.hanler.sendEmptyMessage(Board.UPDATE_USER_INFO);
                       sendmsg(Board.UPDATE_USER_INFO,id);
                   }catch (JSONException e){

                   }


               }else if(TYPE==getWeek){
                   try{
                       Board.weeklist.clear();
                       JSONArray ja=new JSONArray(result);
                       for (int i=0;i<ja.length();i++){
                           JSONObject jb=ja.getJSONObject(i);
                           Board.weeklist.add(getUser(jb));

                       }
//                       Board.hanler.sendEmptyMessage(Board.UPDATE_WEEK_BOARD);
                       sendmsg(Board.UPDATE_WEEK_BOARD,id);
                   }catch (JSONException  e){

                   }

               }else if(TYPE== getTotal){
                   try{
                       Board.totallist.clear();
                       JSONArray ja=new JSONArray(result);
                       for (int i=0;i<ja.length();i++){
                           JSONObject jb=ja.getJSONObject(i);
                           Board.totallist.add(getUser(jb));

                       }
//                       Log.d("Tag","Size of total is "+ja.length());
//                       Log.d("Tag","Size of list is "+Board.totallist.size());

//                       Board.hanler.sendEmptyMessage(Board.UPDATE_TOTAL_BOARD);
                        sendmsg(Board.UPDATE_TOTAL_BOARD,id);
                   }catch (JSONException  e){

                   }

               }

           }
       });
        getthread.start();
        if(threads!=null){
            threads.add(getthread);
        }else{
            threads=new ArrayList<Thread>();
            threads.add(getthread);
        }


    }

    static List<Thread>threads;

    public static void onBoardDestory(){
        if(threads==null||threads.size()==0){
            return;
        }
        for(Thread thread:threads){
            thread.interrupt();
        }
        threads.clear();
    }


     static  void sendmsg(int what,int id){
        Message msg=Board.hanler.obtainMessage();
        Bundle data=new Bundle();
        data.putInt("id",id);
        data.putInt("what",what);
        msg.setData(data);
        msg.sendToTarget();
//         Log.i("LearnWord","sendmsg");
    }


    public static void upload() {
        Thread thread=new Thread(new Runnable() {
            @Override
            public void run() {
                String code = "0";
                String imagepath=user.imagePath;
                HttpParams httpParameters = new BasicHttpParams();
                HttpConnectionParams.setConnectionTimeout(httpParameters, 1000 * 30);
                HttpConnectionParams.setSoTimeout(httpParameters, 1000*30);
                HttpConnectionParams.setTcpNoDelay(httpParameters, true);

                HttpClient httpclient = new DefaultHttpClient(httpParameters);

                HttpPost httppost = new HttpPost(url+upload);
                httppost.addHeader("uuid",user.uid+"");

                MultipartEntity mpEntity = new MultipartEntity();
                try {
                    File imageFile = new File(imagepath);

                    if(!imageFile.exists()) {
//                        Log.i("http", "999");
                        code = "999";
                    }
//            for (Map.Entry<String, String> entry : params.entrySet()) {
//                String key = entry.getKey().toString();
//                String value = entry.getValue().toString();
//                Log.i("Http","KEY:" + key + ",Value:" + value );
//                mpEntity.addPart(key, new StringBody(value));
//            }

                    FileBody file = new FileBody(imageFile,"image/jpg");

                    mpEntity.addPart("avatar", file);

                    httppost.setEntity(mpEntity);
                    HttpResponse response = httpclient.execute(httppost);
                    if (response.getStatusLine().getStatusCode() == 200) {
                        code = EntityUtils.toString(response.getEntity());
//                        System.out.println("result:" + code);
//                        Log.i("http", code);

                    }
                } catch (Exception e) {
//            return code;
                    e.printStackTrace();
                }
            }
        });
        thread.start();


//        return code;
    }


    /**
     * 根据年级代码返回年级信息
     *
     * @param code
     * @return
     */
    public static final String getGrade(int code) {
    /*
     *
     * 513=>"一年级", 514=>"二年级", 515=>"三年级", 516=>"四年级", 517=>"五年级",
     * 518=>"六年级", 519=>"初一", 520=>"初二", 521=>"初三",
     *
     * 769=>"高一", 770=>"高二", 771=>"高三"
     */
        String grade = "";
        if (code <= 259 && code >= 257) {
            grade = "幼儿";
        } else if (code > 771) {
            grade = "高中全年级";
        } else {
            switch (code) {
                case 513:
                    grade = "小学一年级";
                    break;
                case 514:
                    grade = "小学二年级";
                    break;
                case 515:
                    grade = "小学三年级";
                    break;
                case 516:
                    grade = "小学四年级";
                    break;
                case 517:
                    grade = "小学五年级";
                    break;
                case 518:
                    grade = "小学六年级";
                    break;
                case 519:
                    grade = "初中一年级";
                    break;
                case 520:
                    grade = "初中二年级";
                    break;
                case 521:
                    grade = "初中三年级";
                    break;

                case 769:
                    grade = "高中一年级";
                    break;
                case 770:
                    grade = "高中二年级";
                    break;
                case 771:
                    grade = "高中三年级";
                    break;

                default:
                    break;
            }
        }

        return grade;
    }


    public static void setfont(Context context){
        Resources res=context.getResources();
        Configuration c=res.getConfiguration();
        c.fontScale=1;
        res.updateConfiguration(c,res.getDisplayMetrics());
    }

    public static String gettime(int time){
//        Log.d("LearnWord","time is "+time);
        int day,h,m,s;
        s=time%60;
        m=time/60%60;
        h=time/3600%24;
        day=time/3600/24;
        if(day>0){
            return day+"天"+h+"小时";
        }else if(h>0){
            return h+"小时"+m+"分";
        }else if(m>0){
            return m+"分"+s+"秒";
        }else{
            return s+"秒";
        }

    }

    static User getUser(JSONObject jb){
        User user=new User();
        try{
            user.realname=jb.getString("username");
            user.uid=jb.getInt("uid");
//            URL url=new URL(jb.getString("avatar"));
//            HttpURLConnection conn=(HttpURLConnection)url.openConnection();
//            conn.connect();
            user.imagePath=jb.getString("avatar");

            user.time=gettime(jb.getInt("time"));

            int previous=jb.getInt("previous");
            if (previous==0){
                previous=100;
            }
            user.pre=previous;
//            Log.e("L","user is"+user.toString());
//            InputStream is=conn.getInputStream();
//            user.userimg=BitmapFactory.decodeStream(is);
//             user.userimg=Drawable.createFromStream(is,null);
        }catch (JSONException e){
            e.printStackTrace();
        }

        return user;


    }

    public static String getChengHao(Context context,int a){
        String str="";
        try{
            //根据字符串字段名，取字段
            Field field=R.string.class.getField("chenghao"+a);
            //取值
            int resId=(Integer)field.get(new R.string());

            str=context.getString(resId);
        }catch (Exception e){

        }


        return str;
    }

    static Typeface tf;

    public static void changeFonts( Activity act) {

        if(tf==null){
            tf = Typeface.createFromAsset(act.getAssets(),
                    "FZMWFont.ttf");
        }

        ViewGroup root=(ViewGroup)act.findViewById(android.R.id.content);

        for (int i = 0; i < root.getChildCount(); i++) {
            View v = root.getChildAt(i);
            if (v instanceof TextView) {
                ((TextView) v).setTypeface(tf);
            } else if (v instanceof Button) {
                ((Button) v).setTypeface(tf);
            } else if (v instanceof EditText) {
                ((EditText) v).setTypeface(tf);
            } else if (v instanceof ViewGroup) {
                changeFonts( act);
            }
        }
    }

    }
