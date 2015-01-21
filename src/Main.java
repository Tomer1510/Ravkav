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
import java.util.Scanner;
import java.util.LinkedList;
import javax.swing.border.*;
import java.util.ListIterator;
import java.util.Arrays;

public class Main {

	/**
	 * @param args
	 */
	static Session[] se;
	static ApduCmd capdu;
	static CardResponse rapdu;
	static Scanner reader = new Scanner(System.in);
	static String select_by_id = "00A4000002";
	
	
	
	/*
	public static void bruteforce_id() {
		String successes = "";
		try {
			char digits[] = new char[]{'0','1','2','3','C','D','E','F'};
			//,'4','5','6','7','8','9','A','B'
			
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
								System.out.println("Continue?");
								char r = reader.next().charAt(0);
								if (r == 'y') {
									init();
									continue;
								}
								else
									throw e;
							}
							
							if (success()) 
								successes += "\n"+digits[i]+digits[j]+" "+digits[k]+digits[l];
							//Thread.sleep(500);
							//System.out.println(""+digits[i]+digits[j]+" "+digits[k]+digits[l]);
							if (counter % 50 == 0) {
								System.out.println((100*(float)counter/(Math.pow(digits.length, 4)))+"%");
							}
							if (counter % 50 == 0) {
								System.out.println(successes.length() - successes.replace("\n", "").length());
							}
							
						}
			
			System.out.println("##### Successes #####");
			System.out.println(successes);
		} catch (Exception e) {
			System.out.println(e.toString());
			System.out.println("##### Successes #####");
			System.out.println(successes);
		}
	}
	*/
	
	
	
	
	
	public static void do_bruteforce(SmartCard card) {
		
		LinkedList AIDs = card.bruteforce_aid();
		ListIterator it = AIDs.listIterator();
		while ( it.hasNext()) {
			String aid = it.next().toString();
			//System.out.println("\n# AID: "+aid+" #");
			card.bruteforce_record(aid);
		}
		
		
	}
	
	
	
	
	
	public static void do_terminal(SmartCard card) {
		try {
			String input = "";
			while (input != "CLOSE") {
				System.out.print("< ");
				input = reader.nextLine();
				System.out.println("> "+card.se[0].execute(new ApduCmd(input)));
			}
		}
		catch(Exception e) {
			System.out.println(e.toString());
		}
	}
	
	
	public static void initPanel() {
		
		
		
	}
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		/*String[] AIDs = new String[]{"00 02", "00 03", "20 00", "20 01", 
				"20 10", "20 20", "20 2C", "20 2D", "20 30", "20 F0", "21 00",
				"21 01", "21 10", "21 20", "21 F0", "2F 10", "3F 00"};
		for (int i = 0;i < AIDs.length;i ++) {
			System.out.println("# AID: "+AIDs[i]+" #");
			bruteforce_record(AIDs[i]);
		}*/
		//initPanel();
		Ravkav card = new Ravkav();
		//do_bruteforce(card);
		//card.read_tabs();
		//card.dump_all();
		
		//do_bruteforce(card);
		
		//card.read_details();

		//card.latest_events();
		
		//do_terminal(card);
		
		
	}
	

	

}
