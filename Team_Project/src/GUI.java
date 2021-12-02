import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class GUI extends JFrame {
    private final masterArray master;

    // Main Container
    private final JPanel container;
    static CardLayout c;


    private final JComboBox<String> statsCB;
    private final JComboBox<String> statsRangeCB;

    private final JTextArea area2;

    public GUI(masterArray master) {
        this.master = master;

        //Colours
        Color button = new Color(0, 94, 184);
        Color button2 = new Color(0, 114, 206);
        Color text = new Color(255, 255, 255);

        //JLabels
        //Tab 1
        JLabel titleText1 = new JLabel("NHS Covid-19 Forecaster");
        //Tab 2
        JLabel titleText2 = new JLabel("Forecasting Data");
        //Tab 3
        JLabel titleText3 = new JLabel("Statistics Readout");

        JLabel descText = new JLabel("This tool is used to analyse and read the latest COVID-19 data.");
        JLabel descText2 = new JLabel("You can use it to see statistics on-screen, or you can print them into PDF format.");

        // Setting text fonts
        titleText1.setFont(new Font("Verdana", Font.PLAIN, 15));
        titleText2.setFont(new Font("Verdana", Font.PLAIN, 15));
        titleText3.setFont(new Font("Verdana", Font.PLAIN, 15));
        descText.setFont(new Font("Verdana", Font.PLAIN, 12));
        descText2.setFont(new Font("Verdana", Font.PLAIN, 12));

        //Buttons
        //Tab 1
        JButton genPDF = new JButton("Generate a PDF");
        genPDF.setPreferredSize(new Dimension(160, 40));

        JButton statsReadout1 = new JButton("Statistics Readout");
        statsReadout1.setPreferredSize(new Dimension(140, 40));
        JButton foreData1 = new JButton("Forecasting Data");
        foreData1.setPreferredSize(new Dimension(140, 40));

        //Tab 2
        JButton prediction = new JButton("Turn Off Prediction");
        JButton labels = new JButton("Turn Off Labels");

        JButton statsReadout2 = new JButton("Statistics Readout");
        statsReadout2.setPreferredSize(new Dimension(140, 40));
        JButton homepage1 = new JButton("Homepage");
        homepage1.setPreferredSize(new Dimension(140, 40));

        //Tab 3
        JButton foreData2 = new JButton("Forecasting Data");
        foreData2.setPreferredSize(new Dimension(140, 40));
        JButton homepage2 = new JButton("Homepage");
        homepage2.setPreferredSize(new Dimension(140, 40));

        //Placeholder
        //Placeholders
        new JTextArea(5, 20);
        area2 = new JTextArea(7, 30);

        //Making JFrame
        JFrame f = new JFrame();

        //Create main JPanel
        c = new CardLayout(5, 5);
        container = new JPanel(c);
        Color color1 = new Color(198, 163, 159, 125);
        f.getContentPane().setBackground(color1);

        //JComboBox
        statsCB = new JComboBox<>(new String[]{"England", "Northern Ireland", "Scotland", "Wales", "United Kingdom"});
        statsRangeCB = new JComboBox<>(new String[]{"1", "7", "14", "28"});

        JComboBox<String> timescaleCB = new JComboBox<>(new String[]{"7 Day Forecast", "14 Day Forecast", "28 Day Forecast"});
        JComboBox<String> prevCB = new JComboBox<>(new String[]{"Last Week", "Last Fortnight", "Last Month", "Last 6 Months", "Last Year", "All Time"});
        JComboBox<String> regionCB = new JComboBox<>(new String[]{"England", "Northern Ireland", "Scotland", "Wales", "United Kingdom"});
        regionCB.setSelectedIndex(4);
        JComboBox<String> caseCB = new JComboBox<>(new String[]{"Cases", "Deaths", "Both"});
        caseCB.setSelectedIndex(2);

        //======================================================================================================================================================\\
        // xxxx                                                           TAB ONE                                                                          xxxx \\
        //======================================================================================================================================================\\
        JPanel tab1 = new JPanel(new BorderLayout());

        //Top panel
        JPanel titlePanel1 = new JPanel();
        titlePanel1.add(titleText1);

        //Center panel
        JPanel centerPanel1 = new JPanel(new BorderLayout());

        JPanel miniCenter1 = new JPanel();
        BoxLayout boxlayout = new BoxLayout(miniCenter1, BoxLayout.Y_AXIS);
        miniCenter1.setLayout(boxlayout);
        miniCenter1.add(descText);
        miniCenter1.add(descText2);

        // setting alignments
        descText.setAlignmentX(Component.CENTER_ALIGNMENT);
        descText2.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel miniCenter2 = new JPanel();
        miniCenter2.add(genPDF);

        //Bottom panel
        JPanel bottomPanel1 = new JPanel();
        bottomPanel1.setBorder(new EmptyBorder(4, 0, 4, 0));
        bottomPanel1.add(statsReadout1);
        bottomPanel1.add(foreData1);

        genPDF.addActionListener(e -> PDFGenerator.generatePDF(master, "src/pdf.pdf")); //Calls PDF Generator

        //======================================================================================================================================================\\
        // xxxx                                                                   TAB TWO                                                                  xxxx \\
        //=====================================================================================================================================================\\
        JPanel tab2 = new JPanel(new BorderLayout());

        //Top Panel
        JPanel titlePanel2 = new JPanel();
        titlePanel2.setBorder(new EmptyBorder(0, 0, 6, 0));
        titlePanel2.add(titleText2);

        //Center Panel
        JPanel centerPanel2 = new JPanel(new BorderLayout());

        //miniCenter3 = new JPanel();
        paintGraph graph = new paintGraph(master);
        centerPanel2.add(graph);

        JPanel miniCenter4 = new JPanel(new GridLayout(10, 1, 10, 5));
        miniCenter4.setBorder(new EmptyBorder(5, 5, 5, 5));
        miniCenter4.add(timescaleCB);
        miniCenter4.add(prevCB);
        miniCenter4.add(regionCB);
        miniCenter4.add(caseCB);
        miniCenter4.add(prediction);
        miniCenter4.add(labels);

        //Bottom Panel
        JPanel bottomPanel2 = new JPanel();
        bottomPanel2.setBorder(new EmptyBorder(10, 0, 4, 0));
        bottomPanel2.add(homepage1);
        bottomPanel2.add(statsReadout2);

        timescaleCB.addActionListener(e -> { //Selects between 7, 14 and 28 Days.
            graph.setForecast(timescaleCB.getItemAt(timescaleCB.getSelectedIndex()));
            graph.repaint();
        });

        prevCB.addActionListener(e -> { //Selects between 7 days, 2 weeks, a month, 6 months, a year and all time.
            graph.setPrevious(prevCB.getSelectedIndex());
            graph.repaint();
        });

        regionCB.addActionListener(e -> { //Selects region to display
            graph.setRegionData(master.get(regionCB.getSelectedIndex()));
            graph.repaint();
        });

        caseCB.addActionListener(e -> { //Selects between cases,deaths or both.
            graph.setCaseState(caseCB.getSelectedIndex());
            graph.repaint();
        });

        prediction.addActionListener(e -> { //Selects whether to show predictions or not.
            graph.predictionButton(prediction);
            graph.repaint();
        });

        labels.addActionListener(e -> { //Selects whether to show labels or not.
            graph.labelButton(labels);
            graph.repaint();
        });

        //======================================================================================================================================================\\
        // xxxx                                                                  TAB THREE                                                                 xxxx \\
        //======================================================================================================================================================\\
        JPanel tab3 = new JPanel(new BorderLayout());

        //Top Panel
        JPanel titlePanel3 = new JPanel();
        titlePanel3.setBorder(new EmptyBorder(0, 0, -4, 0));
        titlePanel3.add(titleText3);

        //Center Panel
        JPanel centerPanel3 = new JPanel();

        JPanel miniCenter5 = new JPanel();
        area2.setEditable(false);
        miniCenter5.add(area2);

        JPanel miniCenter6 = new JPanel();
        miniCenter6.add(statsCB);
        miniCenter6.add(statsRangeCB);

        //Bottom Panel
        JPanel bottomPanel3 = new JPanel();
        bottomPanel3.setBorder(new EmptyBorder(10, 0, 4, 0));
        bottomPanel3.add(foreData2);
        bottomPanel3.add(homepage2);

        statsCB.addActionListener(e -> updateStatRead());
        statsRangeCB.addActionListener(e -> updateStatRead());

        //Adding to frame
        //TAB ONE
        container.add(tab1, "One");
        tab1.setVisible(true);
        tab1.add(titlePanel1, BorderLayout.NORTH);

        tab1.add(centerPanel1, BorderLayout.CENTER);
        centerPanel1.add(miniCenter1, BorderLayout.NORTH);
        centerPanel1.add(miniCenter2, BorderLayout.SOUTH);

        tab1.add(bottomPanel1, BorderLayout.SOUTH);

        //TAB TWO
        container.add(tab2, "Two");
        tab2.setVisible(false);
        tab2.add(titlePanel2, BorderLayout.NORTH);

        tab2.add(centerPanel2, BorderLayout.CENTER);
        //centerPanel2.add(miniCenter3, BorderLayout.WEST);
        centerPanel2.add(miniCenter4, BorderLayout.EAST);

        tab2.add(bottomPanel2, BorderLayout.SOUTH);

        //TAB THREE
        container.add(tab3, "Three");
        tab3.setVisible(false);
        tab3.add(titlePanel3, BorderLayout.NORTH);

        tab3.add(centerPanel3, BorderLayout.CENTER);
        centerPanel3.add(miniCenter5, BorderLayout.WEST);
        centerPanel3.add(miniCenter6, BorderLayout.EAST);

        tab3.add(bottomPanel3, BorderLayout.SOUTH);

        //Setting button destinations
        statsReadout1.addActionListener(al -> {
            f.setSize(new Dimension(600, 270));
            c.show(container, "Three");
        });
        statsReadout2.addActionListener(al -> {
            f.setSize(new Dimension(600, 270));
            c.show(container, "Three");
        });

        foreData1.addActionListener(al -> {
            f.setSize(new Dimension(800, 600));
            c.show(container, "Two");
        });
        foreData2.addActionListener(al -> {
            f.setSize(new Dimension(800, 600));
            c.show(container, "Two");
        });

        homepage1.addActionListener(al -> {
            f.setSize(new Dimension(550, 225));
            c.show(container, "One");
        });
        homepage2.addActionListener(al -> {
            f.setSize(new Dimension(550, 225));
            c.show(container, "One");
        });

        // Add main JPanel
        f.add(container);
        c.show(container, "Home");

        //JFrame settings
        f.setTitle("NHS Covid-19 Forecaster");
        //f.pack();
        f.setResizable(false);
        f.setDefaultCloseOperation(EXIT_ON_CLOSE);
        f.setVisible(true);
        f.setSize(new Dimension(550, 225));

        updateStatRead();

        //adds animations to buttons
        List<Component> compList = getAllComponents(tab1.getRootPane());
        compList.addAll(getAllComponents(tab2.getRootPane()));
        compList.addAll(getAllComponents(tab3.getRootPane()));
        for (Component comp : compList) {
            if (comp instanceof JButton) {
                comp.setBackground(button);
                ((JButton) comp).setBorderPainted(false);
                ((JButton) comp).setFocusPainted(false);
                comp.setForeground(text);

                comp.addMouseListener(new MouseAdapter() {
                    public void mouseEntered(MouseEvent e) {
                        comp.setBackground(button2);
                    }

                    public void mouseExited(MouseEvent e) {
                        comp.setBackground(button);
                    }

                });
            }
        }
    }

    //gets all components
    public static List<Component> getAllComponents(Container container) {
        Component[] comps = container.getComponents();
        List<Component> compList = new ArrayList<>();
        for (Component comp : comps){
            compList.add(comp);
            if (comp instanceof Container){
                compList.addAll(getAllComponents((Container) comp));
            }
        }
        return compList;
    }

    void updateStatRead() { //Updates the stats on the stat page.
        area2.setText("");
        Stack strings = statGenerator.getStats(master.get(statsCB.getSelectedIndex()), Integer.parseInt(statsRangeCB.getItemAt(statsRangeCB.getSelectedIndex())));
        while (strings.notEmpty()){
            String line = strings.pop();
            if (!line.equals("*")){
                area2.append(line);
            }
        }
    }


}