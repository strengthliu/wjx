package demo1;

import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.Dispatch;

public class ActiveXObject {
	/**
	 * Com对象的池。 JAVA退出时，不会释放COM资源，这里需要在退出时释放。
	 * 初始化为10个实例对象。
	 */
	private ActiveXComponent xl;
	/**
	 * workbooks对象。
	 */
	private Dispatch workbooks;
	
	public ActiveXObject(ActiveXComponent axc,Dispatch workbooks) {
		super();
		this.xl = axc;
		this.workbooks = workbooks;
	}

	public ActiveXComponent getXl() {
		return xl;
	}

	public void setXl(ActiveXComponent xl) {
		this.xl = xl;
	}

	public Dispatch getWorkbooks() {
		return workbooks;
	}

	public void setWorkbooks(Dispatch workbooks) {
		this.workbooks = workbooks;
	}

	
}
