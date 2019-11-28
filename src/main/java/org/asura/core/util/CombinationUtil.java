package org.asura.core.util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.asura.core.util.model.Entry;

public class CombinationUtil {

	public static int getCountOfCombinationByWeight(String word) {
		int a = word.length();
		int count = 0;
		for (int i = 0; i < a + 1; ++i) {
			count = i * (a + 1 - i) + count;
		}
		return count;
	}

	public static <T> List<List<T>> getLimitedCombination(List<List<T>> llist, int length) {
		List<List<T>> result = new ArrayList<>();
		List<List<List<T>>> rlist = getCombination(llist, length);
		for (List<List<T>> list : rlist) {
			List<List<T>> rrlist = getCombination(list);
			result.addAll(rrlist);
		}

		return result;
	}

	public static <T> List<List<T>> getLimitedCombinationWithinMax(List<List<T>> llist, int max) {
		List<List<T>> result = new ArrayList<>();
		for (int i = 1; i <= max; ++i) {
			result.addAll(getLimitedCombination(llist, i));
		}
		return result;
	}

	public static <T> List<List<List<T>>> getPermutation(List<List<T>> list) {
		List<List<List<T>>> llist = new ArrayList<>();
		if (list.size() > 0) {
			List<List<T>> newList = new ArrayList<>();
			newList.add(list.get(0));
			llist.add(newList);
			for (int i = 1; i < list.size(); ++i) {
				llist = buildPermutation(llist, list.get(i));
			}
		}

		return llist;
	}

	private static <T> List<List<List<T>>> buildPermutation(List<List<List<T>>> llist, List<T> element) {
		List<List<List<T>>> newLList = new ArrayList<>();
		for (List<List<T>> list : llist) {
			for (int i = 0; i <= list.size(); ++i) {
				List<List<T>> newList = new ArrayList<>();
				for (Iterator<List<T>> localIterator2 = list.iterator(); localIterator2.hasNext();) {
					List<T> obj = localIterator2.next();
					newList.add(obj);
				}
				newList.add(i, element);
				newLList.add(newList);
			}
		}
		return newLList;
	}

	public static <T> List<List<T>> getCombination(List<T> list, int length) {
		List<List<T>> resultwList = new ArrayList<>();
		if (list.size() <= length) {
			resultwList.add(list);
		} else {
			boolean reverse = false;
			if (list.size() - length < length) {
				length = list.size() - length;
				reverse = true;
			}
			List<List<Entry<T>>> newList = new ArrayList<>();
			List<Entry<T>> wholeList = new ArrayList<>();
			for (int j = 0; j < list.size(); ++j) {
				wholeList.add(new Entry<T>(list.get(j), j));
			}
			for (int i = 0; i < length; ++i) {
				List<Entry<T>> entryList = new ArrayList<>();
				for (int j = 0; j < list.size(); ++j) {
					entryList.add(new Entry<T>(list.get(j), j));
				}
				newList.add(entryList);
			}
			newList = getCombination(newList);

			Iterator<List<Entry<T>>> it = newList.iterator();
			Set<Set<Integer>> resultSet = new HashSet<>();
			while (it.hasNext()) {
				List<Entry<T>> lo = it.next();
				Set<Integer> hs = new HashSet<>();
				for (Iterator<Entry<T>> localIterator = lo.iterator(); localIterator.hasNext();) {
					Entry<T> entry = localIterator.next();
					hs.add(entry.getPosition());
				}
				if (hs.size() < lo.size()) {
					it.remove();
				} else if (resultSet.contains(hs)) {
					it.remove();
				} else {
					resultSet.add(hs);
				}
			}

			if (reverse) {
				List<List<Entry<T>>> nnList = new ArrayList<>();
				for (List<Entry<T>> newchildList : newList) {
					nnList.add(getLeftPart(wholeList, newchildList));
				}
				newList = nnList;
			}

			for (int i = 0; i < newList.size(); ++i) {
				List<T> vList = new ArrayList<>();
				for (int j = 0; j < newList.get(i).size(); ++j) {
					vList.add(newList.get(i).get(j).getObject());
				}
				resultwList.add(vList);
			}
		}

		return resultwList;
	}

