package com.dreamteam.app.ui;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpStatus;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.dreamteam.app.adapter.CommentAdapter;
import com.dreamteam.app.commons.SeriaHelper;
import com.dreamteam.app.commons.UMHelper;
import com.dreamteam.app.entity.UMCommentListEntity;
import com.dreateam.app.ui.R;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.umeng.socialize.bean.MultiStatus;
import com.umeng.socialize.bean.SocializeConfig;
import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.bean.UMComment;
import com.umeng.socialize.controller.listener.SocializeListeners.FetchCommetsListener;
import com.umeng.socialize.controller.listener.SocializeListeners.MulStatusListener;

public class CommentUI extends Activity
{
	private PullToRefreshListView commentLv;
	private ArrayList<UMComment> mComments = new ArrayList<UMComment>();
	private CommentAdapter mAdapter;
	

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		initView();
		initData();
	}

	private void initView()
	{
		setContentView(R.layout.comment);
		commentLv = (PullToRefreshListView) findViewById(R.id.comment_Lv);
	}
	
	private void initData()
	{
		File file = new File("");
		UMCommentListEntity entity = (UMCommentListEntity) SeriaHelper.newInstance().readObject(file);
		
		
		
		UMHelper.getUMSocialService().getComments(this, new FetchCommetsListener()
		{
			@Override
			public void onStart()
			{
			}
			
			@Override
			public void onComplete(int status, List<UMComment> comments, SocializeEntity entity)
			{
				if(status == HttpStatus.SC_OK && comments != null)
				{
					mComments.addAll(comments);
					mAdapter.notifyDataSetChanged();
				}
			}
		}, System.currentTimeMillis());
		mAdapter = new CommentAdapter(this, mComments);
		commentLv.setAdapter(mAdapter);
	}

	//必须public
	public void onAddComment(View v)
	{
		UMComment comment = new UMComment();
		comment.mText = "我来写个评论";
		
		UMHelper.getUMSocialService().postComment(this, comment, new MulStatusListener()
		{
			
			@Override
			public void onStart()
			{
				
			}
			
			@Override
			public void onComplete(MultiStatus multiStatus, int status, SocializeEntity entity)
			{
				if(status == HttpStatus.SC_OK)
				{
					System.out.println("send ok");
				}
			}
		}, SocializeConfig.getSelectedPlatfrom());
	}
}
