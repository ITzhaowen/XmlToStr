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
	public String addValues() {
		return this.addValues(",");
	}
	
	/**
 	 * 将获取到的结果的值，按顺序拼接到字符串中
	 * @param splitSign : 分割符号
	 * @return
	 */
	public String addValues(String splitSign) {
		StringBuffer sb = new StringBuffer();
		for (Map<String,String> map : mapInList) {
			for (String str : map.keySet()) {
				sb.append(map.get(str).trim());
				sb.append(splitSign);
			}
		}
		return sb.toString().substring(0, sb.length()-1);
	}
	
}
