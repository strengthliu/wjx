package com.ccwihr_server.data;

import java.util.List;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class InfoItem {
	/**
	 * 滚动通知条数
	 */
	private static final int marqsize = 5;

	private String id;
	private String name;
	private int count;

	/**
	 * 上次toJson到现在，变化了多少次
	 */
	private int ch = 0;

	private String marqueeText;
	List<String> marqueeTexts;

	private void buildMarqueeText() {
		// marqueeTexts.remove(index)
		
	}

	/***
	 * 根据
	 * 
	 * @param i
	 *            变化数，考勤入门为1、考勤出门为-1、食堂消费为0；
	 * @param changeInfo
	 */
	public synchronized void change(int i, String changeInfo) {
		ch++; // 有新信息了，下次一定要列新。
		count += i;
		marqueeTexts.add(changeInfo);
	}

	
	public synchronized JSONObject toJson() {
		if (ch > 0) {
			buildMarqueeText();
			JSONObject j = new JSONObject();
//			JSONObject jsonobject = new JSONObject();
//			JSONArray jarray = new JSONArray();
//			JSONObject data = new JSONObject();

			j.put("id", id);
			j.put("id", id);
			j.put("id", id);
			j.put("id", id);
			// if(store.info.)
			ch = 0;
			return j;
		} else
			return null;
	}
}
