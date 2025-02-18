package com.fourinx.HotelBookingApplication.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Optional;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fourinx.HotelBookingApplication.model.BookedRoom;
import com.fourinx.HotelBookingApplication.repository.BookingRepository;
import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.UnitValue;

import jakarta.servlet.http.HttpServletResponse;

@Service
public class BillingService {

	@Autowired
	private BookingRepository bookingRepository;

	public void generateInvoice(HttpServletResponse response, Long bookingId) throws IOException {
		// Fetch booking details from the database
		Optional<BookedRoom> bookingOpt = bookingRepository.findById(bookingId);
		if (!bookingOpt.isPresent()) {
			throw new RuntimeException("Booking ID " + bookingId + " not found");
		}

		BookedRoom booking = bookingOpt.get();

		// Set response type to PDF
		response.setContentType("application/pdf");
		response.setHeader("Content-Disposition", "attachment; filename=Hotel_Invoice_" + bookingId + ".pdf");

		PdfWriter writer = new PdfWriter(response.getOutputStream());
		PdfDocument pdf = new PdfDocument(writer);
		Document document = new Document(pdf, PageSize.A4);

		// Get page width for centering text
		float pageWidth = pdf.getDefaultPageSize().getWidth();

		// Current date formatting
		SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");
		String todayDate = sdf.format(new Date());

		// Hotel Name & Header
		document.add(new Paragraph("CT Haven Palace").setBold().setFontSize(20).setTextAlignment(TextAlignment.CENTER));

		document.add(new Paragraph("Pathanivas Road, Barkul").setFontSize(12).setTextAlignment(TextAlignment.CENTER));

		document.add(new Paragraph("Phone: +91 9876543210 | Email: info@cthaven.com").setFontSize(12)
				.setTextAlignment(TextAlignment.CENTER));

		/*
		 * document.add(new Paragraph("\nINVOICE")
		 * .setBold().setFontSize(18).setTextAlignment(TextAlignment.RIGHT));
		 */
		/*
		 * document.add(new Paragraph("DATE: " + todayDate)
		 * .setFontSize(12).setTextAlignment(TextAlignment.RIGHT)); document.add(new
		 * Paragraph("INVOICE NO : CT-"
		 * +LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")) + "-"+
		 * String.format("%04d", booking.getBookingId()))
		 * .setFontSize(12).setTextAlignment(TextAlignment.LEFT));
		 */
		// Invoice Header (Date - Right, Invoice Number - Left)
		Table invoiceHeaderTable = new Table(new float[] { 1, 1 });
		invoiceHeaderTable.setWidth(UnitValue.createPercentValue(100));

		invoiceHeaderTable.addCell(new Cell()
				.add(new Paragraph("INVOICE NO : CT-" + LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"))
						+ "-" + String.format("%04d", booking.getBookingId())))
				.setBorder(Border.NO_BORDER).setBold().setTextAlignment(TextAlignment.LEFT));

		invoiceHeaderTable.addCell(new Cell().add(new Paragraph("DATE: " + todayDate)).setBorder(Border.NO_BORDER)
				.setTextAlignment(TextAlignment.RIGHT));

		document.add(invoiceHeaderTable);

		document.add(new Paragraph("\n"));

		// Customer & Booking Details Table
		Table customerTable = new Table(new float[] { 300, 250 });
		customerTable.setWidth(UnitValue.createPercentValue(100));

		customerTable.addCell(
				new Cell().add(new Paragraph("BILL TO").setBold()).setBorder(new SolidBorder(ColorConstants.BLACK, 1)));
		customerTable.addCell(new Cell().add(new Paragraph("OTHER INFORMATION").setBold())
				.setBorder(new SolidBorder(ColorConstants.BLACK, 1)));

		customerTable.addCell(new Cell()
				.add(new Paragraph(
						"Guest Name: " + booking.getGuestFullName() + "\n" + "Email: " + booking.getGuestEmail() + "\n"
								+ "Phone: " + booking.getPhone() + "\n" + "Address: " + booking.getAddress()))
				.setBorder(new SolidBorder(ColorConstants.BLACK, 1)));

		customerTable
				.addCell(
						new Cell()
								.add(new Paragraph("Check-In Date: " + booking.getCheckInDate() + "\n"
										+ "Check-Out Date: " + booking.getCheckOutDate()))
								.setBorder(new SolidBorder(ColorConstants.BLACK, 1)));

		document.add(customerTable);
		document.add(new Paragraph("\n"));

		// Booking Details Table
		Table table = new Table(new float[] { 70, 120, 60, 60, 100, 100 });
		table.setWidth(UnitValue.createPercentValue(100));
		table.setMinHeight(250); // Adjust this value to increase table height

		// Table Header
		table.addHeaderCell(new Cell().add(new Paragraph("Room No").setBold()));
		table.addHeaderCell(new Cell().add(new Paragraph("Guest Name").setBold()));
		table.addHeaderCell(new Cell().add(new Paragraph("Adults").setBold()));
		table.addHeaderCell(new Cell().add(new Paragraph("Children").setBold()));
		table.addHeaderCell(new Cell().add(new Paragraph("Room Type").setBold()));
		table.addHeaderCell(new Cell().add(new Paragraph("Total Amount").setBold()));

		/*
		 * // Assuming `bookings` is a List containing booking data
		 * 
		 * table.addCell(new Cell().add(new
		 * Paragraph(String.valueOf(booking.getRoom().getId()))).setPaddingTop(5));
		 * table.addCell(new Cell().add(new
		 * Paragraph(booking.getGuestFullName())).setPaddingTop(5).setPaddingBottom(5));
		 * table.addCell(new Cell().add(new
		 * Paragraph(String.valueOf(booking.getNumOfAdults()))).setPaddingTop(5)
		 * .setPaddingBottom(5)); table.addCell(new Cell().add(new
		 * Paragraph(String.valueOf(booking.getNumOfChildren()))).setPaddingTop(5)
		 * .setPaddingBottom(5)); table.addCell(new Cell().add(new
		 * Paragraph(String.valueOf(booking.getRoom().getRoomType()))).setPaddingTop(5)
		 * .setPaddingBottom(5)); table.addCell(new Cell().add(new Paragraph("$" +
		 * booking.getAmount())).setPaddingTop(5).setPaddingBottom(5));
		 */
		// Booking Row
		table.addCell(new Cell().add(new Paragraph(String.valueOf(booking.getRoom().getId()))));
		table.addCell(new Cell().add(new Paragraph(booking.getGuestFullName())));
		table.addCell(new Cell().add(new Paragraph(String.valueOf(booking.getNumOfAdults()))));
		table.addCell(new Cell().add(new Paragraph(String.valueOf(booking.getNumOfChildren()))));
		table.addCell(new Cell().add(new Paragraph(String.valueOf(booking.getRoom().getRoomType()))));
		table.addCell(new Cell().add(new Paragraph("$" + booking.getAmount())));
		document.add(table);
		document.add(new Paragraph("\n"));

	//	PdfFont font = PdfFontFactory.createFont(StandardFonts.TIMES_ROMAN, PdfEncodings.UTF8);
		DecimalFormat decimalFormat = new DecimalFormat("#0.00");
		// Total Calculation
		document.add(
				new Paragraph().add(new Text("SUBTOTAL: ").setBold()).add(new Text(" ₹" + booking.getAmount())).setTextAlignment(TextAlignment.RIGHT));
		
		document.add(new Paragraph().add(new Text("TAX (5%): ").setBold()).add(new Text(" ₹" + decimalFormat.format(booking.getAmount() * 0.05))).setTextAlignment(TextAlignment.RIGHT));
				
		document.add(new Paragraph("TOTAL: " + decimalFormat.format(booking.getAmount() * 1.05)).setBold()
				.setTextAlignment(TextAlignment.RIGHT));

		// Footer - Centered at Bottom
		document.showTextAligned(
				new Paragraph("Thanks for staying with us at CT Haven Palace!!").setBold().setFontSize(12),
				pageWidth / 2, // X - Centered horizontally
				60, // Y - Positioned near bottom
				TextAlignment.CENTER);
		// Contact Info in Footer (Just Below "Thank You" Message)
		/*document.showTextAligned(
				new Paragraph("For inquiries, contact us at: +91 9876543210 | Email: info@cthavenpalace.com")
						.setFontSize(10),
				pageWidth / 2, // X - Centered horizontally
				40, // Y - Positioned just below "Thank You" message
				TextAlignment.CENTER);
*/
		document.close();
	}

