package demo1;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

public class T1 {

	public static void main(String[] parm){
    try{
    	FileInputStream fileIn = new FileInputStream("D:\\wjx\\demo1\\t1.xls");
        POIFSFileSystem fs = new POIFSFileSystem(fileIn);
        HSSFWorkbook wb = new HSSFWorkbook(fs);
        HSSFSheet sheet = wb.getSheetAt(0);
        sheet.setForceFormulaRecalculation(true);
        HSSFRow row = sheet.getRow(2);
        if (row == null)
            row = sheet.createRow(2);
        HSSFCell cell = row.getCell(2);
		 System.out.println(cell.getNumericCellValue());

		HSSFCell cell1 = row.getCell(3);
        if (cell1 == null)
            cell1 = row.createCell(3);
        cell1.setCellValue(9.00);
		 System.out.println(cell.getNumericCellValue());
		cell.setCellFormula(cell.getCellFormula());
		System.out.println(cell.getCellFormula());
		 System.out.println(cell.getNumericCellValue());
        // Write the output to a file
        FileOutputStream fileOut = new FileOutputStream("D:\\wjx\\demo1\\t1.xls");
        
//        
//        row = sheet.getRow(2);
//        HSSFCell cellS = row.getCell(2);
//
////        if (HSSFCell.CELL_TYPE_FORMULA == cellS.getCellType()) 
////        {
//		     //取得公式单元格的公式,重新设置
//			 cellS.setCellFormula(cellS.getCellFormula());
//			 System.out.println(cellS.getNumericCellValue());
////		 }else {
////			 cellS.setCellValue(value+"");// 给单元格赋String值
////		 }
		
        //写入文件，未关闭
		wb.write(fileOut);
		
		// TODO Auto-generated method stub
		JacobExcelTool tool = new JacobExcelTool();
		//打开
		tool.OpenExcel("D:\\wjx\\demo1\\t1.xls",true,false);
		//调用Excel宏
		tool.callMacro("VBATest");
		//关闭并保存，释放对象
		tool.CloseExcel(true, true);


        sheet.setForceFormulaRecalculation(true);
       }catch(Exception e){
    	   e.printStackTrace();
       }
    
    try{
    	FileInputStream fileIn = new FileInputStream("D:\\wjx\\demo1\\t1.xls");
        POIFSFileSystem fs = new POIFSFileSystem(fileIn);
        HSSFWorkbook wb = new HSSFWorkbook(fs);
        HSSFSheet sheet = wb.getSheetAt(0);
        sheet.setForceFormulaRecalculation(true);
        HSSFRow row = sheet.getRow(2);
        HSSFCell cell = row.getCell(2);

		 System.out.println("重新打开文件读取数据："+String.valueOf(cell.getNumericCellValue()));

       }catch(Exception e){
    	   e.printStackTrace();
       }
	}
}