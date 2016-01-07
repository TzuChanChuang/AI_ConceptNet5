import java.util.ArrayList;
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
		}else if(token[0].equals("has")||token[0].equals("have")){
			ans = has();
		}else if(token[0].equals("will")||token[0].equals("can")||token[0].equals("can't")||token[0].equals("cannot")||token[0].equals("do")||token[0].equals("does")){
			this.concept2 = token[2];
			ans = can();
		}else if(token[0].equals("is")||token[0].equals("are")||token[0].equals("was")||token[0].equals("were")){
			this.concept2 = token[2];
			ans = is();
		}else{	//relation type
			this.concept2 = token[2];
			ans = rel();
		}
		  
		System.out.println("----------------close SQLite----------------");
		// close database
		database.closeStmt();
		database.disconnDB();
		// printf
		System.out.println("----------------answer----------------");
		System.out.println(ans);
		
	}
	
	public String what() throws Exception{
		System.out.println("----------------IsA, after level----------------");
		// Get some words from database
		List<myDATA> datalist = database.searchTable(concept1, "IsA", "%", 1.5,false);
		if(datalist.size()==0){
			datalist = database.searchTable(concept1, "IsA", "%", 0.9,false);
		}
		// put intorelationList and handle _start
		for (int i = 0; i < datalist.size(); i++) {
			myDATA data_temp = datalist.get(i);
			data_temp.weight = data_temp.weight+1.5;
			datalist.set(i, data_temp);
			System.out.println(datalist.get(i).end);
		}
		System.out.println("----------------DefinedAs, after level----------------");
		// Get some words from database
		List<myDATA> datalist2 = database.searchTable(concept1, "InstanceOf", "%", 1.5,false);
		if(datalist2.size()==0){
			datalist2 = database.searchTable(concept1, "InstanceOf", "%", 0.9,false);
		}
		// put intorelationList and handle _start
		for (int i = 0; i < datalist2.size(); i++) {
			myDATA data_temp = datalist2.get(i);
			data_temp.weight = data_temp.weight+0.8;
			datalist2.set(i, data_temp);
			System.out.println(datalist2.get(i).end);
		}
		datalist.addAll(datalist2);
		System.out.println("----------------MadeOf, after level----------------");
		// Get some words from database
		 datalist2 = database.searchTable(concept1, "MadeOf", "%", 1.5,false);
		// put intorelationList and handle _start
		for (int i = 0; i < datalist2.size(); i++) {
			myDATA data_temp = datalist2.get(i);
			data_temp.weight = data_temp.weight+1;
			datalist2.set(i, data_temp);
			System.out.println(datalist2.get(i).end);
		}
		datalist.addAll(datalist2);
		
		// check part of
		System.out.println("----------------PartOf, after level----------------");
		// Get some words from database
		List<myDATA> datalist_partof = database.searchTable(concept1, "PartOf", "%", 1.5,false);
		// put intorelationList and handle _start
		for (int j = 0;j < datalist_partof.size(); j++) {
			// modify datalist
			for(int i=0; i<datalist.size(); i++){
				if (datalist.get(i).end.equals(datalist_partof.get(j).end)) {
					myDATA data_temp = datalist.get(i);
					data_temp.end = "part of " + data_temp.end;
					data_temp.weight = (data_temp.weight + datalist_partof.get(j).weight) / 2;
					datalist.set(i, data_temp);
				}
			}
			// modify partof concept data 
			myDATA data_temp = datalist_partof.get(j);
			data_temp.weight = data_temp.weight+1;
			data_temp.end = "part of " + data_temp.end;
			datalist_partof.set(j, data_temp);
			System.out.println(datalist_partof.get(j).end);
		}
		datalist.addAll(datalist_partof);
		
		
		
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
        ans = "";
		if (datalist.size() == 0) {
			ans="Not sure";
		}else if(datalist.size() <5){
			for(int i=0; i<datalist.size(); i++){
				String[] token_what = datalist.get(i).end.split("/");
				ans += token_what[0] +" w="+ datalist.get(i).weight+"\n";
			}
		}else{
			for(int i=0; i<5; i++){
				String[] token_what = datalist.get(i).end.split("/");
				ans += token_what[0] +" w="+ datalist.get(i).weight+"\n";
			}
		}
		return ans;
	}
	public String when() throws Exception{
		
		return ans;
	}
	public String where() throws Exception{
		System.out.println("----------------AtLocation, after level----------------");
		// Get some words from database
		List<myDATA> datalist = database.searchTable(concept1, "AtLocation", "%", 1.5,false);
		// put intorelationList and handle _start
		if(datalist.size()==0){
			datalist = database.searchTable(concept1+"%", "AtLocation", "%", 1.5,false);
		}
		for (int i = 0; i < datalist.size(); i++) {
			myDATA data_temp = datalist.get(i);
			data_temp.weight = data_temp.weight+3.2;
			datalist.set(i, data_temp);
			System.out.println(datalist.get(i).end+" w="+ datalist.get(i).weight);
		}
		System.out.println("---------------UsedFor, before level----------------");
		// Get some words from database	
		List<myDATA> datalist2 = database.searchTable("%", "UsedFor", concept1, 2,false);
		if(datalist2.size()==0){
			datalist2 = database.searchTable("%", "UsedFor", concept1+"%", 1.5,false);
		}
		// put intorelationList and handle _start
		for (int i = 0; i < datalist2.size(); i++) {
			myDATA data_temp = datalist2.get(i);
			data_temp.weight = data_temp.weight;
			datalist2.set(i, data_temp);
			System.out.println(datalist2.get(i).start+" w="+ datalist2.get(i).weight);
		}
		datalist.addAll(datalist2);
		System.out.println("---------------LocationOfAction, after level----------------");
		// Get some words from database
		datalist2 = database.searchTable(concept1, "LocationOfAction", "%", 1.5,false);
		// put intorelationList and handle _start
		for (int i = 0; i < datalist2.size(); i++) {
			myDATA data_temp = datalist2.get(i);
			data_temp.weight = data_temp.weight+8;
			datalist2.set(i, data_temp);
			System.out.println(datalist2.get(i).end+" w="+ datalist2.get(i).weight);
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
		ans = "";
		if(datalist.size() == 0){	
			ans="here";
		}else if (datalist.size() <5){
			for(int i=0; i<datalist.size(); i++){
				if(datalist.get(i).rel.equals("UsedFor")){
					String[] token_where = datalist.get(i).start.split("/");
					ans += token_where[0]+" w="+ datalist.get(i).weight+"\n";
				}else{
					String[] token_where = datalist.get(i).end.split("/");
					ans += token_where[0]+" w="+ datalist.get(i).weight+"\n";
				}
			}
		}else{
			for(int i=0; i<5 ; i++){
				if(datalist.get(i).rel.equals("UsedFor")){
					String[] token_where = datalist.get(i).start.split("/");
					ans += token_where[0]+" w="+ datalist.get(i).weight+"\n";
				}else{
					String[] token_where = datalist.get(i).end.split("/");
					ans += token_where[0]+" w="+ datalist.get(i).weight+"\n";
				}
			}
		}
		return ans;
	}
	public String why() throws Exception{
		System.out.println("----------------MotivatedByGoal, after level----------------");
		// Get some words from database
		List<myDATA> datalist = database.searchTable(concept1, "MotivatedByGoal", "%", 1.5,false);
		// put intorelationList and handle _start
		if(datalist.size()==0){
			datalist = database.searchTable(concept1+"%", "MotivatedByGoal", "%", 1.5,false);
		}
		for (int i = 0; i < datalist.size(); i++) {
			myDATA data_temp = datalist.get(i);
			data_temp.weight = data_temp.weight+1.5;
			datalist.set(i, data_temp);
			System.out.println(datalist.get(i).end+" w="+ datalist.get(i).weight);
		}
		
		System.out.println("---------------CausesDesire, after level----------------");
		// Get some words from database
		List<myDATA> datalist2 = database.searchTable(concept1, "CausesDesire", "%", 1.5,false);
		// put intorelationList and handle _start
		if(datalist2.size()==0){
			datalist2 = database.searchTable(concept1+"%", "CausesDesire", "%", 1.5,false);
		}
		for (int i = 0; i < datalist2.size(); i++) {
			myDATA data_temp = datalist2.get(i);
			data_temp.weight = data_temp.weight;
			datalist2.set(i, data_temp);
			System.out.println(datalist2.get(i).end+" w="+ datalist2.get(i).weight);
		}
		datalist.addAll(datalist2);
		System.out.println("---------------Causes, before level----------------");
		// Get some words from database
		datalist2 = database.searchTable("%", "Causes", concept1, 1.5,false);
		// put intorelationList and handle _start
		if(datalist2.size()==0){
			datalist2 = database.searchTable("%", "Causes", concept1+"%", 1.5,false);
		}
		for (int i = 0; i < datalist2.size(); i++) {
			myDATA data_temp = datalist2.get(i);
			data_temp.weight = data_temp.weight;
			datalist2.set(i, data_temp);
			System.out.println(datalist2.get(i).start+" w="+ datalist2.get(i).weight);
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
        ans = "";
		if (datalist.size() == 0) {
			ans="Unknown";
		}else if(datalist.size() <5){
			for(int i=0; i<datalist.size(); i++){
				String[] token_why;
				if(datalist.get(i).rel.equals("Causes")){
					token_why = datalist.get(i).start.split("/");
				}else{
					token_why = datalist.get(i).end.split("/");
				}
				ans += token_why[0] +" w="+ datalist.get(i).weight+"\n";
			}
		}else{
			for(int i=0; i<5; i++){
				String[] token_why;
				if(datalist.get(i).rel.equals("Causes")){
					token_why = datalist.get(i).start.split("/");
				}else{
					token_why = datalist.get(i).end.split("/");
				}
				ans += token_why[0] +" w="+ datalist.get(i).weight+"\n";
			}
		}
		return ans;
	}
	public String who() throws Exception{
		System.out.println("----------------dbpedia/mainInterest, before level----------------");
		// Get some words from database
		List<myDATA> datalist = database.searchTable("%", "dbpedia/mainInterest", concept1+"%", 0.5,false);
		// put intorelationList and handle _start
		for (int i = 0; i < datalist.size(); i++) {
			myDATA data_temp = datalist.get(i);
			data_temp.weight = data_temp.weight+2;
			datalist.set(i, data_temp);
			System.out.println(datalist.get(i).start+" w="+ datalist.get(i).weight);
		}

		System.out.println("---------------dbpedia/knownFor, before level----------------");
		// Get some words from database
		List<myDATA> datalist2 = database.searchTable("%", "dbpedia/knownFor", concept1+"%", 0.5,false);
		// put intorelationList and handle _start
		String[] token_who_knownFor;
		for (int i = 0; i < datalist2.size(); i++) {
			
			myDATA data_temp = datalist2.get(i);
			data_temp.weight = data_temp.weight+2;
			datalist2.set(i, data_temp);
			token_who_knownFor = datalist2.get(i).start.split("/");
			int l= token_who_knownFor.length;
			System.out.println(token_who_knownFor[l-1]+" w="+ datalist2.get(i).weight);
		}
		datalist.addAll(datalist2);
		System.out.println("---------------CapableOf, before level----------------");
		// Get some words from database
		datalist2 = database.searchTable("%", "CapableOf", concept1+"%", 1.5,false);
		// put intorelationList and handle _start
		for (int i = 0; i < datalist2.size(); i++) {
			myDATA data_temp = datalist2.get(i);
			data_temp.weight = data_temp.weight+0.5;
			datalist2.set(i, data_temp);
			System.out.println(datalist2.get(i).start+" w="+ datalist2.get(i).weight);
		}
		datalist.addAll(datalist2);
		System.out.println("---------------CreatedBy, after level----------------");
		// Get some words from database
		datalist2 = database.searchTable(concept1, "CreatedBy", "%", 1.5,false);
		// put intorelationList and handle _start
		for (int i = 0; i < datalist2.size(); i++) {
			myDATA data_temp = datalist2.get(i);
			data_temp.weight = data_temp.weight+3.5;
			datalist2.set(i, data_temp);
			System.out.println(datalist2.get(i).end+" w="+ datalist2.get(i).weight);
		}
		datalist.addAll(datalist2);
		System.out.println("---------------IsA, after level----------------");
		// Get some words from database
		datalist2 = database.searchTable(concept1, "IsA", "%", 1.5,false);
		// put intorelationList and handle _start
		for (int i = 0; i < datalist.size(); i++) {
			myDATA data_temp = datalist.get(i);
			data_temp.weight = data_temp.weight+1;
			datalist.set(i, data_temp);
			System.out.println(datalist.get(i).end);
		}
		datalist.addAll(datalist2);
		
		// 如果查不到東西
		ans = "";
		String[] token_who;
		if (datalist.size() == 0) {
			ans = "someone";
		} else if (datalist.size() < 5) {
			for (int i = 0; i < datalist.size(); i++) {
				if (datalist.get(i).rel.equals("CreatedBy")||datalist.get(i).rel.equals("IsA")) {
					token_who = datalist.get(i).end.split("/");
					ans += token_who[0] + " w=" + datalist.get(i).weight + "\n";
				}else if(datalist.get(i).rel.equals("dbpedia/knownFor")) {
					token_who = datalist.get(i).start.split("/");
					int l= token_who.length;
					ans +=token_who[l-1]+" w=" + datalist.get(i).weight + "\n";
				}else {
					token_who = datalist.get(i).start.split("/");
					ans += token_who[0] + " w=" + datalist.get(i).weight + "\n";
				}
			}
		} else {
			for (int i = 0; i < 5; i++) {
				if (datalist.get(i).rel.equals("CreatedBy")||datalist.get(i).rel.equals("IsA")) {
					token_who = datalist.get(i).end.split("/");
					ans += token_who[0] + " w=" + datalist.get(i).weight + "\n";
				} else if(datalist.get(i).rel.equals("knownFor")) {
					token_who = datalist.get(i).start.split("/");
					int l= token_who.length;
					ans += token_who[l-1]+" w=" + datalist.get(i).weight + "\n";
				}else {
					token_who = datalist.get(i).start.split("/");
					ans += token_who[0] + " w=" + datalist.get(i).weight + "\n";
				}
			}
		}
		return ans;
	}
	public String how() throws Exception{
		
		return ans;
	}
public String can() throws Exception{
		
		List<String> listA_b = new ArrayList<>();
		List<String> listB_f = new ArrayList<>();
		
		System.out.println("----------------can, CapableOf search----------------");
		//A_b
		List<myDATA> datalistA = database.searchTable(concept1, "CapableOf", "%", 0.9,false);
		for(int i=0; i<datalistA.size(); i++){
			String[] token = datalistA.get(i).end.split("/");
			listA_b.add(token[0]);	
		}
		if(listA_b.contains(concept2)) return "Yes";
		//B_f
		List<myDATA> datalistB = database.searchTable("%", "CapableOf", concept2, 0.9,false);
		for(int i=0; i<datalistB.size(); i++){
			String[] token = datalistB.get(i).start.split("/");		
			listB_f.add(token[0]);
		}
		if(listB_f.contains(concept1)) return "Yes";
		
		System.out.println("----------------can, Desires search----------------");
		//A_b
		datalistA = database.searchTable(concept1, "Desires", "%", 0.9,false);
		for(int i=0; i<datalistA.size(); i++){
			String[] token = datalistA.get(i).end.split("/");
			listA_b.add(token[0]);	
		}
		if(listA_b.contains(concept2)) return "Yes";
		//B_f
		datalistB = database.searchTable("%", "Desires", concept2, 0.9,false);
		for(int i=0; i<datalistB.size(); i++){
			String[] token = datalistB.get(i).start.split("/");		
			listB_f.add(token[0]);
		}
		if(listB_f.contains(concept1)) return "Yes";
		
		System.out.println("----------------can, UsedFor search----------------");
		//A_b
		datalistA = database.searchTable(concept1, "UsedFor", "%", 0.9,false);
		for(int i=0; i<datalistA.size(); i++){
			String[] token = datalistA.get(i).end.split("/");
			listA_b.add(token[0]);	
		}
		if(listA_b.contains(concept2)) return "Yes";
		//B_f
		datalistB = database.searchTable("%", "UsedFor", concept2, 0.9,false);
		for(int i=0; i<datalistB.size(); i++){
			String[] token = datalistB.get(i).start.split("/");		
			listB_f.add(token[0]);
		}
		if(listB_f.contains(concept1)) return "Yes";
		
		System.out.println("----------------can, ReceivesAction search----------------");
		//A_b
		datalistA = database.searchTable(concept1, "ReceivesAction", "%", 0.9,false);
		for(int i=0; i<datalistA.size(); i++){
			String[] token = datalistA.get(i).end.split("/");
			listA_b.add(token[0]);	
		}
		if(listA_b.contains(concept2)) return "Yes";
		//B_f
		datalistB = database.searchTable("%", "ReceivesAction", concept2, 0.9,false);
		for(int i=0; i<datalistB.size(); i++){
			String[] token = datalistB.get(i).start.split("/");		
			listB_f.add(token[0]);
		}
		if(listB_f.contains(concept1)) return "Yes";
		
		System.out.println("----------------can, DefinedAs search----------------");
		//A_DefinedAs
		List<String> listA_definedas = new ArrayList<>();
		datalistA = database.searchTable(concept1, "DefinedAs", "%", 1.5,false);
		for(int i=0; i<datalistA.size(); i++){
			String[] token = datalistA.get(i).end.split("/");		
			listA_definedas.add(token[0]);
		}
		datalistA = database.searchTable("%", "DefinedAs", concept1, 1.5,false);
		for(int i=0; i<datalistA.size(); i++){
			String[] token = datalistA.get(i).start.split("/");		
			listA_definedas.add(token[0]);
		}
		// loop for IsA 
		for (int i = 0; i < listA_definedas.size(); i++) {
			if (listB_f.contains(listA_definedas.get(i)))
				return "Yes";
		}
		System.out.println("----------------can, IsA search----------------");
		//A_isA
		listA_definedas = new ArrayList<>();
		datalistA = database.searchTable(concept1, "IsA", "%", 1.5,false);
		for(int i=0; i<datalistA.size(); i++){
			String[] token = datalistA.get(i).end.split("/");		
			listA_definedas.add(token[0]);
		}
		// loop for  DefinedAs
		for(int i=0; i<listA_definedas.size(); i++){
			if(listB_f.contains(listA_definedas.get(i)))
				return "Yes";
		}
		
		return "No";
	}
	public String has() throws Exception{
		
		return ans;
	}
	public String is() throws Exception{
		List<String> listA_b = new ArrayList<>();
		List<String> listB_f = new ArrayList<>();
		
		// special
		if(concept1.equals(concept2))
			return "Yes";
		
		
		System.out.println("----------------is, Antonym search----------------");
		// A_b
		List<myDATA> datalistA = database.searchTable(concept1, "Antonym", "%",1.5, false);
		for (int i = 0; i < datalistA.size(); i++) {
			String[] token = datalistA.get(i).end.split("/");
			listA_b.add(token[0]);
		}
		if (listA_b.contains(concept2))
			return "No";
		// B_f
		List<myDATA> datalistB = database.searchTable("%", "Antonym", concept2,1.5, false);
		for (int i = 0; i < datalistB.size(); i++) {
			String[] token = datalistB.get(i).start.split("/");
			listB_f.add(token[0]);
		}
		if (listB_f.contains(concept1))
			return "No";
		// 互相contain
		// A_b B_f
		for (int i = 0; i < listA_b.size(); i++) {
			if (listB_f.contains(listA_b.get(i))) {
				return "No";
			}
		}
		
		listA_b = new ArrayList<>();
		listB_f = new ArrayList<>();
		System.out.println("----------------is, IsA search----------------");
		//A_b
		datalistA = database.searchTable(concept1, "IsA", "%", 1.5,false);
		for(int i=0; i<datalistA.size(); i++){
			String[] token = datalistA.get(i).end.split("/");
			listA_b.add(token[0]);
//System.out.println("A " + token[0]);			
		}
		if(listA_b.contains(concept2)) return "Yes";
		//B_f
		datalistB = database.searchTable("%", "IsA", concept2, 1.5,false);
		for(int i=0; i<datalistB.size(); i++){
			String[] token = datalistB.get(i).start.split("/");		
			listB_f.add(token[0]);
//System.out.println("B " + token[0]);
		}
		if(listB_f.contains(concept1)) return "Yes";
		// 互相contain
		//A_b B_f
		for(int i=0; i<listA_b.size(); i++){
			if(listB_f.contains(listA_b.get(i))){
				return "Yes";
			}
		}

		listA_b = new ArrayList<>();
		listB_f = new ArrayList<>();
		System.out.println("----------------is, NotIsA search----------------");
		// A_b
		datalistA = database.searchTable(concept1, "NotIsA", "%",1.5, false);
		for (int i = 0; i < datalistA.size(); i++) {
			String[] token = datalistA.get(i).end.split("/");
			listA_b.add(token[0]);
		}
		if (listA_b.contains(concept2))
			return "No";
		// B_f
		datalistB = database.searchTable("%", "NotIsA", concept2,1.5, false);
		for (int i = 0; i < datalistB.size(); i++) {
			String[] token = datalistB.get(i).start.split("/");
			listB_f.add(token[0]);
		}
		if (listB_f.contains(concept1))
			return "No";
		// 互相contain
		// A_b B_f
		for (int i = 0; i < listA_b.size(); i++) {
			if (listB_f.contains(listA_b.get(i))) {
				return "No";
			}
		}

		listA_b = new ArrayList<>();
		listB_f = new ArrayList<>();
		System.out.println("----------------is, HasProperty search----------------");
		//A_b
		datalistA = database.searchTable(concept1, "HasProperty", "%", 1.5,false);
		for(int i=0; i<datalistA.size(); i++){
			String[] token = datalistA.get(i).end.split("/");
			listA_b.add(token[0]);
		}
		if(listA_b.contains(concept2)) return "Yes";
		//B_f
		datalistB = database.searchTable("%", "HasProperty", concept2, 1.5,false);
		for(int i=0; i<datalistB.size(); i++){
			String[] token = datalistB.get(i).start.split("/");		
			listB_f.add(token[0]);
		}
		if(listB_f.contains(concept1)) return "Yes";
		// 互相contain
		//A_b B_f
		for(int i=0; i<listA_b.size(); i++){
			if(listB_f.contains(listA_b.get(i))){
				return "Yes";
			}
		}

		listA_b = new ArrayList<>();
		listB_f = new ArrayList<>();
		System.out.println("----------------is, NotHasProperty search----------------");
		// A_b
		datalistA = database.searchTable(concept1, "NotHasProperty", "%",1.5, false);
		for (int i = 0; i < datalistA.size(); i++) {
			String[] token = datalistA.get(i).end.split("/");
			listA_b.add(token[0]);
		}
		if (listA_b.contains(concept2))
			return "No";
		// B_f
		datalistB = database.searchTable("%", "NotHasProperty", concept2,1.5, false);
		for (int i = 0; i < datalistB.size(); i++) {
			String[] token = datalistB.get(i).start.split("/");
			listB_f.add(token[0]);
		}
		if (listB_f.contains(concept1))
			return "No";
		// 互相contain
		// A_b B_f
		for (int i = 0; i < listA_b.size(); i++) {
			if (listB_f.contains(listA_b.get(i))) {
				return "No";
			}
		}

		listA_b = new ArrayList<>();
		listB_f = new ArrayList<>();
		System.out.println("----------------is, MadeOf search----------------");
		// A_b
		datalistA = database.searchTable(concept1, "MadeOf", "%",1.5, false);
		for (int i = 0; i < datalistA.size(); i++) {
			String[] token = datalistA.get(i).end.split("/");
			listA_b.add(token[0]);
		}
		if (listA_b.contains(concept2))
			return "Yes";
		// B_f
		datalistB = database.searchTable("%", "MadeOf", concept2,1.5, false);
		for (int i = 0; i < datalistB.size(); i++) {
			String[] token = datalistB.get(i).start.split("/");
			listB_f.add(token[0]);
		}
		if (listB_f.contains(concept1))
			return "Yes";
		// 互相contain
		// A_b B_f
		for (int i = 0; i < listA_b.size(); i++) {
			if (listB_f.contains(listA_b.get(i))) {
				return "Yes";
			}
		}
		
		listA_b = new ArrayList<>();
		listB_f = new ArrayList<>();
		System.out.println("----------------is, DefinedAs search----------------");
		// A_b
		datalistA = database.searchTable(concept1, "DefinedAs", "%",1.5, false);
		for (int i = 0; i < datalistA.size(); i++) {
			String[] token = datalistA.get(i).end.split("/");
			listA_b.add(token[0]);
		}
		if (listA_b.contains(concept2))
			return "Yes";
		// B_f
		datalistB = database.searchTable("%", "DefinedAs", concept2,1.5, false);
		for (int i = 0; i < datalistB.size(); i++) {
			String[] token = datalistB.get(i).start.split("/");
			listB_f.add(token[0]);
		}
		if (listB_f.contains(concept1))
			return "Yes";
		// 互相contain
		// A_b B_f
		for (int i = 0; i < listA_b.size(); i++) {
			if (listB_f.contains(listA_b.get(i))) {
				return "Yes";
			}
		}
		
		listA_b = new ArrayList<>();
		listB_f = new ArrayList<>();
		System.out.println("----------------is, Synonym search----------------");
		// A_b
		datalistA = database.searchTable(concept1, "Synonym", "%",1.5, false);
		for (int i = 0; i < datalistA.size(); i++) {
			String[] token = datalistA.get(i).end.split("/");
			listA_b.add(token[0]);
		}
		if (listA_b.contains(concept2))
			return "Yes";
		// B_f
		datalistB = database.searchTable("%", "Synonym", concept2,1.5, false);
		for (int i = 0; i < datalistB.size(); i++) {
			String[] token = datalistB.get(i).start.split("/");
			listB_f.add(token[0]);
		}
		if (listB_f.contains(concept1))
			return "Yes";
		// 互相contain
		// A_b B_f
		for (int i = 0; i < listA_b.size(); i++) {
			if (listB_f.contains(listA_b.get(i))) {
				return "Yes";
			}
		}
		
		//else 
		return "No";
	}
	public String rel() throws Exception{
		
		System.out.println("----------------rel, first search----------------");
		// Get some words from database
		List<myDATA> datalist = database.searchTable(concept1, question, concept2, 1.5,false);
		List<myDATA> datalist2 = database.searchTable(concept2, question, concept1, 1.5,false);
		// put intorelationList and handle _start
		if(datalist.size()!=0 || datalist2.size()!=0)
			return "Yes";
		
		System.out.println("----------------rel, second search----------------");
		// ?-A-?
		List<String> listA = new ArrayList<>();
		List<String> listB = new ArrayList<>();
		// after
		List<myDATA> datalistA = database.searchTable(concept1, "IsA", "%", 1.5,false);
		datalist2 = database.searchTable(concept1, "PartOf", "%", 1.5,false);
		datalistA.addAll(datalist2);
		datalist2 = database.searchTable(concept1, "SimilarTo", "%", 1.5,false);
		datalistA.addAll(datalist2);
		for(int i=0; i<datalistA.size(); i++){
			listA.add(datalistA.get(i).end);
		}
		// before
		datalistA = database.searchTable("%", "IsA", concept1, 1.5,false);
		datalist2 = database.searchTable("%", "PartOf", concept1, 1.5,false);
		datalistA.addAll(datalist2);
		datalist2 = database.searchTable("%", "SimilarTo", concept1, 1.5,false);
		datalistA.addAll(datalist2);
		for(int i=0; i<datalistA.size(); i++){
			listA.add(datalistA.get(i).start);
		}
		// datalistA has B?
		for(int i=0; i<listA.size(); i++){
			datalist2 = database.searchTable(listA.get(i), question, concept2, 1.5,false);
			if(datalist2.size()!=0){
				return "Yes";
			}
			datalist2 = database.searchTable(concept2, question, listA.get(i), 1.5,false);
			if(datalist2.size()!=0){
				return "Yes";
			}
		}
		// ?-B-?
		List<myDATA> datalistB = database.searchTable(concept2, "IsA", "%", 1.5,false);
		datalist2 = database.searchTable(concept2, "PartOf", "%", 1.5,false);
		datalistB.addAll(datalist2);
		datalist2 = database.searchTable(concept2, "SimilarTo", "%", 1.5,false);
		datalistB.addAll(datalist2);
		for(int i=0; i<datalistB.size(); i++){
			listB.add(datalistB.get(i).end);
		}
		// before
		datalistB = database.searchTable("%", "IsA", concept2, 1.5,false);
		datalist2 = database.searchTable("%", "PartOf", concept2, 1.5,false);
		datalistB.addAll(datalist2);
		datalist2 = database.searchTable("%", "SimilarTo", concept2, 1.5,false);
		datalistB.addAll(datalist2);
		for(int i=0; i<datalistB.size(); i++){
			listB.add(datalistB.get(i).start);
		}
		// datalistA has B?
		for(int i=0; i<datalistB.size(); i++){
			datalist2 = database.searchTable(listB.get(i), question, concept1, 1.5,false);
			if(datalist2.size()!=0){
				return "Yes";
			}
			datalist2 = database.searchTable(concept1, question, listB.get(i), 1.5,false);
			if(datalist2.size()!=0){
				return "Yes";
			}
		}
		// A-?-?-B
		for(int i=0; i<listA.size(); i++){
			for(int j=1; j<listB.size(); j++){
				datalist = database.searchTable(listA.get(i), question, listB.get(j), 1.5, false);
				if(datalist.size()!=0){
					return "Yes";
				}
				datalist = database.searchTable(listB.get(j), question, listA.get(i), 1.5, false);
				if(datalist.size()!=0){
					return "Yes";
				}
				
			}
		}
		
		return "No";

	}
}
