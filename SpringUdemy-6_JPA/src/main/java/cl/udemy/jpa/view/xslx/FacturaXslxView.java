package cl.udemy.jpa.view.xslx;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.document.AbstractXlsxView;

import com.lowagie.text.Phrase;

import cl.udemy.jpa.modelo.Factura;
import cl.udemy.jpa.modelo.ItemFactura;

@Component("factura/ver.xlsx")
public class FacturaXslxView extends AbstractXlsxView {

	@Override
	protected void buildExcelDocument(Map<String, Object> model, Workbook workbook, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		response.setHeader("Content-Disposition","attachment; filename=\"factura_view.xlsx\"");
		Factura factura = (Factura)model.get("factura");
		Sheet sheet = workbook.createSheet("Factura Spring");
		
		MessageSourceAccessor mensajes = getMessageSourceAccessor();
		
		Row row = sheet.createRow(0);
		Cell cell = row.createCell(0);
		
		cell.setCellValue(mensajes.getMessage("text.factura.ver.datos.cliente"));
		row = sheet.createRow(1);
		cell = row.createCell(0);
		cell.setCellValue(factura.getCliente().getNombre() + " " + factura.getCliente().getApellido());
		
		row = sheet.createRow(2);
		cell = row.createCell(0);
		cell.setCellValue(factura.getCliente().getEmail());
		
		sheet.createRow(4).createCell(0).setCellValue(mensajes.getMessage("text.factura.ver.datos.factura"));
		sheet.createRow(5).createCell(0).setCellValue(mensajes.getMessage("text.cliente.factura.folio") + ": " + factura.getId());
		sheet.createRow(6).createCell(0).setCellValue(mensajes.getMessage("text.cliente.factura.folio") + ": " + factura.getId());
		sheet.createRow(7).createCell(0).setCellValue(mensajes.getMessage("text.cliente.factura.fecha") + ": " + factura.getCreateAt());
		
		CellStyle headerStyle = workbook.createCellStyle();
		headerStyle.setBorderBottom(BorderStyle.MEDIUM);
		headerStyle.setBorderTop(BorderStyle.MEDIUM);
		headerStyle.setBorderRight(BorderStyle.MEDIUM);
		headerStyle.setBorderLeft(BorderStyle.MEDIUM);
		headerStyle.setFillForegroundColor(IndexedColors.SKY_BLUE.index);
		headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		
		
		CellStyle bodyStyle = workbook.createCellStyle();
		bodyStyle.setBorderBottom(BorderStyle.THIN);
		bodyStyle.setBorderTop(BorderStyle.THIN);
		bodyStyle.setBorderRight(BorderStyle.THIN);
		bodyStyle.setBorderLeft(BorderStyle.THIN);
		
		Row header = sheet.createRow(9);
		header.createCell(0).setCellValue(mensajes.getMessage("text.factura.form.item.nombre"));
		header.createCell(1).setCellValue(mensajes.getMessage("text.factura.form.item.precio"));
		header.createCell(2).setCellValue(mensajes.getMessage("text.factura.form.item.cantidad"));
		header.createCell(3).setCellValue(mensajes.getMessage("text.factura.form.item.total")); 
		
		header.getCell(0).setCellStyle(headerStyle);
		header.getCell(1).setCellStyle(headerStyle);
		header.getCell(2).setCellStyle(headerStyle);
		header.getCell(3).setCellStyle(headerStyle);
		
		int rowNum = 10;
		for (ItemFactura item : factura.getItems()) {
			
			Row fila = sheet.createRow(rowNum++);
			cell= fila.createCell(0);
			cell.setCellValue(item.getProducto().getNombre());
			cell.setCellStyle(bodyStyle);
			
			cell= fila.createCell(1);
			cell.setCellValue(item.getProducto().getPrecio());
			cell.setCellStyle(bodyStyle);
			
			cell = fila.createCell(2);
			fila.createCell(2).setCellValue(item.getCantidad());
			cell.setCellStyle(bodyStyle);
			
			cell = fila.createCell(3);
			fila.createCell(3).setCellValue(item.calcularImporte());
			cell.setCellStyle(bodyStyle);
		}
		
		Row filaTotal = sheet.createRow(rowNum);
		cell = filaTotal.createCell(2);
		cell.setCellValue(mensajes.getMessage("text.factura.form.total"));
		cell.setCellStyle(bodyStyle);
		
		cell = filaTotal.createCell(3);
		cell.setCellValue(factura.getTotal());
		cell.setCellStyle(bodyStyle);
	}

}
