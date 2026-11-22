package dulcebrioche;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

import java.io.File;
import java.io.FileOutputStream;
import java.awt.Desktop;
import java.util.List;

import java.awt.Desktop;
import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

public class GenerarPDF {

    public static void generarRecibo(int idPedido, List<ItemCarrito> detalle, double total)
    {
        try
        {
            String carpeta =System.getProperty("user.home") + "\\Documents\\RecibosDulceBrioche";

            File directorio = new File(carpeta);

            if(!directorio.exists())
            {
                directorio.mkdirs();
            }

            String ruta = carpeta + "\\ReciboPedido_" + idPedido + ".pdf";

            Document documento = new Document();

            PdfWriter.getInstance(documento, new FileOutputStream(ruta));

            documento.open();

            Font titulo = FontFactory.getFont(FontFactory.HELVETICA_BOLD,18);

            Paragraph encabezado = new Paragraph("DULCE BRIOCHE",titulo);

            encabezado.setAlignment(Element.ALIGN_CENTER);

            documento.add(encabezado);

            documento.add(new Paragraph(" "));
            documento.add(new Paragraph("Pedido #" + idPedido));

            documento.add(new Paragraph("Cliente: "+ Sesion.nombreUsuario));

            documento.add(new Paragraph(" "));

            PdfPTable tabla = new PdfPTable(4);

            tabla.setWidthPercentage(100);

            tabla.addCell("Producto");
            tabla.addCell("Precio");
            tabla.addCell("Cantidad");
            tabla.addCell("Subtotal");

            for(ItemCarrito item : detalle)
            {
                tabla.addCell(item.Nombre);

                tabla.addCell("$"+String.format("%.2f",item.Precio));

                tabla.addCell(String.valueOf(item.Cantidad));

                tabla.addCell("$" +String.format("%.2f",item.getSubtotal()));
            }

            documento.add(tabla);

            documento.add(new Paragraph(" "));

            documento.add(new Paragraph("TOTAL: $"+ String.format("%.2f",total)));

            documento.add(new Paragraph("Estado: Pendiente"));

            documento.close();

            Desktop.getDesktop().open(new File(ruta));
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
}