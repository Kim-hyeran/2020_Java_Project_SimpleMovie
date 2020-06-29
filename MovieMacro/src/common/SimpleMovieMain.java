package common;

import java.math.BigInteger;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Scanner;

import daum.BoxOfficeDaum;
import daum.ReplyCrawlerDaum;
import naver.BoxOfficeNaver;
import naver.ReplyCrawlerNaver;
import persistence.ReplyDAO;

public class SimpleMovieMain {

	private static final String HashMap = null;

	public static void main(String[] args) throws Exception {
		
		BoxOfficeParser bParser=new BoxOfficeParser();
		BoxOfficeNaver bon=new BoxOfficeNaver();
		BoxOfficeDaum bod=new BoxOfficeDaum();
		ReplyCrawlerNaver nCrawler=new ReplyCrawlerNaver();
		ReplyCrawlerDaum dCrawler=new ReplyCrawlerDaum();
		ReplyDAO rDao=new ReplyDAO();
		
		//순위, 영화 제목, 예매율, 장르, 상영 시간, 개봉일자, 감독, 출연진, 누적관객수, 누적매출액, 네이버코드, 다음코드
		
		//1. 박스오피스 정보+네이버 영화 정보+다음 영화 정보(1위부터 10위까지)
		String[][] movieRank=new String[10][12];
		
		//1-1. BoxOffice Parsing
		movieRank=bParser.getParser();
		
		//1-2. Naver BoxOffice Crawling
		movieRank=bon.naverMovieRank(movieRank);
		
		//1-3. Daum BoxOffice Crawling
		movieRank=bod.daumMovieRank(movieRank);
		
		//2. view 단 실행(interface 호출)
		int userInput=userInterface(movieRank); //사용자가 입력한 영화 번호(순위)
		
		//3. 사용자가 선택한 영화의 네이버, 다음 댓글 정보를 수집 및 분석
		//3-1. MongoDB 데이터 삭제
		rDao.deleteReply(movieRank[userInput-1][1]); //수집하는 리뷰의 영화가 MongoDB에 저장되어 있는 영화라면 해당 영화 리뷰를 우선 삭제 후 새로운 리뷰 저장
		//3-2. Naver 영화 리뷰 수집, MongoDB에 저장
		HashMap<String, Integer> nMap=nCrawler.naverCrawler(movieRank[userInput-1][1], movieRank[userInput-1][10]); //10 : 배열에 저장된 네이버 영화 코드의 위치
									//userInput : 사용자가 찾고자 하는 영화의 순위(-1 : 배열이기 때문에 값이 0번부터 저장되어 있음)
		//3-3. Daum 영화 리뷰 수집, MongoDB에 저장
		HashMap<String, Integer> dMap=dCrawler.daumCrawler(movieRank[userInput-1][1], movieRank[userInput-1][11]);
		
		//4. 사용자에게 결과 출력
		double nTotal=nMap.get("total"); //integer형 total 값을 double 변수 안에 넣기
		double avgNaver=nTotal/nMap.get("count"); //모든 리뷰의 점수를 합쳐 평균을 내도록 하는 코드
		double dTotal=dMap.get("total");
		double avgDaum=dTotal/dMap.get("count");
		
		DecimalFormat dropDot=new DecimalFormat(".#"); //소수점 첫 번째 자리까지 출력
		DecimalFormat threeDot=new DecimalFormat("###,###");
		//bigInteger : integer, long의 범위를 벗어나 무한대에 가까운 정수까지도 표현 가능
		//객체 생성 시 괄호 안에는 String형만이 들어올 수 있다(long보다 더 큰 숫자를 표현하기 때문에 논리적으로 그 이상의 정수를 넣을 수 없다)
		BigInteger money=new BigInteger(movieRank[userInput-1][9]);
		
		System.out.println();
		System.out.println("☆		★		☆		★		☆");
		System.out.println("\t\t☆Description of "+movieRank[userInput-1][1]+"☆");
		System.out.println("☆장르 : "+movieRank[userInput-1][3]+"\t\t☆예매율 : "+movieRank[userInput-1][2]+"%");
		System.out.println("☆상영시간 : "+movieRank[userInput-1][4]+"\t\t☆개봉일 : "+movieRank[userInput-1][5]);
		System.out.println("☆감독 : "+movieRank[userInput-1][6]);
		System.out.println("☆출연진 : "+movieRank[userInput-1][7]);
											//DecimalFormat에 값을 넣기 위해서는 숫자(정수, 실수)여야만 하는데, Array를 String으로 설정하였기 때문에 형변환이 필요
		System.out.println("☆누적관객수 : "+threeDot.format(Integer.parseInt(movieRank[userInput-1][8]))+"명"+"\t\t☆누적매출액 : "+threeDot.format(money)+"원");
		System.out.println("☆네이버 리뷰 : "+nMap.get("count")+"건"+"\t\t☆네이버 평점 : "+dropDot.format(avgNaver)+"점");
		System.out.println("☆다음 리뷰 : "+dMap.get("count")+"건"+"\t\t☆다음 평점 : "+dropDot.format(avgDaum)+"점");
		System.out.println("☆		★		☆		★		☆");
	
	}
		
