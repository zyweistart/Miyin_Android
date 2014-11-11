package com.ancun.widget;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import start.utils.LogUtils;
import start.utils.TimeUtils;
import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.ancun.unicom.R;

/**
 * 播放器
 * @author Start
 */
public class PlayerView extends LinearLayout implements OnClickListener,OnSeekBarChangeListener {

	//播放进度条
	public static final int UPDATEPROGRSS=0xAC0006;
	
	private int currentPosition;
	private boolean playerFlag;
	private MediaPlayer mMediaPlayer;
	private ExecutorService singleThreadExecutor;
	private String playerFilePath;
	
	private ImageButton player_view_ib_player;
	private TextView player_view_tv_playtime;
	private SeekBar player_view_sb_progrss;
	private TextView player_view_tv_totaltime;
	
	public PlayerView(Context context,AttributeSet attrs){
		super(context,attrs);
		LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.module_player_view, this);
		player_view_ib_player=(ImageButton)findViewById(R.id.player_view_ib_player);
		player_view_ib_player.setOnClickListener(this);
		player_view_tv_playtime=(TextView)findViewById(R.id.player_view_tv_playtime);
		player_view_sb_progrss=(SeekBar)findViewById(R.id.player_view_sb_progrss);
		player_view_sb_progrss.setOnSeekBarChangeListener(this);
		player_view_tv_totaltime=(TextView)findViewById(R.id.player_view_tv_totaltime);
		singleThreadExecutor=Executors.newSingleThreadExecutor();
	}
	public void initPlayerFile(String filePath){
		playerFilePath=filePath;
	}
	/**
	 * 初始化播放
	 */
	public void startPlayer(){
		File currentPlayerFile=new File(playerFilePath);
		if(!currentPlayerFile.exists()){
			return;
		}
		try {
			//如果播放之前已有播放则先释放
			if(mMediaPlayer!=null){
				if(mMediaPlayer.isPlaying()){
					mMediaPlayer.stop();
				}
				mMediaPlayer.release();
				mMediaPlayer=null;
			}
			mMediaPlayer=new MediaPlayer();
			mMediaPlayer.setDataSource(currentPlayerFile.getAbsolutePath());
			mMediaPlayer.setOnCompletionListener(new OnCompletionListener() {
				@Override
				public void onCompletion(MediaPlayer mp) {
					currentPosition=0;
					player_view_ib_player.setImageDrawable(getContext().getResources().getDrawable(R.drawable.widget_playerview_media_play));
					player_view_tv_playtime.setText(TimeUtils.convertTime(currentPosition));
					player_view_sb_progrss.setProgress(currentPosition);
				}
			});
			mMediaPlayer.prepare();
			mMediaPlayer.start();
			currentPosition=0;
			int duration=mMediaPlayer.getDuration();
			player_view_ib_player.setImageDrawable(getContext().getResources().getDrawable(R.drawable.widget_playerview_media_pause));
			player_view_tv_playtime.setText(TimeUtils.convertTime(currentPosition));
			player_view_sb_progrss.setMax(duration);
			player_view_sb_progrss.setProgress(currentPosition);
			player_view_tv_totaltime.setText(TimeUtils.convertTime(duration));
			executeProgrss();
		} catch (IllegalArgumentException e) {
			LogUtils.logError(e);
		} catch (SecurityException e) {
			LogUtils.logError(e);
		} catch (IllegalStateException e) {
			LogUtils.logError(e);
		} catch (IOException e) {
			LogUtils.logError(e);
		}
	}
	
	@Override
	public void onProgressChanged(SeekBar seekBar, int progress,boolean fromUser) {
		if(fromUser){
			//如果是用户手动拖动
			if(mMediaPlayer!=null){
				mMediaPlayer.seekTo(progress);
				if(!mMediaPlayer.isPlaying()){
					mMediaPlayer.start();
					executeProgrss();
					player_view_ib_player.setImageDrawable(getContext().getResources().getDrawable(R.drawable.widget_playerview_media_pause));
				}
			}else{
				player_view_sb_progrss.setProgress(0);
			}
		}
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
		
	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		
	}

	/**
	 * 继续播放
	 */
	public void player(){
		if(mMediaPlayer!=null){
			if(!mMediaPlayer.isPlaying()){
				if(currentPosition>0){
					mMediaPlayer.seekTo(currentPosition);
				}
				mMediaPlayer.start();
				executeProgrss();
				player_view_ib_player.setImageDrawable(getContext().getResources().getDrawable(R.drawable.widget_playerview_media_pause));
			}
		}
	}
	/**
	 * 暂停播放
	 */
	public void pause(){
		if(mMediaPlayer!=null){
			if(mMediaPlayer.isPlaying()){
				mMediaPlayer.pause();
				player_view_ib_player.setImageDrawable(getContext().getResources().getDrawable(R.drawable.widget_playerview_media_play));
			}
		}
	}
	
	@Override
	public void onClick(View v) {
		if(v.getId()==R.id.player_view_ib_player){
			if(mMediaPlayer!=null){
				if(mMediaPlayer.isPlaying()){
					pause();
				}else{
					player();
				}
			}
		}
	}
	
	private void executeProgrss(){
		playerFlag=true;
		singleThreadExecutor.execute(progrss);
	}
	
	/**
	 * 播放器进度条更新
	 */
	private Runnable progrss = new Runnable() {
		
		@Override
		public void run() {
			while(playerFlag){
				try {
					handler.sendEmptyMessage(UPDATEPROGRSS);
					Thread.sleep(200);
				} catch (Exception e) {
					LogUtils.logError(e);
				}
			}
		}
		
	};
	
	private Handler handler = new Handler() {
		
		@Override
		public void handleMessage(Message msg) {
			switch(msg.what){
				case UPDATEPROGRSS:
					if(mMediaPlayer!=null&&mMediaPlayer.isPlaying()){
						currentPosition=mMediaPlayer.getCurrentPosition();
						//显示进度条位置
						player_view_sb_progrss.setProgress(currentPosition);
						//显示当前播放时间
						player_view_tv_playtime.setText(TimeUtils.convertTime(currentPosition));
					}else{
						playerFlag=false;
					}
					break;
			}
		}
	};
	
}