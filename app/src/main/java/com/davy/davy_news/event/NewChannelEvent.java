package com.davy.davy_news.event;

import com.davy.davy_news.bean.Channel;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * author: Davy
 * date: 18/7/14
 */
public class NewChannelEvent {

    public List<Channel> selecteDatas;
    public List<Channel> unSelecteDatas;
    public List<Channel> allChannels;

    public String firstChannelName;

    public NewChannelEvent(List<Channel> allChannels,String firstChannelName){

        if(allChannels == null) return;
        this.allChannels = allChannels;
        this.firstChannelName = firstChannelName;

        selecteDatas = new ArrayList<>();
        unSelecteDatas = new ArrayList<>();
        Iterator iterator = allChannels.iterator();
        while (iterator.hasNext()){
            Channel channel = (Channel) iterator.next();
            if(channel.getItemType() == Channel.TYPE_MY || channel.getItemType() == Channel.TYPE_OTHER){

                iterator.remove();
            }else if(channel.getItemType() == Channel.TYPE_MY_CHANNEL){
                selecteDatas.add(channel);
            }else{
                unSelecteDatas.add(channel);
            }
        }
    }

}
