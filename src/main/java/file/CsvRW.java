package file;

import java.io.FileReader;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opencsv.CSVReader;

public class CsvRW {
    static Logger logger = LoggerFactory.getLogger(CsvRW.class);

    public static ArrayList<String[]> importCsvFile(String filename) {

        CSVReader csvReader = null;
        ArrayList<String[]> list = new ArrayList<String[]>();

        try {
            logger.info(filename);
            csvReader = new CSVReader(new FileReader(filename), ',');

            if (csvReader != null) {

                //first row is title, so past
                csvReader.readNext();
                String[] csvRow = null;//row

                while ((csvRow = csvReader.readNext()) != null) {
//	                  System.out.println(csvRow[0]+"==="+csvRow[1]+"==="+csvRow[2]);
                    list.add(csvRow);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}
