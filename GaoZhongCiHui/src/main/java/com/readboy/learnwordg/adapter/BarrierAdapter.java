package com.readboy.learnwordg.adapter;//package com.readboy.learnwordg.adapter;
//
//import android.app.Activity;
//import android.content.Context;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.BaseAdapter;
//import android.widget.ImageView;
//import android.widget.RatingBar;
//import android.widget.TextView;
//
//import com.readboy.learnwordg.R;
//import com.readboy.learnwordg.util.Util;
//
//public class BarrierAdapter extends BaseAdapter {
//
//	Context context;
//	LayoutInflater mInflater;
//
//	public BarrierAdapter(Context context) {
//		// TODO Auto-generated constructor stub
//		this.context=context;
//		mInflater=((Activity)context).getLayoutInflater();
//	}
//
//	@Override
//	public boolean areAllItemsEnabled() {
//		// TODO Auto-generated method stub
//		return false;
//	}
//
//	@Override
//	public boolean isEnabled(int position) {
//		// TODO Auto-generated method stub
//		if (position>= Util.curstage){
//			return false;
//		}else{
//			return true;
//		}
//	}
//
//	@Override
//	public int getCount() {
//		// TODO Auto-generated method stub
//		return 20;
//	}
//
//	@Override
//	public Object getItem(int position) {
//		// TODO Auto-generated method stub
//		return position;
//	}
//
//
//
//	@Override
//	public long getItemId(int position) {
//		// TODO Auto-generated method stub
//		return position;
//	}
//
//	@Override
//	public View getView(int position, View convertView, ViewGroup parent) {
//		// TODO Auto-generated method stub
//		ViewHolder viewHolder = null;
//		if (convertView == null) {
//			convertView = mInflater.inflate(R.layout.barrieritem, null);
//			viewHolder = new ViewHolder(convertView);
//			convertView.setTag(viewHolder);
//		} else {
//			viewHolder = (ViewHolder) convertView.getTag();
//		}
//		viewHolder.stagename.setText("第"+ Util.toCHS(position + 1)+"关");
//        viewHolder.stagenum.setText(position+1+"");
//        if((position+1)%5==0){
//            viewHolder.gift.setVisibility(View.VISIBLE);
//        }else{
//            viewHolder.gift.setVisibility(View.GONE);
//        }
//
//        if(!isEnabled(position)){
//            viewHolder.locked.setVisibility(View.VISIBLE);
//            viewHolder.stagenum.setVisibility(View.GONE);
//        }else{
//            viewHolder.locked.setVisibility(View.GONE);
//            viewHolder.stagenum.setVisibility(View.VISIBLE);
//        }
//
//        if(Util.spendtime[position+1]==0){
//            viewHolder.rate.setRating(0);
//        }else{
//            viewHolder.rate.setRating(rate(Util.spendtime[position+1]));
//        }
//
//		return convertView;
//	}
//
//    int rate(int time){
//        int min=time/60;
//        int second=time%60;
//        int rate;
//        if (time==0){
//            return 0;
//        }
//        if (min<3){
//            rate=5;
//        }else if (min<4){
//            rate=4;
//        }else if (min<5){
//            rate=3;
//        }else if (min<6){
//            rate=2;
//        }else {
//            rate=1;
//        }
//        return  rate;
//    }
//
//	class ViewHolder {
//
//		private TextView stagename;
//		private RatingBar rate;
//        private TextView stagenum;
//        private ImageView gift;
//        private ImageView locked;
//
//
//		public ViewHolder(View view) {
//
//			stagename=(TextView) view.findViewById(R.id.stagename);
//			rate=(RatingBar) view.findViewById(R.id.rate);
//            stagenum=(TextView) view.findViewById(R.id.stagenum);
//            gift=(ImageView) view.findViewById(R.id.stage_gift);
//            locked=(ImageView) view.findViewById(R.id.stage_locked);
//		}
//	}
//
//}