	private static <T> List<Entry<T>> getLeftPart(List<Entry<T>> sourceList, List<Entry<T>> valueList) {
		List<Entry<T>> leftList = new ArrayList<>();
		for (Iterator<Entry<T>> localIterator1 = sourceList.iterator(); localIterator1.hasNext();) {
			Entry<T> entry = localIterator1.next();
			boolean contains = false;
			for (Iterator<Entry<T>> localIterator2 = valueList.iterator(); localIterator2.hasNext();) {
				Entry<T> ventry = localIterator2.next();
				if (ventry.getPosition() == entry.getPosition()) {
					contains = true;
					break;
				}
			}
			if (!(contains)) {
				leftList.add(entry);
			}
		}

		return leftList;
	}

	public static <T> List<List<T>> getCombination(List<List<T>> list) {
		Hashtable<Integer, Integer> recorder = new Hashtable<>();
		Hashtable<Integer, Integer> done = new Hashtable<>();
		int changePosition = 0;
		int currentPosition = 0;
		List<List<T>> result = new ArrayList<>();

		for (int i = 0; i < list.size(); ++i) {
			if (((List<T>) list.get(i)).size() == 0) {
				list.remove(i);
			}
		}

		for (int i = 0; i < list.size(); ++i) {
			recorder.put(Integer.valueOf(i), Integer.valueOf(0));
		}

		for (int i = 0; i < list.size(); ++i) {
			done.put(Integer.valueOf(i), Integer.valueOf(((List<T>) list.get(i)).size() - 1));
		}

		while ((recorder.size() <= done.size()) && (recorder.size() != 0)) {
			List<T> oneList = new ArrayList<>();
			for (int i = 0; i < list.size(); ++i) {
				oneList.add(((List<T>) list.get(i)).get(((Integer) recorder.get(Integer.valueOf(i))).intValue()));
			}
			result.add(oneList);
			adjustValue(list, recorder, done, currentPosition, changePosition);
		}

		return result;
	}

	private static <T> void adjustValue(List<List<T>> list, Hashtable<Integer, Integer> recorder,
			Hashtable<Integer, Integer> done, int currentPosition, int changePosition) {
		for (int i = 0; i < list.size(); ++i) {
			if (i < changePosition) {
				if (((Integer) recorder.get(Integer.valueOf(i))).intValue() < ((Integer) done.get(Integer.valueOf(i)))
						.intValue()) {
					if (currentPosition != i)
						continue;
					recorder.put(Integer.valueOf(i),
							Integer.valueOf(((Integer) recorder.get(Integer.valueOf(i))).intValue() + 1));
					currentPosition = i;
					return;
				}

				recorder.put(Integer.valueOf(i), Integer.valueOf(0));
				for (int j = i + 1; j < changePosition; ++j) {
					if (((Integer) recorder.get(Integer.valueOf(j)))
							.intValue() < ((Integer) done.get(Integer.valueOf(j))).intValue()) {
						recorder.put(Integer.valueOf(j),
								Integer.valueOf(((Integer) recorder.get(Integer.valueOf(j))).intValue() + 1));
						break;
					}
					recorder.put(Integer.valueOf(j), Integer.valueOf(0));
				}

				currentPosition = 0;
			} else {
				if ((i != changePosition) || (currentPosition != changePosition))
					continue;
				if (((Integer) recorder.get(Integer.valueOf(i))).intValue() < ((Integer) done.get(Integer.valueOf(i)))
						.intValue()) {
					recorder.put(Integer.valueOf(i),
							Integer.valueOf(((Integer) recorder.get(Integer.valueOf(i))).intValue() + 1));
					currentPosition = i;
					return;
				}
				recorder.put(Integer.valueOf(i), Integer.valueOf(0));
				for (int j = i + 1; j < done.size() + 1; ++j) {
					if (j < done.size()) {
						if (((Integer) recorder.get(Integer.valueOf(j)))
								.intValue() < ((Integer) done.get(Integer.valueOf(j))).intValue()) {
							recorder.put(Integer.valueOf(j),
									Integer.valueOf(((Integer) recorder.get(Integer.valueOf(j))).intValue() + 1));
							break;
						}
						recorder.put(Integer.valueOf(j), Integer.valueOf(0));
					} else {
						recorder.put(Integer.valueOf(j), Integer.valueOf(0));
					}
				}
				currentPosition = 0;
				++changePosition;

				return;
			}
		}

	}
}
