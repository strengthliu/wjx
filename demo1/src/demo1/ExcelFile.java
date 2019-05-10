package demo1;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.Dispatch;

public class ExcelFile extends ExcelUtil implements Cloneable {

	public static void main(String[] args) {
		ExcelFile f = new ExcelFile();
		f.name = "1";
		f.num = 1;
		// f.ls.add(new ExcelFile());
		ExcelFile f1 = null;
		try {
			f1 = (ExcelFile) f.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		System.out.println(f1.name);
		System.out.println(f1.ls.size());
		System.out.println(f1.num);

		Object obj = f;
		if (obj instanceof ExcelFile)
			System.out.println("y");
		else
			System.out.println("n");
	}

	/**
	 * 操作COM对象引用， 多处持有，这里只是引用。
	 */
//	ActiveXComponent xl = null; // Excel对象
//	Dispatch workbooks = null; //
	ActiveXObject axo = null;


//	private 	ExcelUtil tool;
	// 同名文件的实例，最多有10个，就是说，同一个Excel文件，同时最多允许10个用户访问。
	public Vector<ExcelFile> ls = new Vector<ExcelFile>(10);

	public String name;

	public int num;
	public ExcelFile() {
		super();
		this.ls.add(this);
		this.num = this.ls.size();
	}
	public ExcelFile(ActiveXObject axo, String name) {
		super();
		this.axo = axo;
		this.name = name;
		this.ls.add(this);
		this.num = this.ls.size();
	}

	public Object clone() throws CloneNotSupportedException {
		ExcelFile ret = (ExcelFile) super.clone();
		ret.ls.add(ret);
		ret.num = ret.ls.size();
		return ret;
	}

	/*
	 * 退出时释放资源。 因为有调用COM对象，所以担心不能正常释放资源。
	 * 
	 * @see java.lang.Object#finalize()
	 */
	protected void finalize() throws Throwable {
		initComponents();
	}

	public List<ActiveXObject> getAllWorkbooks() {
		ArrayList<ActiveXObject> ret = new ArrayList<ActiveXObject>();
		ret.add(this.axo);
		for (int i = 0; i < this.ls.size(); i++) {
			ret.add(this.ls.get(i).axo);
		}
		return ret;
	}

//	public ExcelFile getAvailableInstance() {
//		return null;
//	}

	public ActiveXObject getActiveXObject() {
		return axo;
	}

		
	public boolean isBusy() {
		// TODO lock机制
		return true;
	}

	public boolean lock() {
		// TODO lock机制
		return true;
	}

	/**
	 * 根据参数，执行Excel表宏，再将所有sheet另存为mode所标示的文件格式。
	 * @param params
	 * @param mode 
	 * @return
	 */
	public List<String> report(Object[] params, int mode) {
		// TODO 根据参数，执行Excel表宏，再将所有sheet另存为mode所标示的文件格式。
		return null;
	}


}
