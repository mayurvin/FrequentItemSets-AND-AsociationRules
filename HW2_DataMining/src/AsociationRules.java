import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class AsociationRules {
	
	
	

	public HashSet<String> findUnCommonItem(HashSet<String> set, Set<String> original) {
		HashSet<String> newC = new HashSet<String>();
		String s[] = new String[set.size()];
		String o[] = new String[original.size()];
		int i = 0, j = 0;
		Iterator<String> it = set.iterator();
		while (it.hasNext()) {
			s[i] = it.next();
			i++;
		}

		i = 0;
		Iterator<String> ot = original.iterator();
		while (ot.hasNext()) {
			o[i] = ot.next();
			i++;
		}

		i = 0;
		for (i = 0; i < o.length; i++) {
			for (j = 0; j < s.length; j++) {
				if (s[j].equalsIgnoreCase(o[i])) {
					;
				} else if (!set.contains(o[i])) {
					newC.add(o[i]);
				}
			}
		}
		return newC;
	}

	public HashMap<String, Float> queryTemplate1(String rule_body_head, String number_any_none,
			String itemList, HashMap<String, Float> allRules) {
		
		int flag1 = 0, flag2 = 0, num = 0;
		HashMap<String, Float> resultSet = new HashMap<String, Float>();
		if(itemList.contains("(")){
			itemList = itemList.replace("(", "");
			itemList = itemList.replace(")", "");
		}
		String items[] = itemList.split(",");
		if (rule_body_head.equalsIgnoreCase("rule")) {
			flag1 = 1;
		} else if (rule_body_head.equalsIgnoreCase("body")) {
			flag1 = 2;
		} else if (rule_body_head.equalsIgnoreCase("head")) {
			flag1 = 4;
		} else {

		}

		if (number_any_none.equalsIgnoreCase("any")) {
			flag2 = 7;
		} else if (number_any_none.equalsIgnoreCase("none")) {
			flag2 = 11;
		} else {

			num = Integer.parseInt(number_any_none);
			flag2 = 15;
		}

		//System.out.println();
		//System.out.println("All Rules"+allRules);
		if ((flag1 + flag2) == 8) {

			for (Map.Entry<String, Float> ent : allRules.entrySet()) {
				int i = 0;
				for (i = 0; i < items.length; i++) {
					if (ent.getKey().contains(items[i].trim())) {
						resultSet.put(ent.getKey(), ent.getValue());
						break;
					}
				}
			}
		}

		else if ((flag1 + flag2) == 12) {

			for (Map.Entry<String, Float> ent : allRules.entrySet()) {
				int i = 0, tFlag = 0;
				for (i = 0; i < items.length; i++) {
					if (ent.getKey().contains(items[i].trim())) {
						tFlag = 1;
						break;
					}
				}

				if (tFlag == 0) {
					resultSet.put(ent.getKey(), ent.getValue());
				}
			}

			
		}

		else if ((flag1 + flag2) == 9) {

			for (Map.Entry<String, Float> ent : allRules.entrySet()) {
				int i = 0;
				String keySplit[] = ent.getKey().split("->");
				String body = keySplit[0];
				String head = keySplit[1];

				for (i = 0; i < items.length; i++) {
					if (body.contains(items[i].trim())) {
						resultSet.put(ent.getKey(), ent.getValue());
						break;
					}
				}
			}

			//System.out.println("Frequent item Set :");
			//System.out.println(resultSet);
		}

		else if ((flag1 + flag2) == 13) {

			for (Map.Entry<String, Float> ent : allRules.entrySet()) {
				int i = 0, tFlag = 0;
				String keySplit[] = ent.getKey().split("->");
				String body = keySplit[0];
				String head = keySplit[1];
				for (i = 0; i < items.length; i++) {
					if (body.contains(items[i].trim())) {
						tFlag = 1;
						break;
					}
				}

				if (tFlag == 0) {
					resultSet.put(ent.getKey(), ent.getValue());
				}
			}

			//System.out.println("Frequent item Set :");
			//System.out.println(resultSet);
		}

		else if ((flag1 + flag2) == 11) {

			for (Map.Entry<String, Float> ent : allRules.entrySet()) {
				int i = 0;
				String keySplit[] = ent.getKey().split("->");
				String body = keySplit[0];
				String head = keySplit[1];

				for (i = 0; i < items.length; i++) {
					if (head.contains(items[i].trim())) {
						resultSet.put(ent.getKey(), ent.getValue());
						break;
					}
				}
			}

			//System.out.println("Frequent item Set :");
			//System.out.println(resultSet);
		}

		else if ((flag1 + flag2) == 15) {

			for (Map.Entry<String, Float> ent : allRules.entrySet()) {
				int i = 0, tFlag = 0;
				//System.out.println(ent.getKey());
				String keySplit[] = ent.getKey().split("->");
				String body = keySplit[0];
				String head = keySplit[1];
				
				for (i = 0; i < items.length; i++) {
					if (head.contains(items[i].trim())) {
						tFlag = 1;
						break;
					}
				}

				if (tFlag == 0) {
					resultSet.put(ent.getKey(), ent.getValue());
				}
			}

			//System.out.println("Frequent item Set :");
			//System.out.println(resultSet);
		}

		else if ((flag1 + flag2) == 16) {

			HashSet<String> original = new HashSet<String>();
			for (int i = 0; i < items.length; i++) {
				original.add(items[i].trim());
			}

			HashSet<HashSet<String>> allSubsets = generateAllSubsets(original);
			Iterator<HashSet<String>> iter = allSubsets.iterator();
			HashSet<HashSet<String>> numSet = new HashSet<HashSet<String>>();
			//System.out.println(allSubsets);

			while (iter.hasNext()) {
				HashSet<String> set = iter.next();

				if (set.size() == num) {
					numSet.add(set);
				} else {
				}
			}
			//System.out.println();
			//System.out.println("NumSet: " + numSet);
			
			for (Map.Entry<String, Float> ent : allRules.entrySet()) {
				int i = 0;
				String str = ent.getKey().replace("[", "");
				str = str.replace("]", "");
				HashSet<String> ruleSet = new HashSet<String>();
				String keySplit[] = str.split("->");
				String body[] = keySplit[0].split(",");
				String head[] = keySplit[1].split(",");

				for (i = 0; i < body.length; i++) {
					ruleSet.add(String.valueOf(body[i]));
				}

				for (i = 0; i < head.length; i++) {
					ruleSet.add(String.valueOf(head[i]));
				}

				Iterator<HashSet<String>> numIter = numSet.iterator();
				while (numIter.hasNext()) {
					int temp = 0;
					String str1 = String.valueOf(numIter.next());

					str1 = str1.replace("[", "");
					str1 = str1.replace("]", "");
					str1 = str1.replace(" ", "");
					String contents[] = str1.split(",");

					for (String k : contents) {
						if (ruleSet.contains(k.trim())) {
							continue;
						} else {
							temp = 1;
							break;
						}
					}
					if (temp == 0) {
						resultSet.put(ent.getKey(), ent.getValue());
					}
				}

			}
			//System.out.println();
//			System.out.println("resultSet);
		}
		else if ((flag1 + flag2) == 17) { // body-- number
			HashSet<String> original = new HashSet<String>();
			for (int i = 0; i < items.length; i++) {
				original.add(items[i].trim());
			}

			HashSet<HashSet<String>> allSubsets = generateAllSubsets(original);
			Iterator<HashSet<String>> iter = allSubsets.iterator();

			HashSet<HashSet<String>> numSet = new HashSet<HashSet<String>>();

			while (iter.hasNext()) {
				HashSet<String> set = iter.next();

				if (set.size() == num) {
					numSet.add(set);
				} else {
				}
			}

			for (Map.Entry<String, Float> ent : allRules.entrySet()) {
				int i = 0, tFlag = 0;
				HashSet<String> ruleSet = new HashSet<String>();
				String str = ent.getKey().replace("[", "");
				str = str.replace("]", "");

				String keySplit[] = str.split("->");
				String body[] = keySplit[0].split(",");

				for (i = 0; i < body.length; i++) {
					ruleSet.add(String.valueOf(body[i]));
				}

				Iterator<HashSet<String>> numIter = numSet.iterator();
				while (numIter.hasNext()) {
					int temp = 0;
					String str1 = String.valueOf(numIter.next());

					str1 = str1.replace("[", "");
					str1 = str1.replace("]", "");
					str1 = str1.replace(" ", "");
					String contents[] = str1.split(",");

					for (String k : contents) {
						if (ruleSet.contains(k.trim())) {
							continue;
						} else {
							temp = 1;
							break;
						}
					}
					if (temp == 0) {
						resultSet.put(ent.getKey(), ent.getValue());
					}
				}
			}
			//System.out.println("Frequent item Set :");
			//System.out.println(resultSet);
		}
		else if ((flag1 + flag2) == 19) {

			HashSet<String> original = new HashSet<String>();
			for (int i = 0; i < items.length; i++) {
				original.add(items[i].trim());
			}

			HashSet<HashSet<String>> allSubsets = generateAllSubsets(original);
			Iterator<HashSet<String>> iter = allSubsets.iterator();
			HashSet<HashSet<String>> numSet = new HashSet<HashSet<String>>();

			while (iter.hasNext()) {
				HashSet<String> set = iter.next();

				if (set.size() == num) {
					numSet.add(set);
				} else {
				}
			}

			for (Map.Entry<String, Float> ent : allRules.entrySet()) {
				int i = 0, tFlag = 0;
				String str = ent.getKey().replace("[", "");
				str = str.replace("]", "");
				HashSet<String> ruleSet = new HashSet<String>();
				String keySplit[] = str.split("->");
				String head[] = keySplit[1].split(",");

				for (i = 0; i < head.length; i++) {
					ruleSet.add(String.valueOf(head[i]));
				}

				Iterator<HashSet<String>> numIter = numSet.iterator();
				while (numIter.hasNext()) {
					int temp = 0;
					String str1 = String.valueOf(numIter.next());

					str1 = str1.replace("[", "");
					str1 = str1.replace("]", "");
					str1 = str1.replace(" ", "");
					String contents[] = str1.split(",");

					for (String k : contents) {
						if (ruleSet.contains(k.trim())) {
							continue;
						} else {
							temp = 1;
							break;
						}
					}
					if (temp == 0) {
						resultSet.put(ent.getKey(), ent.getValue());
					}
				}

			}
			//System.out.println("Frequent item Set :");
			//System.out.println(resultSet);

		}
		return resultSet;

	}
	
	public HashMap<String, Float> queryTemplate2(String rule_body_head, String number, HashMap<String, Float> allRules) {
		int num = 0;
		HashMap<String, Float> resultSet = new HashMap<String, Float>();
		num = Integer.parseInt(number);
		rule_body_head = rule_body_head.toUpperCase();
		switch (rule_body_head) {
		case "RULE":
			//System.out.println("Rule case");
			for (Map.Entry<String, Float> ent : allRules.entrySet()) {
				HashSet<String> ruleSet = new HashSet<String>();
				int i = 0;

				String str = ent.getKey().replace("[", "");
				str = str.replace("]", "");

				String keySplit[] = str.split("->");
				String body[] = keySplit[0].split(",");
				String head[] = keySplit[1].split(",");

				for (i = 0; i < body.length; i++) {
					ruleSet.add(String.valueOf(body[i]));
				}
				for (i = 0; i < head.length; i++) {
					ruleSet.add(String.valueOf(head[i]));
				}
				if (ruleSet.size() >= num) {
					resultSet.put(ent.getKey(), ent.getValue());
				}
			}
			//System.out.println("Resultset is:");
			//System.out.println(resultSet);
			break;

		case "HEAD":
			for (Map.Entry<String, Float> ent : allRules.entrySet()) {
				HashSet<String> ruleSet = new HashSet<String>();
				int i = 0;

				String str = ent.getKey().replace("[", "");
				str = str.replace("]", "");

				String keySplit[] = str.split("->");
				String head[] = keySplit[1].split(",");

				for (i = 0; i < head.length; i++) {
					ruleSet.add(String.valueOf(head[i]));
				}
				if (ruleSet.size() >= num) {
					resultSet.put(ent.getKey(), ent.getValue());
				}
			}
			//System.out.println("Resultset is:");
			//System.out.println(resultSet);
			break;

		case "BODY":
			for (Map.Entry<String, Float> ent : allRules.entrySet()) {
				HashSet<String> ruleSet = new HashSet<String>();
				int i = 0;

				String str = ent.getKey().replace("[", "");
				str = str.replace("]", "");

				String keySplit[] = str.split("->");
				String body[] = keySplit[0].split(",");
				for (i = 0; i < body.length; i++) {
					ruleSet.add(String.valueOf(body[i]));
				}

				if (ruleSet.size() >= num) {
					resultSet.put(ent.getKey(), ent.getValue());
				}
			}
			//System.out.println("Resultset is:");
			//System.out.println(resultSet);
			break;

		default:
			// System.out.println("Wrong input");
		}
//		System.out.println(resultSet.size() + " in tP2");
		return resultSet;
	}

	public HashSet<String> queryTemplate3(String inputCategory, HashMap<String, Float> allRules) {
		String operator = "";
		HashSet<String> rData1HashSet1 = new HashSet<String>();
		HashSet<String> rData1HashSet2 = new HashSet<String>();
		HashMap<String, Float> tempData1 = new HashMap<String, Float>();
		HashMap<String, Float> tempData2 = new HashMap<String, Float>();
		HashMap<String, Float> tempData3 = new HashMap<String, Float>();
		HashMap<String, Float> tempData4 = new HashMap<String, Float>();
		HashMap<String, Float> tempData5 = new HashMap<String, Float>();
		HashMap<String, Float> tempData6 = new HashMap<String, Float>();
		HashMap<String, Float> tempData7 = new HashMap<String, Float>();
		HashMap<String, Float> tempData8 = new HashMap<String, Float>();

		if (inputCategory.toLowerCase().contains("and")) {

			String[] str = inputCategory.split("AND");
			operator = "AND";
			if (str[0].contains(">=")) {
				String[] leftStr = str[0].split(">=");
				String rule_body_head = leftStr[0];

				String number_any_none = leftStr[1];
				// /if template 1 is decided then
				tempData1 = queryTemplate2(rule_body_head, number_any_none, allRules);

			} else {
				String[] leftStr = str[0].trim().split(" ");
				String rule_body_head = leftStr[0];
				String number_any_none = leftStr[2];
				String itemList = leftStr[4];
				tempData2 = queryTemplate1(rule_body_head, number_any_none, itemList,allRules);

			}
			if (str[1].contains(">=")) {
				String[] rightStr = str[1].split(">=");
				String rule_body_head = rightStr[0];
				String number_any_none = rightStr[1];

				tempData3 = queryTemplate2(rule_body_head, number_any_none, allRules);

			} else {

				String[] rightStr = str[1].trim().split(" ");
				String rule_body_head = rightStr[0];
				String number_any_none = rightStr[2];
				String itemList = rightStr[4];
				tempData4 = queryTemplate1(rule_body_head, number_any_none, itemList,allRules);

			}

		} else {
			String[] str = inputCategory.toLowerCase().split("or");
			operator = "OR";
			if (str[0].contains(">=")) {
				String[] leftStr = str[0].split(">=");
				String rule_body_head = leftStr[0];
				String number_any_none = leftStr[1];

				tempData5 = queryTemplate2(rule_body_head, number_any_none,allRules);

			} else {
				String[] leftStr = str[0].trim().split(" ");
				String rule_body_head = leftStr[0];
				String number_any_none = leftStr[2];
				String itemList = leftStr[4];
				tempData6 = queryTemplate1(rule_body_head, number_any_none, itemList,allRules);
			}
			if (str[1].contains(">=")) {
				String[] rightStr = str[1].split(">=");
				String rule_body_head = rightStr[0];
				String number_any_none = rightStr[1];
				queryTemplate2(rule_body_head, number_any_none,allRules);
				tempData7 = queryTemplate2(rule_body_head, number_any_none,allRules);
			} else {
				String[] rightStr = str[1].trim().split(" ");
				String rule_body_head = rightStr[0];
				String number_any_none = rightStr[2];
				String itemList = rightStr[4];
				tempData8 = queryTemplate1(rule_body_head, number_any_none, itemList,allRules);

			}

		}
		HashMap<HashMap<String, Float>, Float> temp1 = new HashMap<HashMap<String, Float>, Float>();
		if (tempData1.size() != 0) {
			temp1.put(tempData1, (float) tempData1.size());
		}
		if (tempData2.size() != 0) {
			temp1.put(tempData2, (float) tempData1.size());
		}
		if (tempData3.size() != 0) {
			temp1.put(tempData3, (float) tempData1.size());
		}
		if (tempData4.size() != 0) {
			temp1.put(tempData4, (float) tempData1.size());
		}
		if (tempData5.size() != 0) {
			temp1.put(tempData5, (float) tempData1.size());
		}
		if (tempData6.size() != 0) {
			temp1.put(tempData6, (float) tempData1.size());
		}
		if (tempData7.size() != 0) {
			temp1.put(tempData7, (float) tempData1.size());
		}
		if (tempData8.size() != 0) {
			temp1.put(tempData8, (float) tempData1.size());
		}

		int cnt = 1;
		for (Map.Entry<HashMap<String, Float>, Float> ent : temp1.entrySet()) {
			if (cnt == 1) {
				for (Map.Entry<String, Float> myent : ent.getKey().entrySet()) {
					rData1HashSet1.add(myent.getKey());
				}
				cnt++;
			}
			if (cnt == 2) {
				for (Map.Entry<String, Float> myent : ent.getKey().entrySet()) {
					rData1HashSet2.add(myent.getKey());
				}
				cnt++;
			}
		}
		if (operator.equals("AND")) {
			rData1HashSet1.retainAll(rData1HashSet2);
		} else if (operator.equals("OR")){
				rData1HashSet1.addAll(rData1HashSet2);
		}
		//System.out.println(rData1HashSet1);
		return rData1HashSet1;
	}
	@SuppressWarnings("unchecked")
	public HashSet<HashSet<String>> generateAllSubsets(
			Set<String> original) {

		HashSet<HashSet<String>> allSubsets = new HashSet<HashSet<String>>();

		allSubsets.add(new HashSet<String>()); // Add empty set.

		Iterator<String> it = original.iterator();
		while (it.hasNext()) {
			String element = (String) it.next();

			// Deep copy all subsets to temporary power set.
			HashSet<HashSet<String>> tempClone = new HashSet<HashSet<String>>();
			for (HashSet<String> subset : allSubsets) {
				tempClone.add((HashSet<String>) subset.clone());
			}

			// All element to all subsets of the temporary power set.
			Iterator<HashSet<String>> it2 = tempClone.iterator();
			while (it2.hasNext()) {
				Set<String> s = (HashSet<String>) it2.next();
				s.add(element);
			}
			allSubsets.addAll(tempClone);
		}
		return allSubsets;
	}

}