	public byte[] generateExcelInvoice(Long bookingId) throws IOException {
		Optional<BookedRoom> optionalBooking = bookingRepository.findById(bookingId);
		if (optionalBooking.isEmpty()) {
			throw new IllegalArgumentException("Booking not found");
		}

		BookedRoom booking = optionalBooking.get();
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		Workbook workbook = new XSSFWorkbook();
		Sheet sheet = workbook.createSheet("Invoice");

		Row header = sheet.createRow(0);
		header.createCell(0).setCellValue("Customer Name");
		header.createCell(1).setCellValue("Email");
		header.createCell(2).setCellValue("Room ID");
		header.createCell(3).setCellValue("Check-in Date");
		header.createCell(4).setCellValue("Check-out Date");
		header.createCell(5).setCellValue("Total Amount");

		Row data = sheet.createRow(1);
		data.createCell(0).setCellValue(booking.getGuestFullName());
		data.createCell(1).setCellValue(booking.getGuestEmail());
		data.createCell(2).setCellValue(booking.getRoom().getId());
		data.createCell(3).setCellValue(booking.getCheckInDate().toString());
		data.createCell(4).setCellValue(booking.getCheckOutDate().toString());
		data.createCell(5).setCellValue(booking.getAmount());

		workbook.write(outputStream);
		workbook.close();
		return outputStream.toByteArray();
	}

	public byte[] generateWordInvoice(Long bookingId) throws IOException {
		Optional<BookedRoom> optionalBooking = bookingRepository.findById(bookingId);
		if (optionalBooking.isEmpty()) {
			throw new IllegalArgumentException("Booking not found");
		}

		BookedRoom booking = optionalBooking.get();
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		XWPFDocument document = new XWPFDocument();
		XWPFParagraph paragraph = document.createParagraph();
		XWPFRun run = paragraph.createRun();

		run.setText("Hotel Management Invoice");
		run.addBreak();
		run.setText("Customer: " + booking.getGuestFullName());
		run.addBreak();
		run.setText("Email: " + booking.getGuestEmail());
		run.addBreak();
		run.setText("Room ID: " + booking.getRoom().getId());
		run.addBreak();
		run.setText("Check-in: " + booking.getCheckInDate());
		run.addBreak();
		run.setText("Check-out: " + booking.getCheckOutDate());
		run.addBreak();
		run.setText("Total Amount: $" + booking.getAmount());

		document.write(outputStream);
		document.close();
		return outputStream.toByteArray();
	}
}
