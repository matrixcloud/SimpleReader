package com.dreamteam.app.ui;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpStatus;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.dreamteam.app.adapter.CommentAdapter;
import com.dreamteam.app.commons.UMHelper;
import com.dreamteam.custom.ui.PullToRefreshListView;
import com.dreamteam.custom.ui.PullToRefreshListView.OnRefreshListener;
import com.dreateam.app.ui.R;
import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.bean.UMComment;
import com.umeng.socialize.controller.listener.SocializeListeners.FetchCommetsListener;

public class Comment extends Activity
{
	private PullToRefreshListView commentLv;
	private ArrayList<UMComment> mComments = new ArrayList<UMComment>();
	

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
		commentLv.setOnRefreshListener(new OnRefreshListener()
		{
			@Override
			public void onRefresh()
			{
				
			}
		});
	}
	
	private void initData()
	{
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
				}
			}
		}, System.currentTimeMillis());
		
		CommentAdapter adapter = new CommentAdapter(this, mComments);
		commentLv.setAdapter(adapter);
	}

	//必须public
	public void onAddComment(View v)
	{
		System.out.println("onAddComment");
	}
}
