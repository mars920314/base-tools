package file;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.poi.xwpf.usermodel.Borders;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.VerticalAlign;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;


// TODO: Auto-generated Javadoc
/**
 * The Class WordWr.
 * http://www.w3ii.com/zh-CN/apache_poi_word/default.html
 */
public class WordWr {
	
	public static XWPFDocument createWord() {
		// Blank Document
		XWPFDocument document = new XWPFDocument();
		return document;
	}
	
	public static void writeWord(XWPFDocument document, String fileName) throws IOException {
		// Write the Document in file system
		FileOutputStream out = new FileOutputStream(new File(fileName));
		document.write(out);
		out.close();
	}
	
	public static void addParagraph(XWPFDocument document, String text) {
		// create Paragraph
		XWPFParagraph paragraph = document.createParagraph();
		XWPFRun run = paragraph.createRun();
		run.setText(text);
	}
	
	public static void addTable(XWPFDocument document, List<String[]> contentList) {
		// create table
		XWPFTable table = document.createTable();
		for (int i = 0; i < contentList.size(); i++) {
			String[] contents = contentList.get(i);
			XWPFTableRow tableRow = null;
			if (i == 0)
				tableRow = table.getRow(0);
			else
				tableRow = table.createRow();
			for (int j = 0; j < contents.length; j++) {
				XWPFTableCell tableCell = null;
				if (j == 0)
					tableCell = tableRow.getCell(0);
				else
					tableCell = tableRow.addNewTableCell();
				tableCell.setText(contents[j]);
			}
		}
	}
	
	public static void addFontstyle(XWPFDocument document, String text, Map<String, Integer> paragraphMap, Map<String, Integer> fontMap) {
		// create Paragraph
		XWPFParagraph paragraph = document.createParagraph();
		// Set Alignment
		// LEFT(1), CENTER(2), RIGHT(3), BOTH(4), MEDIUM_KASHIDA(5), DISTRIBUTE(6), NUM_TAB(7), HIGH_KASHIDA(8), LOW_KASHIDA(9), THAI_DISTRIBUTE(10);
		if (fontMap.containsKey("alignment"))
			paragraph.setAlignment(ParagraphAlignment.valueOf(paragraphMap.get("alignment")));
		// Set Borders
		if (fontMap.containsKey("border_top"))
			paragraph.setBorderLeft(Borders.valueOf(paragraphMap.get("border_top")));
		if (fontMap.containsKey("border_bottom"))
			paragraph.setBorderLeft(Borders.valueOf(paragraphMap.get("border_bottom")));
		if (fontMap.containsKey("border_left"))
			paragraph.setBorderLeft(Borders.valueOf(paragraphMap.get("border_left")));
		if (fontMap.containsKey("border_right"))
			paragraph.setBorderLeft(Borders.valueOf(paragraphMap.get("border_right")));

		// create sentence
		XWPFRun run = paragraph.createRun();
		// Set Bold
		if (fontMap.containsKey("bold") && fontMap.get("bold") > 0)
			run.setBold(true);
		// Set Italic
		if (fontMap.containsKey("italic") && fontMap.get("italic") > 0)
			run.setItalic(true);
		// Set text Position
		if (fontMap.containsKey("position"))
			run.setTextPosition(fontMap.get("position"));
		// Set Strik
		if (fontMap.containsKey("strik") && fontMap.get("strik") > 0)
			run.setStrike(true);
		// Set Font Size
		if (fontMap.containsKey("size"))
			run.setFontSize(fontMap.get("size"));
		// Set Subscript
		// BASELINE(1), SUPERSCRIPT(2), SUBSCRIPT(3);
		if (fontMap.containsKey("subscript"))
			run.setSubscript(VerticalAlign.valueOf(fontMap.get("subscript")));

		run.setText(text);
		// Add Break
		if (fontMap.containsKey("break") && fontMap.get("break") > 0)
			run.addBreak();
	}

}
