package com.dreamteam.app.ui;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.dreamteam.app.commons.AppConfig;
import com.dreamteam.app.commons.AppContext;
import com.dreamteam.app.db.DbManager;
import com.dreamteam.app.db.FavoItemDbHelper;
import com.dreamteam.app.utils.MD5;
import com.dreateam.app.ui.R;
import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.bean.UMComment;
import com.umeng.socialize.controller.RequestType;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.listener.SocializeListeners.FetchCommetsListener;

@SuppressWarnings("deprecation")
public class ItemDetail extends FragmentActivity
{
	private ImageButton collectBtn;
	private ImageButton shareBtn;
	private ImageButton commentBtn;
	private TextView countTv;//评论列表
	private TextView topTitleTv;
	private static WebView mWebView;
	//html全局属性
	public final static String WEB_STYLE = "<style>* "
			+ "{font-size:16px;line-height:20px;}"
			+ "p {color:#333;backgroud-color:#0}"
			+ "img {max-width:310px;} "
			+ "pre {font-size:9pt;line-height:12pt;font-family:Courier New,Arial;border:1px solid #ddd;border-left:5px solid #6CE26C;background:#f6f6f6;padding:5px;} "
			+ "a {text-decoration: none;color:#3E62A6}" 
			+ "h1 {text-align:center;font-size:20px}"
			+ "</style>";
	private String title;
	private String pubdate;
	private String itemDetail;
	private UMSocialService mController;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		
		initView();
		loadData();
		initComments();
	}

	private void initComments()
	{
		String key = MD5.Md5(title + pubdate);
		mController = UMServiceFactory.getUMSocialService(AppConfig.UM_BASE_KEY + key,
					RequestType.SOCIAL);
		mController.getComments(this, new FetchCommetsListener()
		{
			@Override
			public void onStart()
			{
			}
			@Override
			public void onComplete(int status, List<UMComment> comments, SocializeEntity entity)
			{
				if(status == 200 && comments != null && !comments.isEmpty())
				{
					countTv.setText(comments.size() + "");
				}
			}
		}, -1);
	}

	@SuppressLint({ "NewApi", "SetJavaScriptEnabled" })
	private void initView()
	{
		setContentView(R.layout.feed_item_detail);
		topTitleTv = (TextView) findViewById(R.id.fid_top_title);
		shareBtn = (ImageButton) findViewById(R.id.fid_btn_share);
		shareBtn.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v)
			{
				mController.openShare(ItemDetail.this, false);
			}
			
		});
		commentBtn = (ImageButton) findViewById(R.id.fid_btn_comment);
		commentBtn.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v)
			{
				mController.openComment(ItemDetail.this, false);
			}
		});
		collectBtn = (ImageButton) findViewById(R.id.fid_btn_collecte);
		collectBtn.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v)
			{
				//判断是否已收藏
				DbManager helper = new DbManager(ItemDetail.this, DbManager.DB_NAME, null, 1);
				SQLiteDatabase db = helper.getWritableDatabase();
				FavoItemDbHelper.insert(db, title, pubdate, itemDetail);
				Toast.makeText(ItemDetail.this, "添加成功!", Toast.LENGTH_SHORT).show();
			}
		});
		countTv = (TextView) findViewById(R.id.fid_tv_comment_count);
		mWebView = (WebView) findViewById(R.id.my_web_view);
		mWebView.getSettings().setBuiltInZoomControls(true);
		mWebView.getSettings().setDisplayZoomControls(false);
		mWebView.getSettings().setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
		mWebView.getSettings().setJavaScriptEnabled(true);
	}
	
	private void loadData()
	{
		Intent intent = getIntent();
		String sectionTitle = intent.getStringExtra("section_title");
		topTitleTv.setText(sectionTitle);
		
		StringBuffer sb = new StringBuffer();
		title = intent.getStringExtra("title");
		pubdate = intent.getStringExtra("pubdate");
		itemDetail = intent.getStringExtra("item_detail");
		//过滤img宽和高
		itemDetail = itemDetail.replaceAll("(<img[^>]*?)\\s+width\\s*=\\s*\\S+", "$1");
		itemDetail = itemDetail.replaceAll(
				"(<img[^>]*?)\\s+height\\s*=\\s*\\S+", "$1");
		//图片双击
		 itemDetail = itemDetail.replaceAll("(<img[^>]+src=\")(\\S+)\"",
					"$1$2\" onClick=\"javascript:mWebViewImageListener.onImageClick('$2')\"");
		//是否加载图片
		SharedPreferences pref = AppContext.getPrefrences(this);
		if(!pref.getBoolean("pref_imageLoad", false))
		{
			itemDetail = itemDetail.replaceAll("(<|;)\\s*(IMG|img)\\s+([^;^>]*)\\s*(;|>)", "");
		}
		sb.append("<h1>" + title + "</h1>"
				  + "<p>" + pubdate + "</p>");
		sb.append("<body>" + itemDetail + "<body>");
		mWebView.loadDataWithBaseURL(null, WEB_STYLE + sb.toString(), "text/html", "UTF-8", null);
	}
}

