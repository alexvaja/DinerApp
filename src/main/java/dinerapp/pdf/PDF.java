package dinerapp.pdf;

import java.io.FileOutputStream;
import java.util.List;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import dinerapp.model.entity.Food;

public class PDF 
{
	public static void exportToPDF(List <Food> foods, List <Integer> tities, String date) 
	{
		Document document = new Document();
		try 
		{	
			PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream("Raport.pdf"));
			document.open();
			
			Image logo = Image.getInstance("/../../../resources/static/images/logo.png");
		    //Fixed Positioning
			logo.setAbsolutePosition(100f, 550f);
		    //Scale to new height and new width of image
			logo.scaleAbsolute(200, 200);
		    //Add to document
		    document.add(logo);
			
			
			Paragraph title = new Paragraph();
			title.add("Raportul de mancare pe ziua " + date);
			title.setAlignment(Element.ALIGN_CENTER);
			document.add(title);
			
			
			PdfPTable table = new PdfPTable(2); // 3 columns.
			table.setWidthPercentage(100); // Width 100%
			table.setSpacingBefore(30f); // Space before table
			table.setSpacingAfter(30f); // Space after table

			// Set Column widths
			float[] columnWidths = { 1f, 1f};
			table.setWidths(columnWidths);

			PdfPCell foodCell = new PdfPCell(new Paragraph("Mancare"));
			foodCell.setBorderColor(BaseColor.BLUE);
			foodCell.setPaddingLeft(10);
			foodCell.setHorizontalAlignment(Element.ALIGN_CENTER);
			foodCell.setVerticalAlignment(Element.ALIGN_MIDDLE);

			PdfPCell quantityCell = new PdfPCell(new Paragraph("Numar de portii"));
			quantityCell.setBorderColor(BaseColor.BLUE);
			quantityCell.setPaddingLeft(10);
			quantityCell.setHorizontalAlignment(Element.ALIGN_CENTER);
			quantityCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			table.addCell(foodCell);
			table.addCell(quantityCell);

			//table.setHeaderRows(1);

			PdfPCell[] cells = table.getRow(0).getCells();
			for (int j = 0; j < cells.length; j++)
				cells[j].setBackgroundColor(BaseColor.GRAY);
			
			
			 for (int i = 1; i < foods.size(); i++)
			 {
	    	     table.addCell(foods.get(i).getName());
	    	     table.addCell(tities.get(i).toString());
	         }
			 
			document.add(table);

			document.close();
			writer.close();

		} catch (Exception e) 
		{
			e.printStackTrace();
		}
	}
}
