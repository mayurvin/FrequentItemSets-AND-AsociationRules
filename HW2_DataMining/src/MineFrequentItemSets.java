import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.Set;

public class MineFrequentItemSets {

	private static double MIN_SUPPORT = 0.5;
	private static double MIN_CONFIDENCE = 0.7;
	private static String ITEMSEP = ",";
	private static ArrayList<String> dataset ;
	private static int itemCount = 0;
			
	static HashMap<String, Float> allRules = new HashMap<String, Float>();
	static HashMap<HashSet<String>, Float> mainMap = new HashMap<HashSet<String>, Float>();
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		System.out.println("Enter Min Support Percentage: ");
		MIN_SUPPORT = (double) sc.nextDouble() / 100.0 ;
		System.out.println("Enter Min Confidence percentage: ");
		MIN_CONFIDENCE = sc.nextDouble() / 100.0;
		
		MineFrequentItemSets minefreqItemsObj = new MineFrequentItemSets();
		AsociationRules objRules = new AsociationRules();

		
		System.out.println("Support is set to be " + MIN_SUPPORT * 100 + "%");
		MineFrequentItemSets mineFrequentItemSets = new MineFrequentItemSets();
		ArrayList<HashMap<String, Double>> freqItemSets = new ArrayList<HashMap<String, Double>>();
		HashMap<String, Double> levelOneFrequentItems = minefreqItemsObj.parseInput("gene_expression.txt", MineFrequentItemSets.dataset = new ArrayList<String>());
		freqItemSets.add(levelOneFrequentItems);
		int totalSize = 0;
		for (int i = 0; i < itemCount && i < freqItemSets.size(); i++) {
			System.out.println("Number of length-" + freqItemSets.size() + " Frequent Itemsets: " + freqItemSets.get(i).size());
			totalSize += freqItemSets.get(i).size();
			HashMap<String, Double> freqItemSet = mineFrequentItemSets.generateFreqItemset(freqItemSets.get(i), i + 1);
			if (freqItemSet.size() > 0)
				freqItemSets.add(freqItemSet);
			else
				break;
		}
		System.out.println("Total Frequent Itemsets: " + totalSize);
		
		// Calculating L1
		HashMap<HashSet<String>, Integer> L1Map = new HashMap<HashSet<String>, Integer>();
		HashSet<HashSet<String>> L1 = new HashSet<HashSet<String>>();
		
		for (String entry : dataset) {
			
			String enrtySplitArray[] = entry.split(",");
			for (int i = 0; i < enrtySplitArray.length; i++) {
				HashSet<String> tempSet = new HashSet<String>();
				tempSet.add(enrtySplitArray[i]);
				if (!L1Map.containsKey(tempSet)) {
					L1Map.put(tempSet, 1);

				} else {
					L1Map.put(tempSet, L1Map.get(tempSet) + 1);

				}
			}
		}

		float tempConf = 0;
		for (Map.Entry<HashSet<String>, Float> ent : mainMap.entrySet()) {

			Set<String> original = new HashSet<String>();
			if (ent.getKey().size() > 1) {
				original = ent.getKey();
				HashSet<HashSet<String>> allSubsets = objRules.generateAllSubsets(original);
				Iterator<HashSet<String>> iter = allSubsets.iterator();
				while (iter.hasNext()) {
					HashSet<String> set = iter.next();
					if (set.size() >= 1 && set.size() != original.size()) {
						//System.out.println(set + "->" + minefreqItemsObj.unCommon(set, original));
						tempConf = (float) mainMap.get(original) / (float) mainMap.get(set);
						if (tempConf >= MIN_CONFIDENCE) {
							allRules.put(set + "->" + objRules.findUnCommonItem(set, original), tempConf);
						}
					}
				}
			}
		}
		System.out.println("Association Rule set size:" + allRules.size());
		
