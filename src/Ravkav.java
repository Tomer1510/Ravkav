

import com.jaccal.Atr;
import com.jaccal.CardException;
import com.jaccal.CardResponse;
import com.jaccal.Session;
import com.jaccal.SessionFactory;
import com.jaccal.command.ApduCmd;
import com.jaccal.util.NumUtil;
import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.LinkedList;
import javax.swing.border.*;

import net.miginfocom.layout.LC;
import net.miginfocom.swing.MigLayout;

import java.util.ListIterator;

public class Ravkav extends SmartCard implements ActionListener {

	/**
	 * @param args
	 */


		static final String[] PROFILES = {
				  "Standard",
				  "Standard",
				   "2",
				   "Extended Student",
				   "Senior Citizen",
				   "Handicapped",
				   "Poor vision / blind",
				   "7",
				   "8",
				   "9",
				    "Ministry of Defence",
				    "11",
				    "12",
				    "Public Transport Works",
				    "14",
				    "15",
				    "16",
				    "17",
				    "18",
				    "Regular Student",
				    "20",
				    "21",
				    "22",
				    "23",
				    "24",
				    "25",
				    "26",
				    "27",
				    "28",
				    "29",
				    "30",
				    "31",
				    "Child aged 5-10",
				    "Youth",
				    "National Service",
				    "Of \"takad\" zayin",
				    "Israel Police",
				    "Prison Services",
				    "Member of Parliament",
				    "Parliament Guard",
				    "Eligible for Social Security",
				    "Victim of Hostilities",
				    "New Immigrant in Rural Settlement"
		};
	
	
	String ROUTES[];
	
	Map<Integer, String> tabs_op;
	
	Scanner reader = new Scanner(System.in);
	String select_by_id = "00A4000002";
	private boolean debug = false;
	private int EP = 852073200;
	private JPanel events_panel;
	private JPanel details_panel;
	private JPanel tabs_panel;
	private JTextArea dob;
	private JTextArea active_profile;
	private JTextArea[] events;
	private JButton rEvents;
	private JButton rDetails;
	private JButton rTabs;
	private JTextArea[] tabs;
	private JTextArea profile_valid;
	private JButton rAll;
	private JLabel[] tabs_labels;
	
