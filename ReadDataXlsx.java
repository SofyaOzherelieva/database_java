package ru.mipt.java2017.hw3;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReadDataXlsx {
  private static final Logger logger = LoggerFactory.getLogger("Excel Reader");

  private Workbook workbook;
  private Sheet sheet;

  private ReadDataXlsx(String fileName) throws IOException {
    FileInputStream fileInputStream = new FileInputStream(new File(fileName));
    workbook = new XSSFWorkbook(fileInputStream);
    sheet = workbook.getSheetAt(0);
  }

  public static ReadDataXlsx createReader(String fileName) {
    try {
      return new ReadDataXlsx(fileName);
    } catch (FileNotFoundException e) {
      logger.error("File wasn't found: {}", fileName);
      return null;
    } catch (IOException e) {
      logger.error("Reading error: {} ", e.getMessage());
      return null;
    }
  }

  private int getNumberOfRows() {
    return sheet.getLastRowNum();
  }

  private int getNumberOfCells(int i) {
    return sheet.getRow(i).getLastCellNum();
  }

  private String getCellValue(int i, int j) {
    return sheet.getRow(i).getCell(j).getStringCellValue();
  }

  private int getIndColumnByName(int row_num, String name) {
    for (int i = 0; i <= getNumberOfCells(row_num); ++i) {
      if (getCellValue(row_num, i).equals(name)) {
        return i;
      }
    }
    return -1;
  }

  public class BookDescription {
    String title;
    String[] authors;
    BookDescription(String title, String[] authors) {
      this.title = title;
      this.authors = authors;
    }
  }


  // Get all information about all books.
  Map<BigDecimal, BookDescription> getAllBooksDescription() {
    Map<BigDecimal, BookDescription> isbnBookDescription = new HashMap<>();
    int titleId = getIndColumnByName(0, "Title");
    int isbnId = getIndColumnByName(0, "ISBN");
    int authorsId = getIndColumnByName(0, "Authors");

    int numberOfRows = getNumberOfRows();
    for (int i = 1; i <= numberOfRows; ++i) {
      String curISBN = getCellValue(i, isbnId).
          replace("\u00a0"," ").substring("ISBN13: ".length() - 1).trim();

      Long isbn1 = Long.parseLong(curISBN);
      BigDecimal isbn = BigDecimal.valueOf(isbn1);

      String curAuthors = getCellValue(i, authorsId);
      String curTitle = getCellValue(i, titleId).trim();

      String[] authors = curAuthors.split(",");
      for (int j = 0; j < authors.length; ++j) {
        authors[j] = authors[j].trim();
      }

      BookDescription descriptionBook = new BookDescription(curTitle, authors);
      isbnBookDescription.put(isbn, descriptionBook);
    }
    return isbnBookDescription;
  }

  public Set<String> getAllUniqueAuthors(Map<BigDecimal, BookDescription> books) {
    Set<String> authors = new HashSet<>();
    Collection<BookDescription> descriptions = books.values();
    for (BookDescription description : descriptions) {
      for (String author : description.authors) {
        if (!authors.contains(author)) {
          authors.add(author);
        }
      }
    }
    return authors;
  }
}
