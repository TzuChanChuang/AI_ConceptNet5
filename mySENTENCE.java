import java.math.BigDecimal;
import java.util.List;
import java.util.Vector;


public class mySENTENCE {
	public Vector<String> concepts;
	public String sentence="";
	public int length;
	public double score;
	public DATABASE database; 
	
	public void buildSentence(DATABASE database){
		this.database = database;
		Grammar grammar = new Grammar();
		String VorN;
		int main = 0, which =0, from = 0;
		String subjectTerm = "";
		

		for (int i=0; i<concepts.size()-1; i=i+2){

			//conceptA rel conceptB 
			//     i       i+1    i+2
			
			switch (concepts.get(i+1)) {
				case "InstanceOf":
					if(main == 0) {
						sentence = concepts.get(i) + " " +grammar.verbConversionNowType("be", concepts.get(i))+" kind of " + concepts.get(i+2) +" ";
						subjectTerm = concepts.get(i);
					}
					else if(which==0){
						sentence = sentence + "which " +grammar.verbConversionNowType("be", concepts.get(i))+ " kind of " + concepts.get(i+2) + " ";
						which++;
					}
					else 
						return;
					main++;
					break;
				case "IsA":
					if(main == 0){
						sentence = concepts.get(i) + " " +grammar.verbConversionNowType("be", concepts.get(i))+ " " + grammar.AorAn(concepts.get(i+2)) + " " + concepts.get(i+2) +" ";
						subjectTerm = concepts.get(i);
					}
					else if(which==0){
						sentence = sentence + "which " +grammar.verbConversionNowType("be", concepts.get(i))+ " " + grammar.AorAn(concepts.get(i+2)) +" " + concepts.get(i+2) + " ";
						which++;
					}
					else 
						return;
					main++;
					break;
				case "DerivedFrom":
					if(main==0){
						sentence = concepts.get(i) + " "+grammar.verbConversionNowType("be", concepts.get(i))+" derived from " + concepts.get(i+2) +" ";
						subjectTerm = concepts.get(i);
					}
					else if(which==0){
						sentence = sentence + "which "+grammar.verbConversionNowType("be", concepts.get(i))+" derived from " + concepts.get(i+2) + " ";
						which++;
					}
					else 
						return;
					main++;
					break;
				case "Synonym":
					if(main==0){
						sentence = concepts.get(i) + " "+grammar.verbConversionNowType("be", concepts.get(i))+" synonymous with " + concepts.get(i+2) +" ";
						subjectTerm = concepts.get(i);
					}
					else if(which==0){
						sentence = sentence + "which "+grammar.verbConversionNowType("be", concepts.get(i))+" synonymous with " + concepts.get(i+2) + " ";
						which++;
					}
					else 
						return;
					main++;
					break;
				case "dbpedia/genre":
					if(main==0){
						sentence = concepts.get(i) + " may have a type of " + concepts.get(i+2) +" ";
						subjectTerm = concepts.get(i);
					}
					else if(which==0){
						sentence = sentence + "which may have a type of " + concepts.get(i+2) + " ";
						which++;
					}
					else 
						return;
					main++;
					break;
				case "EtymologicallyDerivedFrom":
					if(main==0){
						sentence = concepts.get(i) + " "+grammar.verbConversionNowType("be", concepts.get(i))+" etymologically derived from " + concepts.get(i+2) +" ";
						subjectTerm = concepts.get(i);
					}
					else if(which==0){
						sentence  = sentence + "which "+grammar.verbConversionNowType("be", concepts.get(i))+" etymologivally derived from "+ concepts.get(i+2) + " ";
						which++;
					}
					else 
						return;
					main++;
					break;
				case "UsedFor":
					if(isVerb(concepts.get(i+2))) VorN = "to";
					else VorN="for";
					if(main==0){
						sentence = "You can use " + concepts.get(i) +" " + VorN + " " + concepts.get(i+2) +" ";
						subjectTerm = "you";
					}
					else if(which==0){
						sentence = sentence + "which you can use "+ VorN+" " + concepts.get(i+2) + " ";
						which++;
					}
					else 
						return;
					main++;
					break;
				case "AtLocation":
					if(main==0){
						sentence = "There "+grammar.verbConversionNowType("be", concepts.get(i))+" "+ concepts.get(i) + " at " + concepts.get(i+2) +" ";
						subjectTerm = "there";
					}
					else if(from==0){
						sentence = sentence + "from " + concepts.get(i+2) + " ";
						from ++;
					}
					else 
						return;
					break;
				case "HasSubevent":
					if(main==0){
						sentence = "When you "+concepts.get(i) + ", you may " + concepts.get(i+2) +" ";
						subjectTerm = "you";
					}
					else sentence = sentence + "and then "+subjectTerm+" may " + concepts.get(i+2) + " ";
					main++;
					break;
				case "HasPrerequisite":
					if(main==0) {
						sentence = "If you want to "+concepts.get(i) + ", you have to " + concepts.get(i+2) +" ";
						subjectTerm = "you";
					}
					else if(which==0){
						sentence  = sentence + "which "+subjectTerm+" need to " + concepts.get(i+2) + " first ";
						which++;
					}
					else 
						return;
					main++;
					break;
				case "CapableOf":
					if(main==0) {
						sentence = concepts.get(i) + " can " + concepts.get(i+2) +" ";
						subjectTerm = concepts.get(i);
					}
					else if(which==0){
						sentence  = sentence + "which can " + concepts.get(i+2) + " ";
						which++;
					}
					else 
						return;
					main++;
					break;
				case "Antonym":
					if(main==0) {
						sentence = concepts.get(i) + " "+grammar.verbConversionNowType("be", concepts.get(i))+" contrast to " + concepts.get(i+2) +" ";
						subjectTerm = concepts.get(i);
					}
					else if(which==0){
						sentence  = sentence + "which "+grammar.verbConversionNowType("be", concepts.get(i))+" contrast to " + concepts.get(i+2) + " ";
						which++;
					}
					else 
						return;
					main++;
					break;
				case "Causes":
					if(main==0) {
						sentence = grammar.addING(concepts.get(i)) + " may cause " + concepts.get(i+2) +" ";
						subjectTerm = grammar.addING(concepts.get(i));
					}
					else if(which==0){
						sentence  = sentence + "which may cause " + concepts.get(i+2) + " ";
						which++;
					}
					else 
						return;
					main++;
					break;
				case "MotivatedByGoal":
					if(isVerb(concepts.get(i+2))) VorN = "to ";
					else VorN="";
					if(main==0) {
						sentence = "You would " + concepts.get(i) + "  becasue you want "+VorN + concepts.get(i+2) +" ";
						subjectTerm = "you";
					}
					else if(which==0){
						sentence  = sentence + "which "+grammar.verbConversionNowType("be", concepts.get(i))+" because of the desire of " + grammar.addING(concepts.get(i+2)) + " ";
						which++;
					}
					else 
						return;
					main++;
					break;
				case "HasProperty":
					if(main==0) {
						sentence = concepts.get(i) + " may be " + concepts.get(i+2) +" ";
						subjectTerm = concepts.get(i);
					}
					else if(which==0){
						sentence  = sentence + "which may be " + concepts.get(i+2) + " ";
						which++;
					}
					else 
						return;
					main++;
					break;
				case "SimilarTo":
					if(main==0) {
						sentence = concepts.get(i) + " "+grammar.verbConversionNowType("be", concepts.get(i))+" similar to " + concepts.get(i+2) + " ";
						subjectTerm = concepts.get(i);
					}
					else if(which==0){
						sentence  = sentence + "which "+grammar.verbConversionNowType("be", concepts.get(i))+" similar to " + concepts.get(i+2) + " ";
						which++;
					}
					else 
						return;
					main++;
					break;
				case "PartOf":
					if(main==0) {
						sentence = concepts.get(i) + " "+grammar.verbConversionNowType("be", concepts.get(i))+" part of the " + concepts.get(i+2) +" ";
						subjectTerm = concepts.get(i);
					}
					else if(which==0){
						sentence  = sentence + "which "+grammar.verbConversionNowType("be", concepts.get(i))+" part of the " + concepts.get(i+2) + " ";
						which++;
					}
					else 
						return;
					main++;
					break;
				case "HasContext":
					if(main==0) {
						sentence = concepts.get(i) + " "+grammar.verbConversionNowType("be", concepts.get(i))+" related to " + concepts.get(i+2) +" ";
						subjectTerm = concepts.get(i);
					}
					else if(which==0){
						sentence  = sentence + "which "+grammar.verbConversionNowType("be", concepts.get(i))+" related to " + concepts.get(i+2) + " ";
						which++;
					}
					else 
						return;
					main++;
					break;
				case "ReceivesAction":
					if(main==0) {
						sentence = concepts.get(i) + " " + grammar.verbConversionPassiveType(concepts.get(i+2), concepts.get(i)) +" ";
						subjectTerm = concepts.get(i);
					}
					else if(which==0){
						sentence = sentence + "which " + grammar.verbConversionPassiveType(concepts.get(i+2), concepts.get(i)) +" ";
						which++;
					}
					main++;
					break;
				case "HasA":
					if(main==0) {
						sentence =  concepts.get(i) + " "+grammar.verbConversionNowType("have", concepts.get(i))+" "+grammar.AorAn(concepts.get(i+2))+" " + concepts.get(i+2) +" ";
						subjectTerm = concepts.get(i);
					}
					else if(which==0){
						sentence  = sentence + "which "+grammar.verbConversionNowType("have", concepts.get(i))+" "+grammar.AorAn(concepts.get(i+2))+" " + concepts.get(i+2) + " ";
						which++;
					}
					else 
						return;
					main++;
					break;
				case "HasFirstSubevent":
					if(main==0) {
						sentence = "When you " + concepts.get(i) + " you may first " + concepts.get(i+2) +" ";
						subjectTerm = "you";
					}
					else if(which==0){
						sentence  = sentence + "which "+subjectTerm+" may first " + concepts.get(i+2) + " ";
						which++;
					}
					else 
						return;
					main++;
					break;
				case "DefinedAs":
					if(main==0) {
						sentence = concepts.get(i) + " "+grammar.verbConversionNowType("be", concepts.get(i))+" defined as " + concepts.get(i+2) +" ";
						subjectTerm = concepts.get(i);
					}
					else if(which==0){
						sentence  = sentence + "which "+grammar.verbConversionNowType("be", concepts.get(i))+" defined as " + concepts.get(i+2) + " ";
						which++;
					}
					else 
						return;
					main++;
					break;
				case "HasLastSubevent":
					if(main==0) {
						sentence = "After " + grammar.addING(concepts.get(i)) + " you may " + concepts.get(i+2) +" ";
						subjectTerm = "you";
					}
					else if(which==0){
						sentence  = sentence + "which "+subjectTerm+" will then " + concepts.get(i+2) + " ";
						which++;
					}
					else 
						return;
					main++;
					break;
				case "MemberOf":
					if(main==0) {
						sentence = concepts.get(i) + " "+grammar.verbConversionNowType("be", concepts.get(i))+" member of " + concepts.get(i+2) +" ";
						subjectTerm = concepts.get(i);
					}
					else if(which==0){
						sentence  = sentence + "which "+grammar.verbConversionNowType("be", concepts.get(i))+" member of " + concepts.get(i+2) + " ";
						which++;
					}
					else 
						return;
					main++;
					break;
				case "CausesDesire":
					if(main==0) {
						sentence = concepts.get(i) + " may lead to " + concepts.get(i+2) +" ";
						subjectTerm = concepts.get(i);
					}
					else if(which==0){
						sentence  = sentence + "which may lead to " + concepts.get(i+2) + " ";
						which++;
					}
					else 
						return;
					main++;
					break;
				case "MadeOf":
					if(main==0) {
						sentence = concepts.get(i) + " "+grammar.verbConversionNowType("be", concepts.get(i))+" made of " + concepts.get(i+2) +" ";
						subjectTerm = concepts.get(i);
					}
					else if(which==0){
						sentence  = sentence + "which "+grammar.verbConversionNowType("be", concepts.get(i))+" made of " + concepts.get(i+2) + " ";
						which++;
					}
					else 
						return;
					main++;
					break;
				case "Desires":
					if(main==0) {
						sentence = concepts.get(i) + " may desire to " + concepts.get(i+2) +" ";
						subjectTerm = concepts.get(i);
					}
					else if(which==0){
						sentence  = sentence + "which may desire to " + concepts.get(i+2) + " ";
						which++;
					}
					else 
						return;
					main++;
					break;
				case "NotCapableOf":
					if(main==0) {
						sentence = concepts.get(i) + " may not be able to " + concepts.get(i+2) +" ";
						subjectTerm = concepts.get(i);
					}
					else if(which==0){
						sentence  = sentence + "which may not be able to " + concepts.get(i+2) + " ";
						which++;
					}
					else 
						return;
					main++;
					break;
				case "NotDesires":
					if(main==0) {
						sentence = concepts.get(i) + " may not desire for " + concepts.get(i+2) +" ";
						subjectTerm = concepts.get(i);
					}
					else if(which==0){
						sentence  = sentence + "which may not desire for " + concepts.get(i+2) + " ";
						which++;
					}
					else 
						return;
					main++;
					break;
				case "NotHasProperty":
					if(main==0) {
						sentence =  concepts.get(i) + " may not have the property of " + concepts.get(i+2) +" ";
						subjectTerm = concepts.get(i);
					}
					else if(which==0){
						sentence  = sentence + "which may not have the property of " + concepts.get(i+2) + " ";
						which++;
					}
					else 
						return;
					main++;
					break;
				case "CreatedBy":
					if(main==0) {
						sentence = concepts.get(i) + " "+grammar.verbConversionNowType("be", concepts.get(i))+" created by " + concepts.get(i+2) +" ";
						subjectTerm = concepts.get(i);
					}
					else if(which==0){
						sentence  = sentence + "which "+grammar.verbConversionNowType("be", concepts.get(i))+" created by " + concepts.get(i+2) + " ";
						which++;
					}
					else 
						return;
					main++;
					break;
				case "dbpedia/influenceBy":
					if(main==0) {
						sentence = concepts.get(i) + " may be influenced by " + concepts.get(i+2) +" ";
						subjectTerm = concepts.get(i);
					}
					else if(which==0){
						sentence  = sentence + "which may be influenced by " + concepts.get(i+2) + " ";
						which++;
					}
					else 
						return;
					main++;
					break;
				case "NotIsA":
					if(main==0) {
						sentence = concepts.get(i) + " "+grammar.verbConversionNowType("be", concepts.get(i))+" not "+grammar.AorAn(concepts.get(i+2))+" " + concepts.get(i+2) +" ";
						subjectTerm = concepts.get(i);
					}
					else if(which==0){
						sentence  = sentence + "which "+grammar.verbConversionNowType("be", concepts.get(i))+" not "+grammar.AorAn(concepts.get(i+2))+" " + concepts.get(i+2) + " ";
						which++;
					}
					else 
						return;
					main++;
					break;
				case "NotHasA":
					if(main==0) {
						sentence = concepts.get(i) + " may not have "+grammar.AorAn(concepts.get(i+2))+" " + concepts.get(i+2) +" ";
						subjectTerm = concepts.get(i);
					}
					else if(which==0){
						sentence  = sentence + "which may not have "+grammar.AorAn(concepts.get(i+2))+" " + concepts.get(i+2) + " ";
						which++;
					}
					else 
						return;
					main++;
					break;
				case "DesireOf":
					if(isVerb(concepts.get(i+2))) VorN = "to ";
					else VorN = "";
					if(main==0) {
						sentence = concepts.get(i) + " may desire " + VorN + concepts.get(i+2) +" ";
						subjectTerm = concepts.get(i);
					}
					else if(which==0){
						sentence  = sentence + "which may desire " +VorN+ concepts.get(i+2) + " ";
						which++;
					}
					else 
						return;
					main++;
					break;
				case "SymbolOf":
					if(main==0) {
						sentence = concepts.get(i) + " may be the symbol of" + concepts.get(i+2) +" ";
						subjectTerm = concepts.get(i);
					}
					else if(which==0){
						sentence  = sentence + "which may be the symbol of " + concepts.get(i+2) + " ";
						which++;
					}
					else 
						return;
					main++;
					break;
				case "LocatedNear":
					if(main==0) {
						sentence = concepts.get(i) + " may be near to " + concepts.get(i+2) +" ";
						subjectTerm = concepts.get(i);
					}
					else if(which==0){
						sentence  = sentence + "which may be near to " + concepts.get(i+2) + " ";
						which++;
					}
					else 
						return;
					main++;
					break;
				case "LocationOfAction":
					if(main==0) {
						sentence = "We" + concepts.get(i) + " at " + concepts.get(i+2) +" ";
						subjectTerm = "we";
					}
					else if(which==0){
						sentence  = sentence + "which may be done at " + concepts.get(i+2) + " ";
						which++;
					}
					else 
						return;
					main++;
					break;
				case "NotMadeOf":
					if(main==0) {
						sentence = concepts.get(i) + " "+grammar.verbConversionNowType("be", concepts.get(i))+" not consisted of " + concepts.get(i+2) +" ";
						subjectTerm = concepts.get(i);
					}
					else if(which==0){
						sentence  = sentence + "which "+grammar.verbConversionNowType("be", concepts.get(i))+" not consisted of " + concepts.get(i+2) + " ";
						which++;
					}
					else 
						return;
					main++;
					break;
				case "dbpedia/mainInterest":
					if(main==0) {
						sentence = concepts.get(i) + " "+grammar.verbConversionNowType("be", concepts.get(i))+" interested in " + concepts.get(i+2) +" ";
						subjectTerm = concepts.get(i);
					}
					else if(which==0){
						sentence  = sentence + "which "+grammar.verbConversionNowType("be", concepts.get(i))+" interested in " + concepts.get(i+2) + " ";
						which++;
					}
					else 
						return;
					main++;
					break;
				case "SimilarSize":
					if(main==0) {
						subjectTerm = concepts.get(i);
						sentence = concepts.get(i) + " may have similar size as " + concepts.get(i+2) +" ";
					}
					else if(which==0){
						sentence  = sentence + "which may have similar size as " + concepts.get(i+2) + " ";
						which++;
					}
					else 
						return;
					main++;
					break;
				default:
					sentence = sentence + "  error: case undefined, rel=" +concepts.get(i+1)+" ";
					return;
			}//switch
									
			
		}// for all concepts
		
	}
	
