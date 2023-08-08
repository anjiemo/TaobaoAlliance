package com.example.taobaoalliance.utils;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.example.taobaoalliance.base.BaseApplication;
import com.example.taobaoalliance.model.domain.IBaseInfo;
import com.example.taobaoalliance.presenter.ITickPresenter;
import com.example.taobaoalliance.ui.activity.TicketActivity;

public class TicketUtils {

    public static void toTicketPage(IBaseInfo baseInfo) {
        Context appContext = BaseApplication.getAppContext();
        //特惠列表内容被点击
        //处理数据
        String title = baseInfo.getTitle();
        String url = baseInfo.getUrl();
        if (TextUtils.isEmpty(url)) {
            url = baseInfo.getUrl();
        }
        String cover = baseInfo.getCover();
        //拿到TicketPresenter去加载数据
        ITickPresenter ticketPresenter = PresenterManager.getInstance().getTicketPresenter();
        ticketPresenter.getTicket(title, url, cover);
        Intent intent = new Intent(appContext, TicketActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        appContext.startActivity(intent);
    }
}
