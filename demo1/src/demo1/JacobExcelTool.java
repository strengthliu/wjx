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
		// ��
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
		// ����Excel��
		tool.callMacro("VBATest");
		// �رղ����棬�ͷŶ���
		tool.CloseExcel(true, true);

	}

	private static String[] ABC = { "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q",
			"R", "S", "T", "U", "V", "W", "X", "Y", "Z" };

	private ActiveXComponent xl = null; // Excel����

	private static Dispatch workbooks = null; // ����������

	private Dispatch workbook = null; // ���幤����

	private Dispatch sheet = null;

	private Dispatch sheets = null;// ���sheets���϶���

	private Dispatch currentSheet = null;// ��ǰsheet

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
	 * ת����Ԫ��λ�� ���֧��26*26��
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
	 * ��ȡֵ
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
	 * ��excel�ļ�
	 * 
	 * @param filepath
	 *            �ļ�·������
	 * @param visible
	 *            �Ƿ���ʾ��
	 * @param readonly
	 *            �Ƿ�ֻ����ʽ��
	 * 
	 */

	public void OpenExcel(String filepath, boolean visible, boolean readonly) {
		try {
			initComponents(); // ���ԭʼ����
			ComThread.InitSTA();// ������ͬʱ����һ���̣߳������߳���ס
			// ComThread.InitMTA(true);//��ͬʱ���ж��
			if (xl == null)
				xl = new ActiveXComponent("Excel.Application"); // Excel����
			xl.setProperty("Visible", new Variant(visible));// �����Ƿ���ʾ��excel
			if (workbooks == null)
				workbooks = xl.getProperty("Workbooks").toDispatch(); // ����������
			workbook = Dispatch.invoke( // �򿪾��幤����
					workbooks, "Open", Dispatch.Method,
					new Object[] { filepath, new Variant(false), new Variant(false) }, // �Ƿ���ֻ����ʽ��
					new int[1]).toDispatch();
		} catch (Exception e) {
			e.printStackTrace();
			releaseSource();
		}
	}

	/**
	 * 
	 * �ر�excel�ĵ�
	 * 
	 * @param f
	 *            ���岻�� ���ر��Ƿ񱣴棿Ĭ��false��
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
	 * �ͷ���Դ
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
	 * �����µĹ�����(sheet)�������Ӻ�ΪĬ��Ϊ��ǰ����Ĺ�������
	 * 
	 */
	public Dispatch addSheet() {
		return Dispatch.get(Dispatch.get(workbook, "sheets").toDispatch(), "add").toDispatch();
	}

	/**
	 * 
	 * �޸ĵ�ǰ������������
	 * 
	 * @param newName
	 * 
	 */
	public void modifyCurrentSheetName(String newName) {
		Dispatch.put(getCurrentSheet(), "name", newName);
	}

	/**
	 * 
	 * �õ���ǰ������������
	 * 
	 * @return
	 * 
	 */
	public String getCurrentSheetName() {
		return Dispatch.get(getCurrentSheet(), "name").toString();
	}

	/**
	 * 
	 * �õ�������������
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
	 * �õ�sheets�ļ��϶���
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
	 * �õ���ǰsheet
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
	 * ͨ�����������ֵõ�������
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
	 * ͨ�������������õ�������(��һ��������indexΪ1)
	 * 
	 * @param index
	 * @return sheet����
	 * 
	 */

	public Dispatch getSheetByIndex(Integer index) {
		return Dispatch.invoke(getSheets(), "Item", Dispatch.Get, new Object[] { index }, new int[1]).toDispatch();
	}

	/**
	 * 
	 * �õ�sheet������
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
	 * ����excel��
	 * 
	 * @param macroName
	 *            ����
	 * 
	 */
	public void callMacro(String macroName) {
		Dispatch.call(xl, "Run", new Variant(macroName));
	}

	/**
	 * 
	 * ����excel��
	 * 
	 * @param macroName
	 *            ����
	 * @param param
	 *            ���ݲ���
	 */
	public void callMacro(String macroName, Object param) {
		Dispatch.call(xl, "Run", new Variant(macroName), new Variant(param));
	}

	/**
	 * 
	 * ��Ԫ��д��ֵ
	 * 
	 * @param sheet
	 *            ��������sheet
	 * @param position
	 *            ��Ԫ��λ�ã��磺C1
	 * @param type
	 *            ֵ������ �磺value
	 * @param value
	 * 
	 */

	public void setValue(Dispatch sheet, String position, String type, Object value) {
		Dispatch cell = Dispatch.invoke(sheet, "Range", Dispatch.Get, new Object[] { position }, new int[1])
				.toDispatch();
		Dispatch.put(cell, type, value);
	}

	/**
	 * д��ֵ
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
	 * д������
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

		// ��ʼ��
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
			// new Variant(readonly) }, // �Ƿ���ֻ����ʽ��
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
	 * ��Ԫ���ȡֵ
	 * 
	 * @param position
	 *            ��Ԫ��λ�ã��磺 C1
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

	// ===========EXCEL �����ļ�Ϊ������ʽ =========
	/**
	 * 
	 * ����������Ϊ
	 * 
	 * @param filePath
	 *            ����Ϊ��·��
	 * 
	 */
	public void SaveAs(String filePath) {
		Dispatch.invoke(workbook, "SaveAs", Dispatch.Method, new Object[] { filePath, new Variant(44) }, new int[1]);
	}

	/**
	 * ����Ϊpdf
	 */
	public void excelToPDF() {
		Dispatch sheet = Dispatch.invoke(sheets, "Item", Dispatch.Get, new Object[] { new Integer(1) }, new int[1])
				.toDispatch();
		// String sheetname = Dispatch.get(sheet, "name").toString();
		Dispatch.call(sheet, "Activate");// ָ���sheet
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

	//EXCELתPDF
	public static String xlsToPdf(String inFilePath,String outFilePath){
		ComThread.InitSTA(true);
		ActiveXComponent ax=new ActiveXComponent("Excel.Application");
		try{
			ax.setProperty("Visible", new Variant(false));
			ax.setProperty("AutomationSecurity", new Variant(3)); //���ú�
			Dispatch excels=ax.getProperty("Workbooks").toDispatch();
	 
			Dispatch excel=Dispatch.invoke(excels,"Open",Dispatch.Method,new Object[]{
				inFilePath,
				new Variant(false),
				new Variant(false)
			},
			new int[9]).toDispatch();
			//ת����ʽ
			Dispatch.invoke(excel,"ExportAsFixedFormat",Dispatch.Method,new Object[]{
				new Variant(0), //PDF��ʽ=0
				outFilePath,
				new Variant(0)  //0=��׼ (���ɵ�PDFͼƬ�����ģ��) 1=��С�ļ� (���ɵ�PDFͼƬ����һ����Ϳ)
			},new int[1]);
	 
			//�������ʹ��SaveAs
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
	public static final int EXCEL_43 = 43; // Excel 2003 ���Կ���

	/**
	 * EXCELתHTML
	 * 
	 * @param xlsfile
	 *            EXCEL�ļ�ȫ·��
	 * @param htmlfile
	 *            ת����HTML���·��
	 */
	public static void excelToHtml(String xlsfile, String htmlfile) {
		// ��ʼ��
		ComThread.InitSTA();
		ActiveXComponent app = new ActiveXComponent("Excel.Application"); // ����Excel
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
	 * EXCELתXML
	 * 
	 * @param xlsfile
	 *            EXCEL�ļ�ȫ·��
	 * @param xmlfile
	 *            ת����XML���·��
	 */
	public static void excelToXml(String xlsfile, String xmlfile) {
		// ��ʼ��
		ComThread.InitSTA();
		ActiveXComponent app = new ActiveXComponent("Excel.Application"); // ����Excel
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