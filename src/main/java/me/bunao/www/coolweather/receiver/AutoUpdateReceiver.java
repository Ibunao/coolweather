package me.bunao.www.coolweather.receiver;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import me.bunao.www.coolweather.service.AutoUpdateService;

public class AutoUpdateReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		//启动服务
		Intent i = new Intent(context, AutoUpdateService.class);
		context.startService(i);
	}

}
