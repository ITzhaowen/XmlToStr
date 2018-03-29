package com.xmltostr;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 取解析后的内容
 * @author 赵文
 *
 */
public class RecodeParsingXml {
	private List<Map<String,String>> mapInList;

	public List<Map<String,String>> getMaps() {
		return mapInList;
	}

	public void setMaps(List<Map<String,String>> mapInList) {
		this.mapInList = mapInList;
	}

	/**
	 * 构造方法
	 * @param mapInList：目标List
	 */
	public RecodeParsingXml(List<Map<String,String>> mapInList) {
		this.mapInList = mapInList;
	}

	/**
	 * 获取具体的某个 Map
	 * @param index
	 * @return
	 */
	public Map<String,String> getMap(int index){
		return mapInList.get(index);
	}
	
	/**
 	 * 将获取到的结果的值，按顺序拼接到字符串中
 	 * 分隔符 默认为 逗号 ","
	 * @return
	 */
	public String getAddValues() {
		return this.getAddValues(",");
	}
	
	/**
 	 * 将获取到的结果的值，按顺序拼接到字符串中
	 * @param splitSign : 分割符号
	 * @return
	 */
	public String getAddValues(String splitSign) {
		StringBuffer sb = new StringBuffer();
		for (Map<String,String> map : mapInList) {
			for (String str : map.keySet()) {
				sb.append(map.get(str).trim());
				sb.append(splitSign);
			}
		}
		return sb.toString().substring(0, sb.length()-1);
	}
	
	/**
	 * 根据 Map 中的 key 来获取值
	 * 现在的 List 中 只有一个 Map 使用 List 是为了 合并数据的时候方便
	 * @param key
	 * @return
	 */
	public String getMapValues(String key){
		return mapInList.get(0).get(key);
	}
	
}
