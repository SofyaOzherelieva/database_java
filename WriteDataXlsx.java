package ru.mipt.java2017.hw3;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.mipt.java2017.hw3.models.*;

public class WriteDataXlsx {
  private static Logger logger = LoggerFactory.getLogger("Excel Writer");
  private final FileOutputStream fileOutputStream;
  private Workbook workbook;

  static WriteDataXlsx createProvider(String fileName) {
    try {
      return new WriteDataXlsx(fileName);
    }
    catch (FileNotFoundException e) {
      logger.error("File wasn't found: {}", fileName);
      return null;
    } catch (IOException e) {
      logger.error("Writing error: {}", e.getMessage());
      return null;
    }
  }

  private WriteDataXlsx(String fileName) throws IOException {
    fileOutputStream = new FileOutputStream(new File(fileName));
    workbook = new XSSFWorkbook();
  }

  void writeToDatabase(List<Book> books, List<Author> authors, List<BookAuthor> bookAuthor) {
    addSheetBooks(books);
    addSheetAuthors(authors);
    addSheetBookAuthors(bookAuthor);
    try {
      workbook.write(fileOutputStream);
      workbook.close();
    } catch (Exception e) {
      logger.error("can't write to output file");
    }
  }

  private void addSheetBooks(List<Book> books) {
    Sheet sheet = workbook.createSheet("Books");
    Row row = sheet.createRow(0);
    row.createCell(0).setCellValue("ID");
    row.createCell(1).setCellValue("ISBN");
    row.createCell(2).setCellValue("Title");
    row.createCell(3).setCellValue("Cover");

    int curInd = 1;
    for (Book book : books) {
      Row currentRow = sheet.createRow(curInd++);
      currentRow.createCell(0).setCellValue(book.getID());
      currentRow.createCell(1).setCellValue(String.valueOf(book.getISBN()));
      currentRow.createCell(2).setCellValue(book.getTitle());
      currentRow.createCell(3).setCellValue(book.getCover());

    }
  }

  private void addSheetAuthors(List<Author> authors) {
    Sheet sheet = workbook.createSheet("Authors");
    Row row = sheet.createRow(0);
    row.createCell(0).setCellValue("ID");
    row.createCell(1).setCellValue("Name");

    int curInd = 1;
    for (Author author : authors) {
      Row currentRow = sheet.createRow(curInd++);
      currentRow.createCell(0).setCellValue(author.getID());
      currentRow.createCell(1).setCellValue(author.getName());
    }
  }

  private void addSheetBookAuthors(List<BookAuthor> booksAuthors) {
    Sheet sheet = workbook.createSheet("Books_Authors");
    Row row = sheet.createRow(0);
    row.createCell(0).setCellValue("ID");
    row.createCell(1).setCellValue("books_id");
    row.createCell(2).setCellValue("authors_id");
    row.createCell(3).setCellValue("order");

    int curInd = 1;
    for (BookAuthor book_author : booksAuthors) {
      Row currentRow = sheet.createRow(curInd++);
      currentRow.createCell(0).setCellValue(book_author.getID());
      currentRow.createCell(1).setCellValue(book_author.getBookId());
      currentRow.createCell(2).setCellValue(book_author.getAuthorId());
      currentRow.createCell(3).setCellValue(book_author.getNum());
    }
  }
}
