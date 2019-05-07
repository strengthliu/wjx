package demo1;

import java.util.Hashtable;
import java.util.List;

public class ExcelService {

	public ExcelService() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * Tool的池。
	 */
	Hashtable<String,JacobExcelTool> pool = new Hashtable<String,JacobExcelTool>();
	
	/**
	 * 用指定参数获取指定名称的Excel表路径，参数用于执行宏和计算等操作。
	 * @param ExcelName
	 * @param parms[]
	 * @return
	 * @author 强
	 * @since 2019.05.05
	 */
	public List<String> getExcel(String ExcelName,Object[] parms){
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

}
