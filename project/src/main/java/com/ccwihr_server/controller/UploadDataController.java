package com.ccwihr_server.controller;
//UploadDataController

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;

//import io.swagger.annotations.ApiOperation;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/update")
public class UploadDataController {

	/**
	 * 上传刷卡记录数据
	 * @param status
	 * @param request
	 * @return
	 */
//	@ApiOperation(value="查询",notes="获取列表",tags={"get video list 2"})
    @RequestMapping(value="/timerecordsdata", produces = "text/html; charset=utf-8")
    public @ResponseBody String updateTimeRecordsData(List status,HttpServletRequest request) {
//    	request.getParameter(arg0)
		System.out.println(status);
        return String.valueOf(1);
    }
    
    /**
     * 上传刷卡机数据
     * @param status
     * @param request
     * @return
     */
//	@ApiOperation(value="查询",notes="获取列表",tags={"get video list 2"})
    @RequestMapping(value="/clocksdata", produces = "text/html; charset=utf-8")
    public @ResponseBody String updateClocksData(List status,HttpServletRequest request) {
//    	request.getParameter(arg0)
		System.out.println(status);
        return String.valueOf(1);
    }
    
    /**
     * 上传地点数据
     * @param status
     * @param request
     * @return
     */
//	@ApiOperation(value="查询",notes="获取列表",tags={"get video list 2"})
    @RequestMapping(value="/areadata", produces = "text/html; charset=utf-8")
    public @ResponseBody String updateAreaData(List status,HttpServletRequest request) {
//    	request.getParameter(arg0)
		System.out.println(status);
        return String.valueOf(1);
    }
    
    /**
     * 上传人员数据
     * @param status
     * @param request
     * @return
     */
//	@ApiOperation(value="查询",notes="获取列表",tags={"get video list 2"})
    @RequestMapping(value="/employeedata", produces = "text/html; charset=utf-8")
    public @ResponseBody String updateEmployeeData(List status,HttpServletRequest request) {
//    	request.getParameter(arg0)
		System.out.println(status);
        return String.valueOf(1);
    }
    
    /**
     * 上传部门数据
     * @param status
     * @param request
     * @return
     */
//	@ApiOperation(value="查询",notes="获取列表",tags={"get video list 2"})
    @RequestMapping(value="/departmentdata", produces = "text/html; charset=utf-8")
    public @ResponseBody String updateDepartmentData(List status,HttpServletRequest request) {
//    	request.getParameter(arg0)
		System.out.println(status);
        return String.valueOf(1);
    }
    
    public UploadDataController(){
    	super();
    	System.out.println("UploadDataController init ...");
    }

}
