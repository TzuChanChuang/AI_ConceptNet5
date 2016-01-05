import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


public class Part1 {
	
	public static void main(String[] args) throws IOException {
		
		System.out.println("----------------Part1 Question----------------");
		System.out.print("Enter your concept > ");
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		String sInput = in.readLine();
		
		//  test one concept
		Markov myMarkov = new Markov(sInput);
		double state_return = 1, t_b=2, t_f=2;
		while(state_return!=0){
			System.out.println("----------------Markov "+t_b+" "+t_f+"----------------");
			try {
				state_return = myMarkov.one_concept_question(t_b,t_f);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(state_return==1) t_b-=0.5;
			else if(state_return==2) t_f-=0.5;
		}
		
	}
	
	
}
