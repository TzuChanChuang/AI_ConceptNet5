import java.util.Vector;


public class mySENTENCE {
	public Vector<String> concepts;
	public String sentence="";
	public int length;
	public double score;
	
	public void buildSentence(){
		int n_num=0, v_num=0;
		
		for (int i=0; i<concepts.size(); i=i+2){
			//conceptA rel conceptB 
			//     i       i+1    i+2
			
			switch (concepts.get(i+1)) {
				case "InstanceOf":
					sentence = sentence + concepts.get(i) + " is kind of " + concepts.get(i+2) +" ";
					n_num=n_num+2;
					break;
				case "IsA":
					sentence = sentence + concepts.get(i) + " is a " + concepts.get(i+2) +" ";
					n_num=n_num+2;
					break;
				case "DerivedFrom":
					sentence = sentence + concepts.get(i) + " is derived from " + concepts.get(i+2) +" ";
					break;
				case "Synonym":
					sentence = sentence + concepts.get(i) + " is synonymous with " + concepts.get(i+2) +" ";
					break;
				case "dbpedia":
					sentence = sentence + concepts.get(i) + " may have a type of " + concepts.get(i+2) +" ";
					n_num = n_num+2;
					break;
				case "EtymologicallyDerivedFrom":
					sentence = sentence + concepts.get(i) + " is etymologically derived from " + concepts.get(i+2) +" ";
					break;
				case "UsedFor":
					sentence = sentence + ". If you " + concepts.get(i) + ", you may want (to) " + concepts.get(i+2) +" ";
					break;
				case "AtLocation":
					sentence = sentence + ". There is "+ concepts.get(i) + " at " + concepts.get(i+2) +" ";
					break;
				case "HasSubevent":
					sentence = sentence + ". When you "+concepts.get(i) + ", you may  " + concepts.get(i+2) +" ";
					break;
				case "HasPrerequisite":
					sentence = sentence + ". If you want to "+concepts.get(i) + ", you have to " + concepts.get(i+2) +" ";
					break;
				case "CapableOf":
					sentence = sentence + "" + concepts.get(i) + " can " + concepts.get(i+2) +" ";
					break;
				case "Antonym":
					sentence = sentence + "" + concepts.get(i) + " is contrast to " + concepts.get(i+2) +" ";
					break;
				case "Causes":
					sentence = sentence + "" + concepts.get(i) + " may cause " + concepts.get(i+2) +" ";
					break;
				case "MotivatedByGoal":
					sentence = sentence + ". You would " + concepts.get(i) + "  becasue you want (to) " + concepts.get(i+2) +" ";
					break;
				case "HasProperty":
					sentence = sentence + "" + concepts.get(i) + " may be " + concepts.get(i+2) +" ";
					break;
				case "SimilarTo":
					sentence = sentence + "" + concepts.get(i) + "  and " + concepts.get(i+2) +" are similar";
					break;
				case "PartOf":
					sentence = sentence + "" + concepts.get(i) + " is part of " + concepts.get(i+2) +" ";
					break;
				case "HasContext":
					sentence = sentence + "" + concepts.get(i) + " is term in " + concepts.get(i+2) +" ";
					break;
				case "HasA":
					sentence = sentence + "" + concepts.get(i) + " has a " + concepts.get(i+2) +" ";
					break;
				case "HasFirstSubevent":
					sentence = sentence + ". Before " + concepts.get(i) + " you will " + concepts.get(i+2) +" ";
					break;
				case "DefinedAs":
					sentence = sentence + "" + concepts.get(i) + " is defined as " + concepts.get(i+2) +" ";
					break;
				case "HasLastSubevent":
					sentence = sentence + ". After " + concepts.get(i) + " you may " + concepts.get(i+2) +" ";
					break;
				case "MemberOf":
					sentence = sentence + "" + concepts.get(i) + " is member of " + concepts.get(i+2) +" ";
					break;
				case "CausesDesire":
					sentence = sentence + "" + concepts.get(i) + " may lead to " + concepts.get(i+2) +" ";
					break;
				case "MadeOf":
					sentence = sentence + "" + concepts.get(i) + " is made of " + concepts.get(i+2) +" ";
					break;
				case "Desires":
					sentence = sentence + "" + concepts.get(i) + "  desires to " + concepts.get(i+2) +" ";
					break;
				case "NotCapableOf":
					sentence = sentence + "" + concepts.get(i) + " may not be able to " + concepts.get(i+2) +" ";
					break;
				case "NotDesires":
					sentence = sentence + "" + concepts.get(i) + " may not desire for " + concepts.get(i+2) +" ";
					break;
				case "NotHasProperty":
					sentence = sentence + "" + concepts.get(i) + " may not have the property of " + concepts.get(i+2) +" ";
					break;
				case "CreatedBy":
					sentence = sentence + "" + concepts.get(i) + " is created by " + concepts.get(i+2) +" ";
					break;
				case "dbpedia/influenceBy":
					sentence = sentence + "" + concepts.get(i) + " may be influenced by " + concepts.get(i+2) +" ";
					break;
				case "NotIsA":
					sentence = sentence + "" + concepts.get(i) + " is not a " + concepts.get(i+2) +" ";
					break;
				case "NotHasA":
					sentence = sentence + "" + concepts.get(i) + " may not have a " + concepts.get(i+2) +" ";
					break;
				case "DesireOf":
					sentence = sentence + "" + concepts.get(i) + " may desire (to)  " + concepts.get(i+2) +" ";
					break;
				case "SymbolOf":
					sentence = sentence + "" + concepts.get(i) + " may represent " + concepts.get(i+2) +" ";
					break;
				case "LocatedNear":
					sentence = sentence + "" + concepts.get(i) + " may be located near by " + concepts.get(i+2) +" ";
					break;
				case "LocationOfAction":
					sentence = sentence + ". We" + concepts.get(i) + " at " + concepts.get(i+2) +" ";
					break;
				case "NotMadeOf":
					sentence = sentence + "" + concepts.get(i) + " is not consisted of " + concepts.get(i+2) +" ";
					break;
				case "dbpedia/mainInterest":
					sentence = sentence + "" + concepts.get(i) + " is interested in " + concepts.get(i+2) +" ";
					break;
				case "SimilarSize":
					sentence = sentence + "" + concepts.get(i) + " is similar size as " + concepts.get(i+2) +" ";
					break;
				case "":
					sentence = sentence + "" + concepts.get(i) + "  " + concepts.get(i+2) +" ";
					break;
			}
			
			
		}
		
	}
	
	//回傳concepts是否能組成句子
	public boolean isSentence(){
		/*
		"RelatedTo" 
		"Compoun"
		"ReceivesAction"
		"wordnet/adjectivePertainsTo"
		"Attribute"
		"dbpedia/influenced"
		"dbpedia/notableIdea"
		"Entails"
		"wordnet/adverbPertainsTo"
		"dbpedia/spokenIn"
		"dbpedia/languageFamily"
		"dbpedia/knowFor"
		"wordnet/participleOf"
		"dbpedia/field"
		*/
		return true;
	}
	
}
