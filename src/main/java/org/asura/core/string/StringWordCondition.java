package org.asura.core.string;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import org.asura.core.util.CombinationUtil;
import org.asura.core.util.StringUtil;
import org.asura.core.util.spliter.CommonSpliter;

public class StringWordCondition implements IStringCondition {
	private static final long serialVersionUID = -1709326380126952262L;
	private String words;
	private static transient HashMap<String, HashSet<String>> wordMap = new HashMap<>();
	private boolean wholeWord;

	public boolean isWholeWord() {
		return this.wholeWord;
	}

	public void setWholeWord(boolean wholeWord) {
		this.wholeWord = wholeWord;
	}

	public boolean meet(String string) {
		initial(this.words);

		string = StringUtil.getStandardString(string);
		if (this.wholeWord) {
			String[] ss = StringUtil.split(string, "-");
			for (String s : ss)
				if ((wordMap.get(this.words)).contains(s))
					return true;
		} else {
			for (int i = 1; i <= string.length(); ++i) {
				String[] ss = CommonSpliter.split(string, i);
				for (String s : ss) {
					if ((wordMap.get(this.words)).contains(s)) {
						return true;
					}
				}
			}
		}

		return false;
	}

	private static void initial(String word) {
		if (!(wordMap.containsKey(word))) {
			wordMap.put(word, new HashSet<String>());
			String[] ss = word.split(",");
			for (String s : ss) {
				(wordMap.get(word)).add(StringUtil.getStandardString(s));
			}

			List<String> slist = new ArrayList<>();
			for (String s : ss) {
				slist.add(StringUtil.getStandardString(s));
			}
			List<List<String>> llist = CombinationUtil.getCombination(slist, 2);
			for (List<String> list : llist)
				if (list.size() == 2) {
					String s1 = list.get(0);
					String s2 = list.get(1);

					(wordMap.get(word)).add(s1 + s2);
					(wordMap.get(word)).add(s2 + s1);
				}
		}
	}

	public String getWords() {
		return this.words;
	}

	public void setWords(String words) {
		this.words = words;
	}
}
