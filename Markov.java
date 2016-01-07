import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Random;
import java.util.Vector;
import java.util.Collections;
import java.util.Comparator;


public class Markov {
	
	private String concept, concept2;	
	// Hashmap
	public static Hashtable<String, Vector<String>> markovChain = new Hashtable<String, Vector<String>>();
	static Random rnd = new Random();
	
	public int num_sentence = 25;
	
	public Markov(String concept, String concept2){
		this.concept = concept;
		this.concept2 = concept2;
	}
	
	// one concept
	public int question(int question, double threshold_b, double threshold_f) throws Exception{
		DATABASE database = new DATABASE();
		List<String> query = new ArrayList<>();
		List<String> relationList= new ArrayList<>();
		
		// part2 initial
		Vector<String> ListA_f = new Vector<String>();
		Vector<String> ListA_b = new Vector<String>();
		Vector<String> ListB_f = new Vector<String>();
		Vector<String> ListB_b = new Vector<String>();
		
		// Create the first two entries (k:_start, k:_end)
		markovChain.put("_start", new Vector<String>());
		markovChain.put("_end", new Vector<String>());
		//把concept放入 _start 中
		Vector<String> startWords = markovChain.get("_start");
		startWords.add(this.concept);
		// 增加原本input concept的start比重
		startWords.add(this.concept);startWords.add(this.concept); startWords.add(this.concept); startWords.add(this.concept); startWords.add(this.concept); startWords.add(this.concept); startWords.add(this.concept); startWords.add(this.concept);  
		
		//open database
		System.out.println("----------------open SQLite----------------");
		database.buildSQLite();
		database.connDB("ConceptNet_en");
		database.createStmt();
			
		/*handle hash map*/
		// 做出concept的關係hash map
		
		// 以concept=end 往前一層
		System.out.println("----------------before levels----------------");
		// Get some words from database
		List<myDATA> datalist = database.searchTable("%", "%", concept, threshold_f,false);
		// put intorelationList and handle _start
		for (int i = 0; i < datalist.size(); i++) {
			System.out.println(datalist.get(i).start);
			startWords.add(datalist.get(i).start); startWords.add(this.concept); // 增加原本input concept的start比重
			ListA_f.add(datalist.get(i).start); //part1_2
			if(!datalist.get(i).start.equals(this.concept))relationList.add(datalist.get(i).start + " " + datalist.get(i).rel+ " " + datalist.get(i).end + " " + datalist.get(i).weight);
		}
		// 如果查不到東西
		if (datalist.size() == 0) return 2; // 前一層找不到東西
		
		// 以concept=start 往後兩層
		System.out.println("----------------after levels----------------");
		query.add(this.concept);
		int index = 0;
		while(index<query.size()) {
			// Get some words from database
			datalist = database.searchTable(query.get(index), "%", "%", threshold_b, false);
			//printf
			System.out.println(query.get(index));
			//System.out.println(datalist.size());
			// put into query and relationList
			for(int i=0; i<datalist.size(); i++){
				if(!startWords.contains(datalist.get(i).end)&&!query.contains(datalist.get(i).end))
					relationList.add(datalist.get(i).start+" "+datalist.get(i).rel + " " + datalist.get(i).end+" "+datalist.get(i).weight);
				if(question==1 && index==0 && !query.contains(datalist.get(i).end))
					query.add(datalist.get(i).end);
				if(index==0) ListA_b.add(datalist.get(i).end); //part1_2 當是再爬A的後一個時
				if(question==2) System.out.println(datalist.get(i).end);
			}
			index++;
		}
		//如果查不到東西 return
		if(question==1 && query.size()<2) return 1; // 後二層找不到東西
		
		//part1_2
		if(question==2){
			/*爬資料*/
			// 以concept=end 往前一層
			System.out.println("----------------before level concept2----------------");
			// Get some words from database
			datalist = database.searchTable("%", "%", concept2, threshold_f,false);
			// put intorelationList and handle _start
			for (int i = 0; i < datalist.size(); i++) {
				System.out.println(datalist.get(i).start);
				//startWords.add(datalist.get(i).start); startWords.add(this.concept2); // 增加原本input concept2的比重
				ListB_f.add(datalist.get(i).start); //part1_2
				//if(!datalist.get(i).start.equals(this.concept2))relationList.add(datalist.get(i).start + " " + datalist.get(i).rel+ " " + datalist.get(i).end + " " + datalist.get(i).weight);
			}
			// 如果查不到東西
			if (datalist.size() == 0) return 2; // 前一層找不到東西
			
			// 以concept=start 往後一層
			System.out.println("----------------after level concept2----------------");
			// Get some words from database
			datalist = database.searchTable(concept2, "%", "%", threshold_b, false);
			// put into query and relationList
			for(int i=0; i<datalist.size(); i++){
				System.out.println(datalist.get(i).end);
				//if(!startWords.contains(datalist.get(i).end)&&!query.contains(datalist.get(i).end))
				//	relationList.add(datalist.get(i).start+" "+datalist.get(i).rel + " " + datalist.get(i).end+" "+datalist.get(i).weight);
				ListB_b.add(datalist.get(i).end); //part1_2
			}
			//如果查不到東西 return
			if(datalist.size()<2) return 1; // 後二層找不到東西
			
			//reset relationList startWords
			relationList = new ArrayList<>();
			startWords = new Vector<>();
			
			// startWords add two concepts
			startWords.add(concept); startWords.add(concept2);
			
			/* 找兩concepts關係 */
			System.out.println("----------------find two concepts relations----------------");
			//if A_f has B  (B-A)
			if(ListA_f.contains(concept2)){
				datalist = database.searchTable(concept2, "%", concept, threshold_f,false);
				for (int i = 0; i < datalist.size(); i++) {
					for(int repeat=0; repeat<10; repeat++){
						if(!concept2.equals(this.concept))relationList.add(datalist.get(i).start + " " + datalist.get(i).rel+ " " + datalist.get(i).end + " " + datalist.get(i).weight);
					}
				}
				//加入A_b的東西
				datalist = database.searchTable(concept, "%", "%", threshold_f,false);
				for (int i = 0; i < datalist.size(); i++) {
					for(int repeat=0; repeat<10; repeat++){
						if(!datalist.get(i).end.equals(this.concept))relationList.add(datalist.get(i).start + " " + datalist.get(i).rel+ " " + datalist.get(i).end + " " + datalist.get(i).weight);
					}
				}
System.out.println("22222222222222222222222222222222");
			}
			//if A_f has B_b (B-?-A)
			for(int x=0; x<ListB_b.size(); x++){ //ListB_b index
				if(ListA_f.contains(ListB_b.get(x))){ 
					// ?-A
					datalist = database.searchTable(ListB_b.get(x), "%", concept, threshold_f,false);
					for (int i = 0; i < datalist.size(); i++) {
						for(int repeat=0; repeat<10; repeat++){
							if(!ListB_b.get(x).equals(this.concept))relationList.add(datalist.get(i).start + " " + datalist.get(i).rel+ " " + datalist.get(i).end + " " + datalist.get(i).weight);
						}
					}
					// B-?
					datalist = database.searchTable(concept2, "%", ListB_b.get(x), threshold_f,false);
					for (int i = 0; i < datalist.size(); i++) {
						for(int repeat=0; repeat<10; repeat++){
							if(!ListB_b.get(x).equals(this.concept2))relationList.add(datalist.get(i).start + " " + datalist.get(i).rel+ " " + datalist.get(i).end + " " + datalist.get(i).weight);
						}
					}
System.out.println("33333333333333333333333333333333  " );
				}
			}
			//if A_b has B  (A-B)
			if(ListA_b.contains(concept2)){
				datalist = database.searchTable(concept, "%", concept2, threshold_f,false);
				for (int i = 0; i < datalist.size(); i++) {
					for(int repeat=0; repeat<10; repeat++){
						if(!concept2.equals(this.concept))relationList.add(datalist.get(i).start + " " + datalist.get(i).rel+ " " + datalist.get(i).end + " " + datalist.get(i).weight);
					}
				}
				//加入B_b的東西
				datalist = database.searchTable(concept2, "%", "%", threshold_f,false);
				for (int i = 0; i < datalist.size(); i++) {
					for(int repeat=0; repeat<10; repeat++){
						if(!datalist.get(i).end.equals(this.concept2))relationList.add(datalist.get(i).start + " " + datalist.get(i).rel+ " " + datalist.get(i).end + " " + datalist.get(i).weight);
					}
				}
System.out.println("4444444444444444444444444444444444");
			}
			//if A_b has B_f (A-?-B)
			for(int x=0; x<ListB_f.size(); x++){ //ListB_f index
				if(ListA_b.contains(ListB_f.get(x))){ 
					// ?-B
					datalist = database.searchTable(ListB_f.get(x), "%", concept2, threshold_f,false);
					for (int i = 0; i < datalist.size(); i++) {
						for(int repeat=0; repeat<10; repeat++){
							if(!ListB_f.get(x).equals(this.concept2))relationList.add(datalist.get(i).start + " " + datalist.get(i).rel+ " " + datalist.get(i).end + " " + datalist.get(i).weight);
						}
					}
					// A-?
					datalist = database.searchTable(concept, "%", ListB_f.get(x), threshold_f,false);
					for (int i = 0; i < datalist.size(); i++) {
						for(int repeat=0; repeat<10; repeat++){
							if(!ListB_f.get(x).equals(this.concept))relationList.add(datalist.get(i).start + " " + datalist.get(i).rel+ " " + datalist.get(i).end + " " + datalist.get(i).weight);
						}
					}
System.out.println("555555555555555555555555555555555");
				}
			}
			//A_f B_f ,加入_start
			for(int x=0; x<ListA_f.size(); x++){
				if(ListB_f.contains(ListA_f.get(x))){
					datalist = database.searchTable(ListA_f.get(x), "%", concept2, threshold_f,false);
					List<myDATA> datalist2 = database.searchTable(ListA_f.get(x), "%", concept, threshold_f,false);
					for(int y=0; y<datalist.size(); y++){
						for(int z=0; z<datalist2.size(); z++){
							if(!datalist.get(y).start.equals(concept) && !datalist.get(y).start.equals(concept2)){
								startWords.add(datalist.get(y).start);
								for(int repeat=0; repeat<10; repeat++){
									relationList.add(datalist.get(y).start + " " + datalist.get(y).rel + " "+ concept + "_and_" + concept2 + " " + (datalist.get(y).weight+datalist2.get(z).weight)/2);
								}
							}
							
						}
					}
System.out.println("666666666666666666666666666666666");
				}
			}
			//A_b B_b ,加入_start
			for(int x=0; x<ListA_b.size(); x++){
				if(ListB_b.contains(ListA_b.get(x))){
					datalist = database.searchTable( concept2,"%", ListA_b.get(x), threshold_f,false);
					List<myDATA> datalist2 = database.searchTable( concept, "%", ListA_b.get(x),threshold_f,false);
					for(int y=0; y<datalist.size(); y++){
						for(int z=0; z<datalist2.size(); z++){
							if(!datalist.get(y).end.equals(concept) && !datalist.get(y).end.equals(concept2)){
								startWords.add(concept + "_and_" + concept2 );
								for(int repeat=0; repeat<10; repeat++){
									relationList.add(concept + "_and_" + concept2 + " " + datalist.get(y).rel + " "+ datalist.get(y).end + " " + (datalist.get(y).weight+datalist2.get(z).weight)/2);
								}
							}
							
						}
					}
System.out.println("7777777777777777777777777777777");
				}
			}
		}
		
		// 放到hashmap(markovChain)中
		// handle _start
		markovChain.put("_start", startWords);
		// Add the words to the hash table
		if(!relationList.isEmpty())addRelation(relationList);
		else return 3;

		
		/* 生出句子 */
		// random concepts
		System.out.println("----------------generate concepts----------------");
		List<mySENTENCE> mySentenceList = new ArrayList<>();
		int time=0;
		while(mySentenceList.size()<=num_sentence){
			mySENTENCE mySentence = generateConcepets();
			if(mySentence!=null && mySentence.isSentence(this.concept, concept2))
				mySentenceList.add(mySentence);
			time++;
			if(time>250 && time > relationList.size()) {
				System.out.println("too many loop");
				return 3;
			}
		}
        // 根據rel造句
		System.out.println("----------------build sentences----------------");
        for (int i=0; i<mySentenceList.size(); i++){
        	mySentenceList.get(i).buildSentence(database);
        	mySentenceList.get(i).calScore();
        }
		//sort
        Collections.sort(mySentenceList,
        new Comparator<mySENTENCE>() {
            public int compare(mySENTENCE o1, mySENTENCE o2) {
                if(o1.score<o2.score)
                	return 1;
                if(o1.score==o2.score)
                	return 0;
                return -1;
            }
        });
       // print
        System.out.println("----------------show sentences----------------");
       for (int i=0; i<mySentenceList.size(); i++){
       		System.out.println(mySentenceList.get(i).score+"  "+mySentenceList.get(i).concepts.toString());
       		System.out.println(mySentenceList.get(i).score+"  "+mySentenceList.get(i).sentence);
       }
        
       System.out.println("----------------close SQLite----------------");
		// close database
		database.closeStmt();
		database.disconnDB();
		
		System.out.println("----------------answer----------------");
		System.out.println(mySentenceList.get(0).sentence);
		
		return 0;
	}
	
	
	// 把relation list放入key-concpet的hash map list底下
	public void addRelation(List<String> relation_list){
		int i=0;
		
		while(i<relation_list.size()){	
			
			String[] words = relation_list.get(i).split(" ");						
			Vector<String> suffix = markovChain.get(words[0]);
			
			if (suffix == null) {
				suffix = new Vector<String>();
			}
			
			// weight random
			int time = (int)(Double.parseDouble(words[3])*5);
			for (int j=0; j<time; j++)
				suffix.add(relation_list.get(i));
			
			markovChain.put(words[0], suffix);
			
			i++;
		}
		
	}
	
	
	/*
	 * Generate a markov phrase
	 */
	public static mySENTENCE generateConcepets() {
		
		// Vector to hold the phrase
		Vector<String> newPhrase = new Vector<String>();
		
		// String for the next word
		String nextWord = "";
		String subSentence = "";
				
		// Select the first word
		Vector<String> startWords = markovChain.get("_start");
		int startWordsLen = startWords.size();
		nextWord = startWords.get(rnd.nextInt(startWordsLen));
		newPhrase.add(nextWord);
		Vector<String> wordSelection = markovChain.get(nextWord);
		if(wordSelection==null) { //因為有些_start沒有next
			return null; 
		}
		int wordSelectionLen = wordSelection.size();
		
		// Keep looping through the words until we've reached the end
		double weightSum=0;
		int count=0;
		while (nextWord.charAt(nextWord.length()-1) != '.' && wordSelectionLen > 0) {
			
			subSentence = wordSelection.get(rnd.nextInt(wordSelectionLen));
			String[] words = subSentence.split(" ");
			nextWord = words[2];
			newPhrase.add(words[1]); newPhrase.add(words[2]);
			wordSelection = markovChain.get(nextWord);
			
			//紀錄個數及weight sum
			weightSum += Double.parseDouble(words[3]);
			count++;
			
			// break
			if(wordSelection==null)break;
			
			// random break
			if(rnd.nextInt(1)==1) break;
			
			//next
			wordSelectionLen = wordSelection.size();	
		}
		
		// print 結果
		//System.out.println(newPhrase.toString() +"\n" + "AvgWeight: "+weightSum/count + " length: " + newPhrase.size());
		
		//return 
		mySENTENCE mySentence = new mySENTENCE();
		mySentence.concepts = newPhrase;
		mySentence.score = weightSum/count;
		mySentence.length = newPhrase.size();
		return mySentence;
	}
}