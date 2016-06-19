package me.bunao.www.coolweather.util;

/**
 * Created by Expect on 2016/6/19.
 */
/*回调函数*/
public interface HttpCallbackListener {
    void onFinish(String response);

    void onError(Exception e);
}
