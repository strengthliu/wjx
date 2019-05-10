package demo1;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.ComThread;
import com.jacob.com.Dispatch;
import com.jacob.com.Variant;

/**
 * 缓存池
 * 
 * @author Administrator
 */
/**
 * @author qiang.liu
 *
 */
public class CachePool {
	// TODO: 自动维护过期的条目，避免内存过大。

	// 单例模式
	private static CachePool instance;

	// 缓存Map
	private static Map<String, Object> cacheItems;

	/**
	 * 初始ActiveX对象数。
	 */
	private static final int CACHE_INSTANCE_COUNT = 10;

	/**
	 * 每个ActiveX对象控制的workbook数。
	 */
	private static final int CACHE_WORKBOOK_COUNT = 50;

	/**
	 * 默认缓存超时时间。为48小时。
	 */
	private static final int DEFAULT_EXPIRES = 48 * 60 * 60 * 1000;
	private static final int CACHE_COUNT_SAME_FILE = 10;

	/**
	 * 获取唯一的实例
	 * 
	 * @return instance
	 */
	public synchronized static CachePool getInstance() {
		if (instance == null) {
			instance = new CachePool();
		}
		return instance;
	}
	/**
	 * Com对象的池。 JAVA退出时，不会释放COM资源，这里需要在退出时释放。 初始化为10个实例对象。
	 */
	private Hashtable<ActiveXObject, Integer> xls = new Hashtable<ActiveXObject, Integer>(CACHE_INSTANCE_COUNT);

	private CachePool() {
		cacheItems = new HashMap<String, Object>();
		// initComponents(); // 清空原始变量
		// ComThread.InitSTA();// 仅允许同时运行一个线程，其他线程锁住
		ComThread.InitMTA(true);// 可同时运行多个，可能有问题.

		for (int i = 0; i < CACHE_INSTANCE_COUNT; i++) {
			ActiveXComponent xl = new ActiveXComponent("Excel.Application"); // Excel对象
			xl.setProperty("Visible", new Variant(false));// 设置是否显示打开excel
			xl.setProperty("AutomationSecurity", new Variant(1)); // 设置宏运行权限（1-3）：3为不可用，1可用.
			Dispatch workbooks = xl.getProperty("Workbooks").toDispatch(); // 工作簿对象
			ActiveXObject axo = new ActiveXObject(xl, workbooks);
			xls.put(axo, 0);
		}
	}

