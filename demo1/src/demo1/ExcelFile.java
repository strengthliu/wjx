package demo1;

import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.Dispatch;

public class ExcelFile {


	/**
	 * 操作COM对象引用，
	 * 多处持有，这里只是引用。
	 */
	private ActiveXComponent xl = null; // Excel对象
	private Dispatch workbooks = null; //

	/**
	 * 一个Excel文件单独使用的对象，
	 * 只在这个类中持有。
	 */
	private Dispatch workbook = null; // 具体工作簿
	private Dispatch sheet = null; // 写参数的那个表
	private Dispatch sheets = null; // 写参数的那个表

	private Dispatch currentSheet = null; // 当前操作表

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	
	/* 退出时释放资源。
	 * 因为有调用COM对象，所以担心不能正常释放资源。
	 * @see java.lang.Object#finalize()
	 */
	protected void finalize() throws Throwable {
		initComponents();
	}
	private void initComponents() {
		workbook = null;
		currentSheet = null;
		sheets = null;
		sheet = null;
	}

}
