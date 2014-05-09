package com.dreamteam.app.ui;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpStatus;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.dreamteam.app.adapter.CommentAdapter;
import com.dreamteam.app.commons.UMHelper;
import com.dreamteam.custom.ui.PullToRefreshListView;
import com.dreamteam.custom.ui.PullToRefreshListView.OnRefreshListener;
import com.dreateam.app.ui.R;
import com.umeng.socialize.bean.MultiStatus;
import com.umeng.socialize.bean.SocializeConfig;
import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.bean.UMComment;
import com.umeng.socialize.controller.listener.SocializeListeners.FetchCommetsListener;
import com.umeng.socialize.controller.listener.SocializeListeners.MulStatusListener;

public class Comment extends Activity
{
	private PullToRefreshListView commentLv;
	private LinearLayout emptyLayout;
	private ProgressBar loadingBar;

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
		emptyLayout = (LinearLayout) findViewById(R.id.emptyTip);
		loadingBar = (ProgressBar) findViewById(R.id.loadingAnimation);
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
				loadingBar.setVisibility(View.GONE);
				commentLv.setVisibility(View.VISIBLE);
				if(status == HttpStatus.SC_OK && comments != null)
				{
					if(comments.isEmpty())
					{
						commentLv.setEmptyView(emptyLayout);
					}
					else
					{
						mComments.addAll(comments);
						mAdapter.notifyDataSetChanged();
					}
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
