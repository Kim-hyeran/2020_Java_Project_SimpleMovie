package daum;

import java.io.IOException;
import java.util.HashMap;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import domain.ReplyDTO;
import persistence.ReplyDAO;

public class ReplyCrawlerDaum {
	
	int page=1;
	int count=0;
	int total=0;
	String prePage="";
	
	ReplyDAO rDao=new ReplyDAO();
	
	public HashMap<String, Integer> daumCrawler(String movieName, String daumCode) throws IOException {
		
		while(true) {
			String url="https://movie.daum.net/moviedb/grade?movieId="+daumCode+"&type=netizen&page="+page;
			
			Document doc=Jsoup.connect(url).get();
			
			Elements replyList=doc.select("div.main_detail li");
			
			//네이버 영화 페이지와는 달리 빈 페이지를 출력하기 때문에 조건문이 달라진다.
		    if(replyList.size()==0) {
		        break;
		    }
		    
		    String writer="";
		    String content="";
		    int score=0;
		    String regdate="";
			
			for(Element reply:replyList) {
				
				writer=reply.select("div.review_info>a>em").text();
				content=reply.select("p.desc_review").text();
				score=Integer.parseInt(reply.select("div.raking_grade>em").text());
				regdate=reply.select("span.info_append").text().substring(0,10);
				
				System.out.println("\t\t☆DAUM☆");
				System.out.println("☆작성자 : "+writer);
				System.out.println("☆리뷰 : "+content);
				System.out.println("☆평점 : "+score);
				System.out.println("☆날짜 : "+regdate);
				System.out.println();
				
				//MongoDB에 저장(리뷰 한 건씩)
				ReplyDTO rDto=new ReplyDTO(movieName, content, writer, score, regdate);
				rDao.addReply(rDto);
				
				count+=1;
				total+=score;
				
			}
		
		page+=1;
		
		}
		
		System.out.println("☆☆DAUM에서 "+count+"건의 영화 리뷰를 수집하였습니다.");
		System.out.println();
		
		HashMap<String, Integer> map=new HashMap<String, Integer>();
		map.put("count", count);
		map.put("total", total);
		
		return map;
		
	}

}