		//String query = sc.next();
		//if(query.contains("RULE") && query.contains(""))
		/*
		 * For template 1: 
		 * 1. RULE HAS ANY OF G6_UP 
		 * 2. RULE HAS 1 OF G1_UP 
		 * 3. RULE HAS 1 OF (G1_UP, G10_DOWN) 
		 * 4. BODY HAS ANY OF G6_UP 
		 * 5. BODY HAS NONE OF G72_UP 
		 * 6. BODY HAS 1 OF (G1_UP, G10_DOWN) 
		 * 7. HEAD HAS ANY OF G6_UP 
		 * 8. HEAD HAS NONE OF (G1_UP, G6_UP) 
		 * 9. HEAD HAS 1 OF (G6_UP, G8_UP) 
		 * 10. RULE HAS 1 OF (G1_UP, G6_UP, G72_UP) 
		 * 11. RULE HAS ANY OF (G1_UP, G6_UP, G72_UP)
		 * 
		 * For template 2: 
		 * 1. SIZE OF RULE >= 3 
		 * 2. SIZE OF BODY >= 2 
		 * 3. SIZE OF HEAD >= 2
		 * 
		 * For template 3: 
		 * 1. BODY HAS ANY OF G1_UP AND HEAD HAS 1 OF G59_UP 
		 * 2. BODY HAS ANY OF G1_UP OR HEAD HAS 1 OF G6_UP 
		 * 3. BODY HAS 1 OF G1_UP OR HEAD HAS 2 OF G6_UP 
		 * 4. HEAD HAS 1 OF G1_UP AND BODY HAS 0 OF DISEASE 
		 * 5. HEAD HAS 1 OF DISEASE OR RULE HAS 1 OF (G72_UP, G96_DOWN)
		 * 6. BODY HAS 1 of (G59_UP, G96_DOWN) AND SIZE OF RULE >=3
		 * 
		 */
		while(true){
			System.out.println("Enter the template number for query: ");
			int templateNumber = sc.nextInt();
			switch(templateNumber){
			case 1:
				System.out.println("Enter query as exact template {RULE|BODY|HEAD} HAS ({ANY|NUMBER|NONE}) OF (ITEM1, ITEM2, ..., ITEMn):  ");
				sc.nextLine();
				String query1 = sc.nextLine();
				String rule_body_head1 = query1.split("HAS")[0].trim();
				String number_any_none = query1.split("OF")[0].split("HAS")[1].trim();
				String itemSet = query1.split("OF")[1].trim();
				HashMap<String, Float> resultMap1 =	objRules.queryTemplate1(rule_body_head1, number_any_none, itemSet, allRules);
				System.out.println(resultMap1.size());
				break;
			case 2:
				System.out.println("Enter query as exact template SizeOf({BODY|HEAD|RULE}) >= NUMBER:  ");
				sc.nextLine();
				String query2 = sc.nextLine();
				String rule_body_head2 = query2.split("SIZE OF")[1].split(">=")[0].trim();
				String number2 = query2.split("SIZE OF")[1].split(">=")[1].trim();
				HashMap<String, Float> resultMap2 = objRules.queryTemplate2(rule_body_head2, number2, allRules);
				System.out.println(resultMap2.size());
				break ;
			case 3:
				System.out.println("Enter query as exact template form as Any combined templates using AND or OR :  ");
				sc.nextLine();
				String query3 = sc.nextLine();
				HashSet<String> resultSet3 =	objRules.queryTemplate3(query3, allRules);
				System.out.println(resultSet3.size());
				break;
			default :
				sc.close();
				return ;
			}
		}
	}
	
	private HashMap<String, Double> parseInput(String inFileString, ArrayList<String> dataset) {
		HashMap<String, Integer> candidateItems = new HashMap<String, Integer>();
		HashMap<String, Double> levelOneFrequentItems = new HashMap<String, Double>();

		File inFile = new File(inFileString);
		FileReader fr = null;
		BufferedReader br = null;
		itemCount = 0;
		try {
			fr = new FileReader(inFile.getAbsoluteFile());
			br = new BufferedReader(fr);
			String line;
			while ((line = br.readLine()) != null) {
				StringBuffer dataBuffer = new StringBuffer();
				String[] contents = line.split("\t");
				itemCount++;
				for (int i = 1; i < contents.length; i++) {
					String key = "";
					if (i == contents.length - 1) {
						key = contents[i];
					} else {
						key = "G" + i + "_" + contents[i].toUpperCase();
					}
					dataBuffer.append(key);
					dataBuffer.append(ITEMSEP);
					int value = 0;
					if (candidateItems.containsKey(key)) {
						value = candidateItems.get(key);
					}
					value++;
					candidateItems.put(key, value);
				}
				dataset.add(dataBuffer.deleteCharAt(dataBuffer.length() - 1).toString());
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		//System.out.println("itemCount: " + itemCount);
		for (Entry<String, Integer> entry : candidateItems.entrySet()) {
			String key = entry.getKey();
			Integer value = entry.getValue();
			double support = (double) value.intValue() / (double) itemCount;
			if (support >= MIN_SUPPORT) {
				levelOneFrequentItems.put(key, support);
				HashSet<String> set = new HashSet<String>();
				set.add(key);
				mainMap.put(set, (float) support);
			} else {
				//System.out.println("Removed key: " + key + " Support: " + support);
			}
		}

		return levelOneFrequentItems;
	}

	@SuppressWarnings("unchecked")
	private HashMap<String, Double> generateFreqItemset(HashMap<String, Double> freqItemSet, int currentLevel) {
		HashMap<String, Double> newFreqItemSet = new HashMap<String, Double>();
		HashSet<String> candidateSet = new HashSet<String>();

		Set<Entry<String, Double>> entrySet = freqItemSet.entrySet();
		Object[] entryArray = entrySet.toArray();

		for (int i = 0; i < entrySet.size(); i++) {
			for (int j = i + 1; j < entrySet.size(); j++) {
				String p = ((Entry<String, Double>) entryArray[i]).getKey();
				String q = ((Entry<String, Double>) entryArray[j]).getKey();
				if (p.indexOf(ITEMSEP) != -1 && q.indexOf(ITEMSEP) != -1) {
					String pinit = p.substring(0, p.lastIndexOf(ITEMSEP));
					String qinit = q.substring(0, q.lastIndexOf(ITEMSEP));
					if (pinit.equals(qinit)) {
						//String pfinal = p.substring(p.lastIndexOf(ITEMSEP) + 1);
						String qfinal = q.substring(q.lastIndexOf(ITEMSEP) + 1);
						candidateSet.add(p + ITEMSEP + qfinal);
					}
				} else {
					candidateSet.add(p + ITEMSEP + q);
				}
			}
		}
		for (String key : candidateSet) {
			outerLoop: for (String transaction : dataset) {
				boolean contains = false;
				for (String item : key.split(ITEMSEP)) {
					if (transaction.contains(ITEMSEP + item + ITEMSEP) || transaction.startsWith(item) || transaction.endsWith(item)) {
						contains = true;
					} else
						continue outerLoop;
				}
				if (contains) {
					double value = 0;
					if (newFreqItemSet.containsKey(key)) {
						value = newFreqItemSet.get(key);
					}
					value++;
					newFreqItemSet.put(key, value);
				}
			}
			if (newFreqItemSet.containsKey(key)) {
				double support = newFreqItemSet.get(key) / (double) itemCount;
				if (support >= MIN_SUPPORT) {
					newFreqItemSet.put(key, support);
					//System.out.println("Retained key: " + key + " Support: " + support);
				} else {
					newFreqItemSet.remove(key);
				}
			}
		}
		for(Map.Entry<String, Double> entry: newFreqItemSet.entrySet()){
			HashSet<String> set = new HashSet<String>();
			String[] str = entry.getKey().split(",");
			for(String st: str){
				set.add(st);
			}
			mainMap.put(set, entry.getValue().floatValue());
		}
		return newFreqItemSet;
	}
	

}