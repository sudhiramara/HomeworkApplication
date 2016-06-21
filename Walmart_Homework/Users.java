/*
  author @sudhiramara
*/

import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.Map.Entry;

    /*
    Users class contains the main method and it takes the input from user and creates object for HomeworkApplication class to perform 
    required manipulations on the data using walmart open API and displays the required output
    */


public class Users {

	private static Scanner scan;
	static String stars[] ;

	public static void main(String[] args) throws Exception {
        if(System.getProperty("os.name").toLowerCase().contains("windows"))
        {
          stars = new String[]{" "," "," "," "," "," "," "," "," "," "};
        }else{
        	stars = new String[]{"🌕 🌑 🌑 🌑 🌑","🌕 🌓 🌑 🌑 🌑","🌕 🌕 🌑 🌑 🌑","🌕 🌕 🌓 🌑 🌑","🌕 🌕 🌕 🌑 🌑","🌕 🌕 🌕 🌓 🌑","🌕 🌕 🌕 🌕 🌑","🌕 🌕 🌕 🌕 🌓","🌕 🌕 🌕 🌕 🌕","🌑 🌑 🌑 🌑 🌑"};    
        	 }
		while (true) {
		if (System.getProperty("os.name").toLowerCase().contains("windows10")) {
			System.out.println("\033[H\033[2J");	
			}	
		else if(System.getProperty("os.name").toLowerCase().contains("windows")){
               System.out.println("  ");
		}else{
                System.out.println("\033[H\033[2J");
		}
		    System.out.println(" ");
			System.out.println("Enter your search term ");
			scan = new Scanner(System.in);
			String item = scan.nextLine();
			System.out.println(" ");
			try {
				HomeworkApplication homeapp = new HomeworkApplication();
				Set<Entry<Integer, ReviewVO>> sortedprodRevSet = homeapp.getRecommendations(item.trim());
				if (sortedprodRevSet != null) {
					Iterator<Entry<Integer, ReviewVO>> iterator = sortedprodRevSet.iterator();
					System.out.println(
							"| PRODUCT RECOMMENDATIONS FOR \""+ item.trim() +"\" BASED ON PREVIOUS CUSTOMER REVIEWS |");
					System.out.println(
							"--------------------------------------------------------------------------------------");

					while (iterator.hasNext()) {
						Map.Entry<Integer, ReviewVO> entry = iterator.next();
						System.out.println(" Product ID :" + "  " + entry.getKey());
						System.out.println("");
						System.out.println(" Name : " + entry.getValue().name);
						System.out.println("");
						System.out.println(" Review Rating & count:" + Users.printRating(entry.getValue().average) + " ("+ entry.getValue().average  +" out of 5.0) ("
								+ entry.getValue().revCount + ")");
						System.out.println("");
						System.out.println(
								"-------------------------------------------------------------------------");
					}
				}
			} catch (Exception ex) {
				System.out.println("No recommendations for this product");
			}
			System.out.println("Enter \"Q\" to Quit or any other key to continue ");
			scan = new Scanner(System.in);
			String option = scan.nextLine();
			if (option.equals("q") || option.equals("Q")) {
				break;
			}
		}
	}		

		public static String printRating(double A) {
		if (A == 1.0)
			return stars[0];
		else if ((A > 1.0) && (A < 2.0))
			return stars[1];
		else if (A == 2.0)
			return stars[2];
		else if ((A > 2.0) && (A < 3.0))
			return stars[3];
		else if (A == 3.0)
			return stars[4];
		else if ((A > 3.0) && (A < 4.0))
			return stars[5];
		else if (A == 4.0)
			return stars[6];
		else if ((A > 4.0) && (A < 5.0))
			return stars[7];
		else if(A == 5.0)
			return stars[8];
		else
			return stars[9];

		}

	}

