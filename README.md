# SimpleMovie Project:movie_camera:
JAVA기반 한국 영화 박스오피스 1~10위 정보를 Parsing 및 Crawling하고 MongoDB에 저장 후 사용자에게 정보를 출력해주는 Console Programing

## :heavy_check_mark: Developer Environment
  - Language : :coffee: JAVA 1.8
  - IDE Tool : :computer: Eclipse
  - Package Manager : Maven Repository
  - Using Package : jsoup, json-simple, mongo-java-driver
  - Version Tools : Github, Sourcetree
  - Parsing URL : [한국영화진흥위원회](https://www.kobis.or.kr/kobisopenapi/homepg/main/main.do)
  - Crawling URL
    + [NAVER MOVIE](https://movie.naver.com/)
    + [DAUM MOVIE](https://movie.daum.net/)

## :pencil2: Repository Structure Description
#### 1. src/common
  - [BoxOfficeParser](https://github.com/Kim-hyeran/2020_Java_Project_SimpleMovie/tree/master/MovieMacro/src/common/BoxOfficeParser) : 한국영화진흥위원회에서 일별 박스오피스 정보 수집(순위, 영화제목, 누적관객수, 누적매출액)
  - [SimpleMovieMain](https://github.com/Kim-hyeran/2020_Java_Project_SimpleMovie/tree/master/MovieMacro/src/common/SimpleMovieMain) : 프로그램 새작하는 곳, 콘솔 프로그래밍 view 단
#### 2. src/naver
  - [BoxOfficeNaver](https://github.com/Kim-hyeran/2020_Java_Project_SimpleMovie/tree/master/MovieMacro/src/BoxOfficeNaver) : 네이버에서 박스오피스 1~10위에 해당하는 사이트 고유의 영화 코드 수집
  - [ReplyCrawlerNaver](https://github.com/Kim-hyeran/2020_Java_Project_SimpleMovie/tree/master/MovieMacro/src/naver/ReplyCrawlerNaver) : 네이버에서 해당 영화의 댓글, 평점, 작성자, 작성일자를 수집해 MongoDB에 저장
#### 3. src/daum
  - [BoxOfficeDaum](https://github.com/Kim-hyeran/2020_Java_Project_SimpleMovie/tree/master/MovieMacro/src/daum/BoxOfficeDaum) : 다음에서 박스오피스 1~10위에 해당하는 사이트 고유의 영화 코드 수집
  - [ReplyCrawlerDaum](https://github.com/Kim-hyeran/2020_Java_Project_SimpleMovie/tree/master/MovieMacro/src/daum/ReplyCrawlerDaum) : 다음에서 해당 영화의 댓글, 평점, 작성자, 작성일자를 수집해 MongoDB에 저장
#### 4. src/persistence
  - [ReplyDAO](https://github.com/Kim-hyeran/2020_Java_Project_SimpleMovie/tree/master/MovieMacro/src/persistence/ReplyDAO) : 네이버, 다음에서 수집한 영화 댓글 저장 또는 삭제할 때 사용하는 DAO
#### 5. src/domain
  - [ReplyDTO](https://github.com/Kim-hyeran/2020_Java_Project_SimpleMovie/tree/master/MovieMacro/src/domain/ReplyDTO) : 네이버, 다음에서 영화 댓글 수집 후 MongoDB에 저장할 때 사용하는 DTO
#### 6. pom.xml
  - Maven에서 build할 Library를 설정하는 장소

## :love_letter: How to use?
  1. BoxOfficeParser에서 발급받은 key를 교체한다.
  2. ReplyDAO에서 MongoDB를 세팅한다(Connect, DB, Collection 등)
  3. 메인 프로그램을 실행한다.
  4. 1~10위 중 원하는 영화를 선택하고, 순위를 입력한다.
  5. Run the Program!
