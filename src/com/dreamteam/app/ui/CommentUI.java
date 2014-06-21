package com.dreamteam.app.ui;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpStatus;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewStub;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.dreamteam.app.adapter.CommentAdapter;
import com.dreamteam.app.commons.UMHelper;
import com.dreateam.app.ui.R;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.umeng.socialize.bean.MultiStatus;
import com.umeng.socialize.bean.SocializeConfig;
import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.bean.UMComment;
import com.umeng.socialize.controller.listener.SocializeListeners.FetchCommetsListener;
import com.umeng.socialize.controller.listener.SocializeListeners.MulStatusListener;

/**
 * @description TODO
 * @author zcloud
 * @date Jun 21, 2014
 */
public class CommentUI extends Activity
{
	private PullToRefreshListView commentLv;
	private ArrayList<UMComment> mComments = new ArrayList<UMComment>();
	private CommentAdapter mAdapter;
	private InputMethodManager inputMethodMgr;
	private EditText inputEt;
	private String oldUsrMsg;
	

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
		commentLv.setOnRefreshListener(new OnRefreshListener<ListView>()
		{
			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView)
			{
				refreshComments();
			}
		});
		findViewById(R.id.comment_return).setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				finish();
			}
		});
	}

	private void refreshComments()
	{
		UMHelper.getUMSocialService().getComments(this,
			new FetchCommetsListener()
			{
				@Override
				public void onStart()
				{
				}
		
				@Override
				public void onComplete(int status,
						List<UMComment> comments, SocializeEntity entity)
				{
					if (status == HttpStatus.SC_OK && comments != null)
					{
						mComments.addAll(comments);
						mAdapter.notifyDataSetChanged();
					}
				}
			}, System.currentTimeMillis());
	}

	private void initData()
	{
//		File file = new File("");
//		UMCommentListEntity entity = (UMCommentListEntity) SeriaHelper
//				.newInstance().readObject(file);
		refreshComments();
		mAdapter = new CommentAdapter(this, mComments);
		commentLv.setAdapter(mAdapter);
	}

	// 必须public
	public void onAddComment(View v)
	{
		ViewStub vs = (ViewStub) findViewById(R.id.comment_menu);
		View view = vs.inflate();

		inputEt = (EditText) view
				.findViewById(R.id.comment_context_et);
		inputEt.setFocusable(true);
		inputEt.requestFocus();
		// call inputmethod widget
		inputMethodMgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		inputMethodMgr.showSoftInput(inputEt, InputMethodManager.SHOW_FORCED);

		Button btn = (Button) view.findViewById(R.id.comment_send_btn);
		btn.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				String msg = inputEt.getText().toString();
				if(msg.equalsIgnoreCase(oldUsrMsg)){
					Toast.makeText(CommentUI.this, "禁止重复提交", Toast.LENGTH_SHORT).show();
					return;
				}
				if (msg != null && !msg.isEmpty())
				{
					oldUsrMsg = msg;
					sendComment(msg);
				}
			}
		});

	}

	private void sendComment(String msg)
	{
		UMComment comment = new UMComment();
		comment.mText = msg;
		UMHelper.getUMSocialService().postComment(this, comment,
				new MulStatusListener()
				{
					@Override
					public void onStart()
					{
					}

					@Override
					public void onComplete(MultiStatus multiStatus, int status,
							SocializeEntity entity)
					{
						if (status == HttpStatus.SC_OK)
						{
							Toast.makeText(CommentUI.this, "发布成功",
									Toast.LENGTH_SHORT).show();
//							inputMethodMgr.hideSoftInputFromInputMethod(inputEt.getWindowToken(),0);
							refreshComments();
						}
					}
				}, SocializeConfig.getSelectedPlatfrom());
	}
}
