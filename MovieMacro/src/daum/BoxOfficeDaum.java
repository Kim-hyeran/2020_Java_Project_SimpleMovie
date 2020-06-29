package daum;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class BoxOfficeDaum {

	int finalCnt=0;
	String baseUrl="http://ticket2.movie.daum.net/Movie/MovieRankList.aspx";
	
	public String[][] daumMovieRank(String[][] movieRank) throws IOException {
		
		Document doc=Jsoup.connect(baseUrl).get();
		Elements movieList=doc.select("div.desc_boxthumb>strong.tit_join>a");
		
		for(Element movie : movieList) {
			
			if(finalCnt==10) {
				break;
			}
			
			String title=movie.text();
			
			int flag=999;
			
			for (int i = 0; i < movieRank.length; i++) {
				
				if (movieRank[i][1].equals(title)) {
					flag=i;
					break;
				}
				
			}
			
			if(flag==999) {
				continue;
			}
			
			String url=movie.attr("href");
			Document movieDoc=Jsoup.connect(url).get(); //영화 상세 정보 페이지 데이터
			
			//상세 페이지가 없는 영화는 건너 뛰도록 함
			if(movieDoc.select("span.txt_name").size()==0) {
				continue;
			}

			String daumHref=movieDoc.select("a.area_poster").get(0).attr("href");
			String daumCode=daumHref.substring(daumHref.lastIndexOf("=")+1, daumHref.lastIndexOf("#")); //영화 코드만 추출
			
			movieRank[flag][11]=daumCode;
			
			finalCnt+=1;
			
		}
		
		return movieRank;
	}

}
