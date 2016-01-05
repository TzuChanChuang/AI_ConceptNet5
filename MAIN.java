import java.sql.SQLException;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import org.json.JSONException;


public class MAIN {
	
	public static void main(String[] args) {
		Vector< String> markovChain = new Vector<String>();
		markovChain.add(	 "A");
		markovChain.add(	 "B");
		markovChain.add(	 "C");
		markovChain.add(	 "A");
		for (int i=0; i<markovChain.size(); i++)
			System.out.println(markovChain.get(i));
	}

}
