package file;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelRW {

    public static List<String[]> readExcel(String filename) throws Exception {
        List sheetData = new ArrayList();
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(filename);
            XSSFWorkbook workbook = new XSSFWorkbook(fis);
            XSSFSheet sheet = workbook.getSheetAt(0);
            Iterator rows = sheet.rowIterator();
            while (rows.hasNext()) {
                XSSFRow row = (XSSFRow) rows.next();
                Iterator cells = row.cellIterator();
                List data = new ArrayList();
                while (cells.hasNext()) {
                    XSSFCell cell = (XSSFCell) cells.next();
                    data.add(cell);
                }
                sheetData.add(data);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fis != null) {
                fis.close();
            }
        }
        return getExelData(sheetData);
    }

    public static List<String[]> readExcelBySheet(String filename, String sheetname) throws Exception {
        List sheetData = new ArrayList();
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(filename);
            XSSFWorkbook workbook = new XSSFWorkbook(fis);
            XSSFSheet sheet = workbook.getSheet(sheetname);
            Iterator rows = sheet.rowIterator();
            while (rows.hasNext()) {
                XSSFRow row = (XSSFRow) rows.next();
                Iterator cells = row.cellIterator();
                List data = new ArrayList();
                while (cells.hasNext()) {
                    XSSFCell cell = (XSSFCell) cells.next();
                    data.add(cell);
                }
                sheetData.add(data);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fis != null) {
                fis.close();
            }
        }
        return getExelData(sheetData);
    }

    private static List<String[]> getExelData(List sheetData) {
        List<String[]> contentList = new LinkedList<String[]>();
        for (int i = 0; i < sheetData.size(); i++) {
            List list = (List) sheetData.get(i);
            String[] thisContent = new String[list.size()];
            for (int j = 0; j < list.size(); j++) {
                XSSFCell cell = (XSSFCell) list.get(j);
            	int type = cell.getCellType();
            	if (type==0)
            		thisContent[j] = "" + cell.getNumericCellValue();
            	else
            		thisContent[j] =  cell.getRichStringCellValue().getString();
            }
            contentList.add(thisContent);
        }
        return contentList;
    }
	
	static public void writeExcel(String fileName, List<String[]> contentList) throws Exception {
		// create a new file
		FileOutputStream outStream = new FileOutputStream(fileName);
		// create a new workbook
		Workbook workbook = new XSSFWorkbook();
		Sheet thisSheet = workbook.createSheet();
		
		int rowID = 0;
		for (String[] thisContent : contentList) {
		    Row thisRow = thisSheet.createRow(rowID++);
		    for (int columnID=0; columnID<thisContent.length; columnID++) {
		    	Cell thisCell = thisRow.createCell(columnID);
		    	thisCell.setCellValue(thisContent[columnID]);
		    }
		}
		
		try {
			workbook.write(outStream);
			outStream.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	static public void writeExcelBySheet(String fileName, Map<String, List<String[]>> contentListMap) throws Exception {
		// create a new file
		FileOutputStream outStream = new FileOutputStream(fileName);
		// create a new workbook
		Workbook workbook = new XSSFWorkbook();
		for(Map.Entry<String, List<String[]>> entry : contentListMap.entrySet()){
			String sheetname = entry.getKey();
			List<String[]> contentList = entry.getValue();
			Sheet thisSheet = workbook.createSheet(sheetname);
			int rowID = 0;
			for (String[] thisContent : contentList) {
			    Row thisRow = thisSheet.createRow(rowID++);
			    for (int columnID=0; columnID<thisContent.length; columnID++) {
			    	Cell thisCell = thisRow.createCell(columnID);
			    	thisCell.setCellValue(thisContent[columnID]);
			    }
			}
		}
		workbook.write(outStream);
		outStream.close();
	}
	
}