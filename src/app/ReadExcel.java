package app;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xwpf.usermodel.*;
import org.apache.xmlbeans.XmlCursor;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblBorders;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STBorder;

public class ReadExcel {

    private final static String PATH = "/src/resources/";
    private final static String OUT_PATH = "/src/result/";

    public static void main(String[] args) throws IOException, ParseException {

        Locale.setDefault(Locale.ROOT);

        Map<String, Person> persons = new LinkedHashMap<>();
        InterestList interestList = new InterestList();
        Calculator calculator = new Calculator(interestList);

        String projectPath = System.getProperty("user.dir");
        String inputPath = projectPath + PATH + "info.xlsx";
        String blankPath = projectPath + PATH + "test_blank.docx";
        String outputPath = projectPath + OUT_PATH;
        FileInputStream inputStream = new FileInputStream(new File(inputPath));

        Workbook workbook = new XSSFWorkbook(inputStream);
        Sheet firstSheet = workbook.getSheetAt(0);
        Iterator<Row> iterator = firstSheet.iterator();


        while (iterator.hasNext()) {
            Row nextRow = iterator.next();
            SimpleDateFormat dateTemp = new SimpleDateFormat("dd-MMM-yyyy");
            Iterator<Cell> cellIterator = nextRow.cellIterator();
            int indexFile = 0;

            String egn = "";
            String name = "";
            String invoiceNumber = "";
            Date date = null;
            double bond = 0.0;
            String city = "";
            String address = "";
            String court = "";
            String courtAddress = "";
            String postalCode = "";
            String charge = "";
            String honorary = "";

            DataFormatter df = new DataFormatter();
            while (cellIterator.hasNext()) {

                Cell cell = cellIterator.next();

                switch (indexFile) {
                    case 0:
                        egn = df.formatCellValue(cell);
                        break;
                    case 1:
                        name = cell + "";
                        break;
                    case  2:
                        invoiceNumber = cell + "";
                        break;
                    case  3:
                        date = dateTemp.parse(cell + "");
                        break;
                    case 4:
                        bond = Double.valueOf(cell + "");
                        break;
                    case 5:
                        city = cell + "";
                        break;
                    case 6:
                        address = cell + "";
                        break;
                    case 7:
                        court = cell + "";
                        break;
                    case 8:
                        courtAddress = cell + "";
                        break;
                    case 9:
                        postalCode = df.formatCellValue(cell);
                        break;
                    case 10:
                        charge = df.formatCellValue(cell);
                        break;
                    case 11:
                        honorary = df.formatCellValue(cell);
                        break;
                }
                indexFile++;
            }

            Person currentPerson = new Person(egn, name, bond, invoiceNumber, date, city,
                address, court, courtAddress, postalCode, charge, honorary);
            calculator.calculateRates(currentPerson);
            persons.put(currentPerson.getFullName(), currentPerson);
        }

        int documentCount = 1;
        for (Person person : persons.values()) {
            FileInputStream blankStream = new FileInputStream(blankPath);
            XWPFDocument document = new XWPFDocument(blankStream);
            FileOutputStream out = new FileOutputStream( new File(outputPath + (person.getFullName() + ".docx")));

            // Filling info in new document
            List<XWPFParagraph> paragraphs = document.getParagraphs();
            for (XWPFParagraph xwpfParagraph : paragraphs) {
                List<XWPFRun> runs = xwpfParagraph.getRuns();
                for (XWPFRun run : runs) {
                    String searchedText = "";
                    if(run.getText(0) != null) {
                        searchedText = run.getText(0).trim();
                    }

                    formatText(person, xwpfParagraph, run, searchedText);
                }
            }

            document.write(out);
            System.out.println(documentCount + " documents created!");
            out.close();
            blankStream.close();
            documentCount++;
        }

        workbook.close();
        inputStream.close();
    }

