package naver;

import java.io.IOException;
import java.util.HashMap;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import domain.ReplyDTO;
import persistence.ReplyDAO;

public class ReplyCrawlerNaver {
	
	int page=1;
	int count=0;
	int total=0;
	String prePage="";
	
	//method
	//접근제한자 [지정어] 데이터반환타입 메서드명(매개변수) {}
	public HashMap<String, Integer> naverCrawler(String movieName, String naverCode) throws IOException { //영화 페이지의 댓글 수집을 위해서는 포털 사이트에서 각각 지정한 영화 코드가 필요
			//<> : Generic, <key, value>형으로 값을 삽입해야 함.
			//		기본자료형은 넣을 수 없음(int 기본자료형 -> integer 객체자료형 : wrapper class)
		String writer="";
		String content="";
		int score=0; //DB에서 계산 처리를 거치게 설정할 수 있음
		String regdate="";
		
		ReplyDAO rDao=new ReplyDAO();
		
		while(true) {
			String url="https://movie.naver.com/movie/bi/mi/pointWriteFormList.nhn?code="+naverCode+"&type=after&isActualPointWriteExecute=false&isMileageSubscriptionAlready=false&isMileageSubscriptionReject=false&page="+page;
			
			Document doc=Jsoup.connect(url).get();
			
			Elements replyList=doc.select("div.score_result li");
			String nowPage=doc.select("input#page").attr("value");
			
			//리뷰 마지막 페이지에 도달하면 반복문을 중단하게 만드는 코드
			if(nowPage.equals(prePage)) {
				break;
			} else {
				prePage=nowPage;
			}
			
			for(Element reply:replyList) {
				
				writer=reply.select("div.score_reple a>span").text();
				content=reply.select("div.score_reple>p>span").text();
				score=Integer.parseInt(reply.select("div.star_score>em").text()); //String형으로 가져온 데이터를 integer형으로 형변환
				//String preRegDate=reply.select("div.score_reple em").get(1).text();
				//regDate=preRegDate.substring(0, preRegDate.lastIndexOf(" "));
				regdate=reply.select("div.score_reple em").get(1).text().substring(0, 10);
				
				System.out.println("\t\t☆NAVER☆");
				System.out.println("☆작성자 : "+writer);
				System.out.println("☆리뷰 : "+content);
				System.out.println("☆평점 : "+score);
				System.out.println("☆날짜 : "+regdate);
				System.out.println();
				
				//MongoDB에 수집한 데이터(리뷰 한 건씩)를 저장하는 코드
				ReplyDTO rDto=new ReplyDTO(movieName, content, writer, score, regdate);
				//System.out.println(rDto.toString());
				rDao.addReply(rDto);
				
				total+=score; //평점의 총합
				count+=1; //or count++;
				
			}
			
			page+=1; //or page++;
		
		}
		
		System.out.println("☆☆NAVER에서 "+count+"건의 영화 리뷰를 수집하였습니다.");
		System.out.println();
		
		//양쪽 generic이 일치해야 함, 일치하는 경우 오른쪽 generic을 생략할 수 있음
		HashMap<String, Integer>nMap=new HashMap<String, Integer>();
		nMap.put("count", count);
		nMap.put("total", total);
		
		return nMap;
		
	}

}
