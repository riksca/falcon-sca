package org.calontir.marshallate.falcon.print;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.calontir.marshallate.falcon.dto.Authorization;
import org.calontir.marshallate.falcon.dto.Fighter;
import org.calontir.marshallate.falcon.utils.MarshalUtils;

public class CardMaker {

    private static Font largFont = new Font(Font.FontFamily.TIMES_ROMAN, 18f, Font.NORMAL);
    private static Font medFont = new Font(Font.FontFamily.TIMES_ROMAN, 18, Font.NORMAL);
    private static Font normalFont = new Font(Font.FontFamily.TIMES_ROMAN, 12f, Font.NORMAL);
    private static Font smallNormalFont = new Font(Font.FontFamily.TIMES_ROMAN, 10f, Font.NORMAL);
    private static Font smallFont = new Font(Font.FontFamily.TIMES_ROMAN, 8f, Font.NORMAL);
    private static Font smallerFont = new Font(Font.FontFamily.TIMES_ROMAN, 6f, Font.NORMAL);
    private DateTime startDate;
    private DateTime endDate;

    public void build(final OutputStream os, final List<Fighter> data, final DateTime startDate, final DateTime endDate) throws Exception {
        this.startDate = startDate;
        this.endDate = endDate;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Document document = new Document(PageSize.LETTER);
        PdfWriter writer = PdfWriter.getInstance(document, baos);
        document.open();
        addMetaData(document);
        for (Iterator<Fighter> it = data.iterator(); it.hasNext();) {
            Fighter f = it.next();
            writeBackground(writer, document);
            writeToPage(document, f);
            tearOff(writer, document, f);
            if (it.hasNext()) {
                document.newPage();
            }
        }
        document.close();
        baos.writeTo(os);
    }

    private void addMetaData(Document document) {
        document.addTitle("Fighter Card");
        document.addAuthor("Deputy Earl Marshal");
        document.addCreator("Calontir Marshal Project");
    }

    public void writeBackground(PdfWriter writer, Document document) throws BadElementException, DocumentException, IOException {
        Rectangle size = document.getPageSize();
        PdfContentByte cbunder = writer.getDirectContentUnder();
        Image img = loadBackground();
        if (img != null) {
            img.setAbsolutePosition(36, 36);
            //img.scaleAbsoluteWidth(size.getRight());
            //img.scaleAbsoluteHeight(size.getTop());
            cbunder.addImage(img);
        }

    }

    private Image loadBackground() throws BadElementException, IOException {
        String name = "background.jpg";

        return loadImage(name);
    }

    private Image loadSignature() throws BadElementException, IOException {
        return loadImage("sig.png");
    }

    private Image loadImage(String filename) throws BadElementException, IOException {

        URL url = getClass().getResource(filename);
        if (url == null) {
            Logger.getLogger(CardMaker.class.getName()).log(Level.FINE,
                    String.format("Didn't find as %s, looking for /%s", filename, filename));
            url = getClass().getResource("/" + filename);
        }
        if (url != null) {
            Logger.getLogger(CardMaker.class.getName()).log(Level.FINE, "Found image, loading");
            return Image.getInstance(url);
        }

        Logger.getLogger(CardMaker.class.getName()).log(Level.SEVERE,
                String.format("Could not get %s from classpath", filename));

        return null;
    }

    private void writeToPage(Document document, Fighter fighter) throws DocumentException {
        Rectangle size = document.getPageSize();
        PdfPTable table = new PdfPTable(1);
        PdfPCell cell;
        float padding = 65.5f;

        table.setTotalWidth(size.getRight());
        cell = new PdfPCell(new Phrase("\n\n\n\n\n\n", largFont));
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("The Society for Creative Anachronism, Inc\nKingdom of Calontir\n", largFont));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setPaddingLeft(padding);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Combat Authorizations", normalFont));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setPaddingLeft(padding);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);