		//VIEW : 프로그램 시작 인터페이스+사용자 값 입력
	public static int userInterface(String[][] movieRank) {
		
		//Scanner : 사용자에게 값을 키보드로 입력할 수 있게 함
		Scanner sc=new Scanner(System.in);
		int userInput=0;
		
		// 2. View
		// 2-1. 유저에게 BoxOffice 예매율 1위부터 10위까지의 정보 제공
		Calendar cal=Calendar.getInstance(); //날짜 지정 코드
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy.MM.dd");
		String today=sdf.format(cal.getTime());
		SimpleDateFormat engSdf=new SimpleDateFormat("MM.dd");
		String engDay=engSdf.format(cal.getTime());
		
		System.out.println("★SimpleMovie Ver1.2★");
		System.out.println("☆☆Developer : Kim HyeRan(candybox)");
		System.out.println("☆		★		☆		★		☆");
		System.out.println("☆TODAY : "+today);
		System.out.println("☆Real-time BoxOffice Rank ("+engDay+")");
		
		for (int i = 0; i < movieRank.length; i++) {
			String noneCode="";
			
			if(movieRank[i][10]==null || movieRank[i][11]==null) {
				noneCode=" (상영 정보가 존재하지 않습니다.)";
			} /*else {
				//통과 : 사용자에게 입력받는 값이 1~10
				sc.close();
				break;
			}*/
			System.out.println("*"+movieRank[i][0]+"위\t"+movieRank[i][1]+noneCode);
		}
		
		System.out.println("☆		★		☆		★		☆");
		
		//2-2. 사용자가 입력하는 부분
		while(true) {
			System.out.println("☆☆영화 번호(순위)를 입력하세요.");
			System.out.print("	번호 : ");

			//유효성 체크
			//1부터 10까지의 값(정상)
			//1)1~10 이외의 값을 넣었을 때
			userInput=sc.nextInt();
			
			if(userInput<0 || userInput>10) {
				System.out.println("☆☆잘못된 입력입니다. 1부터 10 사이의 값을 입력하세요.");
				System.out.println();
				continue;
			//2)정보가 없는 영화를 선택했을 때
			} else if (movieRank[userInput-1][10]==null) {
				//사용자가 입력한 번호의 영화에 정보가 존재하는지 체크
				System.out.println("☆☆해당 영화는 성영 정보가 존재하지 않습니다.");
				System.out.println();
				continue;
			} else {
				//사용자가 입력한 값이 0부터 10
				break;
			}
		}
		
		System.out.println();
		
		return userInput;
	}

	//movieRank를 출력하는 코드
	public static void printArr(String[][] movieRank) {
	
		for (int i = 0; i < movieRank.length; i++) {
			System.out.print(movieRank[i][0]+"\t");
			System.out.print(movieRank[i][1]+"\t");
			System.out.print(movieRank[i][2]+"\t");
			System.out.print(movieRank[i][3]+"\t");
			System.out.print(movieRank[i][4]+"\t");
			System.out.print(movieRank[i][5]+"\t");
			System.out.print(movieRank[i][6]+"\t");
			System.out.print(movieRank[i][7]+"\t");
			System.out.print(movieRank[i][8]+"\t");
			System.out.print(movieRank[i][9]+"\t");
			System.out.print(movieRank[i][10]+"\t");
			System.out.print(movieRank[i][11]);
			System.out.println();
			}
		
	}

}
