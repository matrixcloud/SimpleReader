package com.dreamteam.app.ui;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.RemoteException;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.dreamteam.app.adapter.ItemListAdapter;
import com.dreamteam.app.commons.AppContext;
import com.dreamteam.app.commons.HtmlFilter;
import com.dreamteam.app.commons.IFlyHelper;
import com.dreamteam.app.commons.ItemListEntityParser;
import com.dreamteam.app.commons.SectionHelper;
import com.dreamteam.app.commons.SeriaHelper;
import com.dreamteam.app.commons.UIHelper;
import com.dreamteam.app.entity.FeedItem;
import com.dreamteam.app.entity.ItemListEntity;
import com.dreateam.app.ui.R;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.iflytek.speech.SpeechConstant;
import com.iflytek.speech.SpeechSynthesizer;
import com.iflytek.speech.SynthesizerListener;

/**
 * @description TODO
 * @author zcloud
 * @date 2013/11/14
 */
public class ItemList extends Activity
{

	public static final String tag = "ItemList";
	private PullToRefreshListView itemLv;
	private ImageButton backBtn;
	private ImageButton playBtn;
	private TextView feedTitleTv;
	private ItemListAdapter mAdapter;
	private SeriaHelper seriaHelper;
	private ArrayList<FeedItem> mItems = new ArrayList<FeedItem>();
	private ArrayList<String> speechTextList = new ArrayList<String>();
	private String sectionTitle;
	private String sectionUrl;
	private SpeechSynthesizer tts;
	private SynthesizerListener mTtsListener;
	private BroadcastReceiver mReceiver;
	// 开始词
	private static final String START_WORDS = "欢迎收听";
	private static int speechCount = 0;
	private boolean existSpeech = false;// 退出tts
	public static final String ACTION_UPDATE_ITEM_LIST = "com.dreamteam.action.update_item_list";
	private boolean isNight;// 是否夜间

	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		initView();
		initData();
		initTts();
		initBroadeCast();
	}

	private void initBroadeCast()
	{
		mReceiver = new BroadcastReceiver()
		{
			@Override
			public void onReceive(Context context, Intent intent)
			{
				String link = intent.getStringExtra("link");
				boolean isFavorite = intent.getBooleanExtra("is_favorite",
						false);
				for (FeedItem i : mItems)
				{
					if (i.getLink().equals(link))
					{
						i.setFavorite(isFavorite);
						break;
					}
				}
			}
		};
		IntentFilter filter = new IntentFilter();
		filter.addAction(ACTION_UPDATE_ITEM_LIST);
		registerReceiver(mReceiver, filter);
	}

	private void initTts()
	{
		tts = new SpeechSynthesizer(this, null);
		tts.setParameter(SpeechConstant.ENGINE_TYPE, "local");
		tts.setParameter(SpeechSynthesizer.SPEED, "50");
		tts.setParameter(SpeechSynthesizer.PITCH, "50");
		mTtsListener = new SynthesizerListener.Stub()
		{
			@Override
			public void onSpeakResumed() throws RemoteException
			{
			}
			@Override
			public void onSpeakProgress(int arg0) throws RemoteException
			{
			}
			@Override
			public void onSpeakPaused() throws RemoteException
			{
			}
			@Override
			public void onSpeakBegin() throws RemoteException
			{
			}

			@Override
			public void onCompleted(int arg0) throws RemoteException
			{
				speechCount++;
				if (speechCount > speechTextList.size())
					return;
				tts.startSpeaking(speechTextList.get(speechCount), mTtsListener);
			}

			@Override
			public void onBufferProgress(int arg0) throws RemoteException
			{
			}
		};
	}

	private void initView()
	{
		UIHelper.initTheme(this);
		setContentView(R.layout.feed_item_list);
		feedTitleTv = (TextView) findViewById(R.id.fil_feed_title);
		playBtn = (ImageButton) findViewById(R.id.fil_play_btn);
		playBtn.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				if(!IFlyHelper.checkSpeechServiceInstall(ItemList.this))
				{
					IFlyHelper.openDownloadDialog(ItemList.this);
					return;
				}
				if (existSpeech)
				{
					tts.stopSpeaking(mTtsListener);
					existSpeech = false;
					return;
				}
				startSpeech();
				existSpeech = true;
				Toast.makeText(ItemList.this, "再按一次退出播放", Toast.LENGTH_SHORT)
						.show();
			}
		});
		backBtn = (ImageButton) findViewById(R.id.fil_back_btn);
		backBtn.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				ItemList.this.finish();
			}
		});
		itemLv = (PullToRefreshListView) findViewById(R.id.fil_lv_feed_item);
		itemLv.setOnRefreshListener(new OnRefreshListener<ListView>()
		{
			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView)
			{
				
			}
		});
		
		
