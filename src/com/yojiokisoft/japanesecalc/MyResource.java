package com.yojiokisoft.japanesecalc;

/**
 * リソース関連のユーティリティ
 */
public class MyResource {
	/**
	 * リソース名からリソースIDを得る.
	 * 
	 * @param name リソース名
	 * @return リソースID
	 */
	public static int getResourceIdByName(String name) {
		App app = App.getInstance();
		String packageName = app.getPackageName();
		return app.getResources().getIdentifier(name, "drawable", packageName);
	}
}
