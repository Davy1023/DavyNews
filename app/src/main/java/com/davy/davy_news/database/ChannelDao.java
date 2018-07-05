package com.davy.davy_news.database;

import com.davy.davy_news.bean.Channel;

import org.litepal.crud.DataSupport;
import org.litepal.crud.callback.SaveCallback;
import org.litepal.crud.callback.UpdateOrDeleteCallback;

import java.util.ArrayList;
import java.util.List;

/**
 * author: Davy
 * date: 18/7/4
 */
public class ChannelDao {

    /**
     * 获取所有频道
     */
    public static List<Channel> getChannels(){
        return DataSupport.findAll(Channel.class);
    }

    /**
     * 保存所有频道
     */
    public static void saveChannels(final List<Channel> channels){
        if(channels == null) return;
        if(channels.size()>0){
            final List<Channel> channelList = new ArrayList<>();
            channelList.addAll(channels);
            DataSupport.deleteAllAsync(Channel.class).listen(new UpdateOrDeleteCallback() {
                @Override
                public void onFinish(int rowsAffected) {

                    DataSupport.markAsDeleted(channelList);
                    DataSupport.saveAllAsync(channelList).listen(new SaveCallback() {
                        @Override
                        public void onFinish(boolean success) {

                        }
                    });
                }
            });
        }
    }
}
