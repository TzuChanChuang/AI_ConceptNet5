import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Grammar {
	
	private HashMap<String, String>irregularNouns;
	private HashMap<String, String>irregularPassiveVerb;
	private ArrayList<String> pluralPronouns;
	private ArrayList<String> Vowel;
	
	public static void main(String[] args) throws IOException {
		while(true){
			System.out.print("Enter your verb > ");
			BufferedReader in1 = new BufferedReader(new InputStreamReader(System.in));
			String verbInput = in1.readLine();
			
			System.out.print("Enter your noun > ");
			BufferedReader in2 = new BufferedReader(new InputStreamReader(System.in));
			String nounInput = in2.readLine();
			
			try {
				Grammar grammar = new Grammar();
				
				//System.out.println(grammar.verbConversionPassiveType(verbInput, nounInput));
				System.out.println(grammar.isPlural(nounInput));
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public Grammar(){
		irregularNouns = new HashMap<String, String>();
		irregularNouns.put("man", "men");
		irregularNouns.put("louse", "lice");
		irregularNouns.put("child", "children");
		irregularNouns.put("foot", "feet");
		irregularNouns.put("goose", "geese");
		irregularNouns.put("cow", "kine");
		irregularNouns.put("ox", "oxen");
		irregularNouns.put("mouse", "mice");
		irregularNouns.put("brother", "brethren");
		irregularNouns.put("tooth", "teeth");
		irregularNouns.put("woman", "women");
		irregularNouns.put("calf", "calves");
		irregularNouns.put("elf", "elves");
		irregularNouns.put("half", "halves");
		irregularNouns.put("hoof", "hooves");
		irregularNouns.put("knife", "knives");
		irregularNouns.put("leaf", "leaves");
		irregularNouns.put("life", "lives");
		irregularNouns.put("loaf", "loaves");
		irregularNouns.put("scarf", "scarves");
		irregularNouns.put("self", "selves");
		irregularNouns.put("sheaf", "sheaves");
		irregularNouns.put("shelf", "shelves");
		irregularNouns.put("thief", "thieves");
		irregularNouns.put("wife", "wives");
		irregularNouns.put("wolf", "wolves");
		
		irregularPassiveVerb = new HashMap<String, String>();
		irregularPassiveVerb.put("be", "been");
		irregularPassiveVerb.put("bear", "born");
		irregularPassiveVerb.put("become", "become");
		irregularPassiveVerb.put("begin", "begun");
		irregularPassiveVerb.put("bite", "bit");
		irregularPassiveVerb.put("blow", "blown");
		irregularPassiveVerb.put("break", "broken");
		irregularPassiveVerb.put("bring", "brought");
		irregularPassiveVerb.put("build", "built");
		irregularPassiveVerb.put("buy", "bought");
		irregularPassiveVerb.put("catch", "caught");
		irregularPassiveVerb.put("choose", "chosen");
		irregularPassiveVerb.put("come", "come");
		irregularPassiveVerb.put("cost", "cost");
		irregularPassiveVerb.put("cut", "cut");
		irregularPassiveVerb.put("dig", "dug");
		irregularPassiveVerb.put("do", "done");
		irregularPassiveVerb.put("draw", "drawn");
		irregularPassiveVerb.put("drive", "driven");
		irregularPassiveVerb.put("eat", "eaten");
		irregularPassiveVerb.put("fall", "fallen");
		irregularPassiveVerb.put("feed", "fed");
		irregularPassiveVerb.put("feel", "felt");
		irregularPassiveVerb.put("fight", "fought");
		irregularPassiveVerb.put("find", "found");
		irregularPassiveVerb.put("fly", "flown");
		irregularPassiveVerb.put("forget", "forgotten");
		irregularPassiveVerb.put("get", "got");
		irregularPassiveVerb.put("give", "given");
		irregularPassiveVerb.put("grow", "grown");
		irregularPassiveVerb.put("hang", "hung");
		irregularPassiveVerb.put("have", "had");
		irregularPassiveVerb.put("hear", "heard");
		irregularPassiveVerb.put("hide", "hidden");
		irregularPassiveVerb.put("hit", "hit");
		irregularPassiveVerb.put("hold", "held");
		irregularPassiveVerb.put("hurt", "hurt");
		irregularPassiveVerb.put("keep", "kept");
		irregularPassiveVerb.put("know", "known");
		irregularPassiveVerb.put("lead", "led");
		irregularPassiveVerb.put("leave", "left");
		irregularPassiveVerb.put("lend", "lent");
		irregularPassiveVerb.put("let", "let");
		irregularPassiveVerb.put("lie", "lain");
		irregularPassiveVerb.put("lose", "lost");
		irregularPassiveVerb.put("make", "made");
		irregularPassiveVerb.put("mean", "meant");
		irregularPassiveVerb.put("meet", "met");
		irregularPassiveVerb.put("mistake", "mistaken");
		irregularPassiveVerb.put("pay", "paid");
		irregularPassiveVerb.put("put", "put");
		irregularPassiveVerb.put("read", "read");
		irregularPassiveVerb.put("ride", "ridden");
		irregularPassiveVerb.put("ring", "rung");
		irregularPassiveVerb.put("rise", "risen");
		irregularPassiveVerb.put("run", "run");
		irregularPassiveVerb.put("say", "said");
		irregularPassiveVerb.put("see", "seen");
		irregularPassiveVerb.put("sell", "sold");
		irregularPassiveVerb.put("send", "sent");
		irregularPassiveVerb.put("set", "set");
		irregularPassiveVerb.put("shake", "shaken");
		irregularPassiveVerb.put("shine", "shone");
		irregularPassiveVerb.put("show", "shown");
		irregularPassiveVerb.put("sing", "sung");
		irregularPassiveVerb.put("sit", "sat");
		irregularPassiveVerb.put("sleep", "slept");
		irregularPassiveVerb.put("speak", "spoken");
		irregularPassiveVerb.put("spend", "spent");
		irregularPassiveVerb.put("stand", "stood");
		irregularPassiveVerb.put("swim", "swum");
		irregularPassiveVerb.put("take", "taken");
		irregularPassiveVerb.put("tell", "told");
		irregularPassiveVerb.put("think", "thought");
		irregularPassiveVerb.put("throw", "thrown");
		irregularPassiveVerb.put("understand", "understood");
		irregularPassiveVerb.put("wake", "woken");
		irregularPassiveVerb.put("wear", "worn");
		irregularPassiveVerb.put("win", "won");
		irregularPassiveVerb.put("write", "written");
		
		pluralPronouns = new ArrayList<String>();
		pluralPronouns.add("you");
		pluralPronouns.add("they");
		pluralPronouns.add("we");
		
		Vowel = new ArrayList<String>();
		Vowel.add("a");
		Vowel.add("e");
		Vowel.add("i");
		Vowel.add("o");
		Vowel.add("u");

	}
	
	public String nounConversion(String noun){
		
		String ans;
		
		if(irregularNouns.get(noun)!=null)
			ans = irregularNouns.get(noun);
		else if(noun.substring(noun.length()-2).equals("ch") || noun.substring(noun.length()-2).equals("sh") || noun.substring(noun.length()-1).equals("s") || noun.substring(noun.length()-1).equals("z") || noun.substring(noun.length()-1).equals("x"))
			ans = noun+"es";
		else if(noun.substring(noun.length()-1).equals("y")){
			if(noun.substring(noun.length()-2).equals("ay") || noun.substring(noun.length()-2).equals("ey") || noun.substring(noun.length()-2).equals("iy") || noun.substring(noun.length()-2).equals("oy") || noun.substring(noun.length()-2).equals("uy"))
				ans = noun+"s";
			else
				ans = noun.substring(0, noun.length()-1)+"ies";
		}
		else
			ans = noun+"s";
		
		return ans;
	}
	
	
	public String verbConversionNowType(String verb, String noun){
		
		String ans=verb;
		
		if(verb.equals("be")){
			if(noun.equals("I"))
				ans = "am";
			else if(isPlural(noun))
				ans = "are";
			else
				ans = "is";
		}
		else if(verb.equals("have")){
			if(noun.equals("I"))
				ans = "have";
			else if(isPlural(noun))
				ans = "have";
			else
				ans = "has";
		}
		else{
			if(!isPlural(noun) || !noun.equals("I")){
				if(verb.substring(verb.length()-2).equals("ch") || verb.substring(verb.length()-2).equals("sh") || verb.substring(verb.length()-1).equals("s") || verb.substring(verb.length()-1).equals("z") || verb.substring(verb.length()-1).equals("x"))
					ans = verb+"es";
				else if(verb.substring(verb.length()-1).equals("y")){
					if(verb.substring(verb.length()-2).equals("ay") || verb.substring(verb.length()-2).equals("ey") || verb.substring(verb.length()-2).equals("iy") || verb.substring(verb.length()-2).equals("oy") || verb.substring(verb.length()-2).equals("uy"))
						ans = verb+"s";
					else
						ans = verb.substring(0, verb.length()-1)+"ies";
				}
				else if(verb.equals("go"))
					ans = "goes";
				else
					ans = verb+"s";
			}
		}
		return ans;
	}
	
	public boolean isPlural(String noun){
		boolean ans=false;
		
		if(pluralPronouns.contains(noun) || irregularNouns.containsValue(noun))
			ans = true;
		else if(noun.substring(noun.length()-1).equals("s"))   //English name which has XXXs  will GGGGGG
			ans = true;
		
		return ans;
	}
	
	public String verbConversionPassiveType(String verb, String noun){
		
		String ans = verb;
		
		if(irregularPassiveVerb.get(verb)!=null)
			ans = irregularPassiveVerb.get(verb);
		else if(verb.substring(verb.length()-1).equals("e"))
			ans = verb+"d";
		else if(verb.substring(verb.length()-1).equals("y")){
			if(verb.substring(verb.length()-2).equals("ay") || verb.substring(verb.length()-2).equals("ey") || verb.substring(verb.length()-2).equals("iy") || verb.substring(verb.length()-2).equals("oy") || verb.substring(verb.length()-2).equals("uy"))
				ans = verb+"ed";
			else
				ans = verb.substring(0, verb.length()-1)+"ied";
		}
		else if(verb.length()>=3 && isCVC(verb.substring(verb.length()-3)))
			ans = verb+verb.substring(verb.length()-1)+"ed";
		else
			ans = verb+"ed";
		
		if(noun.equals("I"))
			ans = "am "+ans;
		else if(isPlural(noun))
			ans = "are "+ans;
		else
			ans = "is "+ans;
		
		return ans;
	}
	
	public String AorAn(String noun){
		if(noun.charAt(0)=='a' || noun.charAt(0)=='e' || noun.charAt(0)=='i' || noun.charAt(0)=='o' || noun.charAt(0)=='u')
			return "an";
		return "a";
	}
	
	public String addING(String verb){
		
		String[] token = verb.split("_");
		String output = token[0]+"ing";
		for(int i=1; i<token.length; i++)
			output = output + "_" + token[i];
		return output;
		
	}
	
	public boolean isCVC(String suffixFragment){ //摮嚗�嚗� CVC
		
		boolean ans=false;
		
		if(!Vowel.contains(suffixFragment.substring(0,suffixFragment.length()-2)) && Vowel.contains(suffixFragment.substring(1,suffixFragment.length()-1)) && !Vowel.contains(suffixFragment.substring(2,suffixFragment.length())))
			ans = true;
		
		return ans;
	}
}
