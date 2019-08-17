package com.datayes.test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import file.MyFileReader;
import file.MyFileWriter;

public class TestStat {

	public final static SimpleDateFormat FORMAT_DATE_EN = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss.SSS");

	public final static Pattern pt = Pattern.compile("newsID:\\d{8}");

	public static void main(String[] args) throws ParseException {
		List<Stat> statList = new ArrayList<Stat>();
		List<Long> timeList = new ArrayList<Long>();
		for (String line : MyFileReader.readFileList("etc/batch_500", "utf-8")) {
			Date date = FORMAT_DATE_EN.parse(line.substring(0, 23));
			Matcher match = pt.matcher(line);
			if (match.find()) {
				String str = match.group();
				Long id = Long.valueOf(str.substring(7));
				if(line.contains("appoint"))
					statList.add(new Stat(date.getTime(), id, 1));
				else if(line.contains("success"))
					statList.add(new Stat(date.getTime(), id, 2));
			}
		}
		Map<Long, LinkedList<Stat>> timeline = new HashMap<Long, LinkedList<Stat>>();
		for(Stat stat : statList){
			Long id = stat.id;
			if(!timeline.containsKey(id))
				timeline.put(id, new LinkedList<Stat>());
			LinkedList<Stat> statLast = timeline.get(id);
			if(stat.type==1)
				statLast.add(stat);
			else if(stat.type==2)
				if(statLast.size()>0)
					timeList.add(stat.date - statLast.pop().date);
		}
		MyFileWriter.writeFile(timeList.stream().map(l -> l.toString()).collect(Collectors.joining("\r\n")), "etc/batch_500_time");
		timeline.entrySet().stream().filter(entry -> entry.getValue().size()>0).forEach(entry -> System.out.println(entry.getKey()));
	}

}

class Stat {
	public Stat(Long date, Long id, Integer type) {
		super();
		this.date = date;
		this.id = id;
		this.type = type;
	}
	Long date;
	Long id;
	Integer type;
}