package com.ccwihr_server.controller;
//CheckInInfoController

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ccwihr_server.data.Store;
import com.ccwihr_server.mapper.ClocksMapper;
import com.google.gson.Gson;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/get")
public class CheckInInfoController {

	@Autowired
	private ClocksMapper clock;

    public CheckInInfoController(){
    	super();
    	System.out.println("init in CheckInInfoController.");
    }
	
	
	/**
	 * 
	 * @param request
	 * @return
	 */
    @RequestMapping(value="/checkInfo",method = RequestMethod.GET)
    public @ResponseBody JSONObject checkInfo(HttpServletRequest request) {
    	JSONObject jsonobject = new JSONObject();
    	JSONArray jarray = new JSONArray();
    	JSONObject data = new JSONObject();
    	data.put("id", "d8");
    	data.put("name", "jkaldsf");
    	data.put("count", 103);
    	data.put("marqueeText", "fdsafdsafdsfdsafdsafdsa");
    	jarray.add(data);
    	jsonobject.put("data", jarray);
//    	if(!tUserMapper.equals(null)) System.out.println("fdsa");
        return jsonobject;
    }

    /***
     * 手动强制刷新Store。
     * @return
     */
    @RequestMapping(value="/updateStore",method = RequestMethod.GET)
    public @ResponseBody JSONObject  updateStore() {
		if(clock == null)
			System.out.println("clock is null");
		else 
			System.out.println("clock is not null");
    	
    	System.out.println("updateStore");
    	JSONObject jsonobject = new JSONObject();
    	jsonobject.put("id", "d8");
    	Store.rebuild();
        return jsonobject;
    	
    }
    
}
