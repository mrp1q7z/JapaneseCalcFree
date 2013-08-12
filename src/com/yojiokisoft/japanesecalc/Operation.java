package com.yojiokisoft.japanesecalc;

import java.math.BigDecimal;

public enum Operation {
	PLUS {
		BigDecimal eval(BigDecimal x, BigDecimal y) {
			return x.add(y);
		}
	},
	MINUS {
		BigDecimal eval(BigDecimal x, BigDecimal y) {
			return x.subtract(y);
		}
	},
	TIMES {
		BigDecimal eval(BigDecimal x, BigDecimal y) {
			BigDecimal z = x.multiply(y);
			return z.setScale(11, BigDecimal.ROUND_HALF_UP);
		}
	},
	DIVIDE {
		BigDecimal eval(BigDecimal x, BigDecimal y) {
			return x.divide(y, 12, BigDecimal.ROUND_HALF_UP);
		}
	};
	abstract BigDecimal eval(BigDecimal x, BigDecimal y);
}
