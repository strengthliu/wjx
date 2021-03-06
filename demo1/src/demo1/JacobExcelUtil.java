package demo1;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
 
import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.ComThread;
import com.jacob.com.Dispatch;
import com.jacob.com.Variant;
 
/**
 * Jacob excel Util
 *
 */
public class JacobExcelUtil {
	
	public static void main(String[] args) {
		try {
			JacobExcelUtil util = new JacobExcelUtil("D:\\Work\\项目\\wjx\\demo1\\t1.xls");
			ExcelCell cell = new ExcelCell();
			cell.setRow(4);
			cell.setCol(3);
			cell.setValue("8");
			List<ExcelCell> ls = new ArrayList<ExcelCell>();
			ls.add(cell);
			util.putData(ls);
//			util.GetValue(sheet, position)
		} catch ( Exception e) {
			e.printStackTrace();
		}
	}
 

	private String file;
	private boolean readonly = false;
 
	private static String[] ABC = { "A", "B", "C", "D", "E", "F", "G", "H",
			"I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U",
			"V", "W", "X", "Y", "Z" };
 
	public JacobExcelUtil(String file) throws FileNotFoundException {
		this.file = file;
		File f = new File(file);
		if(!f.exists()) {
			throw new FileNotFoundException();
		}
	}
 
	/**
	 * 写入数据
	 * @param cells
	 */
	public void putData(List<ExcelCell> cells) throws Exception {
		if(cells == null || cells.size() == 0 ) {
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
			filename = file;
			excel = new ActiveXComponent("Excel.Application");
			excel.setProperty("Visible", new Variant(true));
			excel.setProperty("AutomationSecurity", new Variant(1));
			workbooks = excel.getProperty("Workbooks").toDispatch();
			workbook = Dispatch.invoke(
					workbooks,
					"Open",
					Dispatch.Method,
					new Object[] { filename, new Variant(false),
							
							new Variant(readonly)// 是否以只读方式打开
							},	
					new int[1]).toDispatch();
	
			// put data
			sheet = Dispatch.get(workbook, "ActiveSheet").toDispatch();
			String position = null;
			int row = 0;
			int col = 0;
			int max = 26*26-1;
			for (ExcelCell c : cells) {
				row = c.getRow();
				col = c.getCol();
				if (row < 0 || col < 0 || col > max || c.getValue() == null
						|| c.getValue().trim().equals("")) {
					continue;
				}
				position = translateLocation(c.getRow(), c.getCol());
				setValue(sheet, position, c.getValue());
			}
			Thread.sleep(5000);
			position = translateLocation(3,3);
			String v = getValue(sheet,position);
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
	 * 转换单元格位置
	 * 最多支持26*26列
	 * @param i
	 * @param j
	 * @return
	 */
	public String translateLocation(int i, int j) {
		String loc = "";
		if (i <= 26) {
			loc = ABC[i-1] + (j);
		} else {
			loc = ABC[i/26-1] + ABC[i%26-1] + (j);
		}
 
		return loc;
	}
 
	/**
	 * 写入值
	 * 
	 * @param sheet
	 * @param position
	 * @param value
	 */
	protected void setValue(Dispatch sheet, String position, String value) {
		Dispatch cell = Dispatch.invoke(sheet, "Range", Dispatch.Get,
				new Object[] { position }, new int[1]).toDispatch();
		Dispatch.put(cell, "Value", value);
	}
 
	/**
	 * 读取值
	 * 
	 * @param sheet
	 * @param position
	 * @return
	 */
	protected String getValue(Dispatch sheet, String position) {
		Dispatch cell = Dispatch.invoke(sheet, "Range", Dispatch.Get,
				new Object[] { position }, new int[1]).toDispatch();
		String value = Dispatch.get(cell, "Value").toString();
		return value;
	}
	

}