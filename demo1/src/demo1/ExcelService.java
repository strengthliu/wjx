package demo1;

import java.util.ArrayList;
import java.util.Collections;
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
 * 同时打开的文件过多，会导致内存不足；频繁打开文件，效率会低；需要有一个性能上的取舍。
 * 一个文件同时多人次使用的情况，
 * 有两个需要缓存维护的地方：一是取得一个com，生成一个ActiveXComponent;二是取得一个excel文件。
 * 一个Excel文件打开后，在资源允许的情况下，尽量不关闭，这样可以节约时间，提高效率。 需要维护的变量：
 * 一个Excel进程所控制的所有workbook实例。 是否workbooks是一个进程空间的？ 验证：
 * 两个JacobExcelTool实例，各打开一个Excel， 查询workbooks的workbook数，并访问其中的表， 看数据是否是同一个。测试过，不是。
 * 
 * 
 * ComThread.InitSTA();// 仅允许同时运行一个线程，其他线程锁住 ComThread.InitMTA(true);//
 * 可同时运行多个，可能有问题.
 * 
 * @author qiang.liu
 *
 */
public class ExcelService {

	/**
	 * Excel文件对象池
	 */
	CachePool pool = CachePool.getInstance();
	

	/**
	 * 单例模式
	 */
	private static ExcelService instance;
	private ExcelService() {
	}
	public synchronized static ExcelService getExcelServiceInstance() {
        if (instance == null) {  
            instance = new ExcelService();  
        }  
        return instance;  
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
		// TODO: 根据名字，找到持有这个excel表的工具。如果没有，就分配一个。
		ExcelFile file = CachePool.getInstance().getExcelFile(ExcelName);

		int mode = JacobExcelTool.EXCEL_HTML; // 转换模式
		// 调用工具，执行
		List<String> ret = file.report(params, mode);
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

}
