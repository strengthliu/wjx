package com.ccwihr_server.vo;

/**
 * @author qiang.liu
 *
 */
public class IndInfo extends VO {
	/**
	 * 位置ID。如：厂区为cq，是js的元素ID。
	 */
	String locationID;
	
	/**
	 * 位置名称。如：“厂区”
	 */
	String locationName;
	
	/**
	 * 人数量。
	 */
	int count;
	
	/**
	 * 当前走马灯文字。
	 */
	String mrqr;
}
