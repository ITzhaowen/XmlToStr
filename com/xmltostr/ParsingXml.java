package com.xmltostr;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
/**
 * 
 * 解析XML
 * XML中，一部分是需要分开，有几个子标签就分为几部分。
 * 每个标签中都要取到 name 和 vaule。
 * 现在的 List 中 只有一个 Map 使用 List 是为了 合并数据的时候方便
 * @author 赵文
 *
 */
public class ParsingXml {
	
	private List<String> allHaveName; //存放每个数据的Name
	private List<String> allHaveValue; //存放每个数据的值
	private String selfSplit = ""; //分隔符 
	private String oldRootName = ""; //最根的元素名字
	private String attrKey = ""; //将此属性作为 Key
	
	public ParsingXml() {
		this(";"); //自定义分隔符(默认使用 分号 隔开)
	}

	/**
	 * @param selfSplit：数据的 key 重复的时候，value拼接的分隔符。
	 */
	public ParsingXml(String selfSplit) {
		this(selfSplit, "Name");
	}
	
	/**
	 * 
	 * @param selfSplit：数据的 key 重复的时候，value拼接的分隔符。
	 * @param attrKey：希望将此属性 作为 key
	 */
	public ParsingXml(String selfSplit, String attrKey) {
		this.selfSplit = selfSplit;
		this.attrKey = attrKey;
		allHaveName = new ArrayList<String>();
		allHaveValue = new ArrayList<String>();
	}

	/**
	 * 根据 XML 中的 String 内容进行解析
	 * @param str：xml内容
	 * @throws DocumentException 
	 */
	public List<Map<String,String>> parsingStringInXml(String str) throws DocumentException {
		String strToXmlContent = strToXmlContent(str);
		Document doc = DocumentHelper.parseText(strToXmlContent); // 将字符串转为XML
        Element rootElement = doc.getRootElement();
        oldRootName = rootElement.getName();
        List<Map<String,String>> map = recursiveToGetInfo(rootElement);
        return map;
	}

	/**
	 * 根据 XML 文件的位置来解析 XML 文件
	 * @param xmlPath：xml文件的位置
	 * @throws DocumentException 
	 */
	public List<Map<String,String>> parsingXml(String xmlPath) throws DocumentException {
		SAXReader reader=new SAXReader();
        Document fromXml=reader.read(new File(xmlPath));  
        Element rootElement = fromXml.getRootElement();
        oldRootName = rootElement.getName();
        List<Map<String,String>> map = recursiveToGetInfo(rootElement);
        return map;
	}

	/**
	 * 将 String 类型的 XML 加空格和回车
	 * @param string
	 * @return
	 */
	private String strToXmlContent(String string) {
		String[] split = string.split("</");
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < split.length; i++) {
			sb.append(split[i]);
			if (i != split.length -1) {
				sb.append("\n         </");
			}
		}
		return sb.toString();
	}
	
	
	/**
	 * 使用递归方法来获取到 XML 文件中的 Name 和 Value 的值
	 * @param rootElement ：根节点
	 * @param mainInfo :主要内容标签名字
	 * @param docid : Name 可重复的标签
	 * @return
	 */
	
	private List<Map<String,String>> recursiveToGetInfo(Element rootElement) {
		//循环得到数据
		forInfo(rootElement);
		//将数据合并
		List<Map<String,String>> lists = infoToOne(selfSplit);
		return lists;
	}

	/**
	 * 将数据拼接好，返回给调用者调用方便的值
	 * @param selfSplit2 :分隔符
	 * @param nameNotOneValue 
	 * @param nameNotOneName 
	 */
	private List<Map<String, String>> infoToOne(String selfSplit2) {
		List<Map<String, String>> mapInList = new ArrayList<Map<String, String>>(); //用于放最终数据
		Map<String,String> mapAll = new LinkedHashMap<String, String>(); //用来存放都有的数据
		//将都有的数据先整合到 mapAll 中
		for (int z = 0; z < allHaveName.size(); z++) {
			if (mapAll.containsKey(allHaveName.get(z))) { //如果 key 已存在
				String addStrToStr = "";
				if ("".equalsIgnoreCase(allHaveValue.get(z))) {
					addStrToStr =  mapAll.get(allHaveName.get(z)) + selfSplit2 + " ";
				}else {
					addStrToStr =  mapAll.get(allHaveName.get(z)) + selfSplit2 + allHaveValue.get(z);
				}
				mapAll.put(allHaveName.get(z), addStrToStr);
			}else {
				if ("".equalsIgnoreCase(allHaveValue.get(z))) {
					mapAll.put(allHaveName.get(z), " ");
				}else {
					mapAll.put(allHaveName.get(z), allHaveValue.get(z));
				}
			}
		}
		mapInList.add(mapAll);
		return mapInList;
	}

	/**
	 * 循环得出主要内容
	 * @param rootElement ：标签名字
	 */
	private void forInfo(Element rootElement) {
		if (rootElement.hasMixedContent()) { // 有子标签的 标签
			List<Element> elements = rootElement.elements();
			for (Element element : elements) {
				forInfo(element);
			}
		}else if(!oldRootName.equalsIgnoreCase(rootElement.getName()) && oldRootName.equalsIgnoreCase(rootElement.getParent().getName())){ // 最外层是 根标签的 标签
			allHaveName.add(rootElement.getName());
			allHaveValue.add(rootElement.getStringValue());
		}else { // 将 Name 和 值获取到
			Map<String,String> attsMap = new HashMap<String, String>();
			List<Attribute> attributes = rootElement.attributes();
			for (Attribute attribute : attributes) {
				attsMap.put(attribute.getName(), attribute.getValue());
			}
			if (attsMap.containsKey(attrKey)) { //如果有 Name 值，将 Name 作为 Key
				allHaveName.add(attsMap.get(attrKey));
				allHaveValue.add(rootElement.getStringValue());
			}else {  //如果有 Name 值，将 Name 作为 Key
				allHaveName.add(rootElement.getName());
				allHaveValue.add(rootElement.getStringValue());
			}
		}
	}
	
}
