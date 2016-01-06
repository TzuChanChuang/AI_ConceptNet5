import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Part2_search {
	private String question, concept1, concept2;
	private String ans;
	DATABASE database = new DATABASE();
	public Part2_search(String[] token) throws Exception{
		
		this.question = token[0];
		this.concept1 = token[1];
		//open database
		System.out.println("----------------open SQLite----------------");
		database.buildSQLite();
		database.connDB("ConceptNet_en");
		database.createStmt();
		String ans;
		if(token[0].equals("what")){
			ans = what();
		}else if(token[0].equals("when")){
			ans = when();
		}else if(token[0].equals("where")){
			ans = where();
		}else if(token[0].equals("why")){
			ans = why();
		}else if(token[0].equals("who")){
			ans = who();
		}else if(token[0].equals("how")){
			ans = how();
		}else if(token[0].equals("can")||token[0].equals("can't")||token[0].equals("cannot")){
			this.concept2 = token[2];
			ans = can();
		}else if(token[0].equals("will")||token[0].equals("is")||token[0].equals("are")||token[0].equals("was")||token[0].equals("were")){
			this.concept2 = token[2];
			ans = is();
		}else if(token[0].equals("do")||token[0].equals("does")){
			this.concept2 = token[2];
			ans = does();
		}else{	//relation type
			this.concept2 = token[2];
			ans = rel();
		}
	}
	
	public String what() throws Exception{
		System.out.println("----------------IsA, after level----------------");
		// Get some words from database
		List<myDATA> datalist = database.searchTable(concept1, "IsA", "%", 2,false);
		// put intorelationList and handle _start
		for (int i = 0; i < datalist.size(); i++) {
			myDATA data_temp = datalist.get(i);
			data_temp.weight = data_temp.weight+1.5;
			datalist.set(i, data_temp);
			System.out.println(datalist.get(i).start);
		}
		System.out.println("----------------DefinedAs, after level----------------");
		// Get some words from database
		List<myDATA> datalist2 = database.searchTable(concept1, "InstanceOf", "%", 2,false);
		// put intorelationList and handle _start
		for (int i = 0; i < datalist2.size(); i++) {
			myDATA data_temp = datalist2.get(i);
			data_temp.weight = data_temp.weight+1;
			datalist2.set(i, data_temp);
			System.out.println(datalist2.get(i).start);
		}
		datalist.addAll(datalist2);
		System.out.println("----------------PartOf, after level----------------");
		// Get some words from database
		 datalist2 = database.searchTable(concept1, "PartOf", "%", 2,false);
		// put intorelationList and handle _start
		for (int i = 0; i < datalist2.size(); i++) {
			myDATA data_temp = datalist2.get(i);
			data_temp.weight = data_temp.weight+1.2;
			datalist2.set(i, data_temp);
			System.out.println(datalist2.get(i).start);
		}
		datalist.addAll(datalist2);
		
		
		
		//sort
        Collections.sort(datalist,
        new Comparator<myDATA>() {
            public int compare(myDATA o1, myDATA o2) {
                if(o1.weight<o2.weight)
                	return 1;
                if(o1.weight==o2.weight)
                	return 0;
                return -1;
            }
        });
        
		// 如果查不到東西
		if (datalist.size() == 0) ans="Not sure";
		return ans;
	}
	public String when(){
		
		return ans;
	}
	public String where(){
		
		return ans;
	}
	public String why(){
		
		return ans;
	}
	public String who(){
		
		return ans;
	}
	public String how(){
		
		return ans;
	}
	public String can(){
		
		return ans;
	}
	public String is(){
		
		return ans;
	}
	public String does(){
		
		return ans;
	}
	public String rel(){
		
		return ans;
	}
}