        String p1 = String.format("Let it be known that %s, also known in the modern world as %s "
                + "hailing from the lands of %s, is Authorized to fight within the Kingdom of Calontir "
                + "using the following weapon systems: %s. This combatant by age %s a minor according to modern law.",
                fighter.getScaName(),
                fighter.getModernName(),
                fighter.getScaGroup().getGroupName(),
                MarshalUtils.getAuthDescriptionAsString(fighter.getAuthorization()),
                MarshalUtils.isMinor(fighter) ? "is" : "is not");
        cell = new PdfPCell(new Phrase(p1, normalFont));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setPaddingLeft(padding);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("This writ is your authorization to participate on the field at SCA "
                + "activities.  It must be presented to the list officials at all SCA events to register for "
                + "participation in any martial activity. You may be required to present "
                + "this writ at any time, and to any marshal upon request.", normalFont));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setPaddingLeft(padding);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(String.format("This writ is valid between the dates of %s and %s, Gregorian",
                startDate.toString("MM/dd/yyyy"), endDate.toString("MM/dd/yyyy")), normalFont));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setPaddingLeft(padding);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(String.format("Signed and Authorized by the hand of Honorable Lord Jawhar ibn Akmel el Ghazi, the Calontir Marshal of Cards on this day %s.\n\n",
                new DateTime().toString("MM/dd/yyyy")), normalFont));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setPaddingLeft(padding);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
        table.addCell(cell);

        try {
            Image sig = loadSignature();
            float orgHeight = sig.getHeight();
            float percent = (33 / orgHeight) * 100;
            sig.scalePercent(percent);
            cell = new PdfPCell(sig);
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setPaddingLeft(padding);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setPaddingBottom(0);
            cell.setBorderWidthBottom(0);
            cell.setFixedHeight(33);
            table.addCell(cell);
        } catch (BadElementException ex) {
            Logger.getLogger(CardMaker.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(CardMaker.class.getName()).log(Level.SEVERE, null, ex);
        }

        cell = new PdfPCell(new Phrase(String.format("\n%s  Signature __________________________",
                fighter.getModernName()), normalFont));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setPaddingLeft(padding);
        //cell.setPaddingRight(padding);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
        table.addCell(cell);

        document.add(table);
    }

    private void tearOff(PdfWriter writer, Document document, Fighter fighter) throws BadElementException, DocumentException {
        PdfContentByte cb = writer.getDirectContent();
        PdfPTable table = new PdfPTable(2);
        table.setTotalWidth(288);
        PdfPCell cell;

        // Back of card
        PdfPTable back = new PdfPTable(3);
        back.setWidths(new float[]{1, 3, 1});  //the middle number controls the QR code size
        back.setTotalWidth(144);
        Paragraph p = new Paragraph();
        p.add(new Phrase("Kingdom Specific Authorizations\n", smallFont));
        p.add(new Phrase(MarshalUtils.getAuthsAsString(fighter.getAuthorization()), smallFont));
        cell = new PdfPCell(p);
        cell.setBorder(Rectangle.TOP + Rectangle.LEFT + Rectangle.BOTTOM);
        cell.setRotation(-90);
        cell.setFixedHeight(250.0f);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        back.addCell(cell);

        PdfPTable middleTable = new PdfPTable(2);
        middleTable.setWidths(new int[]{3, 2});

        PdfPCell innerCell = new PdfPCell();
        innerCell.setNoWrap(false);   //true
        innerCell.setVerticalAlignment(Element.ALIGN_TOP);

        p = new Paragraph();
        if (StringUtils.isBlank(fighter.getScaName())) {
            p.add(new Phrase("\nSCA Name: _________________________\n", smallNormalFont));
        } else {
            p.add(new Phrase(String.format("\nSCA Name: %s\n", fighter.getScaName()), smallNormalFont));
        }
        p.add(new Phrase(String.format("Modern Name: %s\n", fighter.getModernName()), smallNormalFont));
        if (MarshalUtils.isMinor(fighter)) {
            p.add(new Phrase(String.format("Group: %s  Minor X\n", fighter.getScaGroup().getGroupName()), smallNormalFont));
        } else {
            p.add(new Phrase(String.format("Group: %s\n", fighter.getScaGroup().getGroupName()), smallNormalFont));
        }
        innerCell.setPhrase(p);
        innerCell.setBorder(Rectangle.NO_BORDER);
        middleTable.addCell(innerCell);

        StringBuilder sb = new StringBuilder(fighter.getScaName());
        sb.append(" - ");
        for (Iterator<Authorization> it = fighter.getAuthorization().iterator(); it.hasNext();) {
            sb.append(it.next().getCode());
            if (it.hasNext()) {
                sb.append(", ");
            }
        }

        innerCell = new PdfPCell();
        BarcodeQRCode qrcode = new BarcodeQRCode(sb.toString(), 1, 1, null);
        Image img = qrcode.getImage();
        img.setAlignment(Image.RIGHT);
        innerCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        innerCell.setBorder(Rectangle.NO_BORDER);
        innerCell.setPadding(0f);
        innerCell.setPaddingRight(1.5f);
        innerCell.setImage(img);
        middleTable.addCell(innerCell);

        cell = new PdfPCell(middleTable);
        cell.setBorder(Rectangle.TOP + Rectangle.BOTTOM);
        cell.setRotation(-90);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        back.addCell(cell);

        cell = new PdfPCell(new Phrase("The Society for Creative Anachronism, Inc\nKingdom of Calontir\nCombat Authorization Card", smallFont));
        cell.setExtraParagraphSpace(1.5f);
        cell.setBorder(Rectangle.BOTTOM + Rectangle.TOP + Rectangle.RIGHT);
        cell.setRotation(-90);
        cell.setFixedHeight(50.0f);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        back.addCell(cell);

        table.addCell(back);

        // Front of card
        PdfPTable front = new PdfPTable(4);
        front.setTotalWidth(144);

        PdfPTable headerTable = new PdfPTable(3);
        headerTable.setWidths(new int[]{1, 4, 1});

        innerCell = new PdfPCell();
        try {
            Image calontrava = loadImage("calontrava_black.gif");
            calontrava.setAlignment(Image.RIGHT | Image.TEXTWRAP);
            //calontrava.setAbsolutePosition(350, 90);
            calontrava.scaleToFit(40f, 40f);
            innerCell.addElement(calontrava);
            innerCell.setBorder(Rectangle.NO_BORDER);
            headerTable.addCell(innerCell);
        } catch (BadElementException | IOException ex) {
            Logger.getLogger(CardMaker.class.getName()).log(Level.SEVERE, null, ex);
        }

        innerCell = new PdfPCell(new Phrase("Kingdom of Calontir\nFighter Authorization Card\n\n", smallFont));
        innerCell.setExtraParagraphSpace(1.5f);
        innerCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        innerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        innerCell.setBorder(Rectangle.NO_BORDER);
        headerTable.addCell(innerCell);

        innerCell = new PdfPCell();
        innerCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        innerCell.setBorder(Rectangle.NO_BORDER);
        innerCell.addElement(new Phrase());
        headerTable.addCell(innerCell);

        cell = new PdfPCell(headerTable);
        cell.setRotation(90);
        cell.setBorder(Rectangle.TOP + Rectangle.LEFT + Rectangle.BOTTOM);
        cell.setFixedHeight(250.0f);
        front.addCell(cell);

        p = new Paragraph();
        if (StringUtils.isBlank(fighter.getScaName())) {
            p.add(new Phrase("SCA Name: _________________________\n", smallFont));
        } else {
            p.add(new Phrase(String.format("SCA Name: %s\n", fighter.getScaName()), smallFont));
        }
        p.add(new Phrase(String.format("Date Issued: %s  Expires: %s\n", startDate.toString("MM/dd/yyyy"), endDate.toString("MM/dd/yyyy")), smallFont));
        p.add(new Phrase(String.format("Issuing Official: %s\n", "Honorable Lord Jawhar ibn Akmel el Ghazi"), smallFont));
        cell = new PdfPCell(p);
        //cell.setExtraParagraphSpace(1.5f);
        cell.setBorder(Rectangle.TOP + Rectangle.BOTTOM);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell.setRotation(90);
        cell.setFixedHeight(250.0f);
        front.addCell(cell);

        cell = new PdfPCell(new Phrase("This card is your authorization to participate on the field at SCA activities.  It must be presented to the list officials at all SCA events to register for participation in any martial activity. You may be requested to show this card to any marshal and/or list official at any time.", smallerFont));
        cell.setBorder(Rectangle.TOP + Rectangle.BOTTOM);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell.setFixedHeight(250.0f);
        cell.setRotation(90);
        front.addCell(cell);

        cell = new PdfPCell(new Phrase("Signature: _________________________\n", smallFont));
        cell.setBorder(Rectangle.BOTTOM + Rectangle.TOP + Rectangle.RIGHT);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setFixedHeight(250.0f);
        cell.setRotation(90);
        front.addCell(cell);

        table.addCell(front);

        table.writeSelectedRows(0, -1, 200, 300, cb);
    }
}
