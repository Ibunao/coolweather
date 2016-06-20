package me.bunao.www.coolweather.service;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.util.Log;

import me.bunao.www.coolweather.receiver.AutoUpdateReceiver;
import me.bunao.www.coolweather.util.HttpCallbackListener;
import me.bunao.www.coolweather.util.HttpUtil;
import me.bunao.www.coolweather.util.Utility;

public class AutoUpdateService extends Service {

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				updateWeather();
			}
		}).start();
		/*Alarm机制具有唤醒cup的功能，保证每次需要执行定时任务的时候cpu能够正常的工作*/
		AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
		int anHour = 8 * 60 * 60 * 1000; // 这是8小时的毫秒数
		/*SystemClock.elapsedRealtime()方法获取到系统开支至今所经历的时间*/
		long triggerAtTime = SystemClock.elapsedRealtime() + anHour;
		Intent i = new Intent(this, AutoUpdateReceiver.class);
		PendingIntent pi = PendingIntent.getBroadcast(this, 0, i, 0);
		/*设定的时间之后将启动intent，也就是指定的广播
		* AlarmManager.ELAPSED_REALTIME_WAKEUP表示让定时任务的触发时间从系统开机开始算起，会唤醒cup
		* 启动广播后，广播再启动服务，从而实现定时后台刷新
		* */
		manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, pi);
		return super.onStartCommand(intent, flags, startId);
	}
	
	/**
	 * 更新天气信息。
	 */
	private void updateWeather() {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		String weatherCode = prefs.getString("weather_code", "");
		String address = "http://www.weather.com.cn/data/cityinfo/" + weatherCode + ".html";
		HttpUtil.sendHttpRequest(address, new HttpCallbackListener() {
			@Override
			public void onFinish(String response) {
//				Log.d("TAG", response);
				Utility.handleWeatherResponse(AutoUpdateService.this, response);
			}

			@Override
			public void onError(Exception e) {
				e.printStackTrace();
			}
		});
	}

}
