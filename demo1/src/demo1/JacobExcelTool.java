package demo1;

import java.util.List;

import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.ComThread;
import com.jacob.com.Dispatch;
import com.jacob.com.Variant;

public class JacobExcelTool {

	public static void main(String[] args) {

		// TODO Auto-generated method stub
		JacobExcelTool tool = new JacobExcelTool();
		// 打开
		tool.OpenExcel("D:\\wjx\\demo1\\t1.xls", false, false);

		// Dispatch sheet = Dispatch.get(workbook, "Sheet1").toDispatch();
		String position = tool.translateLocation(2, 3);
		// tool.getSheetByName("Sheet1");
		// Object a1 = Dispatch.invoke(workbooks, "Range", Dispatch.Get,
		// new Object[] {"A1"},
		// new int[1]).toDispatch();
		//// Dispatch.put((Dispatch) a1, "Value", "12.00");
		// tool.setValue(sheet, position, "5");
		// Dispatch.put(a2, "Formula", "=A1*2");
		Dispatch sheet = Dispatch.get(workbooks, "ActiveSheet").toDispatch();

		tool.setValue(sheet, "D3", "Value", 4);
		// 调用Excel宏
		tool.callMacro("VBATest");
		// 关闭并保存，释放对象
		tool.CloseExcel(true, true);

	}

	private static String[] ABC = { "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q",
			"R", "S", "T", "U", "V", "W", "X", "Y", "Z" };

	private ActiveXComponent xl = null; // Excel对象

	private static Dispatch workbooks = null; // 工作簿对象

	private Dispatch workbook = null; // 具体工作簿

	private Dispatch sheet = null;

	private Dispatch sheets = null;// 获得sheets集合对象

	private Dispatch currentSheet = null;// 当前sheet

	public ActiveXComponent getXl() {
		return xl;
	}

	public Dispatch getWorkbooks() {
		return workbooks;
	}

	public Dispatch getWorkbook() {
		return workbook;
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
			initComponents(); // 清空原始变量
			ComThread.InitSTA();// 仅允许同时运行一个线程，其他线程锁住
			// ComThread.InitMTA(true);//可同时运行多个
			if (xl == null)
				xl = new ActiveXComponent("Excel.Application"); // Excel对象
			xl.setProperty("Visible", new Variant(visible));// 设置是否显示打开excel
			if (workbooks == null)
				workbooks = xl.getProperty("Workbooks").toDispatch(); // 工作簿对象
			workbook = Dispatch.invoke( // 打开具体工作簿
					workbooks, "Open", Dispatch.Method,
					new Object[] { filepath, new Variant(false), new Variant(false) }, // 是否以只读方式打开
					new int[1]).toDispatch();
		} catch (Exception e) {
			e.printStackTrace();
			releaseSource();
		}
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
	 * 得到当前工作表的名字
	 * 
	 * @return
	 * 
	 */
	public String getCurrentSheetName() {
		return Dispatch.get(getCurrentSheet(), "name").toString();
	}

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
	 * 通过工作表名字得到工作表
	 * 
	 * @param name
	 *            sheetName
	 * @return
	 * 
	 */
	public Dispatch getSheetByName(String name) {
		return Dispatch.invoke(getSheets(), "Item", Dispatch.Get, new Object[] { name }, new int[1]).toDispatch();
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

	private void initComponents() {
		workbook = null;
		currentSheet = null;
		sheets = null;
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
	 * 另存为pdf
	 */
	public void excelToPDF() {
		Dispatch sheet = Dispatch.invoke(sheets, "Item", Dispatch.Get, new Object[] { new Integer(1) }, new int[1])
				.toDispatch();
		// String sheetname = Dispatch.get(sheet, "name").toString();
		Dispatch.call(sheet, "Activate");// 指定活动sheet
		Dispatch.call(sheet, "Select");
		int excelToPdf = 57;
		//
		Dispatch.invoke(excel, "SaveAs", Dispatch.Method,
				new Object[] { outFile, new Variant(excelToPdf), new Variant(true), new Variant(excelToPdf),
						new Variant(excelToPdf), new Variant(true), new Variant(true), new Variant(excelToPdf),
						new Variant(true), new Variant(true), new Variant(true) },
				new int[1]);
		Dispatch.call(excel, "Close", new Variant(false));
	}

	//EXCEL转PDF
	public static String xlsToPdf(String inFilePath,String outFilePath){
		ComThread.InitSTA(true);
		ActiveXComponent ax=new ActiveXComponent("Excel.Application");
		try{
			ax.setProperty("Visible", new Variant(false));
			ax.setProperty("AutomationSecurity", new Variant(3)); //禁用宏
			Dispatch excels=ax.getProperty("Workbooks").toDispatch();
	 
			Dispatch excel=Dispatch.invoke(excels,"Open",Dispatch.Method,new Object[]{
				inFilePath,
				new Variant(false),
				new Variant(false)
			},
			new int[9]).toDispatch();
			//转换格式
			Dispatch.invoke(excel,"ExportAsFixedFormat",Dispatch.Method,new Object[]{
				new Variant(0), //PDF格式=0
				outFilePath,
				new Variant(0)  //0=标准 (生成的PDF图片不会变模糊) 1=最小文件 (生成的PDF图片糊的一塌糊涂)
			},new int[1]);
	 
			//这里放弃使用SaveAs
			/*Dispatch.invoke(excel,"SaveAs",Dispatch.Method,new Object[]{
				outFile,
				new Variant(57),
				new Variant(false),
				new Variant(57), 
				new Variant(57),
				new Variant(false), 
				new Variant(true),
				new Variant(57), 
				new Variant(true),
				new Variant(true), 
				new Variant(true)
			},new int[1]);*/
	 
			Dispatch.call(excel, "Close",new Variant(false));
	 
			if(ax!=null){
				ax.invoke("Quit",new Variant[]{});
				ax=null;
			}
			ComThread.Release();
			return "";
		}catch(Exception es){
			return es.toString();
		}
	}


	// public static final int WORD_HTML = 8;
	// public static final int WORD_TXT = 7;
	public static final int EXCEL_HTML = 44;
	public static final int EXCEL_XML = 46;
	public static final int EXCEL_43 = 43; // Excel 2003 测试可用

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

}