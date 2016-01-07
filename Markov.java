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
		//��concept��J _start ��
		Vector<String> startWords = markovChain.get("_start");
		
		//open database
		System.out.println("----------------open SQLite----------------");
		database.buildSQLite();
		database.connDB("ConceptNet_en");
		database.createStmt();
			
		/*handle hash map*/
		// ���Xconcept�����Yhash map
		
		// �Hconcept=end ���e�@�h
		System.out.println("----------------before levels----------------");
		// Get some words from database
		List<myDATA> datalist = database.searchTable("%", "%", concept, threshold_f,false);
		// put intorelationList and handle _start
		for (int i = 0; i < datalist.size(); i++) {
			if(hasRelationType(datalist.get(i).rel)){
				System.out.println(datalist.get(i).start);
				startWords.add(datalist.get(i).start); startWords.add(this.concept); // �W�[�쥻input concept��start��
				ListA_f.add(datalist.get(i).start); //part1_2
				if(!datalist.get(i).start.equals(this.concept))relationList.add(datalist.get(i).start + " " + datalist.get(i).rel+ " " + datalist.get(i).end + " " + datalist.get(i).weight);
			}
		}
		// �p�G�d����F��
		if (datalist.size() == 0) return 2; // �e�@�h�䤣��F��
		
		// �Hconcept=start �����h
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
				if(hasRelationType(datalist.get(i).rel)){
					//�W�[concept�b_start����
					startWords.add(this.concept);startWords.add(this.concept);startWords.add(this.concept);
					if(!startWords.contains(datalist.get(i).end)&&!query.contains(datalist.get(i).end))
						relationList.add(datalist.get(i).start+" "+datalist.get(i).rel + " " + datalist.get(i).end+" "+datalist.get(i).weight);
					if(question==1 && index==0 && !query.contains(datalist.get(i).end))
						query.add(datalist.get(i).end);
					if(index==0) ListA_b.add(datalist.get(i).end); //part1_2 ��O�A��A����@�Ӯ�
					if(question==2) System.out.println(datalist.get(i).end);
				}
			}
			index++;
		}
		//�p�G�d����F�� return
		if(question==1 && query.size()<2) return 1; // ��G�h�䤣��F��
		
		//part1_2
		if(question==2){
			/*�����*/
			// �Hconcept=end ���e�@�h
			System.out.println("----------------before level concept2----------------");
			// Get some words from database
			datalist = database.searchTable("%", "%", concept2, threshold_f,false);
			// put intorelationList and handle _start
			for (int i = 0; i < datalist.size(); i++) {
				if(hasRelationType(datalist.get(i).rel)){
					System.out.println(datalist.get(i).start);
					//startWords.add(datalist.get(i).start); startWords.add(this.concept2); // �W�[�쥻input concept2����
					ListB_f.add(datalist.get(i).start); //part1_2
					//if(!datalist.get(i).start.equals(this.concept2))relationList.add(datalist.get(i).start + " " + datalist.get(i).rel+ " " + datalist.get(i).end + " " + datalist.get(i).weight);
				}
			}
			// �p�G�d����F��
			if (datalist.size() == 0) return 2; // �e�@�h�䤣��F��
			
			// �Hconcept=start ����@�h
			System.out.println("----------------after level concept2----------------");
			// Get some words from database
			datalist = database.searchTable(concept2, "%", "%", threshold_b, false);
			// put into query and relationList
			for(int i=0; i<datalist.size(); i++){
				if(hasRelationType(datalist.get(i).rel)){
					System.out.println(datalist.get(i).end);
					//if(!startWords.contains(datalist.get(i).end)&&!query.contains(datalist.get(i).end))
					//	relationList.add(datalist.get(i).start+" "+datalist.get(i).rel + " " + datalist.get(i).end+" "+datalist.get(i).weight);
					ListB_b.add(datalist.get(i).end); //part1_2
				}
			}
			//�p�G�d����F�� return
			if(datalist.size()<2) return 1; // ��G�h�䤣��F��
			
			//reset relationList startWords
			relationList = new ArrayList<>();
			startWords = new Vector<>();
			
			/* ���concepts���Y */
			System.out.println("----------------find two concepts relations----------------");
			//if A_f has B  (B-A)
			if(ListA_f.contains(concept2)){
				datalist = database.searchTable(concept2, "%", concept, threshold_f,false);
				for (int i = 0; i < datalist.size(); i++) {
					if(hasRelationType(datalist.get(i).rel)){
System.out.println("22222222222222222222222222222222");
						startWords.add(concept2);
						for(int repeat=0; repeat<10; repeat++){
							if(!concept2.equals(this.concept))relationList.add(datalist.get(i).start + " " + datalist.get(i).rel+ " " + datalist.get(i).end + " " + datalist.get(i).weight);
						}
					}
				}
				//�[�JA_b���F��
				datalist = database.searchTable(concept, "%", "%", threshold_f,false);
				for (int i = 0; i < datalist.size(); i++) {
					if(hasRelationType(datalist.get(i).rel)){
System.out.println("22222222222222222222222222222222");
						for(int repeat=0; repeat<10; repeat++){
							if(!datalist.get(i).end.equals(this.concept))relationList.add(datalist.get(i).start + " " + datalist.get(i).rel+ " " + datalist.get(i).end + " " + datalist.get(i).weight);
						}
					}
				}
			}
			//if A_f has B_b (B-?-A)
			for(int x=0; x<ListB_b.size(); x++){ //ListB_b index
				if(ListA_f.contains(ListB_b.get(x))){
					// ?-A
					datalist = database.searchTable(ListB_b.get(x), "%", concept, threshold_f,false);
					for (int i = 0; i < datalist.size(); i++) {
						if(hasRelationType(datalist.get(i).rel)){
System.out.println("33333333333333333333333333333333  " );
							startWords.add(concept2);
							for(int repeat=0; repeat<10; repeat++){
								if(!ListB_b.get(x).equals(this.concept))relationList.add(datalist.get(i).start + " " + datalist.get(i).rel+ " " + datalist.get(i).end + " " + datalist.get(i).weight);
							}
						}
					}
					// B-?
					datalist = database.searchTable(concept2, "%", ListB_b.get(x), threshold_f,false);
					for (int i = 0; i < datalist.size(); i++) {
						if(hasRelationType(datalist.get(i).rel)){
System.out.println("33333333333333333333333333333333  " );
							for(int repeat=0; repeat<10; repeat++){
								if(!ListB_b.get(x).equals(this.concept2))relationList.add(datalist.get(i).start + " " + datalist.get(i).rel+ " " + datalist.get(i).end + " " + datalist.get(i).weight);
							}
						}
					}
				}
			}
			//if A_b has B  (A-B)
			if(ListA_b.contains(concept2)){
				datalist = database.searchTable(concept, "%", concept2, threshold_f,false);
				for (int i = 0; i < datalist.size(); i++) {
					if(hasRelationType(datalist.get(i).rel)){
						startWords.add(concept);
System.out.println("4444444444444444444444444444444444");
						for(int repeat=0; repeat<10; repeat++){
							if(!concept2.equals(this.concept))relationList.add(datalist.get(i).start + " " + datalist.get(i).rel+ " " + datalist.get(i).end + " " + datalist.get(i).weight);
						}
					}
				}
				//�[�JB_b���F��
				datalist = database.searchTable(concept2, "%", "%", threshold_f,false);
				for (int i = 0; i < datalist.size(); i++) {
					if(hasRelationType(datalist.get(i).rel)){
System.out.println("4444444444444444444444444444444444");
						for(int repeat=0; repeat<10; repeat++){
							if(!datalist.get(i).end.equals(this.concept2))relationList.add(datalist.get(i).start + " " + datalist.get(i).rel+ " " + datalist.get(i).end + " " + datalist.get(i).weight);
						}
					}
				}
			}
			//if A_b has B_f (A-?-B)
			for(int x=0; x<ListB_f.size(); x++){ //ListB_f index
				if(ListA_b.contains(ListB_f.get(x))){ 
					// ?-B
					datalist = database.searchTable(ListB_f.get(x), "%", concept2, threshold_f,false);
					for (int i = 0; i < datalist.size(); i++) {
						if(hasRelationType(datalist.get(i).rel)){
							startWords.add(concept);
System.out.println("555555555555555555555555555555555");
							for(int repeat=0; repeat<10; repeat++){
								if(!ListB_f.get(x).equals(this.concept2))relationList.add(datalist.get(i).start + " " + datalist.get(i).rel+ " " + datalist.get(i).end + " " + datalist.get(i).weight);
							}
						}
					}
					// A-?
					datalist = database.searchTable(concept, "%", ListB_f.get(x), threshold_f,false);
					for (int i = 0; i < datalist.size(); i++) {
						if(hasRelationType(datalist.get(i).rel)){
System.out.println("555555555555555555555555555555555");
							for(int repeat=0; repeat<10; repeat++){
								if(!ListB_f.get(x).equals(this.concept))relationList.add(datalist.get(i).start + " " + datalist.get(i).rel+ " " + datalist.get(i).end + " " + datalist.get(i).weight);
							}
						}
					}
				}
			}
			//A_f B_f ,�[�J_start
			for(int x=0; x<ListA_f.size(); x++){
				if(ListB_f.contains(ListA_f.get(x))){
					datalist = database.searchTable(ListA_f.get(x), "%", concept2, threshold_f,false);
					List<myDATA> datalist2 = database.searchTable(ListA_f.get(x), "%", concept, threshold_f,false);
					for(int y=0; y<datalist.size(); y++){
						for(int z=0; z<datalist2.size(); z++){
							if(hasRelationType(datalist.get(y).rel) && datalist.get(y).rel.equals(datalist2.get(z).rel)){
								if(!datalist.get(y).start.equals(concept) && !datalist2.get(z).start.equals(concept2)){
									startWords.add(datalist.get(y).start);
									for(int repeat=0; repeat<10; repeat++){
										relationList.add(datalist.get(y).start + " " + datalist.get(y).rel + " "+ concept + "_and_" + concept2 + " " + datalist.get(y).weight);
									}
								}
System.out.println("666666666666666666666666666666666");
							}
						}
					}
				}
			}
			//A_b B_b ,�[�J_start
			for(int x=0; x<ListA_b.size(); x++){
				if(ListB_b.contains(ListA_b.get(x))){
					datalist = database.searchTable( concept,"%", ListA_b.get(x), threshold_f,false);
					List<myDATA> datalist2 = database.searchTable( concept2, "%", ListA_b.get(x),threshold_f,false);
					for(int y=0; y<datalist.size(); y++){
						for(int z=0; z<datalist2.size(); z++){
							if(hasRelationType(datalist.get(y).rel) && datalist.get(y).rel.equals(datalist2.get(z).rel)){
								if(!datalist.get(y).end.equals(concept) && !datalist2.get(z).end.equals(concept2)){
									startWords.add(concept + "_and_" + concept2 );
									for(int repeat=0; repeat<10; repeat++){
										relationList.add(concept + "_and_" + concept2 + " " + datalist.get(y).rel + " "+ datalist.get(y).end + " " + datalist.get(y).weight);
									}
								}
System.out.println("7777777777777777777777777777777777");
							}
							if(hasRelationType(datalist2.get(z).rel)){//��JB��rel
								if(!datalist.get(y).end.equals(concept) && !datalist2.get(z).end.equals(concept2)){
									startWords.add(concept + "_and_" + concept2 );
									for(int repeat=0; repeat<10; repeat++){
										relationList.add(concept + "_and_" + concept2 + " " + datalist2.get(z).rel + " "+ datalist2.get(z).end + " " + datalist2.get(z).weight);
									}
								}
System.out.println("7777777777777777777777777777777777");
							}
						}
					}
				}
			}
		}
		
		// ���hashmap(markovChain)��
		// handle _start
		markovChain.put("_start", startWords);
		// Add the words to the hash table
		if(relationList.size()>5)addRelation(relationList);
		else return 3;

		
		/* �ͥX�y�l */
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
        // �ھ�rel�y�y
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
	
	
	// ��relation list��Jkey-concpet��hash map list���U
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
		if(wordSelection==null) { //�]������_start�S��next
System.out.println("wordSelection is null  " +  nextWord + "~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
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
			
			//�����ӼƤ�weight sum
			weightSum += Double.parseDouble(words[3]);
			count++;
			
			// break
			if(wordSelection==null)break;
			
			// random break
			if(rnd.nextInt(1)==1) break;
			
			//next
			wordSelectionLen = wordSelection.size();	
		}
		
		// print ���G
		//System.out.println(newPhrase.toString() +"\n" + "AvgWeight: "+weightSum/count + " length: " + newPhrase.size());
		
		//return 
		mySENTENCE mySentence = new mySENTENCE();
		mySentence.concepts = newPhrase;
		mySentence.score = weightSum/count;
		mySentence.length = newPhrase.size();
		return mySentence;
	}
	
	private boolean hasRelationType(String rel){
		if (rel.equals("RelatedTo") 
				|| rel.equals("Compoun")
				|| rel.equals("wordnet/adjectivePertainsTo")
				|| rel.equals("Attribute")
				|| rel.equals("dbpedia/influenced")
				|| rel.equals("dbpedia/notableIdea")
				|| rel.equals("Entails")
				|| rel.equals("wordnet/adverbPertainsTo")
				|| rel.equals("dbpedia/spokenIn")
				|| rel.equals("dbpedia/languageFamily")
				|| rel.equals("dbpedia/knowFor")
				|| rel.equals("wordnet/participleOf")
				|| rel.equals("dbpedia/field")
				|| rel.equals("CompoundDerivedFrom")
				|| rel.equals("NotCauses")){
			return false;
		} 
		return true;
	}
}
