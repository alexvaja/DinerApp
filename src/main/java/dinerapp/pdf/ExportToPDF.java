package dinerapp.pdf;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import dinerapp.model.entity.Food;

public class ExportToPDF 
{

	public static void exportToPDF(String fileName, List<Food> foods, List<Integer> quantitiesBefore4PM, 
			List<Integer>quantitiesAfter4PM, String reportDate) throws
	FileNotFoundException, DocumentException {
		
		Document pdfDoc = new Document(PageSize.A4, 13, 13, 100, 90);
		String column1 = "DENUMIRE MANCARE";
		String column2 = "CANTITATE";
		Font cellFontBold = FontFactory.getFont("Times Roman", 8, BaseColor.BLACK);
		cellFontBold.setStyle(Font.BOLD);

		Font cellFont = FontFactory.getFont("Times Roman", 8, BaseColor.BLACK);
		Font textFont = FontFactory.getFont("Times Roman", 14, BaseColor.BLACK);
		textFont.setStyle(Font.BOLD);

		try
		{
			HeaderFooterPageEvent event = new HeaderFooterPageEvent();
			PdfWriter.getInstance(pdfDoc, new FileOutputStream(fileName)).setPageEvent(event);
			pdfDoc.open();			
			
			/*==========================================================================*/
			/*Tabelul cu mnancarea comandata inainte de ora 16*/
			
			pdfDoc.add(new Paragraph("Raportul pentru data de " + reportDate));
			pdfDoc.add(new Paragraph("Total mancare comandata inainte de ora 16:00"));
			PdfPTable tableBefore4PM = new PdfPTable(2);
			tableBefore4PM.setWidths(new int[] { 10, 10});
			tableBefore4PM.setWidthPercentage(100);
			
			tableBefore4PM.addCell(setCell(cellFontBold, column1));
			tableBefore4PM.addCell(setCell(cellFontBold, column2));
			tableBefore4PM.setSpacingBefore(30f); // Space before table
			tableBefore4PM.setSpacingAfter(30f); // Space after table
			
			for (int index = 0; index < foods.size(); index++)
			{
				if(quantitiesBefore4PM.get(index) != 0) 
				{
					tableBefore4PM.addCell(setCell(cellFont, foods.get(index).getName()));
					tableBefore4PM.addCell(setCell(cellFont, quantitiesBefore4PM.get(index).toString()));
				}	
			}
			tableBefore4PM.setHeaderRows(1);
			/*============================================================================================*/
			
			pdfDoc.add(tableBefore4PM);			
			
			
			/*===========================================================================================*/
			pdfDoc.add(new Paragraph("Total mancare comandata dupa ora 16:00"));
			PdfPTable tableAfter4PM = new PdfPTable(2);
			tableAfter4PM.setWidths(new int[] {10,10});
			tableAfter4PM.setWidthPercentage(100);
			tableAfter4PM.addCell(setCell(cellFontBold, column1));
			tableAfter4PM.addCell(setCell(cellFontBold, column2));
			tableBefore4PM.setSpacingBefore(30f);
			tableBefore4PM.setSpacingAfter(30f);
			
			for (int index = 0; index < foods.size(); index++) 
			{
				if(quantitiesAfter4PM.get(index) != 0)
				{
					tableBefore4PM.addCell(setCell(cellFont, foods.get(index).getName()));
					tableBefore4PM.addCell(setCell(cellFont, quantitiesAfter4PM.get(index).toString()));
				}	
			}			
		} catch (FileNotFoundException | DocumentException e) {
			e.printStackTrace();
		} finally {
			pdfDoc.close();
		}
	}

	private static PdfPCell setCell(Font cellFontBold, String text) {
		Paragraph p = new Paragraph(text, cellFontBold);
		PdfPCell cell = new PdfPCell(p);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		return cell;
	}

	public static void downloadFile(HttpServletResponse response, String fileName) throws IOException {
			
		MediaType mediaType = getMediaTypeForFileName(fileName);
		File file = new File(fileName);

		response.setContentType(mediaType.getType()); // Content-Type: application/pdf
		response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + file.getName());
		response.setContentLength((int) file.length());

		BufferedInputStream inStream = new BufferedInputStream(new FileInputStream(file));
		BufferedOutputStream outStream = new BufferedOutputStream(response.getOutputStream());

		byte[] buffer = new byte[1024];
		int bytesRead = 0;
		while ((bytesRead = inStream.read(buffer)) != -1) {
			outStream.write(buffer, 0, bytesRead);
		}

		outStream.flush();
		outStream.close();
		inStream.close();
	}

	private static MediaType getMediaTypeForFileName(String fileName) {

//		String mineType = servletContext.getMimeType(fileName);
		String mimeType = "application/pdf";

		try {
			MediaType mediaType = MediaType.parseMediaType(mimeType);
			return mediaType;
		} catch (Exception e) {
			return MediaType.APPLICATION_OCTET_STREAM;
		}
	}
}
