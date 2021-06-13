package ua.epam.cargo_delivery.model.reports;

import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.epam.cargo_delivery.exceptions.AppException;
import ua.epam.cargo_delivery.model.db.Delivery;
import ua.epam.cargo_delivery.model.db.DeliveryManager;
import ua.epam.cargo_delivery.model.db.User;

import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class PdfReceipt {
    private static final Logger log = LogManager.getLogger(PdfReceipt.class);
    private static final DateTimeFormatter FORMAT_LOCAL_DATE_TIME = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final Font BOLD = new Font(Font.TIMES_ROMAN, 16, Font.BOLD);

    public static ByteArrayOutputStream genPdf(long idDelivery, User user) {
        Delivery d = DeliveryManager.findDeliveryForUser(idDelivery, user);
        try (Document document = new Document(PageSize.A5, 50, 50, 50, 50);
             ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            PdfWriter.getInstance(document, baos);
            document.open();

            Paragraph company = new Paragraph("123 Delivery", BOLD);
            company.setAlignment(ElementTags.ALIGN_CENTER);
            document.add(company);

            LocalDateTime now = LocalDateTime.now();
            Paragraph info = new Paragraph("Receipt from: " + now.format(FORMAT_LOCAL_DATE_TIME));
            info.setAlignment(Element.ALIGN_RIGHT);
            document.add(info);

            Paragraph name = new Paragraph("Customer: " + user.getName() + " " + user.getSurname());
            name.setAlignment(Element.ALIGN_RIGHT);
            document.add(name);

            Paragraph id = new Paragraph("Delivery id: " + d.getId());
            id.setAlignment(Element.ALIGN_RIGHT);
            id.setSpacingAfter(5);
            document.add(id);


            PdfPTable table = new PdfPTable(2);
            table.setWidthPercentage(100);
            table.addCell(getCell("status"));
            table.addCell(getCell(d.getStatus().toString()));
            table.addCell(getCell("price"));
            table.addCell(getCell(d.getPrice().toString()));
            table.addCell(getCell("Distance"));
            table.addCell(getCell(d.getDistance().toString()));
            table.addCell(getCell("DeliveryDate"));
            table.addCell(getCell(d.getDeliveryDate().toString()));
            table.addCell(getCell("From"));
            table.addCell(getCell(d.getFromName()));
            table.addCell(getCell("To"));
            table.addCell(getCell(d.getToName()));
            table.addCell(getCell("From (latitude, longitude)"));
            table.addCell(getCell(d.getWhence()));
            table.addCell(getCell("To (latitude, longitude)"));
            table.addCell(getCell(d.getWhither()));
            document.add(table);

            Paragraph footer = new Paragraph("Thanks for choosing us!");
            footer.setAlignment(Element.ALIGN_CENTER);
            footer.setSpacingBefore(10);
            document.add(footer);
            return baos;
        } catch (Exception e) {
            String message = "Fail get Receipt";
            log.error(message, e);
            throw new AppException(message, e);
        }
    }

    private static PdfPCell getCell(String value) {
        PdfPCell cell = new PdfPCell();
        cell.setBorderWidth(1);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.addElement(new Paragraph(value));
        return cell;
    }
}
