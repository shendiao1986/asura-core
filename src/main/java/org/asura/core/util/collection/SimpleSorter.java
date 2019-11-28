package org.asura.core.util.collection;

import java.util.ArrayList;
import java.util.List;

import com.asura.tools.data.selection.SelectMethod;
import com.asura.tools.data.selection.data.DataBlock;
import com.asura.tools.data.selection.data.DataBlocks;
import com.asura.tools.data.selection.selectorder.ClauseSelectOrder;

public class SimpleSorter<T> {

	@SuppressWarnings("unchecked")
	public List<T> sort(List<ISimpleSortable<T>> vs, String exp, int max) {
		DataBlock bl = new DataBlock();
		for (ISimpleSortable<T> s : vs) {
			bl.addData(s);
		}
		DataBlocks bls = new SelectMethod(ClauseSelectOrder.fromExpression(exp)).select(bl);

		List<T> list = new ArrayList<>();
		for (int i = 0; i < Math.min(max, bls.getAllDatas().size()); ++i) {
			list.add(((ISimpleSortable<T>) bls.getAllDatas().get(i)).get());
		}

		return list;
	}
}