	/**
	 * 清除所有的Item缓存
	 */
	public synchronized void clearAllItems() {
		cacheItems.clear();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * 销毁时，确保清除资源。
	 * 
	 * @see java.lang.Object#finalize()
	 */
	protected void finalize() throws Throwable {
		// initComponents(); // 清空原始变量
		releaseSource();
		super.finalize();
		System.out.println(this.toString() + "now finalize:" + System.currentTimeMillis());
	}

	private ExcelFile getAWorkBooks(String name) {
		ExcelFile file = (ExcelFile) getCacheItem(name);
		if (file == null) {
			// 找一个workbooks
			// 找到xls里最小的那个
			ArrayList<Integer> v = new ArrayList<Integer>(xls.values());
			Collections.sort(v);

			// 创建一个ExcelFile
			// 写入池中
			// 返回
			return file;
		} else {
			return file;
		}
	}

	/**
	 * 
	 * 获取缓存实例
	 * 
	 * @param name
	 *            缓存名称
	 * @return 缓存实例
	 */
	public synchronized Object getCacheItem(String name) {
		if (cacheItems.containsKey(name)) {
			CacheAble cacheItem = (CacheAble) cacheItems.get(name);
			if (!cacheItem.isExpired()) {
				return cacheItem.getEntity();
			}
		}
		return null;
	}

	// /**
	// *
	// * 获取缓存实例
	// *
	// * @param name
	// * 缓存名称
	// * @return 缓存实例
	// */
	// public synchronized Object getAvailableCacheItem(String name) {
	// for (int cou = 0; cou < CACHE_COUNT_SAME_FILE; cou++) {
	// String realName = name + Integer.valueOf(cou).toString();
	// if (cacheItems.containsKey(name)) {
	// CacheAble cacheItem = (CacheAble) cacheItems.get(name);
	// if (!cacheItem.isExpired() && !((ExcelFile) cacheItem).isBusy()) {
	// ExcelFile ret = (ExcelFile)cacheItem;
	// return cacheItem.getEntity();
	// }
	// }
	// }
	// return null;
	// }

	/**
	 * 根据名字，获取一个Excel表的实例。
	 * 
	 * @param excelName
	 * @return
	 * 
	 */
	public ExcelFile getExcelFile(String excelName) {
		Object obj = this.getCacheItem(excelName);
		if (obj == null) {
			// 什么实例都没有，就新建一个。
			ExcelFile ret = newExcelFile(excelName);
			ret.lock();
			return ret; // 需要同步
		} else {
			if (obj instanceof ExcelFile) {
				ExcelFile f = (ExcelFile) obj;
				if (f.isBusy()) { // f一般是第一个加入到池中的文件
					ExcelFile ret = null;
					List<ActiveXObject> allworkbooks = f.getAllWorkbooks();
					ActiveXObject axo = this.getSuitActiveXObject(allworkbooks); // 需要同步
					try {
						ret = (ExcelFile) f.clone();
						ret.ls.add(ret);
						ret.num = ret.ls.size();
					} catch (CloneNotSupportedException e) {
						// 不能克隆，就新建一个。
						e.printStackTrace();
						ret = new ExcelFile();
						ret.name = f.name;
						ret.ls = f.ls;
						ret.ls.add(ret);
						ret.num = ret.ls.size();
					}
					ret.axo = axo;
					
					return ret;
				} else
					return f;
			}
		}
		return null;
	}

	/**
	 * 获取缓存数据的数量
	 * 
	 * @return
	 */
	public int getSize() {
		return cacheItems.size();
	}

	private ActiveXObject getSuitActiveXObject() {
		return getSuitActiveXObject(null);
	}

	private synchronized ActiveXObject getSuitActiveXObject(List<ActiveXObject> allworkbooks) {
		ActiveXObject ret = null;
		int count = CACHE_WORKBOOK_COUNT;
		Enumeration e = xls.keys();
		while (e.hasMoreElements()) {
			ActiveXObject axo = (ActiveXObject) e.nextElement();
			if (allworkbooks == null || !allworkbooks.contains(axo)) {
				int cou = xls.get(axo);
				if (count > cou) {
					count = cou;
					ret = axo;
				}
			}
		}
		return ret;
	}

	private synchronized ExcelFile newExcelFile(String excelName) {
		ActiveXObject axo = this.getSuitActiveXObject();
		ExcelFile ret = new ExcelFile();
		ret.name = excelName;
		ret.axo = axo;
		ret.ls.add(ret);
		ret.num = ret.ls.size();
		// 将这个实例加入到缓存中。
		this.putCacheItem(excelName, ret, DEFAULT_EXPIRES);
		return (ExcelFile) this.getCacheItem(excelName);
	}

	/**
	 * 存放缓存信息
	 * 
	 * @param name
	 *            名称
	 * @param obj
	 *            实例对象
	 * @param expires
	 *            超时时长
	 */
	public synchronized void putCacheItem(String name, Object obj, long expires) {
		// 判断该对象是否在在缓存池，不在直接put
		if (!cacheItems.containsKey(name)) {
			cacheItems.put(name, new CacheItem(obj, expires));
		}

		// 获取缓存池中对象，更新对象信息
		CacheAble cacheItem = (CacheAble) cacheItems.get(name);
		cacheItem.setCreateTime(new Date());
		cacheItem.setEntity(obj);
		cacheItem.setExpireTime(expires);
	}

	/**
	 * 
	 * 释放资源
	 * 
	 */
	public void releaseSource() {
		clearAllItems();
		Enumeration<ActiveXObject> xlsk = xls.keys();
		while (xlsk.hasMoreElements()) {
			ActiveXObject xl = xlsk.nextElement();
			ActiveXComponent axc = xl.getXl();
			axc.invoke("Quit", new Variant[] {});
			// xl.getWorkbooks() = null;
			xl = null;
		}
		ComThread.Release();
		System.gc();
	}

	/**
	 * 移除缓存数据
	 * 
	 * @param name
	 */
	public synchronized void removeCacheItem(String name) {
		if (!cacheItems.containsKey(name)) {
			return;
		}

		cacheItems.remove(name);
	}

}