	public void reset() {
		dob.setText("");
		for (int i = 0;i < events.length;i++)
			events[i].setText("");
		init();
		
	}
	
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == rEvents) {
			init();
			latest_events();
		}
		else if (e.getSource() == rDetails) {
			init();
			read_details();
		}
		else if (e.getSource() == rTabs) {
			init();
			read_tabs();
		}
		else if (e.getSource() == rAll) {
			init();
			read_tabs();
			read_details();
			latest_events();
		}
		else {
			System.out.println(rDetails.toString());
			System.out.println(e.getSource());
		}
	}
	
	private void initLayout() {
		
		JTabbedPane table = new JTabbedPane();
		this.add(table);
		this.setTitle("Rav Kav");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(400, 550);
		this.setLocationRelativeTo(null);
		this.setVisible(true);
		
		JPanel main_panel = new JPanel();
		table.addTab("Summary", main_panel);
		main_panel.setLayout(new MigLayout(new LC().alignX("left")));
		rAll = new JButton("Read All"); 
		main_panel.add(rAll);
		rAll.addActionListener(this);
		
		details_panel = new JPanel();
		table.addTab("Details", details_panel);
		details_panel.setLayout(new MigLayout(new LC().alignX("left")));
		rDetails = new JButton("Read Details"); 
		details_panel.add(rDetails);
		rDetails.addActionListener(this);
		details_panel.add(new JLabel(" "), "span, grow");

		details_panel.add(new JLabel(" "), "span, grow");
		details_panel.add(new JLabel("Date of birth: "));
		dob = new JTextArea(1, 10);
		details_panel.add(dob, "grow");
		details_panel.add(new JLabel(" "), "span, grow");
		dob.setEditable(false);
		
		
		details_panel.add(new JLabel(" "), "span, grow");
		details_panel.add(new JLabel("Active profile: "));
		details_panel.add(active_profile=new JTextArea(), "grow");
		details_panel.add(new JLabel(" "), "span, grow");
		active_profile.setEditable(false);
		
		details_panel.add(new JLabel(" "), "span, grow");
		details_panel.add(new JLabel("Profile validity: "));
		details_panel.add(profile_valid=new JTextArea(), "grow");
		details_panel.add(new JLabel(" "), "span, grow");
		dob.setEditable(false);
		
		
		
		
		events_panel = new JPanel();
		events_panel.setLayout(new MigLayout(new LC().alignX("left")));
		table.addTab("Events", events_panel);
		
		//panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));		
	
		//panel.setLayout(new BoxLayout(panel, BoxLayout.LINE_AXIS));
		events_panel.setLayout(new MigLayout(new LC().alignX("left")));
		rEvents = new JButton("Read Events"); 
		events_panel.add(rEvents);

		rEvents.addActionListener(this);
		events_panel.add(new JLabel(" "), "span, grow");
		dob.setEditable(false);
		events = new JTextArea[6];
		for (int i = 0;i < 6;i++) {
			//panel.setBorder(BorderFactory.createEmptyBorder(0, 5, 5, 5));
			events_panel.add(new JLabel(" "), "span, grow");

			events_panel.add(new JLabel("Event "+(i+1)+": "));
			events[i] = new JTextArea(1, 15);
			events_panel.add(events[i], "span, grow");
			events[i].setEditable(false);
		}
		
		
		
		tabs_panel = new JPanel();
		tabs_panel.setLayout(new MigLayout(new LC().alignX("left")));
		table.addTab("Tabs", tabs_panel);

		//panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));		
	
		//panel.setLayout(new BoxLayout(panel, BoxLayout.LINE_AXIS));
		tabs_panel.setLayout(new MigLayout(new LC().alignX("left")));
		rTabs = new JButton("Read Tabs"); 
		tabs_panel.add(rTabs);
		tabs_panel.add(new JLabel(" "), "span, grow");

		rTabs.addActionListener(this);
		tabs_panel.add(new JLabel(" "), "span, grow");
		dob.setEditable(false);
		tabs = new JTextArea[8];
		tabs_labels = new JLabel[8];
		for (int i = 0;i < 8;i++) {
			//panel.setBorder(BorderFactory.createEmptyBorder(0, 5, 5, 5));
			//tabs_panel.add(new JLabel(" "), "span, grow");

			tabs_panel.add(tabs_labels[i]=new JLabel("Tab "+(i+1)+": "));
			tabs[i] = new JTextArea(3, 19);
			tabs_panel.add(tabs[i], "span, grow");
			tabs[i].setEditable(false);
		}
		
		
		
		
	}
	
	
	public Ravkav() {
		initLayout();
		init();
	}
	
	public Ravkav(boolean debug_) {
		this.debug = debug_;
		initLayout();
		init();
	}
	
	
	public boolean init() {
		
		ROUTES = new String[300];
		for (int i = 0;i < 300;i++)
			ROUTES[i] = "";
		ROUTES[11] = "Dan Region - Tel-Aviv";
		ROUTES[16] = "Dan Region South";
		ROUTES[52] = "Israel Rail - Central";
		ROUTES[11] = "";
		ROUTES[11] = "";
		ROUTES[11] = "";
		
		tabs_op = new HashMap<Integer, String>();
		tabs_op.put(19, "Rail");
		tabs_op.put(320, "Dan");
		tabs_op.put(416, "Egged");
		
		return super.init();
	}
	
	public boolean success() {
		return rapdu.getStatusWord().toString().equals("90 00 ");
	}
	
	
	
	
	public void latest_events() {
		try {
			if ( !se[0].execute(new ApduCmd("00 A4 00 00 02 20 10")).getStatusWord().toString().equals("90 00 ") )
				return;
			for (int i = 1;i <= 6;i++ ) {
				events[i-1].setText("");
				capdu = new ApduCmd("00 B2 0"+((char)(48+i))+" 00 00");
				rapdu = se[0].execute(capdu);
				System.out.println("Event 0"+((char)(48+i))+":");
				long date_ts = Long.parseLong(rapdu.toString().substring(23, 35).replace(" ", ""), 16);
				if (date_ts == 0)
					continue;
				date_ts =date_ts & 1073741824-1;
				date_ts += EP;
				date_ts += 3600;
				Date date = new Date(date_ts*1000);
				String ts = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(date);
				System.out.println(ts);
				System.out.println(rapdu.toString().substring(23, 35).replace(" ", ""));
				System.out.println();
				events[i-1].setText(events[i-1].getText()+ts);
			}
		} catch (Exception e) {
			
		}
	}
	
	public void read_tabs() {
		try {
			String counters = "";
			
			rapdu = se[0].execute(new ApduCmd("00 A4 00 00 02 20 69"));
			if (!success())
				return;
			rapdu = se[0].execute(new ApduCmd("00 B2 01 00 00"));
			if (!success())
				return;
			
			
			counters = rapdu.toString().substring(4);
			if ( !se[0].execute(new ApduCmd("00 A4 00 00 02 20 20")).getStatusWord().toString().equals("90 00 ") )
				return;
			for (int i = 1;i <= 8;i++ ) {
				tabs[i-1].setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.WHITE), 
			            BorderFactory.createEmptyBorder(1, 15, 1, 15)));
				tabs[i-1].setText("");
				capdu = new ApduCmd("00 B2 0"+((char)(48+i))+" 00 00");
				rapdu = se[0].execute(capdu);
				System.out.println("Tab 0"+((char)(48+i))+":");
				//System.out.println(rapdu.toString());
				/*long date_ts = Long.parseLong(rapdu.toString().substring(17, 23).replace(" ", ""), 16);
				date_ts /= 4;
				date_ts = date_ts*3600*24;
				date_ts -= 3600;
				date_ts += EP;
				*/
				String ts = to_date(rapdu.toString().substring(17, 23));
				if (ts.equals("01/01/1997"))
					continue;
				int counter = Integer.parseInt(counters.substring(9*(i-1), 9*(i-1) + 8).replace(" ", ""), 16);
				int op = Integer.parseInt(rapdu.toString().substring(32, 36).replace(" ", ""), 16);
				//Date date = new Date(date_ts*1000);
				//String ts = new SimpleDateFormat("dd/MM/yyyy").format(date);
				System.out.println(ts);
				System.out.println(op);
				System.out.println(counter);
				System.out.println();
				tabs[i-1].setText("Operator: "+tabs_op.get(op));
				tabs[i-1].setText(tabs[i-1].getText()+"\n"+"Purchase date: ");
				tabs[i-1].setText(tabs[i-1].getText()+ts);
				tabs[i-1].setText(tabs[i-1].getText()+"\n"+"Balance: "+counter);
				
				tabs[i-1].setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(counter>0?Color.GREEN:Color.RED), 
				            BorderFactory.createEmptyBorder(1, 15, 1, 15)));
				
			}
		} catch (CardException e) {
			
		}
		
	}
	
	
	
	
	public void read_details() {
		
		try {
			
			String temp = read_record("20 01", "01");
			System.out.println(temp);
			String birthday = temp.substring(43, 45)+"/"+temp.substring(40, 42)+"/"+temp.substring(34, 36)+temp.substring(37, 39);
			System.out.println("Date of birth: " + birthday);
			dob.setText(birthday);
			String profile_v = to_date(temp.substring(75, 81));
			System.out.println(profile_v);
			int profile_id = Integer.parseInt(temp.substring(73, 75), 16) & 0x3f;
			System.out.println(profile_id);
			profile_valid.setText(profile_v);
			active_profile.setText(PROFILES[profile_id]);
			
		} catch (Exception e) {
			System.out.println("Error: "+e.toString());
		}
		
		
	}
	
	
	public void dump_all() {
		String[] AIDarr = new String[]{"00 02", "20 01", "20 04", "20 10", "20 20", "20 2A",
				"20 2B", "20 2C", "20 2D", "20 30", "20 40", "20 50", "20 69", "20 6A", "20 F0",
				"21 01", "21 04", "21 10", "21 20", "21 40", "21 50", "21 69", "21 F0", "2F 10", "3F 04"};
		LinkedList<String> AIDs = new LinkedList<String>(Arrays.asList(AIDarr));
		ListIterator it = AIDs.listIterator();
		while ( it.hasNext()) {
			String aid = it.next().toString();
			//System.out.println("\n# AID: "+aid+" #");
			bruteforce_record(aid);
		}
	}
	
	
	public String to_date(String Bytes) {
		long date_ts = Long.parseLong(Bytes.replace(" ", ""), 16);
		date_ts /= 4;
		date_ts = date_ts*3600*24;
		date_ts -= 3600;
		date_ts += EP;
		Date date = new Date(date_ts*1000);
		String ts = new SimpleDateFormat("dd/MM/yyyy").format(date);
		return ts;
	}
	
	/*public String to_operator(String op) {
		switch (op) {
		
		case "A8": return "Egged"; break;
		case "28": return "
		
		
		}
		
		return "";
	}*/
	
	
	
	
}
