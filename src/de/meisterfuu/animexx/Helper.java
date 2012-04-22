package de.meisterfuu.animexx;

import java.util.regex.Pattern;


public class Helper {
	public static String BetreffRe(String Betreff){
		String s;
		String begin,mid,end;
		
		if (Betreff.startsWith("Re:")== true){
			s = "Re[2]:"+Betreff.substring(3);
			return s;
		}
		
		int index = Betreff.indexOf("]:");
		int count = 0;
		if(index > 1){
			end = "]:";
			mid = Betreff.substring(3, index);
			begin = Betreff.substring(0, 3);
			if((Pattern.matches("\\d+", mid) == true) && (begin.equals("Re[")) && end.equals("]:")){
				try{
					count = Integer.parseInt(mid)+1;
					return begin+count+end+Betreff.substring(index+2);
				}catch(Exception e){
					s = "Re:"+Betreff;	
					return s;
				}
			}
		}

		
		return "Re:"+Betreff;
	}
}
