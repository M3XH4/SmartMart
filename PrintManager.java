import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import java.awt.print.Paper;
import java.awt.*;
import java.awt.print.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class PrintManager implements GlobalInterface {
    public static int printer = 0;
    public static PrintService[] printServices = PrintServiceLookup.lookupPrintServices(null, null);

    private static final double inch = 72;
    private static final double width = 3 * inch;
    private static final double height = 2 * inch;

    public static void displayPrinters() {
        GlobalInterface.printBackgroundColor(SECONDARY_COMBO);
        for (int i = 0; i < printServices.length; i++) {
            GlobalInterface.centerString(SECONDARY_COMBO, GlobalInterface.putSpaceGaps(40, (String.format("%d. %s", (i + 1), printServices[i].getName()))));
        }
        GlobalInterface.printBackgroundColor(SECONDARY_COMBO);
    }
    public static void displayCurrentPrinter() {
        GlobalInterface.printHorizontalLine(PRIMARY_COMBO);
        GlobalInterface.printBackgroundColor(PRIMARY_COMBO, " Current Printer: " + printServices[printer].getName());
        GlobalInterface.printHorizontalLine(PRIMARY_COMBO);
    }

    public static void executePrint(Printable printable, double paperHeight) {
        PrinterJob printerJob = PrinterJob.getPrinterJob();

        if (printServices.length > 0) {
            try {
                printerJob.setPrintService(printServices[printer]);
            } catch (PrinterException e) {
                throw new RuntimeException(e);
            }
        } else {
            System.out.println("Error!!! No Printer Found.");
            return;
        }

        PageFormat pageFormat = new PageFormat();
        Paper paper = new Paper();
        double tempHeight = (paperHeight != 0) ? paperHeight : pageFormat.getImageableHeight();
        paper.setSize(width, tempHeight);
        paper.setImageableArea(0, 0, width, tempHeight);
        pageFormat.setPaper(paper);
        pageFormat.setOrientation(PageFormat.PORTRAIT);

        Book book = new Book();
        book.append(printable, pageFormat);
        printerJob.setPageable(book);

        try {
            printerJob.print();
            System.out.println("Receipt printed successfully...");
        } catch (PrinterException e) {
            System.out.println("Error Printing: " + e.getMessage());
        }
    }

    public static void print(ArrayList<Product> shoppingCart, Employee employee, double totalCost, double cash, double change) {
        Printable printable = (graphics, pageFormat, pageIndex) -> {
            if (pageIndex > 0) return Printable.NO_SUCH_PAGE;
            Graphics2D g2D = (Graphics2D) graphics;
            g2D.translate(pageFormat.getImageableX(), pageFormat.getImageableY());
            ReceiptTemplate.receiptShop(g2D, shoppingCart, employee, totalCost, cash, change);
            return Printable.PAGE_EXISTS;
        };
        executePrint(printable, 0);
    }
}

class ReceiptTemplate {
    public static int inch = 72;
    public static int width = 3 * inch;
    public static int centerX = width / 2;

    public static void receiptShop(Graphics2D g2D, ArrayList<Product> shoppingCart, Employee employee, double totalCost, double cash, double change) {
        int leftMargin = 20;
        int topMargin = 30;
        int lineStartMargin = 15;
        int lineEndMargin = 180;
        g2D.setFont(new Font("Bebas Neue", Font.PLAIN, 26));
        g2D.drawString("SmartMart", centerX - g2D.getFontMetrics().stringWidth("SmartMart") / 2, topMargin);
        int dividerY = topMargin + 15;
        g2D.drawLine(lineStartMargin, dividerY, lineStartMargin + lineEndMargin, dividerY);

        g2D.setFont(new Font("Bebas Neue", Font.PLAIN, 16));
        int cashierY = dividerY + 15;
        g2D.drawString("CASHIER: " + employee.getName(), lineStartMargin, cashierY);

        g2D.setFont(new Font("Bebas Neue", Font.PLAIN, 12));
        int divider2Y = cashierY + 5;
        g2D.drawLine(lineStartMargin, divider2Y, lineStartMargin + lineEndMargin, divider2Y);

        int productHeaderY = divider2Y + 15;
        g2D.drawString("PRODUCT", leftMargin + 5, productHeaderY);
        g2D.drawString("QTY", leftMargin + 80, productHeaderY);
        g2D.drawString("PRICE", leftMargin + 140, productHeaderY);

        int tableLineY = productHeaderY + 5;
        g2D.drawLine(lineStartMargin, tableLineY, lineStartMargin + lineEndMargin, tableLineY);

        int productDetailY = tableLineY + 15;
        int index = 0;

        for (Product product : shoppingCart) {
            g2D.drawString(product.getName(), leftMargin + 5, productDetailY);
            g2D.drawString(String.format("%.2f PHP", product.getPrice()) , leftMargin + 130, productDetailY);
            g2D.drawString(String.valueOf(product.getQuantity()), leftMargin + 85, productDetailY);
            productDetailY += 15;
            index++;
        }
        int endTableLineY = productDetailY + 5;
        g2D.drawLine(lineStartMargin, endTableLineY, lineStartMargin + lineEndMargin, endTableLineY);

        int totalCostY = endTableLineY + 25;
        g2D.drawString(String.format("TOTAL COST:           %.2f PHP", totalCost), leftMargin ,totalCostY);
        int cashY = totalCostY + 15;
        g2D.drawString(String.format("CASH:                       %.2f PHP", cash), leftMargin, cashY);
        int changeY = cashY + 15;
        g2D.drawString(String.format("CHANGE:                   %.2f PHP", change), leftMargin, changeY);

        int dividerY2 = changeY + 10;
        g2D.drawLine(lineStartMargin, dividerY2, lineStartMargin + lineEndMargin, dividerY2);

        int purchaseDateY = dividerY2 + 15;
        g2D.drawString("PURCHASE DATE: " + getCurrentDateTime(), centerX - g2D.getFontMetrics().stringWidth(("PURCHASE DATE: " + getCurrentDateTime())) / 2, purchaseDateY);

        int messageY = purchaseDateY + 15;
        g2D.drawString("THANK YOU FOR PURCHASING!", centerX - g2D.getFontMetrics().stringWidth("THANK YOU FOR PURCHASING!") / 2, messageY);
    }

    private static String getCurrentDateTime() {
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy hh:mm a");
        return formatter.format(new Date());
    }
}
