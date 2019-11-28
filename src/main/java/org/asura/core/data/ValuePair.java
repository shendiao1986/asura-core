package org.asura.core.data;

import java.util.Objects;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public class ValuePair<T1, T2> {
	private T1 v1;
	private T2 v2;

	public ValuePair(T1 v1, T2 v2) {
		this.v1 = v1;
		this.v2 = v2;
	}

	public T1 getV1() {
		return this.v1;
	}

	public void setV1(T1 v1) {
		this.v1 = v1;
	}

	public T2 getV2() {
		return this.v2;
	}

	public void setV2(T2 v2) {
		this.v2 = v2;
	}

	public int hashCode() {
		return Objects.hashCode(new Object[] { this.v1, this.v2 });
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (obj == this) {
			return true;
		}
		if (obj.getClass() != super.getClass()) {
			return false;
		}

		ValuePair<T1, T2> rhs = (ValuePair<T1, T2>) obj;
		return new EqualsBuilder().append(this.v1, rhs.v1).append(this.v2, rhs.v2).isEquals();
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}
}
