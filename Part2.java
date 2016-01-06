import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Part2 {
	public static void main(String[] args) throws IOException {
		try {
			part2();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	static public void part2()throws Exception{
		System.out.println("----------------Part2 Question----------------");
		
		// input
		System.out.print("Enter your question and concept > ");
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		String sInput = in.readLine();
		
		String[] token = {sInput, ""};
		if(sInput.contains(" ")){
			token = sInput.split(" "); 	//Q:token[0], C:token[1]
		}
		Part2_search ans = new Part2_search(token);
		
	}
}
