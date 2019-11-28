package org.asura.core.util.math;

import java.util.Map;

import com.graphbuilder.math.Expression;
import com.graphbuilder.math.ExpressionTree;
import com.graphbuilder.math.FuncMap;
import com.graphbuilder.math.VarMap;
import com.graphbuilder.math.func.LogFunction;
import com.graphbuilder.math.func.MaxFunction;
import com.graphbuilder.math.func.ModFunction;
import com.graphbuilder.math.func.PowFunction;
import com.graphbuilder.math.func.SinFunction;

public class FormulaUtil {
	public static double getValue(String formula, Map<String, Double> map) {
		Expression expression = ExpressionTree.parse(formula);
		VarMap vm = new VarMap(false);

		FuncMap fm = new FuncMap();
		fm.setFunction("sin", new SinFunction());
		fm.setFunction("max", new MaxFunction());
		fm.setFunction("log", new LogFunction());
		fm.setFunction("pow", new PowFunction());
		fm.setFunction("mod", new ModFunction());

		for (String key : map.keySet()) {
			vm.setValue(key, ((Double) map.get(key)).doubleValue());
		}

		return expression.eval(vm, fm);
	}
}
