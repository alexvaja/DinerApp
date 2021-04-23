package dinerapp.pdf;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.ExceptionConverter;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.PdfWriter;

public class HeaderFooterPageEvent extends PdfPageEventHelper {

	private PdfTemplate t;
	private Image total;

	public void onOpenDocument(PdfWriter writer, Document document) {
		t = writer.getDirectContent().createTemplate(30, 16);
		try {
			total = Image.getInstance(t);
			total.setRole(PdfName.ARTIFACT);
		} catch (DocumentException de) {
			throw new ExceptionConverter(de);
		}
	}

	public void onEndPage(PdfWriter writer, Document document) {
		addHeader(writer);
		addFooter(writer);
	}

	private void addHeader(PdfWriter writer) {

		PdfPTable header = new PdfPTable(2);
		try {

			header.setWidths(new int[] { 2, 24 });
			header.setTotalWidth(527);
			header.setLockedWidth(true);
			header.getDefaultCell().setFixedHeight(40);
			header.getDefaultCell().setBorder(Rectangle.BOTTOM);
			header.getDefaultCell().setBorderColor(BaseColor.LIGHT_GRAY);

			// add image
			// Image logo = Image.getInstance("..\\DinerApp -
			// new\\src\\main\\resources\\static\\images\\AtosLogo.png");
			try {
                Image logo = Image.getInstance(new URL("https://i.imgur.com/alMbYuq.jpg"));
                // WAS Atos logo:  https://i.imgur.com/xfChJpW.png
                logo.setAbsolutePosition(100f, 550f);
                logo.scaleAbsolute(200, 200);
                header.addCell(logo);
			}
			catch(MalformedURLException e)
			{
				e.printStackTrace();
			}
			catch(IOException e)
			{
				e.printStackTrace();
			}

			// add text-date
			PdfPCell text = new PdfPCell();
			text.addElement(new Paragraph(new Date().toString()));
			// text.addElement(new Paragraph("Last update was made by: Vaja Alexandru"));
			text.setHorizontalAlignment(Element.ALIGN_RIGHT);
			text.setPaddingBottom(5);
			text.setPaddingLeft(280);
			text.setBorder(Rectangle.BOTTOM);
			text.setBorderColor(BaseColor.LIGHT_GRAY);
			header.addCell(text);

			// add content
			header.writeSelectedRows(0, -1, 34, 803, writer.getDirectContent());

		} catch (DocumentException de) {
			throw new ExceptionConverter(de);
		}
	}

	private void addFooter(PdfWriter writer) {
		PdfPTable footer = new PdfPTable(3);
		try {
			// defaults
			footer.setWidths(new int[] { 24, 2, 0 });
			footer.setTotalWidth(527);
			footer.setLockedWidth(true);
			footer.getDefaultCell().setFixedHeight(40);
			footer.getDefaultCell().setBorder(Rectangle.TOP);
			footer.getDefaultCell().setBorderColor(BaseColor.LIGHT_GRAY);

			// actual page numbering
			footer.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
			footer.addCell(new Phrase(String.format("Page %d of", writer.getPageNumber()),
					new Font(Font.FontFamily.HELVETICA, 8)));

			// add placeholder for total page count
			PdfPCell totalPageCount = new PdfPCell(total);
			totalPageCount.setBorder(Rectangle.TOP);
			totalPageCount.setBorderColor(BaseColor.LIGHT_GRAY);
			footer.addCell(totalPageCount);

			// add text
			PdfPCell text = new PdfPCell();
			text.setPaddingBottom(15);
			text.setPaddingLeft(10);
			text.setBorder(Rectangle.BOTTOM);
			text.setBorderColor(BaseColor.LIGHT_GRAY);
			footer.addCell(text);
			// write page
			PdfContentByte canvas = writer.getDirectContent();
			canvas.beginMarkedContentSequence(PdfName.ARTIFACT);
			footer.writeSelectedRows(0, -1, 34, 50, canvas);

			canvas.endMarkedContentSequence();
		} catch (DocumentException e) {
			throw new ExceptionConverter(e);
		}
	}

	public void onCloseDocument(PdfWriter writer, Document document) {
		int totalLength = String.valueOf(writer.getPageNumber()).length();
		int totalWidth = totalLength * 5;
		ColumnText.showTextAligned(t, Element.ALIGN_RIGHT,
				new Phrase(String.valueOf(writer.getPageNumber()), new Font(Font.FontFamily.HELVETICA, 8)), totalWidth,
				6, 0);
	}
}