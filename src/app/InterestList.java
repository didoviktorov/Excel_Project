package app;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class InterestList {

    private final static String PATH = "/src/resources/";

    private String projectPath;
    private Map<Date, String> interestList;
    private String inputPath;

    public InterestList() {
        this.interestList = new LinkedHashMap<>();
        this.projectPath = System.getProperty("user.dir");
        this.inputPath = projectPath + PATH + "lihva.xlsx";
    }

    public Map<Date, String> getInterestList() throws IOException, ParseException {
        Locale.setDefault(Locale.ROOT);
        SimpleDateFormat dateTemp = new SimpleDateFormat("dd-MMM-yyyy");

        FileInputStream inputStream = new FileInputStream(new File(inputPath));
        Workbook workbook = new XSSFWorkbook(inputStream);
        Sheet firstSheet = workbook.getSheetAt(0);
        Iterator<Row> iterator = firstSheet.iterator();

        while (iterator.hasNext()) {
            Row nextRow = iterator.next();

            Date cellDate = new Date();
            String percent = "";

            Iterator<Cell> cellIterator = nextRow.cellIterator();
            int indexFile = 0;
            while (cellIterator.hasNext()) {
                Cell cell = cellIterator.next();
                switch (indexFile) {
                    case 0:
                        cellDate = dateTemp.parse(cell + "");
                        break;
                    case 1:
                        percent = cell + "";
                        break;
                }

                indexFile++;
            }

            this.interestList.put(cellDate, percent);
        }

        return Collections.unmodifiableMap(this.interestList);
    }
}
