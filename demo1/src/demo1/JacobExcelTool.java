package demo1;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeoutException;

import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.ComThread;
import com.jacob.com.Dispatch;
import com.jacob.com.Variant;

public class JacobExcelTool  implements Runnable{

	private static String[] ABC = { "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q",
			"R", "S", "T", "U", "V", "W", "X", "Y", "Z" };

	/**
	 * 导出类型常量设置
	 */
	public static final int EXCEL_HTML = 44;
	public static final int EXCEL_XML = 46;
	public static final int EXCEL_43 = 43; // Excel 2003 测试可用
	public static final int excelToPdf = 57;
	public static final int WORD_HTML = 8;
	public static final int WORD_TXT = 7;


	/**
	 * EXCEL转HTML
	 * 
	 * @param xlsfile
	 *            EXCEL文件全路径
	 * @param htmlfile
	 *            转换后HTML存放路径
	 */
	public static void excelToHtml(String xlsfile, String htmlfile) {
		// 初始化
		ComThread.InitSTA();
		ActiveXComponent app = new ActiveXComponent("Excel.Application"); // 启动Excel
		try {
			app.setProperty("Visible", new Variant(false));
			Dispatch excels = app.getProperty("Workbooks").toDispatch();
			Dispatch excel = Dispatch.invoke(excels, "Open", Dispatch.Method,
					new Object[] { xlsfile, new Variant(false), new Variant(true) }, new int[1]).toDispatch();
			Dispatch.invoke(excel, "SaveAs", Dispatch.Method, new Object[] { htmlfile, new Variant(EXCEL_HTML) },
					new int[1]);
			Dispatch.call(excel, "Close", new Variant(false));
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			app.invoke("Quit", new Variant[] {});
			ComThread.Release();
		}
	}

	/**
	 * EXCEL转XML
	 * 
	 * @param xlsfile
	 *            EXCEL文件全路径
	 * @param xmlfile
	 *            转换后XML存放路径
	 */
	public static void excelToXml(String xlsfile, String xmlfile) {
		// 初始化
		ComThread.InitSTA();
		ActiveXComponent app = new ActiveXComponent("Excel.Application"); // 启动Excel
		try {
			app.setProperty("Visible", new Variant(false));
			Dispatch excels = app.getProperty("Workbooks").toDispatch();
			Dispatch excel = Dispatch.invoke(excels, "Open", Dispatch.Method,
					new Object[] { xlsfile, new Variant(false), new Variant(true) }, new int[1]).toDispatch();
			Dispatch.invoke(excel, "SaveAs", Dispatch.Method, new Object[] { xmlfile, new Variant(EXCEL_XML) },
					new int[1]);
			Variant f = new Variant(false);
			Dispatch.call(excel, "Close", f);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			app.invoke("Quit", new Variant[] {});
			ComThread.Release();
		}
	}

	public static long curt = 0;

	public static void time(String a) {
		if (curt != 0) {
			System.out.println("记录点" + a + ":" + (System.currentTimeMillis() - curt));
		}
		curt = System.currentTimeMillis();
	}

	public void run1() {
		ExecutorService executor = Executors.newCachedThreadPool();

		FutureTask<String> future = new FutureTask<String>(new Callable<String>() {
			// 要调用的任务
			public String call() throws Exception { // 抛出异常
				try {
					// 任务体
					// 休眠5秒，用于验证。
					Thread.sleep(5 * 1000);
					// 返回值。
					return "Hello Welcome!";
				} catch (Exception e) {
					throw new Exception("Callable terminated with Exception!"); // call方法可以抛出异常
				}
			}
		});
		executor.execute(future); // 开始异步执行。
		long t = System.currentTimeMillis();
		try {
 
			// String result = future.get(3000, TimeUnit.MILLISECONDS);
			// //取得结果，同时设置超时执行时间为5秒。
			String result = future.get(); // 取得结果，同时设置超时执行时间为5秒。
			System.err.println("result is " + result + ", time is " + (System.currentTimeMillis() - t));
		} catch (InterruptedException e) {
			future.cancel(true);
			System.err.println("Interrupte time is " + (System.currentTimeMillis() - t));
		} catch (ExecutionException e) {
			future.cancel(true);
			System.err.println("Throw Exception time is " + (System.currentTimeMillis() - t));
//		} catch (TimeoutException e) {
//			 future.cancel(true);
//			 System.err.println("Timeout time is " +
//			 (System.currentTimeMillis() - t));
		} finally {
			executor.shutdown();// 最终终止executor
		}
	}

