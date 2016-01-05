import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


public class Part1 {
	
	public static void main(String[] args) throws IOException {
		
		System.out.print("Enter your concept > ");
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		String sInput = in.readLine();
		
		//  test one concept
		Markov myMarkov = new Markov(sInput);
		try {
			myMarkov.one_concept_question();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
}