	public void calScore(){
		//把"_"變成" "
		sentence = sentence.replace('_',' ');
		
		//把最後空白換成句點
		sentence = sentence.substring(0, sentence.length()-1) + ".";
		
		//算length
		String[] token = sentence.split(" ");
		length = token.length;
		
		// score
		if(length>7)
			score = score+((length-7)*0.25);
		else
			score = score;
		score = new BigDecimal(score).setScale(4, BigDecimal.ROUND_HALF_UP).doubleValue();
		if(length<3 || length>15) score = 0;
		
		
	}
	
	public boolean isVerb(String concept){
		int v_num = 0;
		int n_num = 0;
				
		try {
			List<myDATA> datalist = database.searchTable("%", "%", concept+ "/v%", 1, false);
			v_num = datalist.size();
			datalist = database.searchTable(concept + "/v%", "%", "%", 1, false);
			v_num = v_num + datalist.size();
			datalist = database.searchTable("%", "%", concept + "/n%", 1, false);
			n_num = datalist.size();
			datalist = database.searchTable(concept + "/n%", "%", "%", 1, false);
			n_num = n_num + datalist.size();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//return
		return v_num>n_num ;
	}
	
	//回傳concepts是否能組成句子
	public boolean isSentence(int concept_num, String concept1, String concept2){
		if (concepts.contains("RelatedTo") 
				|| concepts.contains("Compoun")
				|| concepts.contains("wordnet/adjectivePertainsTo")
				|| concepts.contains("Attribute")
				|| concepts.contains("dbpedia/influenced")
				|| concepts.contains("dbpedia/notableIdea")
				|| concepts.contains("Entails")
				|| concepts.contains("wordnet/adverbPertainsTo")
				|| concepts.contains("dbpedia/spokenIn")
				|| concepts.contains("dbpedia/languageFamily")
				|| concepts.contains("dbpedia/knowFor")
				|| concepts.contains("wordnet/participleOf")
				|| concepts.contains("dbpedia/field")
				|| concepts.contains("CompoundDerivedFrom")
				|| concepts.contains("NotCauses")) {
			return false;
		}	
		if(concept_num==2){
			if(!concepts.contains(concept1) || !concepts.contains(concept2))  return false;	
		}

		return true;
	}
	
	
	
}
