import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


public class Part1 {
	
	public static void main(String[] args) throws IOException {
		try {
			part1();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
		
	static public void part1()throws Exception{
		System.out.println("----------------Part1 Question----------------");
		
		// input
		System.out.print("Enter your concept > ");
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		String sInput = in.readLine();
		
		//拆開兩個conceptss
		String[] token = {sInput, ""};
		if(sInput.contains(" ")){
			token = sInput.split(" ");
		}
		
		//  test one concept
		Markov myMarkov = new Markov(token[0], token[1]);
		int state_return = 1;
		double t_b, t_f;
		if(token[1]==""){
			t_b = 2; t_f = 2;
		}
		else{
			t_b=2; t_f=2;
		}
		while(state_return!=0){
			System.out.println("----------------Markov "+t_b+" "+t_f+"----------------");
	
			// run
			if(token[1]=="") state_return = myMarkov.question(1, t_b,t_f);
			else state_return = myMarkov.question(2, t_b,t_f);
			
			// there is no answer -> smaller threshold
			/*if(state_return==1) t_b-=0.5;
			else if(state_return==2) t_f-=0.5;
			else if(state_return==3) {
				t_f-=0.5;
				t_b-=0.5;
			}*/
			if(state_return !=0){
				t_f-=0.5; t_b-=0.5;
			}
			//最後底線
			if(t_f==0.5 && t_b==0.5){
				myMarkov.num_sentence =5;
				System.out.println("----------------Markov "+t_b+" "+t_f+"----------------");
				if(token[1]=="") state_return = myMarkov.question(1, t_b,t_f);
				else state_return = myMarkov.question(2, t_b,t_f);
				// 沒辦法找到句子				
				if(state_return!=0) {
					System.out.println("----------------answer----------------");
					if(token[1]=="")System.out.println("cannot generate sentence which contains " + token[0]);
					else System.out.println("cannot generate sentence which contains '" + token[0] +"' and '" + token[1]+"'.");
					break;
				}
			}
		}
	}
	
	
}
