package java2ddrawingapplication;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Paint;
import java.awt.Point;
import java.awt.Stroke;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;


public class DrawingApplicationFrame extends JFrame
{
static ArrayList<MyShapes>art;
    // Create the panels for the top of the application. One panel for each
    // line and one to contain both of those panels.
    JPanel line1 = new JPanel();
    JPanel line2 = new JPanel();
    JPanel topPanel = new JPanel();
   
    // create the widgets for the firstLine Panel.
    private static JButton undo;
    private static JButton clear;
    private static JCheckBox filled;
    private JLabel shape;
    String[] options = {"Line","Rectangle", "Oval"};
    private static JComboBox<String> chosenshape;
    
    //create the widgets for the secondLine Panel.
    private static JCheckBox Grad, Dash;
    private static JButton color1,color2; 
    private JLabel length, width;
    private JTextField lineW, dashL; 
   
    // Variables for drawPanel.
    private DrawPanel dpanel = new DrawPanel();
    private Color c1 = Color.BLUE;
    private Color c2 = Color.GREEN;
    MyShapes current;// holds current shape chosen
    BasicStroke stroke;
    // add status label
    JLabel stat = new JLabel();
    // Constructor for DrawingApplicationFrame
    public DrawingApplicationFrame()
    {   super("Java 2D Drawings");
        // add widgets to panels
        
        // firstLine widgets
        undo = new JButton("Undo");
        undo.addActionListener(new ButtonTask());
        clear = new JButton("Clear");
        undo.addActionListener(new ButtonTask());
        filled = new JCheckBox("Filled");
        shape = new JLabel("Shape");
        chosenshape = new JComboBox(options);
        // adding ^ to first panel
        line1.add(undo);
        line1.add(clear);
        line1.add(shape);
        line1.add(chosenshape);
        line1.add(filled);
        
        // secondLine widgets
        Grad = new JCheckBox("Use Gradient");
        Dash = new JCheckBox("Dashed");
        color1 = new JButton("1st Color...");
        color1.addActionListener(new ButtonTask());
        color2 = new JButton("2nd Color...");
        color2.addActionListener(new ButtonTask());
        width = new JLabel("Line Width");
        length = new JLabel("Dash Length");
        lineW = new JTextField("10",2);
        dashL = new JTextField("15",2);
       
        //adding ^ to second panel 
      line2.add(Grad);
       line2.add(color1);
       line2.add(color2);
       line2.add(width);
      line2.add(length);
       line2.add(lineW);
       line2.add(dashL);
       line2.add(Dash);
       
    // add top panel of two panels
        topPanel.setLayout(new BorderLayout());
        topPanel.add(line1,BorderLayout.NORTH);
        topPanel.add(line2,BorderLayout.SOUTH);
        
    // add topPanel to North, drawPanel to Center, and statusLabel to South
        add(topPanel,BorderLayout.NORTH);
        add(dpanel, BorderLayout.CENTER);
        add(stat,BorderLayout.SOUTH);
        dpanel.setBackground(Color.WHITE);
        //add listeners and event handlers
    }

    // Create event handlers, if needed
    public class ButtonTask implements ActionListener{
    @Override
        public void actionPerformed(ActionEvent event){
            //getActionCommand returns string of button a causes following if statement to run
            if (event.getActionCommand() == "Clear") 
                art.clear();repaint();
            
            if (event.getActionCommand() == "Undo")
                if (art.size() != 0)
                    art.remove(art.size()-1);repaint();
            
            if (event.getActionCommand() == "1st Color...")
                c1 = JColorChooser.showDialog(DrawingApplicationFrame.this,
                    "pick a color", c1);
                if (c1 == null){
                    c1 = Color.GRAY;
                }
            if (event.getActionCommand() == "2nd Color...")
                c2 = JColorChooser.showDialog(DrawingApplicationFrame.this,
                    "pick a color", c2);
                if (c2 == null){
                    c2 = Color.GRAY; 
                        }
            
        }
    }

    // Create a private inner class for the DrawPanel.
    private class DrawPanel extends JPanel
    {

        public DrawPanel()
        {
            addMouseListener(new MouseHandler());
            addMouseMotionListener(new MouseHandler());
            art = new ArrayList<MyShapes>();
        }

        public void paintComponent(Graphics g)
        {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;

            //loop through and draw each shape in the shapes arraylist
            for (MyShapes x:art){
            x.draw(g2d);
            }
        }


        private class MouseHandler extends MouseAdapter implements MouseMotionListener
        {

            public void mousePressed(MouseEvent event)
            { 
                
            int Lwidth = Integer.parseInt(lineW.getText());
            float[] dash ={Float.parseFloat(dashL.getText())};
                    
                 if (Dash.isSelected()){
                     stroke = new BasicStroke(Lwidth, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 10, dash, 0);
            } 
                else
            {
             stroke = new BasicStroke( Lwidth, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
            }
                 String position = "(" + event.getPoint().x +"," +event.getPoint().y+
                         ")";
            stat.setText(position);
            
            Paint p;
            
            if (Grad.isSelected()){
                p = new GradientPaint(0,0,c1,50,50,c2,true);
            }
            else{
            p = c1;
                }
            
            String pickedShape = (String)chosenshape.getSelectedItem();
            MyShapes Shape;
            
            if (pickedShape == "Line"){
       Shape = new MyLine(event.getPoint(),event.getPoint(),p,stroke);
            }
            else if (pickedShape == "Oval"){
            Shape = new MyOval(event.getPoint(),event.getPoint(),p,stroke, filled.isSelected());
            }
            else{
            Shape = new MyRectangle(event.getPoint(),event.getPoint(),p,stroke,filled.isSelected());
            }
            
            art.add(Shape);
            
            }

            public void mouseReleased(MouseEvent event)
            {
             String position = "(" + event.getPoint().x +"," +event.getPoint().y+
                         ")";
            stat.setText(position);
            }
            
            @Override
            public void mouseDragged(MouseEvent event)
            {
             art.get(art.size()-1).setEndPoint(event.getPoint());
             repaint();
                String position = "(" + event.getPoint().x +"," +event.getPoint().y+
                         ")";
            stat.setText(position);
            }

            @Override
            public void mouseMoved(MouseEvent event)
            {
             String position = "(" + event.getPoint().x +"," +event.getPoint().y+
                         ")";
            stat.setText(position);
            }
        }

    }
}
