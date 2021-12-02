import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;

public class PDFGenerator {
    public static void generatePDF(masterArray master, String file){
        var document = new Document(); //Creates New Document

        try { //This opens the PDF and tell it to retrieve the strings needed to write it, writes it then closes the document.
            PdfWriter.getInstance(document, new FileOutputStream(file));
            System.out.println("PDF has been generated");
            document.open();
            document.addTitle("NHS Covid-19 Data");
            document.newPage();

            ArrayList<Paragraph> paraList = new ArrayList<>();

            paraList.add(new Paragraph("Covid-19 Report For " + master.getE().getLast().getDate()));
            paraList.add(new Paragraph("by Team 3"));
            paraList.add(new Paragraph("*"));

            paraList.addAll(generateRegions(master, 1));
            paraList.addAll(generateRegions(master, 7));
            paraList.addAll(generateRegions(master, 14));
            paraList.addAll(generateRegions(master, 28));


            for (Paragraph p: paraList){
                if (p.toString().equals("[*]")){
                    document.add( Chunk.NEWLINE );
                }
                else
                {
                    p.setAlignment(Paragraph.ALIGN_CENTER);
                    document.add(p);
                }
            }

            document.close();

        } catch (FileNotFoundException | DocumentException e) {
            e.printStackTrace();
        }
    }

    private static ArrayList<Paragraph> generateRegions(masterArray master, Integer interval){ //Utilises stat generator in order to print out paragraph data.
        ArrayList<Paragraph> paraList = new ArrayList<>();
        DataList regionData = master.getE();
        master.reset();

        paraList.add(new Paragraph(interval + " Day Summary"));
        paraList.add(new Paragraph("*"));

        for (int i = 0; i < 5; i++){ //Iterates through all regions.
            Stack strings = statGenerator.getStats(regionData, interval);
            while (strings.notEmpty()) {
                paraList.add(new Paragraph(strings.pop()));
            }
            regionData = master.getNext();
        }

        return paraList;
    }


}
