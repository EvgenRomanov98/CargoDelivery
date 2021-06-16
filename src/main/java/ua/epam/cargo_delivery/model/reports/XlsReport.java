package ua.epam.cargo_delivery.model.reports;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import ua.epam.cargo_delivery.exceptions.AppException;
import ua.epam.cargo_delivery.model.db.Delivery;
import ua.epam.cargo_delivery.model.db.DeliveryManager;

import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

public class XlsReport {
    private static final Logger log = LogManager.getLogger(XlsReport.class);
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static ByteArrayOutputStream genXls(String fromRegion, String toRegion, String createDateStr, String deliveryDateStr) {
        try (Workbook book = new HSSFWorkbook();
             ByteArrayOutputStream baos = new ByteArrayOutputStream()) {

            Long fromRegionId = Optional.ofNullable(fromRegion).filter(s -> !s.isBlank())
                    .map(Long::parseLong)
                    .orElse(null);
            Long toRegionId = Optional.ofNullable(toRegion).filter(s -> !s.isBlank())
                    .map(Long::parseLong)
                    .orElse(null);
            LocalDate createDate = createDateStr == null || createDateStr.isBlank() ? null :
                    LocalDate.parse(createDateStr, DATE_FORMAT);
            LocalDate deliveryDate = deliveryDateStr == null || deliveryDateStr.isBlank() ? null :
                    LocalDate.parse(deliveryDateStr, DATE_FORMAT);

            List<Delivery> deliveries = DeliveryManager.findDeliveriesForReport(fromRegionId, toRegionId,
                    createDate, deliveryDate);
            Sheet sheet = book.createSheet("Deliveries");
            int cellNum = 0;
            Row rowHeader = sheet.createRow(0);
            createHeader(cellNum, rowHeader);
            for (int i = 0; i < deliveries.size(); i++) {
                Row row = sheet.createRow(i + 1);
                cellNum = 0;
                createCell(cellNum++, row, deliveries.get(i).getId());
                createCell(cellNum++, row, deliveries.get(i).getCargo().getDescription());
                createCell(cellNum++, row, deliveries.get(i).getFromName());
                createCell(cellNum++, row, deliveries.get(i).getWhence());
                createCell(cellNum++, row, deliveries.get(i).getToName());
                createCell(cellNum++, row, deliveries.get(i).getWhither());
                createDateCell(cellNum++, row, deliveries.get(i).getCreateDate(), book.createDataFormat(), book.createCellStyle());
                createDateCell(cellNum++, row, deliveries.get(i).getDeliveryDate(), book.createDataFormat(), book.createCellStyle());
                createCell(cellNum++, row, deliveries.get(i).getDistance());
                createCell(cellNum++, row, deliveries.get(i).getPrice());
                createCell(cellNum++, row, deliveries.get(i).getCargo().getWeight());
                createCell(cellNum++, row, deliveries.get(i).getCargo().getHeight());
                createCell(cellNum++, row, deliveries.get(i).getCargo().getLength());
                createCell(cellNum++, row, deliveries.get(i).getCargo().getWidth());
                createCell(cellNum++, row, deliveries.get(i).getUser().getName());
                createCell(cellNum++, row, deliveries.get(i).getUser().getSurname());
                createCell(cellNum++, row, deliveries.get(i).getUser().getEmail());
                createCell(cellNum, row, deliveries.get(i).getUser().getPhone());
            }
            for (int i = 0; i <= cellNum; i++) {
                sheet.autoSizeColumn(i);
            }
            book.write(baos);
            return baos;
        } catch (AppException e) {
            throw e;
        } catch (Exception e) {
            String message = "Can't create xls report";
            log.error(message, e);
            throw new AppException(message, e);
        }
    }

    private static void createHeader(int cellNum, Row rowHeader) {
        createCell(cellNum++, rowHeader, "Id");
        createCell(cellNum++, rowHeader, "Description");
        createCell(cellNum++, rowHeader, "From");
        createCell(cellNum++, rowHeader, "From location");
        createCell(cellNum++, rowHeader, "To");
        createCell(cellNum++, rowHeader, "To location");
        createCell(cellNum++, rowHeader, "Create date");
        createCell(cellNum++, rowHeader, "Delivery date");
        createCell(cellNum++, rowHeader, "Distance");
        createCell(cellNum++, rowHeader, "Price");
        createCell(cellNum++, rowHeader, "Weight");
        createCell(cellNum++, rowHeader, "Height");
        createCell(cellNum++, rowHeader, "Length");
        createCell(cellNum++, rowHeader, "Width");
        createCell(cellNum++, rowHeader, "Customer name");
        createCell(cellNum++, rowHeader, "Customer surname");
        createCell(cellNum++, rowHeader, "Customer email");
        createCell(cellNum, rowHeader, "Customer phone");
    }

    private static void createCell(int cellNum, Row row, Object value) {
        Cell cell = row.createCell(cellNum);
        cell.setCellValue(value.toString());
    }

    private static void createDateCell(int cellNum, Row row, LocalDate value, DataFormat format, CellStyle dateStyle) {
        Cell cell = row.createCell(cellNum);
        dateStyle.setDataFormat(format.getFormat("yyyy-MM-dd"));
        cell.setCellStyle(dateStyle);
        cell.setCellValue(value);
    }
}
