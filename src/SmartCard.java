

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
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Scanner;
import java.util.LinkedList;
import javax.swing.border.*;

import net.miginfocom.layout.LC;
import net.miginfocom.swing.MigLayout;

import java.util.ListIterator;

public class SmartCard extends JFrame implements ActionListener {

	/**
	 * @param args
	 */


	
	
	Session[] se;
	ApduCmd capdu;
	CardResponse rapdu;
	Scanner reader = new Scanner(System.in);
	String select_by_id = "00A4000002";
	private boolean debug = false;
	private int EP = 852073200;
	
	
	
	
	public void actionPerformed(ActionEvent e) {
	
	}
	
	
	
	
	public SmartCard() {
		
		init();
	}
	
	public SmartCard(boolean debug_) {
		this.debug = debug_;
		
		init();
	}
	
	
	public boolean init() {
		
	
		
		try {if (se.length > 0) se[0].close();}catch(Exception e){}
		try {
			
			se = SessionFactory.getInstance().createSessions();
			se[0].open();
		} catch(Exception e) {
			System.out.println(e.toString());
			return false;
		}
		return true;
	}
	
	public boolean success() {
		return rapdu.getStatusWord().toString().equals("90 00 ");
	}
	
	public void bruteforce_record(String AID) {
		char digits[] = new char[]{'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};
		//
		//String successes = "";
		int fails = 0;
		try {
			if ( !se[0].execute(new ApduCmd("00 A4 00 00 02 "+AID)).getStatusWord().toString().equals("90 00 ") )
				return;

			for (int k = 0;k < digits.length;k++)
				for (int l = 0;l < digits.length;l++)
				{
					capdu = new ApduCmd("00B2"+digits[k]+digits[l]+"0500");
					try{rapdu = se[0].execute(capdu);}catch(Exception e){
						System.out.println(e.toString());
						char r;
						if (fails >= 10) {
							System.out.println("Continue? "+fails);
							r = reader.next().charAt(0);
						}
						else {
							Thread.sleep(1000);
							r = 'y';
						}
						if (r == 'y') {
							init();
							l--;
							continue;
						}
						else
							throw e;
					}
					
					if (success()) {
						//successes += ""+digits[k]+digits[l]+"\n";
						System.out.println("AID="+AID+"\t"+"RECORD="+digits[k]+digits[l]);
						System.out.println(read_record(AID, ""+digits[k]+digits[l]));

					}
					
					
	
					
				}
			
		} catch (Exception e) {
			System.out.println(e.toString());
			System.out.println(e.getStackTrace().toString());
		}
		
	}
	
	
	public LinkedList<String> bruteforce_aid(int consts[]) {
		String successes = "";
		LinkedList<String> ret = new LinkedList<String>();
		try {
			char digits[] = new char[]{'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};
			//,'4','5','6','7','8','9','A','B'
			int fails = 0;
			int counter = 0;
			int i = consts[0];
			int j = consts[1];
			for (int k = 0;k < digits.length;k++)
				for (int l = 0;l < digits.length;l++)
				{
					counter++;
					capdu = new ApduCmd(select_by_id+digits[i]+digits[j]+digits[k]+digits[l]);
					try{rapdu = se[0].execute(capdu);}catch(Exception e){
						System.out.println(e.toString());
						char r;
						if (fails >= 20) {
							System.out.println("Continue? "+fails);
							r = reader.next().charAt(0);
						}
						else {
							Thread.sleep(1000+fails*200);
							r = 'y';
						}
						if (r == 'y') {
							init();
							l--;
							continue;
						}
						else
							throw e;
					}
					fails = 0;
					if (success()) {
						ret.push(""+digits[i]+digits[j]+" "+digits[k]+digits[l]);
						System.out.println("AID="+digits[i]+digits[j]+" "+digits[k]+digits[l]);
					}
					//Thread.sleep(500);
					//System.out.println(""+digits[i]+digits[j]+" "+digits[k]+digits[l]);
					if (counter % 50 == 0) {
						System.out.println((100*(float)counter/(Math.pow(digits.length, 4)))+"%");
					}
					
				}
			
		} catch (Exception e) {
			
		}
		
	
		
		return ret;
	}
	
	public LinkedList<String> bruteforce_aid() {
		String successes = "";
		LinkedList<String> ret = new LinkedList<String>();
		try {
			char digits[] = new char[]{'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};
			//,'4','5','6','7','8','9','A','B'
			int fails = 0;
			int counter = 0;
			for (int i = 0;i < digits.length;i++) 
				for (int j = 0;j < digits.length;j++)
					for (int k = 0;k < digits.length;k++)
						for (int l = 0;l < digits.length;l++)
						{
							counter++;
							capdu = new ApduCmd(select_by_id+digits[i]+digits[j]+digits[k]+digits[l]);
							try{rapdu = se[0].execute(capdu);}catch(Exception e){
								System.out.println(e.toString());
								char r;
								if (fails++ >= 15) {
									System.out.println("Continue? "+fails);
									r = reader.next().charAt(0);
								}
								else {
									Thread.sleep(2000+fails*200);
									r = 'y';
								}
								if (r == 'y') {
									init();
									l--;
									continue;
								}
								else
									throw e;
							}
							fails = 0;
							if (success()) {
								ret.push(""+digits[i]+digits[j]+" "+digits[k]+digits[l]);
								System.out.println("AID="+digits[i]+digits[j]+" "+digits[k]+digits[l]);
							}
							//Thread.sleep(500);
							//System.out.println(""+digits[i]+digits[j]+" "+digits[k]+digits[l]);
							if (counter % 500 == 0) {
								double percent = Math.round(100*(100*(float)counter/(Math.pow(digits.length, 4))))/100.;
								System.out.println(percent+"% - "+digits[i]+digits[j]+" "+digits[k]+digits[l]);
							}
					
				}
			
		} catch (Exception e) {
			
		}
		
	
		
		return ret;
	}
	
	
	public String read_record(String AID, String RECORD) {
		try {
			System.out.println("test ");
			System.out.println(se);
			if ( !se[0].execute(new ApduCmd("00 A4 00 00 02 "+AID)).getStatusWord().toString().equals("90 00 ") )
				return "AID Error";
			capdu = new ApduCmd("00 B2 "+RECORD+" 00 00");
			rapdu = se[0].execute(capdu);
			if (!success()) {
				System.out.println("Record Error!");
				return "Error";
			}
			
			return rapdu.toString();
			
		}catch(CardException e){System.out.println(e.toString());}
		return "Exception";
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
	
	
	public void dump_all(String[] AIDarr) {
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
