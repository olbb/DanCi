//package com.readboy.learnword.view;
//
//import android.app.Dialog;
//import android.content.Context;
//import android.content.DialogInterface;
//import android.os.Handler;
//import android.util.Log;
//import android.view.Window;
//import android.widget.ImageView;
//
//import com.aispeech.AIEngine;
//import com.aispeech.AIEngineHelper;
//import com.aispeech.AIRecorder;
//import com.readboy.learnword.LearnErrorWord;
//import com.readboy.learnword.LearnWord;
//import com.readboy.learnword.R;
//import com.readboy.learnword.TestWords;
//import com.readboy.learnword.util.Util;
//
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.io.File;
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;
//
///**
// * Created by Mar on 13-9-29.
// */
//
//
//
//public class RecordEvent{
//    String word;
//    String sent;
//    public Handler mh;
//    Dialog dg;
//    public boolean islearning=true;
//    String tempstate;
//
//    boolean isrecording=false;
//    public boolean forresult=true;
//
//    public RecordEvent(Context context){
//        this.context=context;
//        workerThread.execute(initaiengine);
//        mh=new Handler();
//
//
//    }
//
//
//
//    private AIEngine.aiengine_callback callback = new AIEngine.aiengine_callback() {
//        @Override
//        public int run(byte[] id, int type, byte[] data, int size) {
//            if (type == AIEngine.AIENGINE_MESSAGE_TYPE_JSON) {
//                if (!Util.curstate.equals(tempstate)){
//                    mh.post(stoprecord);
//                    return 1;
//                }
//                if (!forresult){
//                    return 1;
//                }
//
//                String result = new String(data, 0, size).trim(); /* must trim the end '\0' */
////                Log.i(TAG,result);
//                try{
//                    JSONObject jb=new JSONObject(result);
//                    int vad_status = jb.getInt("vad_status");
//                    if(vad_status==2&&isrecording){
////                        recorder.stop();
////                        int rv = AIEngine.aiengine_stop(engine);
////                        isrecording = false;
////                        mh.post(stoprecord);
//                        if(dg.isShowing()){
//                            dg.cancel();
//                        }else{
//                            mh.post(stoprecord);
//                        }
//                        isrecording=false;
//                    }
//
//                }catch (JSONException e){
////                    Log.e(TAG,e.toString());
//                    Log.d(TAG,result);
//                    onresult(result);
//                }
//
//
//
//
//            } else if (type == AIEngine.AIENGINE_MESSAGE_TYPE_BIN) {
//
//
//            }
//            return 0;
//        }
//    };
//
//    void onresult(String result){
//         JSONObject jb1,jb2;
//        JSONArray ja;
//        Log.i(TAG, Util.curstate);
//        try{
//            jb1=new JSONObject(result);
////            jb2=jb1.getJSONObject("details");
//            jb2=jb1.getJSONObject("result");
//            ja=jb2.getJSONArray("details");
//            jb2=ja.getJSONObject(0);
//            String word=jb2.getString("char");
//            int score= jb2.getInt("score");
//            Log.d(TAG,word+":"+score);
//            if(Util.curstate.equals("LearnWord")){
//                LearnWord.instance.set(score);
//            }else if(Util.curstate.equals("LearnErrorWord")){
//                LearnErrorWord.instance.set(score);
//            }else if(Util.curstate.equals("TestWord")){
//                if(score>60){
//                    mh.post(new Runnable() {
//                        @Override
//                        public void run() {
//                            TestWords.instance.onCheckedChanged(TestWords.instance.answer,1001);
//                        }
//                    });
//
//                }else {
//                    mh.post(new Runnable() {
//                        @Override
//                        public void run() {
//                            TestWords.instance.onCheckedChanged(TestWords.instance.answer,1000);
//                        }
//                    });
//                }
//            }
//
//        }catch (JSONException e){
//            Log.e(TAG,e.toString());
//            isrecording=false;
//        }
//
//    }
//
//    ImageView im;
//
//    public void setDialog(Context context){
//        dg=new Dialog(context);
//        im=new ImageView(context);
//
//        dg.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        dg.setContentView(im);
//        im.setImageResource(R.drawable.recording);
//        dg.setOnCancelListener(new DialogInterface.OnCancelListener() {
//            @Override
//            public void onCancel(DialogInterface dialogInterface) {
//                if(isrecording){
//                    mh.post(stoprecord);
//                }
//
//            }
//        });
//    }
//
//    public void setWord(String word){
//
//        this.word=word;
//        sent=word;
//    }
//
//    public void setSent(String sent){
//
//        this.sent =sent;
//    }
//
//    public void show(){
//        if(Util.curstate.equals("TestWord")){
//            startrecord();
//            return;
//        }
//        step=3;
//        dg.show();
//        mh.post(show);
//    }
//
//    int step;
//
//    Runnable show=new Runnable() {
//        @Override
//        public void run() {
//            switch (step){
//                case 3:
//                    im.setImageResource(R.drawable.delay3);
//                    step--;
//                    mh.postDelayed(show,1000);
//                    break;
//
//                case 2:
//                    im.setImageResource(R.drawable.delay2);
//                    step--;
//                    mh.postDelayed(show,1000);
//                    break;
//
//                case 1:
//                    im.setImageResource(R.drawable.delay1);
//                    step--;
//                    mh.postDelayed(show,1000);
//                    break;
//
//                default:
//                    im.setImageResource(R.drawable.recording);
//                    isrecording=false;
//                    startrecord();
//                    break;
//            }
//
//        }
//    };
//
//
//    public void startrecord() {
//
//        tempstate= Util.curstate;
////        dg.show();
//        if (isrecording){
////            return;
//            mh.post(stoprecord);
//            return;
//        }
//        isrecording=true;
//
//        byte[] id = new byte[64];
////        int rv = AIEngine.aiengine_start(engine, "{\"app\": {\"userId\": \"user-id\"}, \"audio\": {\"audioType\": \"wav\", \"channel\": 1, \"sampleBytes\": 2, \"sampleRate\": 16000}, \"request\": {\"coreType\": \"en.word.score\", \"refText\": \""+word+"\", \"rank\": 100}}", id, callback);
////        int rv = AIEngine.aiengine_start(engine, "{\"vadEnable\": 1, \"volumeEnable\": 0, \"coreProvideType\": \"native\", \"app\": {\"userId\": \"" + userId + "\"}, \"audio\": {\"audioType\": \"wav\", \"channel\": 1, \"sampleBytes\": 2, \"sampleRate\": 16000}, \"request\": {\"coreType\": \"en.sent.rec\", \"grammar\": \"" + sent + "\"}}", id, callback);
//
//        int rv = AIEngine.aiengine_start(engine, "{\"vadEnable\": 1, \"volumeEnable\": 0, \"coreProvideType\": \"native\", \"app\": {\"userId\": \"user-id\"}, \"audio\": {\"audioType\": \"wav\", \"channel\": 1, \"sampleBytes\": 2, \"sampleRate\": 16000}, \"request\": {\"coreType\": \"en.word.score\", \"refText\": \""+word+"\", \"rank\": 100}}", id, callback);
//
//
//        Log.d(TAG, "engine start: " + rv);
//        String wavPath = AIEngineHelper.getFilesDir(context).getPath() + "/record/" + new String(id).trim() + ".wav";
//        recorder.start(wavPath, new AIRecorder.Callback() {
//            public void run(byte[] data, int size) {
//                AIEngine.aiengine_feed(engine, data, size);
//            }
//        });
////        int timer=word.length()*300+1000;
////        mh.postDelayed(stoprecord,timer);
//    }
//
//    private String appKey = "1379063288000134";
//    private String secretKey = "e735176de9cd8a5fd93474994611d2fc";
//    private String userId = "this-is-user-id";
//    private String deviceId = "";
//    private String serialNumber = "";
//    Context context;
//
//    private ExecutorService workerThread = Executors.newFixedThreadPool(1);
//
//
//
//    long engine=0;
//    String TAG="LearnWord";
//    AIRecorder recorder;
//
//    Runnable initaiengine=new Runnable() {
//
//        @Override
//        public void run() {
//
//			/* create aiengine instance */
//            if (engine == 0) {
//
//                byte buf[] = new byte[64];
////                AIEngine.aiengine_get_device_id(buf, getApplicationContext());
//                AIEngine.aiengine_get_device_id(buf, context);
//                deviceId = new String(buf).trim();
//                Log.d(TAG, "deviceId: " + deviceId);
//
//                File resourceDir = AIEngineHelper.extractResourceOnce(context, "aiengine.resource.zip");
//                File provisionFile = AIEngineHelper.extractProvisionOnce(context, "aiengine.provision");
//                File vadResDir = AIEngineHelper.extractResourceOnce(context, "vad.zip");
////                Log.d(TAG, "resourceDir: " + resourceDir == null ? "" : resourceDir.getAbsolutePath());
////                Log.d(TAG, "provisionFile: " + provisionFile == null ? "" : provisionFile.getAbsolutePath());
//                if (resourceDir == null || provisionFile == null) {
//                    return;
//                }
//
//                serialNumber = AIEngineHelper.registerDeviceOnce(context, appKey, secretKey, deviceId, userId);
//                Log.d(TAG, "serialNumber: " + serialNumber);
//
////                String cfg = String.format("{\"appKey\": \"%s\", \"secretKey\": \"%s\", \"serialNumber\": \"%s\",\"provision\": \"%s\", \"native\": {\"en.word.sent\":{\"res\": \"%s\"}}}",
////                        appKey, secretKey, serialNumber,
////                        provisionFile.getAbsolutePath(),
////                        new File(resourceDir, "bin/eng.snt.splp.0.10").getAbsolutePath());
//
////                String cfg = String.format("{\"appKey\": \"%s\", \"secretKey\": \"%s\", \"serialNumber\": \"%s\",\"provision\": \"%s\", \"native\": {\"en.sent.rec\":{\"res\": \"%s\"}}}",
////                        appKey, secretKey, serialNumber,
////                        provisionFile.getAbsolutePath(),
////                        new File(resourceDir, "bin/eng.rec.splp.0.1").getAbsolutePath());
//
//                 String cfg=String.format("{\"appKey\": \"%s\", \"secretKey\": \"%s\", \"serialNumber\": \"%s\",\"provision\": \"%s\", \"vad\": {\"enable\": 1, \"res\": \"%s\", \"leftMargin\": 120, \"rightMargin\": 120, \"sampleRate\": 16000}, \"native\": {\"en.word.score\":{\"res\": \"%s\"}}}",
//                         appKey, secretKey, serialNumber,
//                         provisionFile.getAbsolutePath(),
//                         new File(vadResDir, "vad.0.1.bin").getAbsolutePath(),
//                         new File(resourceDir, "bin/eng.wrd.strap.1.6").getAbsolutePath());
//
//                engine = AIEngine.aiengine_new(cfg, context);
//                Log.d(TAG, "aiengine: " + engine);
//            }
//
//			/* create airecorder instance  */
//            if (recorder == null) {
//                recorder = AIRecorder.getInstance();
//                Log.d(TAG, "airecorder: " + recorder);
//            }
//        }
//    };
//
//
//    public Runnable stoprecord=new Runnable() {
//        @Override
//        public void run() {
//            recorder.stop();
//            isrecording=false;
//            int rv = AIEngine.aiengine_stop(engine);
//            Log.d(TAG, "engine stop: " + rv);
//            if(dg.isShowing()){
//                dg.cancel();
//            }
//
//        }
//    };
//
//
//}
