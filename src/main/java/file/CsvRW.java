package file;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

public class CsvRW {

	public static List<String[]> readCsvFile(String filename, char separator, char quotechar) throws IOException {
		ArrayList<String[]> list = new ArrayList<String[]>();
		CSVReader csvReader = new CSVReader(new FileReader(filename), separator, quotechar);
		if (csvReader != null) {
//			return csvReader.readAll();
			// first row is title, so past
			csvReader.readNext();
			String[] csvRow = null;
			while ((csvRow = csvReader.readNext()) != null) {
				list.add(csvRow);
			}
		}
		csvReader.close();
		return list;
	}
	/**
	 *  
	 * @param fileName
	 * @param contentList
	 * @param separator ','
	 * @param quotechar '\0'
	 * @throws IOException
	 */
	public static void writeCsv(String fileName, List<String[]> contentList, char separator, char quotechar) throws IOException {
		CSVWriter csvWriter = new CSVWriter(new FileWriter(fileName), separator, quotechar);
//		csvWriter.writeAll(contentList);
		for(String[] thisContent : contentList)
			csvWriter.writeNext(thisContent);
		csvWriter.close();
	}
}
