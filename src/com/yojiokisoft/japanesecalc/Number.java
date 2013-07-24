package com.yojiokisoft.japanesecalc;

public enum Number {
	COMMA("."),
	ZERO("0"), DOUBLE_ZERO("0"),
	ONE("1"), TWO("2"), TREE("3"), FORE("4"), FIVE("5"), SIX("6"), SEVEN("7"), EIGHT("8"), NINE("9"),
	;
	private String value;

	private Number(String n) {
		this.value = n;
	}

	public String getValue() {
		return this.value;
	}
}
