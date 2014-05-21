package com.dreamteam.app.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.dreateam.app.ui.R;
import com.umeng.socialize.bean.UMComment;

public class CommentAdapter extends BaseAdapter
{
	private ArrayList<UMComment> comments;
	private LayoutInflater inflater;
	private Context context;
	
	
	public CommentAdapter(Context context, ArrayList<UMComment> comments)
	{
		this.comments = comments;
		this.context = context;
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	@Override
	public int getCount()
	{
		return comments.size();
	}

	@Override
	public Object getItem(int id)
	{
		return comments.get(id);
	}

	@Override
	public long getItemId(int id)
	{
		return id;
	}

	@Override
	public View getView(int postion, View convertView, ViewGroup parent)
	{
		ViewHolder holder = null;
		
		if(convertView == null)
		{
			inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.comment_item, null);
			holder = new ViewHolder();
			holder.usrIconIv = (ImageView) convertView.findViewById(R.id.usrportrait_iv);
			holder.contentTv = (TextView) convertView.findViewById(R.id.usrcomment_tv);
			holder.usrNameTv = (TextView) convertView.findViewById(R.id.usrname_tv);
//			holder.agoTimeTv =  (TextView) convertView.findViewById(R.id.comment_minuteago);
			convertView.setTag(holder);
		}
		else
		{
			holder = (ViewHolder) convertView.getTag();
		}
		UMComment comment = comments.get(postion);
		holder.usrNameTv.setText(comment.mUname);
//		holder.agoTimeTv.setText(comment.mDt+"");
		holder.contentTv.setText(comment.mText);
		
		
		return convertView;
	}

	private static final class ViewHolder
	{
		ImageView usrIconIv;
		TextView usrNameTv;
		TextView agoTimeTv;
		TextView contentTv;
	}
}
