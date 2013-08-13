/*
 * Copyright (C) 2013 YojiokiSoft
 * 
 * This program is free software; you can redistribute it and/or modify it under the
 * terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 3 of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with this
 * program. If not, see <http://www.gnu.org/licenses/>.
 */

package com.yojiokisoft.japanesecalc.dao;

/**
 * 背景画像エンティティ
 */
public class BackImageEntity {
	public static final int IT_RESOURCE = 1;
	public static final int IT_BITMAP = 2;

	/** 画像タイプ（1=リソース、2=外部取込みビットマップ） */
	public int type;

	/** リソース名 */
	public String resourceName;

	/** ビットマップパス */
	public String bitmapPath;
}
