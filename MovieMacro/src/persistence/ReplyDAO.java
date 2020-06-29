package persistence;

import org.bson.Document;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import domain.ReplyDTO;

public class ReplyDAO {
	
	MongoClient client=MongoClients.create(); //MongoDB 호스트 자동 연결
	MongoDatabase db=client.getDatabase("local"); //데이터베이스 위치
	MongoCollection<Document> collection=db.getCollection("movie"); //데이터를 저장할 collection 설정
	
	//MongoDB에 데이터 저장
	public void addReply(ReplyDTO rDto) {
		
		Document doc=new Document("movieName", rDto.getMovieName())
				.append("content", rDto.getContent())
				.append("writer", rDto.getWriter())
				.append("score", rDto.getScore())
				.append("regdate", rDto.getRegdate());
		
					//one : 단수
		collection.insertOne(doc); //리뷰 한  DB 등록
		
	}
	
	//DB에 등록하려는 영화의 리뷰가 존재할 때 해당 영화 리뷰만 삭제
	public void deleteReply(String movieName) {
		
		//many : 복수
		collection.deleteMany(new Document("movieName", movieName)); //DB에 등록된 다수의 리뷰 삭제		
		
	}

}
