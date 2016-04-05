package cz.parser;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

public class Main {
	private List<Entity> ListOfEntities; 
    private final Map<String, ImageIcon> imageMap;
    
    private String [] getNameList(){
    	String [] s = new String[5];
    	for(int i = 0; i < 5; i++){
    		s[i] = "Link: " + ListOfEntities.get(i).getLink();
    		if( ListOfEntities.get(i).isPositive() ){
    			s[i] += "                       pozitivni feedback";
    		}else{
    			s[i] += "                       negativni feedback";
    		}
    	}
    	return s;
    }    
    public Main(String s) {
    	//ListOfEntities = new ArrayList<>();
    	ParserRun p = new ParserRun();
    	ListOfEntities = p.run(s);
    	
        String[] nameList = getNameList();
        imageMap = createImageMap(nameList);
        JList list = new JList(nameList);
        list.setCellRenderer(new MarioListRenderer());

        JScrollPane scroll = new JScrollPane(list);
        scroll.setPreferredSize(new Dimension(500, 500));

        JFrame frame = new JFrame();
        frame.add(scroll);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public class MarioListRenderer extends DefaultListCellRenderer {

        Font font = new Font("helvitica", Font.BOLD, 12);

        @Override
        public Component getListCellRendererComponent(
                JList list, Object value, int index,
                boolean isSelected, boolean cellHasFocus) {

            JLabel label = (JLabel) super.getListCellRendererComponent(
                    list, value, index, isSelected, cellHasFocus);
            label.setIcon(imageMap.get((String) value));
            
            label.setHorizontalTextPosition(JLabel.RIGHT);
            label.setFont(font);
            return label;
        }
    }

    private Map<String, ImageIcon> createImageMap(String[] list) {
        Map<String, ImageIcon> map = new HashMap<>();
        try {
            map.put(list[0], new ImageIcon(new URL(ListOfEntities.get(0).getImageLink())));
            map.put(list[1], new ImageIcon(new URL(ListOfEntities.get(1).getImageLink())));
            map.put(list[2], new ImageIcon(new URL(ListOfEntities.get(2).getImageLink())));
            map.put(list[3], new ImageIcon(new URL(ListOfEntities.get(3).getImageLink())));
            map.put(list[4], new ImageIcon(new URL(ListOfEntities.get(4).getImageLink())));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return map;
    }

    public static void main(String[] args) {
    	System.out.println("Co se ma vyhledat?");
    	Scanner sc = new Scanner(System.in);
    	String s = sc.nextLine();
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new Main(s);
            }
        });
    }
}