package demo1;

import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.Dispatch;

public class ExcelFile {

	private ActiveXComponent xl = null; // Excel对象

	private Dispatch workbooks = null; //

	private Dispatch workbook = null; // 具体工作簿

	private Dispatch sheet = null; // 写参数的那个表

	private Dispatch currentSheet = null; // 当前操作表

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
