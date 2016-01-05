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
	
	private String concept;	
	// Hashmap
	public static Hashtable<String, Vector<String>> markovChain = new Hashtable<String, Vector<String>>();
	static Random rnd = new Random();
	
	public int num_sentence = 20;
	
	public Markov(String concept){
		this.concept = concept;
	}
	
	// one concept
	public int one_concept_question(double threshold_b, double threshold_f) throws Exception{
		DATABASE database = new DATABASE();
		List<String> query = new ArrayList<>();
		List<String> relationList= new ArrayList<>();
		
		// Create the first two entries (k:_start, k:_end)
		markovChain.put("_start", new Vector<String>());
		markovChain.put("_end", new Vector<String>());
		//��concept��J _start ��
		Vector<String> startWords = markovChain.get("_start");
		startWords.add(this.concept);
		// �W�[�쥻input concept��start��
		startWords.add(this.concept); startWords.add(this.concept); startWords.add(this.concept);
		
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
			System.out.println(datalist.get(i).start);
			startWords.add(datalist.get(i).start);
			relationList.add(datalist.get(i).start + " " + datalist.get(i).rel+ " " + datalist.get(i).end + " " + datalist.get(i).weight);
		}
		// �p�G�d����F��
		if (datalist.size() == 0) return 2;
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
				relationList.add(datalist.get(i).start+" "+datalist.get(i).rel + " " + datalist.get(i).end+" "+datalist.get(i).weight);
				if(index==0 && !query.contains(datalist.get(i).end))
					query.add(datalist.get(i).end);
			}
			index++;
		}
		//�p�G�d����F�� return
		if(query.size()<2) return 1;
		// handle _start
		markovChain.put("_start", startWords);
		// Add the words to the hash table
		addRelation(relationList);

		// �ͥX�y�l
		// random concepts
		System.out.println("----------------generatl sentences----------------");
		List<mySENTENCE> mySentenceList = new ArrayList<>();
		int time=0;
		while(mySentenceList.size()<=num_sentence){
			mySENTENCE mySentence = generateConcepets();
			if(mySentence.isSentence(1, this.concept, ""))mySentenceList.add(mySentence);
			time++;
			if(time>200) {
				//System.out.println("stop in loop");
				return 3;
			}
		}
        // �ھ�rel�y�y
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
		if(wordSelection==null) return null; //�]������_start�S��next
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
			if(rnd.nextInt(3)==1) break;
			
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
}
