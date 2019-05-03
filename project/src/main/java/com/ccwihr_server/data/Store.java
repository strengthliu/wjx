package com.ccwihr_server.data;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ccwihr_server.mapper.ClocksMapper;

public class Store {

	private static Store store = new Store();
	
	/***
	 * 各部门信息
	 */
	private HashMap<String,InfoItem> infos;
	
	/***
	 * 总厂信息
	 */
	private InfoItem info;
	
	
	
//	{
//		System.out.println("init Store.");
//		if(store == null) store = new Store();
//	}
	
	private Store(){
		
	}
	
	/***
	 * 重建今天的数据。
	 * @return
	 */
	public static synchronized boolean rebuild(){
		return true;
	}
	
	public static synchronized boolean add(){
		return true;
	}
	
	/***
	 * 以每天为一个周期。把这段时间的变化，以json形式返回前端。
	 * @return
	 */
	public static synchronized JSONObject toJson(){
		JSONObject j = new JSONObject();
    	JSONObject jsonobject = new JSONObject();
    	JSONArray jarray = new JSONArray();
    	JSONObject data = new JSONObject();
		
//    	if(store.info.)
		return j;
	}
}
