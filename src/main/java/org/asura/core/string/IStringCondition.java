package org.asura.core.string;

import java.io.Serializable;

public interface IStringCondition extends Serializable {
	public boolean meet(String paramString);
}
