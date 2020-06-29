package domain;

public class ReplyDTO {
	
	//1. 변수 생성
	private String movieName; //영화 제목
	private String content; //리뷰 내용
	private String writer; //작성자
	private double score; //평점
	private String regdate; //작성일
	
	//2. 생성자 생성
	public ReplyDTO() {} //default constructor

	//Source(nav menu)-Generate Constructor Using Fields... 로 자동 생성
	//method overloading
	public ReplyDTO(String movieName, String content, String writer, double score, String regdate) {
		super();
		this.movieName = movieName;
		this.content = content;
		this.writer = writer;
		this.score = score;
		this.regdate = regdate;
	}

	//3. getter and setter
	//Source-Generate Getters and Setters...
	public String getMovieName() {
		return movieName;
	}

	public void setMovieName(String movieName) {
		this.movieName = movieName;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getWriter() {
		return writer;
	}

	public void setWriter(String writer) {
		this.writer = writer;
	}

	public double getScore() {
		return score;
	}

	public void setScore(double score) {
		this.score = score;
	}

	public String getRegdate() {
		return regdate;
	}

	public void setRegdate(String regdate) {
		this.regdate = regdate;
	}
	
	//4. toString()
	//Source-Generate toString()...
	//생성한 데이터를 DB에 저장하기 위한 코드를 확인
	@Override
	public String toString() {
		return "ReplyDTO [movieName=" + movieName + ", content=" + content + ", writer=" + writer + ", score=" + score
				+ ", regdate=" + regdate + "]";
	}

}