	public String name;
	public String file;
	private Thread t;
	public void run() {
		OpenExcel(file, false, false);
	}
	   public void start () {
		      System.out.println("Starting " +  name );
		      if (t == null) {
		         t = new Thread (this, name);
		         t.start ();
		      }
		   }
	public static void main(String[] args) {

		JacobExcelTool.time("begin");
		// 耗时：2169
		JacobExcelTool t1 = new JacobExcelTool();
		t1.file="C:\\proj\\wjx-master\\demo1\\t1.xls";
		t1.name="t1";
		JacobExcelTool t2 = new JacobExcelTool();
		t2.file="C:\\proj\\wjx-master\\demo1\\t2.xls";
		t2.name="t2";
		t1.start();
		
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		t2.start();
		
		JacobExcelTool tool = new JacobExcelTool();
		// 打开
		JacobExcelTool.time("1");
		tool.OpenExcel("C:\\proj\\wjx-master\\demo1\\t1.xls", false, false);
//		tool.OpenExcel2("C:\\proj\\wjx-master\\demo1\\t2.xls", false, false);

		String position = tool.translateLocation(4, 3);
		time("9");
		// 耗时：1630
		tool.setValue(position, 8.00);
		time("10");
		// 耗时：474
		tool.toPDF("C:\\proj\\wjx-master\\demo1\\t1.pdf");
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
		tool.toPDF("C:\\proj\\wjx-master\\demo1\\t2.pdf");
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

	// EXCEL转PDF
	public static String xlsToPdf(String inFilePath, String outFilePath) {
		ComThread.InitSTA(true);
		ActiveXComponent ax = new ActiveXComponent("Excel.Application");
		try {
			ax.setProperty("Visible", new Variant(false));
			ax.setProperty("AutomationSecurity", new Variant(3)); // 禁用宏
			Dispatch excels = ax.getProperty("Workbooks").toDispatch();

			Dispatch excel = Dispatch
					.invoke(excels, "Open", Dispatch.Method,
							new Object[] { inFilePath, new Variant(false), new Variant(false) }, new int[9])
					.toDispatch();
			// 转换格式
			Dispatch.invoke(excel, "ExportAsFixedFormat", Dispatch.Method, new Object[] { new Variant(0), // PDF格式=0
					outFilePath, new Variant(0) // 0=标准 (生成的PDF图片不会变模糊) 1=最小文件
												// (生成的PDF图片糊的一塌糊涂)
			}, new int[1]);

			// 这里放弃使用SaveAs
			/*
			 * Dispatch.invoke(excel,"SaveAs",Dispatch.Method,new Object[]{
			 * outFile, new Variant(57), new Variant(false), new Variant(57),
			 * new Variant(57), new Variant(false), new Variant(true), new
			 * Variant(57), new Variant(true), new Variant(true), new
			 * Variant(true) },new int[1]);
			 */

			Dispatch.call(excel, "Close", new Variant(false));

			if (ax != null) {
				ax.invoke("Quit", new Variant[] {});
				ax = null;
			}
			ComThread.Release();
			return "";
		} catch (Exception es) {
			return es.toString();
		}
	}

	private ActiveXComponent xl = null; // Excel对象
	private ActiveXComponent x2 = null; // Excel对象

	/**
	 * 一个Excel进程所控制的所有workbook实例。 是否workbooks是一个进程空间的？ 验证：
	 * 两个JacobExcelTool实例，各打开一个Excel， 查询workbooks的workbook数，并访问其中的表， 看数据是否是同一个。
	 */
	private Dispatch workbooks = null; //
	private Dispatch workbooks2 = null; //

	/**
	 * 工作簿对象，每一个excel表一个对象。
	 */
	private Dispatch workbook = null; // 具体工作簿
	private Dispatch workbook2 = null; // 具体工作簿

	private Dispatch sheet = null;

	private Dispatch sheets = null;// 获得sheets集合对象

	private Dispatch currentSheet = null;// 当前sheet

	public JacobExcelTool() {
		initComponents(); // 清空原始变量
//		 ComThread.InitSTA();// 仅允许同时运行一个线程，其他线程锁住
		ComThread.InitMTA(true);// 可同时运行多个，可能有问题.

		if (xl == null)
			xl = new ActiveXComponent("Excel.Application"); // Excel对象
		if (x2 == null)
			x2 = new ActiveXComponent("Excel.Application"); // Excel对象

		xl.setProperty("Visible", new Variant(false));// 设置是否显示打开excel
		xl.setProperty("AutomationSecurity", new Variant(1)); // 设置宏运行权限（1-3）：3为不可用，1可用.
		x2.setProperty("Visible", new Variant(false));// 设置是否显示打开excel
		x2.setProperty("AutomationSecurity", new Variant(1)); // 设置宏运行权限（1-3）：3为不可用，1可用.

		if (workbooks == null)
			workbooks = xl.getProperty("Workbooks").toDispatch(); // 工作簿对象
		if (workbooks2 == null)
			workbooks2 = x2.getProperty("Workbooks").toDispatch(); // 工作簿对象
	}

	/**
	 * 
	 * 添加新的工作表(sheet)，（添加后为默认为当前激活的工作表）
	 * 
	 */
	public Dispatch addSheet() {
		return Dispatch.get(Dispatch.get(workbook, "sheets").toDispatch(), "add").toDispatch();
	}

	/**
	 * 
	 * 调用excel宏
	 * 
	 * @param macroName
	 *            宏名
	 * 
	 */
	public void callMacro(String macroName) {
		Dispatch.call(xl, "Run", new Variant(macroName));
	}

	/**
	 * 
	 * 调用excel宏
	 * 
	 * @param macroName
	 *            宏名
	 * @param param
	 *            传递参数
	 */
	public void callMacro(String macroName, Object param) {
		Dispatch.call(xl, "Run", new Variant(macroName), new Variant(param));
	}

	/**
	 * 
	 * 关闭excel文档
	 * 
	 * @param f
	 *            含义不明 （关闭是否保存？默认false）
	 */

	public void CloseExcel(boolean f, boolean quitXl) {
		try {
			Dispatch.call(workbook, "Save");
			Dispatch.call(workbook, "Close", new Variant(f));
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (quitXl) {
				releaseSource();
			}
		}
	}

	/**
	 * 另存为pdf
	 */
	public void excelToPDF() {
		Dispatch sheet = Dispatch.invoke(sheets, "Item", Dispatch.Get, new Object[] { new Integer(1) }, new int[1])
				.toDispatch();
		// String sheetname = Dispatch.get(sheet, "name").toString();
		Dispatch.call(sheet, "Activate");// 指定活动sheet
		Dispatch.call(sheet, "Select");

		//
		// Dispatch.invoke(excel, "SaveAs", Dispatch.Method,
		// new Object[] { outFile, new Variant(excelToPdf), new Variant(true),
		// new Variant(excelToPdf),
		// new Variant(excelToPdf), new Variant(true), new Variant(true), new
		// Variant(excelToPdf),
		// new Variant(true), new Variant(true), new Variant(true) },
		// new int[1]);
		// Dispatch.call(excel, "Close", new Variant(false));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * 销毁时，确保清除资源。
	 * 
	 * @see java.lang.Object#finalize()
	 */
	protected void finalize() throws Throwable {
		initComponents(); // 清空原始变量
		releaseSource();
		super.finalize();
		System.out.println(this.toString() + "now finalize:" + System.currentTimeMillis());
	}

	/**
	 * 获取所有表名
	 */
	public List findSheetName() {
		int num = this.getSheetCount();
		List list = new ArrayList();
		for (int i = 1; i <= num; i++) {
			currentSheet = this.getSheetByIndex(i);
			list.add(this.getCurrentSheetName(currentSheet));
		}
		return list;
	}

	/**
	 * 获取位置
	 * 
	 * @param rnum
	 *            最大行数
	 * @param cnum
	 *            最大列数
	 */
	private String getCellPosition(int rnum, int cnum) {
		String cposition = "";
		if (cnum > 26) {
			int multiple = (cnum) / 26;
			int remainder = (cnum) % 26;
			char mchar = (char) (multiple + 64);
			char rchar = (char) (remainder + 64);
			cposition = mchar + "" + rchar;
		} else {
			cposition = (char) (cnum + 64) + "";
		}
		cposition += rnum;
		return cposition;
	}

	/**
	 * 获取最大列数
	 * 
	 * @return
	 */
	private int getColumnCount() {
		currentSheet = this.getCurrentSheet();
		Dispatch UsedRange = Dispatch.get(currentSheet, "UsedRange").toDispatch();
		Dispatch Columns = Dispatch.get(UsedRange, "Columns").toDispatch();
		int num = Dispatch.get(Columns, "count").getInt();
		return num;
	}

	/**
	 * 
	 * 得到当前sheet
	 * 
	 * @return
	 * 
	 */
	public Dispatch getCurrentSheet() {
		currentSheet = Dispatch.get(workbook, "ActiveSheet").toDispatch();
		return currentSheet;
	}

	/**
	 * 
	 * 得到当前工作表的名字
	 * 
	 * @return
	 * 
	 */
	public String getCurrentSheetName() {
		return Dispatch.get(getCurrentSheet(), "name").toString();
	}

	/**
	 * 得到当前工作表的名字
	 * 
	 * @return
	 */
	private String getCurrentSheetName(Dispatch sheets) {
		return Dispatch.get(sheets, "name").toString();
	}

	/**
	 * 获取页脚信息
	 */
	private String getFooter() {
		currentSheet = this.getCurrentSheet();
		Dispatch PageSetup = Dispatch.get(currentSheet, "PageSetup").toDispatch();
		return Dispatch.get(PageSetup, "CenterFooter").toString();
	}

	/**
	 * 获取最大行数
	 * 
	 * @return
	 */
	private int getRowCount() {
		currentSheet = this.getCurrentSheet();
		Dispatch UsedRange = Dispatch.get(currentSheet, "UsedRange").toDispatch();
		Dispatch rows = Dispatch.get(UsedRange, "Rows").toDispatch();
		int num = Dispatch.get(rows, "count").getInt();
		return num;
	}

	/**
	 * 
	 * 通过工作表索引得到工作表(第一个工作簿index为1)
	 * 
	 * @param index
	 * @return sheet对象
	 * 
	 */

	public Dispatch getSheetByIndex(Integer index) {
		return Dispatch.invoke(getSheets(), "Item", Dispatch.Get, new Object[] { index }, new int[1]).toDispatch();
	}

	/**
	 * 通过工作表名字得到工作表
	 * 
	 * @param name
	 *            sheetName
	 * @return
	 */
	private Dispatch getSheetByName(String name) {
		return Dispatch.invoke(getSheets(), "Item", Dispatch.Get, new Object[] { name }, new int[1]).toDispatch();
	}

	/**
	 * 
	 * 得到sheet的总数
	 * 
	 * @return
	 * 
	 */

	public int getSheetCount() {
		int count = Dispatch.get(getSheets(), "count").toInt();
		return count;
	}
	
	public long getWorkBookCount() {
		long count = Dispatch.get(workbooks, "count").toInt();
		return count;
	}
//	public Dispatch getWorkBooks() {
//		if (sheets == null)
//			sheets = Dispatch.get(workbooks, "sheets").toDispatch();
//		return sheets;
//	}

	/**
	 * 
	 * 得到sheets的集合对象
	 * 
	 * @return
	 * 
	 */
	public Dispatch getSheets() {
		if (sheets == null)
			sheets = Dispatch.get(workbook, "sheets").toDispatch();
		return sheets;
	}

	/**
	 * 读取值
	 * 
	 * @param sheet
	 * @param position
	 * @return
	 */
	protected String getValue(Dispatch sheet, String position) {
		Dispatch cell = Dispatch.invoke(sheet, "Range", Dispatch.Get, new Object[] { position }, new int[1])
				.toDispatch();
		String value = Dispatch.get(cell, "Value").toString();
		return value;
	}

	/**
	 * 
	 * 单元格读取值
	 * 
	 * @param position
	 *            单元格位置，如： C1
	 * @param sheet
	 * @return
	 * 
	 */
	public Variant getValue(String position, Dispatch sheet) {
		Dispatch cell = Dispatch.invoke(sheet, "Range", Dispatch.Get, new Object[] { position }, new int[1])
				.toDispatch();
		Variant value = Dispatch.get(cell, "Value");
		return value;

	}

	/**
	 * 读取值
	 * 
	 * @param sheet
	 * @param position
	 * @return
	 */
	protected String GetValue(Dispatch sheet, String position) {
		Dispatch cell = Dispatch.invoke(sheet, "Range", Dispatch.Get, new Object[] { position }, new int[1])
				.toDispatch();
		String value = Dispatch.get(cell, "Value").toString();
		return value;
	}

	public Dispatch getWorkbook() {
		return workbook;
	}

	// /*
	// * 取消兼容性检查，在保存或者另存为时改检查会导致弹窗
	// */
	// private void setCheckCompatibility(){
	// Dispatch.put(wookbook, "CheckCompatibility", false);
	// }

	/**
	 * 
	 * 得到工作薄的名字
	 * 
	 * @return
	 * 
	 */
	public String getWorkbookName() {
		if (workbook == null)
			return null;
		return Dispatch.get(workbook, "name").toString();

	}

	public Dispatch getWorkbooks() {
		return workbooks;
	}

	public ActiveXComponent getXl() {
		return xl;
	}

	private void initComponents() {
		workbook = null;
		currentSheet = null;
		sheets = null;
	}

	/**
	 * 
	 * 修改当前工作表的名字
	 * 
	 * @param newName
	 * 
	 */
	public void modifyCurrentSheetName(String newName) {
		Dispatch.put(getCurrentSheet(), "name", newName);
	}

	/**
	 * 
	 * 打开excel文件
	 * 
	 * @param filepath
	 *            文件路径名称
	 * @param visible
	 *            是否显示打开
	 * @param readonly
	 *            是否只读方式打开
	 * 
	 */

	public void OpenExcel(String filepath, boolean visible, boolean readonly) {
		try {
			// 耗时：336
			workbook = Dispatch.invoke( // 打开具体工作簿
					workbooks, "Open", Dispatch.Method,
					new Object[] { filepath, new Variant(visible), new Variant(readonly) }, // 是否以只读方式打开
					new int[1]).toDispatch();
			time("2");
			Thread.sleep(2000);
			System.out.println("现在有工作薄数量："+getWorkBookCount());
			// // put data
			sheet = Dispatch.get(workbook, "ActiveSheet").toDispatch();
			String position = null;

			position = translateLocation(2, 3);
			String v = getValue(sheet, position);
			// System.out.println("openExcel: "+v);
			// Dispatch.get(workbook, "Names");
			Variant names = Dispatch.get(workbook, "Names");
			System.out.println(names.toString());
			System.out.println(getWorkbookName());
			List l = findSheetName();
			for (int i = 0; i < l.size(); i++)
				System.out.println(l.get(i));
		} catch (Exception e) {
			e.printStackTrace();
			releaseSource();
		}
	}
	public void OpenExcel2(String filepath, boolean visible, boolean readonly) {
		try {
			// 耗时：336
			workbook2 = Dispatch.invoke( // 打开具体工作簿
					workbooks2, "Open", Dispatch.Method,
					new Object[] { filepath, new Variant(visible), new Variant(readonly) }, // 是否以只读方式打开
					new int[1]).toDispatch();
			Thread.sleep(2000);
//			time("2");
			// // put data
//			sheet = Dispatch.get(workbook, "ActiveSheet").toDispatch();
//			String position = null;
//
//			position = translateLocation(2, 3);
//			String v = getValue(sheet, position);
//			// System.out.println("openExcel: "+v);
//			// Dispatch.get(workbook, "Names");
//			Variant names = Dispatch.get(workbook, "Names");
//			System.out.println(names.toString());
//			System.out.println(getWorkbookName());
//			List l = findSheetName();
//			for (int i = 0; i < l.size(); i++)
//				System.out.println(l.get(i));
		} catch (Exception e) {
			e.printStackTrace();
			releaseSource();
		}
	}

	/**
	 * 
	 * 打开excel文件
	 * 
	 * @param workbooks
	 *            工作薄
	 * @param filepath
	 *            文件路径名称
	 * @param visible
	 *            是否显示打开
	 * @param readonly
	 *            是否只读方式打开
	 * 
	 */

	public Dispatch OpenExcel(Dispatch workbooks, String filepath, boolean visible, boolean readonly)  throws Exception{
		try {
			// 耗时：336
			Dispatch workbook = Dispatch.invoke( // 打开具体工作簿
					workbooks, "Open", Dispatch.Method,
					new Object[] { filepath, new Variant(visible), new Variant(readonly) }, // 是否以只读方式打开
					new int[1]).toDispatch();
			return workbook;
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: 修改释放资源方法，下面这个没有参数，不能释放相应的workbooks。
			releaseSource();
			throw new Exception("打开Excel表文件出错。文件名："+filepath+".");
		}
//		return null;
	}

	/**
	 * 写入数据
	 * 
	 * @param cells
	 */
	public void putData(List<ExcelCell> cells) throws Exception {
		if (cells == null || cells.size() == 0) {
			return;
		}

		ActiveXComponent excel = null;
		Dispatch workbooks = null;
		Dispatch workbook = null;
		Dispatch sheet = null;
		String filename = null;

		// 初始化
		ComThread.InitSTA();

		// open file
		try {
			// filename = file;
			// excel = new ActiveXComponent("Excel.Application");
			// excel.setProperty("Visible", new Variant(false));
			// workbooks = excel.getProperty("Workbooks").toDispatch();
			// workbook = Dispatch.invoke(
			// workbooks,
			// "Open",
			// Dispatch.Method,
			// new Object[] { filename, new Variant(false),
			// new Variant(readonly) }, // 是否以只读方式打开
			// new int[1]).toDispatch();
			//
			// // put data
			// sheet = Dispatch.get(workbook, "ActiveSheet").toDispatch();
			String position = null;
			int row = 0;
			int col = 0;
			int max = 26 * 26 - 1;
			for (ExcelCell c : cells) {
				row = c.getRow();
				col = c.getCol();
				if (row < 0 || col < 0 || col > max || c.getValue() == null || c.getValue().trim().equals("")) {
					continue;
				}
				position = translateLocation(c.getRow(), c.getCol());
				setValue(sheet, position, c.getValue());
			}

		} finally {
			// close file
			try {
				Dispatch.call(workbook, "Save");
				Dispatch.call(workbook, "Close", new Variant(false));
			} finally {
				excel.invoke("Quit", new Variant[] {});
				ComThread.Release();
			}
		}
	}

	public void putData1(List<ExcelCell> cells) throws Exception {
		if (cells == null || cells.size() == 0) {
			return;
		}

		ActiveXComponent excel = null;
		Dispatch workbooks = null;
		Dispatch workbook = null;
		Dispatch sheet = null;
		String filename = null;

		// 初始化
		ComThread.InitSTA();

		// open file
		try {
			filename = "D:\\wjx\\demo1\\t1.xls";
			excel = new ActiveXComponent("Excel.Application");
			excel.setProperty("Visible", new Variant(false));
			workbooks = excel.getProperty("Workbooks").toDispatch();
			workbook = Dispatch.invoke(workbooks, "Open", Dispatch.Method,
					new Object[] { filename, new Variant(false), new Variant(false) }, // 是否以只读方式打开
					new int[1]).toDispatch();

			// put data
			sheet = Dispatch.get(workbook, "ActiveSheet").toDispatch();
			String position = null;
			int row = 0;
			int col = 0;
			int max = 26 * 26 - 1;
			for (ExcelCell c : cells) {
				row = c.getRow();
				col = c.getCol();
				if (row < 0 || col < 0 || col > max || c.getValue() == null || c.getValue().trim().equals("")) {
					continue;
				}
				position = translateLocation(c.getRow(), c.getCol());
				setValue(sheet, position, c.getValue());
			}
			position = translateLocation(3, 3);
			String v = getValue(sheet, position);
			System.out.println(v);

		} finally {
			// close file
			try {
				Dispatch.call(workbook, "Save");
				Dispatch.call(workbook, "Close", new Variant(false));
			} finally {
				excel.invoke("Quit", new Variant[] {});
				ComThread.Release();
			}
		}
	}

	/**
	 * 
	 * 释放资源
	 * 
	 */
	public void releaseSource() {
		if (xl != null) {
			xl.invoke("Quit", new Variant[] {});
			xl = null;
		}
		workbooks = null;
		ComThread.Release();
		System.gc();
	}

	// ===========EXCEL 另存文件为其他格式 =========
	/**
	 * 
	 * 工作簿另存为
	 * 
	 * @param filePath
	 *            另存为的路径
	 * 
	 */
	public void SaveAs(String filePath) {
		Dispatch.invoke(workbook, "SaveAs", Dispatch.Method, new Object[] { filePath, new Variant(44) }, new int[1]);
	}

	/**
	 * 设置页脚信息
	 */
	private void setFooter(String foot) {
		currentSheet = this.getCurrentSheet();
		Dispatch PageSetup = Dispatch.get(currentSheet, "PageSetup").toDispatch();
		Dispatch.put(PageSetup, "CenterFooter", foot);
	}

	/*
	 * 为每个表设置打印区域
	 */
	private void setPrintArea() {
		int count = Dispatch.get(sheets, "count").changeType(Variant.VariantInt).getInt();
		for (int i = count; i >= 1; i--) {
			sheet = Dispatch.invoke(sheets, "Item", Dispatch.Get, new Object[] { i }, new int[1]).toDispatch();
			Dispatch page = Dispatch.get(sheet, "PageSetup").toDispatch();
			Dispatch.put(page, "PrintArea", false);
			Dispatch.put(page, "Orientation", 2);
			Dispatch.put(page, "Zoom", false); // 值为100或false
			Dispatch.put(page, "FitToPagesTall", false); // 所有行为一页
			Dispatch.put(page, "FitToPagesWide", 1); // 所有列为一页(1或false)
		}
	}

	/**
	 * 写入值
	 * 
	 * @param sheet
	 * @param position
	 * @param value
	 */
	protected void setValue(Dispatch sheet, String position, String value) {
		Dispatch cell = Dispatch.invoke(sheet, "Range", Dispatch.Get, new Object[] { position }, new int[1])
				.toDispatch();
		Dispatch.put(cell, "Value", value);
	}

	/**
	 * 
	 * 单元格写入值
	 * 
	 * @param sheet
	 *            被操作的sheet
	 * @param position
	 *            单元格位置，如：C1
	 * @param type
	 *            值的属性 如：value
	 * @param value
	 * 
	 */

	public void setValue(Dispatch sheet, String position, String type, Object value) {
		Dispatch cell = Dispatch.invoke(sheet, "Range", Dispatch.Get, new Object[] { position }, new int[1])
				.toDispatch();
		Dispatch.put(cell, type, value);
	}

	void setValue(String position, Object d) {
		// TODO Auto-generated method stub
		// put data
		sheet = Dispatch.get(workbook, "ActiveSheet").toDispatch();
		String position1 = translateLocation(3, 3);
		String v = getValue(sheet, position1);
		System.out.println("setValue(" + d.toString() + ")前：" + v);

		setValue(sheet, position, d.toString());

		position = translateLocation(3, 3);
		v = getValue(sheet, position1);
		System.out.println("setValue(" + d.toString() + ")后：" + v);
	}

	void toPDF(String string) {
		// TODO Auto-generated method stub
		// 转换格式
		Dispatch.invoke(sheet, "ExportAsFixedFormat", Dispatch.Method, new Object[] { new Variant(0), // PDF格式=0
				string, new Variant(0) // 0=标准 (生成的PDF图片不会变模糊)
										// 1=最小文件(生成的PDF图片糊的一塌糊涂)
		}, new int[1]);

		// 这里放弃使用SaveAs
		// Dispatch.invoke(sheet,"SaveAs",Dispatch.Method,new Object[]{
		// string,
		// new Variant(57),
		// new Variant(false),
		// new Variant(57),
		// new Variant(57),
		// new Variant(false),
		// new Variant(true),
		// new Variant(57),
		// new Variant(true),
		// new Variant(true),
		// new Variant(true)
		// },new int[1]);

		// Dispatch.call(sheet, "Close",new Variant(false));

	}

	/**
	 * 转换单元格位置 最多支持26*26列
	 * 
	 * @param i
	 * @param j
	 * @return
	 */
	public String translateLocation(int i, int j) {
		String loc = "";
		if (i <= 26) {
			loc = ABC[i - 1] + (j);
		} else {
			loc = ABC[i / 26 - 1] + ABC[i % 26 - 1] + (j);
		}

		return loc;
	}

	/**
	 * 根据参数，执行Excel表宏，再将所有sheet另存为mode所标示的文件格式。
	 * @param params
	 * @param mode 
	 * @return
	 */
	public List<String> report(Object[] params, int mode) {
		// TODO Auto-generated method stub
		return null;
	}

}