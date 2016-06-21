package com.utils.datahelper;

import java.util.Collection;

/**
 * 集合工具类
 */
public class CollectionUtils
{
	/**
	 * 判断一个集合列表是否为null或者内容为空
	 * @param list
	 * @return
	 */
	public static boolean isEmpty(Collection<?> list)
	{
		if (list == null || list.size() == 0)
			return true;
		else
			return false;
	}

	public static boolean isIn(Object value,Object... arr){
		boolean result = false;
		for (Object obj:arr){
			if (obj instanceof Integer){
				if (((Integer) value).intValue() == ((Integer) obj).intValue()) {
					return true;
				}
			}else{
				if (value == obj){
					return true;
				}
			}
		}
		return result;
	}
}