//		itemLv.setOnRefreshListener(new OnRefreshListener()
//		{
//			public void onRefresh()
//			{
////				if (!AppContext.isNetworkAvailable(ItemList.this))
////				{
////					itemLv.onRefreshComplete();
////					Toast.makeText(ItemList.this, R.string.no_network,
////							Toast.LENGTH_SHORT).show();
////					return;
////				}
////				new RefreshTask().execute(sectionUrl);
//			}
//		});
		itemLv.setOnItemClickListener(new OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id)
			{
				Intent intent = new Intent();
				FeedItem item = mItems.get(position - 1);

				final String link = item.getLink();
				// 改变阅读状态
				if (!item.isReaded())
				{
					item.setReaded(true);
					mAdapter.notifyDataSetChanged();
					
					new Thread()
					{
						@Override
						public void run()
						{
							SeriaHelper helper = SeriaHelper.newInstance();
							File cache = SectionHelper.getSdCache(sectionUrl);
							ItemListEntity entity = new ItemListEntity();
							for (FeedItem i : mItems)
							{
								if (i.getLink().equals(link))
								{
									i.setReaded(true);
								}
							}
							entity.setItemList(mItems);
							helper.saveObject(entity, cache);
						}

					}.start();
				}
				String title = item.getTitle();
				String contentEncoded = item.getContent();
				String pubdate = item.getPubdate();
				boolean isFavorite = item.isFavorite();
				String firstImgUrl = item.getFirstImageUrl();
				if (contentEncoded != null && contentEncoded.length() != 0)
				{
					intent.putExtra("item_detail", contentEncoded);
				}
				intent.putExtra("section_title", sectionTitle);
				intent.putExtra("section_url", sectionUrl);
				intent.putExtra("title", title);
				intent.putExtra("pubdate", pubdate);
				intent.putExtra("link", link);
				intent.putExtra("is_favorite", isFavorite);
				intent.putExtra("first_img_url", firstImgUrl);
				intent.setClass(ItemList.this, ItemDetail.class);
				ItemList.this.startActivity(intent);
			}

		});
	}

	private void initData()
	{
		Intent intent = getIntent();
		sectionTitle = intent.getStringExtra("section_title");
		sectionUrl = intent.getStringExtra("url");
		feedTitleTv.setText(sectionTitle);

		File file = SectionHelper.getSdCache(sectionUrl);
		if (file.exists())
		{
			seriaHelper = SeriaHelper.newInstance();
			ItemListEntity itemListEntity = (ItemListEntity) seriaHelper
					.readObject(file);
			mItems = itemListEntity.getItemList();
			if (mItems != null)
			{
				mAdapter = new ItemListAdapter(this, mItems, isNight);
				itemLv.setAdapter(mAdapter);
				for (int i = 0, n = mItems.size(); i < n; i++)
				{
					FeedItem item = mItems.get(i);
					String input = item.getTitle() + item.getContent();
					speechTextList.add(HtmlFilter.filterHtml(input));
				}
			}
		}
	}

	private class RefreshTask extends
			AsyncTask<String, Integer, ItemListEntity>
	{
		@Override
		protected void onPostExecute(ItemListEntity result)
		{
			if (result == null)
			{
				itemLv.onRefreshComplete();
				Toast.makeText(ItemList.this, R.string.network_exception,
						Toast.LENGTH_SHORT).show();
				return;
			}
			ArrayList<FeedItem> newItems = new ArrayList<FeedItem>();
			File cache = SectionHelper.getSdCache(sectionUrl);
			SeriaHelper helper = SeriaHelper.newInstance();
			ArrayList<FeedItem> items = result.getItemList();
			ItemListEntity old = (ItemListEntity) helper.readObject(cache);
			String oldFirstDate = old.getFirstItem().getPubdate();
			int newCount = 0;
			for (FeedItem i : items)
			{
				if (i.getPubdate().equals(oldFirstDate))
				{
					itemLv.onRefreshComplete();
					Toast.makeText(ItemList.this, R.string.no_update,
							Toast.LENGTH_SHORT).show();
					return;
				}
				newCount++;
				newItems.add(i);
			}
			helper.saveObject(result, cache);
			mAdapter.addItemsToHead(newItems);
			Toast.makeText(ItemList.this, "更新了" + newCount + "条",
					Toast.LENGTH_SHORT).show();
			itemLv.onRefreshComplete();
		}

		@Override
		protected ItemListEntity doInBackground(String... params)
		{
			ItemListEntityParser parser = new ItemListEntityParser();
			return parser.parse(params[0]);
		}
	}

	private void startSpeech()
	{
		DateFormat df = SimpleDateFormat.getTimeInstance();
		String time = df.format(new Date());
		String timeTip = "现在是：" + time;
		tts.startSpeaking(START_WORDS + sectionTitle + "频道" + timeTip
				+ speechTextList.get(0), mTtsListener);
	}

	@Override
	protected void onDestroy()
	{
		tts.stopSpeaking(mTtsListener);
		tts.destory();
		unregisterReceiver(mReceiver);
		super.onDestroy();
	}
}
