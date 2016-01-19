package com.example.skyplayer;

import java.util.ArrayList;

import android.app.Activity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import cn.jpush.android.service.PushService;

public class MainActivity extends Activity {

	private MediaPlayer player;
	private ListView lv;
	private TextView song_info_tv;
	private TextView song_time_tv;
	private ImageView preview_btn;
	private ImageView play_btn;
	private ImageView next_btn;
	private ImageView loop_btn;
	private ImageView share_btn;
	private SeekBar seekbar;
		
	private ArrayList<Music> list;
	private Music crt_music;
	private boolean playbool;
	private int crt_position;
	private int max;
	/**0循环播放 1随机播放  2单曲循环*/
	private int looptype;
	/**进度条总数,为了提高精确度从100改成1000，但是千万别10000，算时间的时候超过int范围*/
	private int maxprocess = 1000;
	/**当前进度*/
	private int process = 0;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		loadMusic();
	}
	
	private void loadMusic()
	{
		player = new MediaPlayer();
		ContentResolver mResolver = this.getContentResolver();   
	    Cursor cursor = mResolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null, MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
	    MusicLogic.getInstacne().initMusic(cursor);
	    
	    ArrayList<String> names = MusicLogic.getInstacne().getMusicNames();
		list = MusicLogic.getInstacne().getMusicList();
		max = list.size();
		lv = (ListView)findViewById(R.id.song_list);
		lv.setTextFilterEnabled(true);
		
		song_info_tv = (TextView)findViewById(R.id.song_name);
		song_time_tv = (TextView)findViewById(R.id.song_time);
		preview_btn = (ImageView)findViewById(R.id.preview_btn);
		next_btn = (ImageView)findViewById(R.id.next_btn);
		share_btn = (ImageView)findViewById(R.id.share_btn);
		play_btn = (ImageView)findViewById(R.id.play_btn);
		loop_btn = (ImageView)findViewById(R.id.loop_btn);
		seekbar = (SeekBar)findViewById(R.id.seekBar1);
		seekbar.setMax(maxprocess);//为了比较精确
		loop_btn.setImageResource(loopArr[0]);
		
		if(names != null)
		{
			lv.setAdapter(new ArrayAdapter<String>(this, R.layout.song_list_xml,names));
		}
		
		initThread();
		initListeners();
		
		crt_position = 0;
        player.setOnCompletionListener(complete); 
        player.setOnPreparedListener(prepare);
		play();
	}
	
	private void initThread()
	{
		new Thread()
		{
	        @Override
	        public void run()
	        {
	          while(true)
	          {  
	        	  try 
	        	  {  
		            sleep(100);  
	        	  } 
	        	  catch (InterruptedException e) 
	        	  {  
		            e.printStackTrace();  
	        	  }  
	        	  
	        	  handler.sendEmptyMessage(0);  
	          }  
	        }
	      }.start();
	}
	
	private void initListeners()
	{
		lv.setOnItemClickListener(itemClick);
		preview_btn.setOnClickListener(previewClick);
		next_btn.setOnClickListener(nextClick);
		play_btn.setOnClickListener(playClick);
		loop_btn.setOnClickListener(loopClick);
		share_btn.setOnClickListener(shareClick);
		seekbar.setOnSeekBarChangeListener(seekbarChange);
	}
	
	public void play() {
		crt_music = list.get(crt_position);
		if(crt_music != null)
		{
			try {
				process = 0;
				player.reset(); //重置多媒体   
				player.setAudioStreamType(AudioManager.STREAM_MUSIC);
				player.setDataSource(crt_music.getUrl());//为多媒体对象设置播放路径   
		        initTitle(crt_music);
		        player.prepareAsync();//准备播放   
		    } catch (Exception e) {   
		        Log.d("MusicService", e.getMessage());   
		    }   
		}
	}
	
	private void goonPlay()
	{
		initBtnState(true);
		player.start();
	}
	
	private void updateProcess()
	{
		if(crt_music != null)
		{
			int crt = (int)(player.getCurrentPosition() / 1000);
			int max = crt_music.getLasttime();
			process = maxprocess * crt / max; 
			song_time_tv.setText(TimeUtil.formatTimeToString(crt, 2) + "/" 
					+ TimeUtil.formatTimeToString(max,2));
			seekbar.setProgress(process);
		}
		
	}
	
	private void changeBar()
	{
		process = seekbar.getProgress();
	    int pro = player.getDuration() * process / maxprocess;
	    player.seekTo(pro);
	}
	
	private void pause()
	{
		initBtnState(false);
		player.pause();
	}
	
	private void playNext()
	{
		crt_position ++;
		if(crt_position >= max)
		{
			crt_position = 0;
		}
		play();
	}
	
	private void playPreview()
	{
		crt_position --;
		if(crt_position < 0)
		{
			crt_position = max - 1;
		}
		play();
	}
	
	private int loopArr[] = {R.drawable.ic_repeat_grey600_36dp,R.drawable.ic_shuffle_grey600_36dp,R.drawable.ic_repeat_one_grey600_36dp};
	
	private void loop()
	{
		looptype ++;
		if(looptype > 2)
		{
			looptype = 0;
		}
		loop_btn.setImageResource(loopArr[looptype]);
	}
	
	//b 是否播放
	private void initBtnState(boolean b)
	{
		playbool = b;
		if(b)
		{
			play_btn.setImageResource(R.drawable.ic_pause_grey600_48dp);
		}
		else
		{
			play_btn.setImageResource(R.drawable.ic_play_arrow_grey600_48dp);
		}
	}
	
	private void initTitle(Music m)
	{
		song_info_tv.setText(m.getTitle() + "(" + m.getArtist() + ")");
	}
	
	private void popShare()
	{
		
	}
	
	//准备播放完毕，开始播放
	private OnPreparedListener prepare = new OnPreparedListener() {
		
		@Override
		public void onPrepared(MediaPlayer mp) {
			// TODO Auto-generated method stub
			int pro = player.getDuration() * process / maxprocess;
			player.seekTo(pro);
			player.start();
			initBtnState(true);
			Log.d("onPrepared", "startPlay");
		}
	};
	
	//播放完毕
	private OnCompletionListener complete = new OnCompletionListener() {
		
		@Override
		public void onCompletion(MediaPlayer mp) {
			
			if(looptype == 0)
			{
				playNext();
			}
			else if(looptype == 1)
			{
				crt_position = (int) (Math.random() * max);
				play();
			}
			else
			{
				play();
			}
		}
	};
	
	//点击选歌
	private OnItemClickListener itemClick = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			crt_position = position;
			play();
		}
	};
	
	//开始播放/暂停
	private OnClickListener playClick = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			if(playbool)
			{
				pause();
			}
			else
			{
				goonPlay();
			}
		}
	};
	
	private OnClickListener previewClick = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			playPreview();
		}
	};
	
	private OnClickListener nextClick = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			playNext();
		}
	};
	
	private OnClickListener shareClick = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			popShare();
		}
	};
	
	//切换循环模式：全部循环，单曲循环，随机播放
	private OnClickListener loopClick = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			loop();
		}
	};
	
	private OnSeekBarChangeListener seekbarChange = new OnSeekBarChangeListener() 
	{
	      
	      @Override
	      public void onStopTrackingTouch(SeekBar seekBar) {
	    	changeBar();
	      }

		@Override
		public void onProgressChanged(SeekBar seekBar, int progress,
				boolean fromUser) {
			// TODO 自动生成的方法存根
			
		}

		@Override
		public void onStartTrackingTouch(SeekBar seekBar) {
			// TODO 自动生成的方法存根
			
		}
	};
	
	private Handler handler = new Handler()
	{

		@Override
		public void handleMessage(Message msg) 
		{
			 switch (msg.what)
			 {
			 	case 0:
			 		if(playbool)
			 		{
			 			updateProcess();
			 		}
			    break;
			 }
		}
		
	};
}
