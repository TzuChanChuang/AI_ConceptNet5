import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import org.json.JSONException;


public class MAIN {
	
public static void main(String[] args) throws Exception {
		
		System.out.print("choose question, 1or 2? > ");
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		String sInput = in.readLine();
		
		while(!sInput.contains("e")&&!sInput.contains("q")){
			if(sInput.contains("1")){
				Part1 mypart1 = new Part1();
				mypart1.part1();
			}
			else if(sInput.contains("2")){
				Part2 mypart2 = new Part2();
				mypart2.part2();
			}
			
			System.out.print("\n\nchoose question, 1or 2? > ");
			sInput = in.readLine();
		}
		
	}

}
