package demo1;

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
 * 有两个需要缓存维护的地方：一是取得一个com，生成一个ActiveXComponent;二是取得一个excel文件。
 * 一个Excel文件打开后，在资源允许的情况下，尽量不关闭，这样可以节约时间，提高效率。 需要维护的变量：
 * 一个Excel进程所控制的所有workbook实例。 是否workbooks是一个进程空间的？ 验证：
 * 两个JacobExcelTool实例，各打开一个Excel， 查询workbooks的workbook数，并访问其中的表， 看数据是否是同一个。
 * 
 * ComThread.InitSTA();// 仅允许同时运行一个线程，其他线程锁住 ComThread.InitMTA(true);//
 * 可同时运行多个，可能有问题.
 * 
 * @author qiang.liu
 *
 */
public class ExcelService {

	/**
	 * Com对象的池。 JAVA退出时，不会释放COM资源，这里需要在退出时释放。
	 */
	Hashtable<String, JacobExcelTool> pool = new Hashtable<String, JacobExcelTool>();

	private ActiveXComponent xl = null; // Excel对象

	/**
	 * 初始对象数。
	 */
	private static final int CACHE_INSTANCE_COUNT = 10;
	/**
	 * Excel对象池，初始化为10个实例对象。
	 */
	private Hashtable<String, ActiveXComponent> xls = new Hashtable<String, ActiveXComponent>(CACHE_INSTANCE_COUNT);

	/**
	 * workbooks对象池。
	 */
	private Hashtable<String, Dispatch> workbooks_s = new Hashtable<String, Dispatch>(CACHE_INSTANCE_COUNT);

	/**
	 * 
	 * 释放资源
	 * 
	 */
	public void releaseSource() {
		Enumeration<String> xlsk = xls.keys();
		while (xlsk.hasMoreElements()) {
			ActiveXComponent xl = xls.get(xlsk.nextElement());
			xl.invoke("Quit", new Variant[] {});
			xl = null;
		}
		Enumeration<String> wbsk = workbooks_s.keys();
		while (wbsk.hasMoreElements()) {
			Dispatch wb = xls.get(wbsk.nextElement());
			wb = null;
		}
		workbooks_s = null;
		ComThread.Release();
		System.gc();
	}

	public ExcelService() {
		// TODO Auto-generated constructor stub
		// initComponents(); // 清空原始变量
		// ComThread.InitSTA();// 仅允许同时运行一个线程，其他线程锁住
		ComThread.InitMTA(true);// 可同时运行多个，可能有问题.

		if (xl == null)
			xl = new ActiveXComponent("Excel.Application"); // Excel对象

		xl.setProperty("Visible", new Variant(false));// 设置是否显示打开excel
		xl.setProperty("AutomationSecurity", new Variant(1)); // 设置宏运行权限（1-3）：3为不可用，1可用.
		Dispatch workbooks = null;
		if (workbooks == null)
			workbooks = xl.getProperty("Workbooks").toDispatch(); // 工作簿对象
	}

	/**
	 * 用指定参数获取指定名称的Excel表路径，参数用于执行宏和计算等操作。
	 * 
	 * @param ExcelName
	 * @param parms[]
	 * @return
	 * @author 强
	 * @since 2019.05.05
	 */
	public List<String> getExcel(String ExcelName, Object[] params) {
		JacobExcelTool excel;
		// TODO: 根据名字，找到持有这个excel表的工具。如果没有，就分配一个。

		excel = new JacobExcelTool();// 测试用

		int mode = JacobExcelTool.EXCEL_HTML; // 转换模式
		// 调用工具，执行
		List<String> ret = excel.report(params, mode);
		return null;
	}

	public static void main(String[] args) {

		JacobExcelTool.time("begin");
		// 耗时：2169
		JacobExcelTool tool = new JacobExcelTool();
		// 打开
		JacobExcelTool.time("1");
		tool.OpenExcel("D:\\Work\\项目\\wjx\\demo1\\t1.xls", false, false);

		String position = tool.translateLocation(4, 3);
		time("9");
		// 耗时：1630
		tool.setValue(position, 8.00);
		time("10");
		// 耗时：474
		tool.toPDF("D:\\Work\\项目\\wjx\\demo1\\t1.pdf");
		time("11");

		// tool.getSheetByName("Sheet1");
		// Object a1 = Dispatch.invoke(workbooks, "Range", Dispatch.Get,
		// new Object[] {"A1"},
		// new int[1]).toDispatch();
		//// Dispatch.put((Dispatch) a1, "Value", "12.00");
		// tool.setValue(sheet, position, "5");
		// Dispatch.put(a2, "Formula", "=A1*2");

		// Dispatch sheet = Dispatch.get(workbooks, "ActiveSheet").toDispatch();
		//
		// tool.setValue(sheet, "D3", "Value", 4);
		// 调用Excel宏
		// 耗时1662
		tool.callMacro("Auto_Open");
		time("12");
		// 耗时：337
		tool.toPDF("D:\\Work\\项目\\wjx\\demo1\\t2.pdf");
		time("13");

		// try {
		// Thread.sleep(5000);
		// } catch (InterruptedException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		// 关闭并保存，释放对象
		// 耗时158
		tool.setValue(position, 9.00);

		time("14");
		// 耗时：440
		tool.CloseExcel(true, true);
		time("15");

	}

	public static long curt = 0;

	public static void time(String a) {
		if (curt != 0) {
			System.out.println("记录点" + a + ":" + (System.currentTimeMillis() - curt));
		}
		curt = System.currentTimeMillis();
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

}
