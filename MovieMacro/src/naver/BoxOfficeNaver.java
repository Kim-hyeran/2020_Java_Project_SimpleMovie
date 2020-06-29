package naver;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class BoxOfficeNaver {
	
	String url="https://movie.naver.com/movie/running/current.nhn";
	
	String title=""; //영화 제목
	String score=""; //평점
	String reserve=""; //예매율
	String genre=""; //장르
	String runningTime=""; //상영 시간
	String openDate=""; //개봉일
	String director=""; //감독
	String actor=""; //출연진
	String naverCode=""; //네이버 영화 코드
	
	int finalCnt=0; //수집을 멈추기 위한 변수(1위~10위까지 완료)
	
	public String[][] naverMovieRank(String[][] movieRank) throws IOException {
		
		Document doc=Jsoup.connect(url).get();
		
		Elements movieList=doc.select("div.lst_wrap>ul.lst_detail_t1 li");
		
		//naver movie information crawling
		for(Element movie:movieList) {
			
			if(finalCnt==10) {
				//1위부터 10위까지의 영화 정보 수집 완료
				break;
			}

			title=movie.select("dt.tit>a").text(); //영화 제목
			
			int flag=999; //0 : kobis의 박스오피스 1~10위의 순위 내에 네이버 박스오피스의 값(영화 제목 title)이 존재하지 않음
			
			for (int i = 0; i < movieRank.length; i++) {
				
				if (movieRank[i][1].equals(title)) {
					//BoxOffice 1~10위 내의 영화로 판별, 크롤링
					//flag=i : 0부터 9값만 input
					flag=i; //두 박스오피스 내에 같은 값이 존재함=크롤링 대상
					break;
				}
				
			}
			
			//1~10위권 밖에 있는 영화=flag가 0~9 이외의 값=크롤링 대상이 아님
			//flag에 크롤링 대상이 아닌 임의의 값을 기본적으로 부여 
			if(flag==999) {
				continue;
			}
			
			//예매율, 감독, 출연진 초기화
			reserve="0.0";
			director="";
			actor="";
			
			if (movie.select("span.num").size()==2) {
				reserve=movie.select("span.num").get(1).text();
			}
			
			score=movie.select("span.num").get(0).text();
			
			genre=movie.select("dd>span.link_txt").get(0).text();
			
			//상영시간
			String temp=movie.select("dl.info_txt1>dd").get(0).text();
			int beginTimeIndex=temp.indexOf("|"); //앞에서부터 인덱스 번호 계산
			int endTimeIndex=temp.lastIndexOf("|"); //뒤에서부터 인덱스 번호 계산
			
			if (beginTimeIndex==endTimeIndex) { //상영시간
				runningTime=temp.substring(0, endTimeIndex); //장르가 설정되지 않아 앞으로 한 칸씩 당겨진 경우
			} else {
				runningTime=temp.substring(beginTimeIndex+2, endTimeIndex); //장르가 설정되어 제대로 입력된 경우
			}
			
			//0 : 없음, 1 : 있음
			int diCode=0; //감독 유무 확인
			int acCode=0; //출연진 유무 확인
			
			if (!movie.select("dt.tit_t2").text().equals("")) {
				diCode=1; //감독이 있는 경우
			}
			
			if (!movie.select("dt.tit_t3").text().equals("")) {
				acCode=1; //출연진이 있는 경우
			}
			
			//감독이나 출연진이 존재하지 않는 각각의 경우
			if (diCode==1&&acCode==0) {
				director=movie.select("dd>span.link_txt").get(1).text(); //감독
			}	else if(diCode==0&&acCode==1) {
					actor=movie.select("dd>span.link_txt").get(1).text(); //출연진
			}	else if(diCode==1&&acCode==1) {
					director=movie.select("dd>span.link_txt").get(1).text(); //감독
					actor=movie.select("dd>span.link_txt").get(2).text(); //출연진
			}
			
			String naverHref=movie.select("dt.tit>a").attr("href"); //네이버 영화 URL
			naverCode=naverHref.substring(naverHref.lastIndexOf("=")+1); //네이버 영화 코드
			
			//개봉일
			int openDtTextIndex=temp.lastIndexOf("개봉"); //개봉이라는 글자를 제외하기 위한 변수
			openDate=temp.substring(endTimeIndex+2, openDtTextIndex);
						
			//수집된 영화 정보를 movieRank에 input
			movieRank[flag][2]=reserve;
			movieRank[flag][3]=genre;
			movieRank[flag][4]=runningTime.trim(); //좌우 여백 제거
			movieRank[flag][5]=openDate.trim();
			movieRank[flag][6]=director;
			movieRank[flag][7]=actor;
			movieRank[flag][10]=naverCode;
			
			finalCnt+=1;
			
			}
		
		return movieRank;
		
	}

}