    private static void formatText(Person person, XWPFParagraph xwpfParagraph, XWPFRun run, String searchedText) {
        switch (searchedText) {
            case "court":
                searchedText = searchedText.replace(searchedText, person.getCourt());
                run.setText(searchedText, 0);
                break;
            case "courtAddress":
                searchedText = searchedText.replace(searchedText, person.getCourtAddress());
                run.setText(searchedText, 0);
                break;
            case "city":
                searchedText = searchedText.replace(searchedText, person.getCity());
                run.setText(searchedText, 0);
                break;
            case "postalCode":
                searchedText = searchedText.replace(searchedText, person.getPostalCode());
                run.setText(searchedText, 0);
                break;
            case "name":
                searchedText = searchedText.replace(searchedText, person.getFullName());
                run.setText(searchedText, 0);
                break;
            case "egn":
                searchedText = searchedText.replace(searchedText, person.getEgn());
                run.setText(searchedText, 0);
                break;
            case "address":
                searchedText = searchedText.replace(searchedText, person.getAddress());
                run.setText(searchedText, 0);
                break;
            case "bond":
                searchedText = searchedText.replace(searchedText, person.getBond() + "");
                run.setText(searchedText, 0);
                break;
            case "bondToText":
                searchedText = searchedText.replace(searchedText, NumberToWord.reformat(person.getBond() + "") + ",");
                run.setText(searchedText, 0);
                break;
            case "facNum":
                searchedText = searchedText.replace(searchedText, person.getInvoiceNumber());
                run.setText(searchedText, 0);
                break;
            case "date":
                searchedText = searchedText.replace(searchedText, dateToText(person.getStartDate()));
                run.setText(searchedText, 0);
                break;
            case "lihva":
                searchedText = searchedText.replace(searchedText, "");
                interestTable(person, xwpfParagraph);
                run.setText(searchedText, 0);
                break;
            case "wholeInterest":
                searchedText = searchedText.replace(searchedText, person.getWholeInterest());
                run.setText(searchedText, 0);
                break;
            case "interestToText":
                searchedText = searchedText.replace(searchedText, NumberToWord.reformat(person.getWholeInterest() + ""));
                run.setText(searchedText, 0);
                break;
            case "startDate":
                searchedText = searchedText.replace(searchedText, dateToText(person.getStartDate()) + " г.");
                run.setText(searchedText, 0);
                break;
            case "endDate":
                searchedText = searchedText.replace(searchedText, " " + person.getLastDate() + " г.");
                run.setText(searchedText, 0);
                break;
            case "sum":
                double sum = person.getBond() + Double.parseDouble(person.getWholeInterest());
                String sumToText = NumberToWord.reformat(sum + "");
                searchedText = searchedText.replace(searchedText, " " + sum + " лв." + sumToText + ".");
                run.setText(searchedText, 0);
                break;
            case "charge":
                String chargeToText = NumberToWord.reformat(person.getCharge());
                searchedText = searchedText.replace(searchedText, person.getCharge() + " лв. " + chargeToText + ",");
                run.setText(searchedText, 0);
                break;
            case "honorary":
                String honoraryToText = NumberToWord.reformat(person.getHonorary());
                searchedText = searchedText.replace(searchedText, person.getHonorary() + " лв. " + honoraryToText + ".");
                run.setText(searchedText, 0);
                break;
        }
    }

    private static void interestTable(Person person, XWPFParagraph paragraph) {
        XmlCursor cursor = paragraph.getCTP().newCursor();
        XWPFTable table = paragraph.getBody().insertNewTbl(cursor);
        CTTblPr tblpro = table.getCTTbl().getTblPr();
        // setting no borders to table rows and columns
        CTTblBorders borders = tblpro.addNewTblBorders();
        borders.addNewBottom().setVal(STBorder.NONE);
        borders.addNewLeft().setVal(STBorder.NONE);
        borders.addNewRight().setVal(STBorder.NONE);
        borders.addNewTop().setVal(STBorder.NONE);
        //also inner borders
        borders.addNewInsideH().setVal(STBorder.NONE);
        borders.addNewInsideV().setVal(STBorder.NONE);
        int rowIndex = 0;
        for (Map.Entry<String, String> entry : person.getRates().entrySet()) {
            XWPFTableRow currentRow = table.createRow();
            String firstCellText = "Лихва: " + entry.getValue() + " лв. "
                + NumberToWord.reformat(entry.getValue()) + " върху главница по фактура №"
                + person.getInvoiceNumber();
            currentRow.getCell(0).setText(firstCellText);

            String[] dates = entry.getKey().split("&");
            currentRow.createCell().setText("от " + dates[0] + " г.");
            currentRow.createCell().setText("до " + dates[1] + " г.");
            rowIndex++;
        }
    }

    private static String dateToText(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);

        String dayStr = "";
        String monthStr = "";
        String yearStr = year + "";

        if(day < 10) {
            dayStr = "0" + day;
        } else {
            dayStr = day + "";
        }

        if(month + 1 < 10) {
            monthStr = "0" + (month + 1);
        } else {
            monthStr = (month + 1) + "";
        }

        return dayStr + "." + monthStr + "." + yearStr;
    }
}
