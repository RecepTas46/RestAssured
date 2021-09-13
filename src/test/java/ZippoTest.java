import POJO.Location;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.List;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class ZippoTest {

    @Test
    public void test() {
        given()
                // hazırlık işlemlerini yapcağız

                .when()
                // link ve aksiyon işlemleri

                .then()
        // test ve extract işlemleri
        ;
    }

    @Test
    public void statusCodeTest() {
        given()
                .when()
                .get("http://api.zippopotam.us/us/90210")
                .then()
                .log().body()   //  log.All() -> bütün respons u gösterir
                .statusCode(200)  // status kontrolü
        ;
    }

    @Test
    public void contentTypeTest() {
        given()
                .when()
                .get("http://api.zippopotam.us/us/90210")
                .then()
                .log().body()
                .contentType(ContentType.JSON)
        ;
    }

    @Test
    public void logTest() {
        given()
                .log().all()
                .when()
                .get("http://api.zippopotam.us/us/90210")

                .then()
                .log().body()
        ;
    }

    @Test
    public void checkStateInResponseBody() {
        given()

                .when()
                .get("http://api.zippopotam.us/us/90210")

                .then()
                .log().body()
                .body("country", equalTo("United States")) // body.country == United States
                .statusCode(200)
        ;
    }

    @Test
    public void bodyJsonPathTest2() {
        given()

                .when()
                .get("http://api.zippopotam.us/us/90210")

                .then()
                .log().body()
                .body("places[0].state", equalTo("California"))
                .statusCode(200)
        ;
    }

    @Test
    public void bodyJsonPathTestHasItem() {
        given()

                .when()
                .get("http://api.zippopotam.us/us/90210")

                .then()
                .log().body()
                .body("places.state", hasItem("California"))  // bütün state lerde aranan eleman var mı?
                .statusCode(200)
        ;

//        places[0].state -> listin 0 indexli elemanının state değerini verir, 1 değer
//        places.state ->    Bütün listteki state leri bir list olarak verir : California,California2   hasItem
//        List<String> list= {'California','California2'}
    }



    @Test
    public void bodyArrayHasSizeTest() {
        given()

                .when()
                .get("http://api.zippopotam.us/us/90210")

                .then()
                .body("places", hasSize(1)) // verilen path deki listin size kontrolü
                .log().body()
                .statusCode(200)
        ;
    }





    // https://gorest.co.in/public/v1/users?page=1
    @Test
    public void queryParamTest()
    {
        given()
                .param("page",1)
                .log().uri()

                .when()
                .get("https://gorest.co.in/public/v1/users")

                .then()
                .log().body()
                .body("meta.pagination.page", equalTo(1));
        ;
    }

    @Test
    public void queryParamTestCoklu()
    {
        for(int page=1;page<=10;page++) {
            given()
                    .param("page", 1)
                    .log().uri()

                    .when()
                    .get("https://gorest.co.in/public/v1/users")

                    .then()
                    .log().body()
                    .body("meta.pagination.page", equalTo(page));
        }
        ;
    }







    @Test
    public void bodyJsonPathTest4() {
        given()

                .when()
                .get("http://api.zippopotam.us/us/90210")

                .then()
                .log().body()
                .body("places[0].'place name'", equalTo("Beverly Hills"))  // bütün state lerde aranan eleman var mı?
                .statusCode(200)
        ;
    }

    @Test
    public void bodyArrayHasSize() {

        given()
                .when()
                .get("http://api.zippopotam.us/us/90210")

                .then()

                .body("places", hasSize(1))
                .log().body()
                .statusCode(200)

        ;
    }

    @Test
    public void combiningTest() {

        given()
                .when()
                .get("http://api.zippopotam.us/us/90210")

                .then()
                .body("places[0].'place name'", equalTo("Beverly Hills"))
                .body("places.state", hasItem("California"))
                .body("places", hasSize(1))
                .log().body()
                .statusCode(200)

        ;
    }

    @Test
    public void pathParamTest1() {
//        String country="us";
//        String zipKod="90210";

        given()
                .pathParam("country", "us")
                .pathParam("zipKod", "90210")

                .log().uri()
                .when()

                .get("http://api.zippopotam.us/{country}/{zipKod}")
                .then()
                .log().body()
                .body("places", hasSize(1))

        ;


    }

    @Test
    public void pathParamTest2() {
        String country = "us";

        for (int i = 90210; i < 90214; i++) {
            //String zipKod=i.toString();

            given()
                    .pathParam("country", country)
                    .pathParam("zipKod", i)

                    .log().uri()
                    .when()

                    .get("http://api.zippopotam.us/{country}/{zipKod}")

                    .then()
                    .log().body()
                    .body("places", hasSize(1))

            ;

        }
    }

    @Test
    public void queryParamTest1() {

        given()
                .param("page", 1)
                .log().uri()
                .when()
                .get("https://gorest.co.in/public/v1/users")

                .then()
                .log().body()
                .body("meta.pagination.page", equalTo(1))


        ;

    }

    //birden 10 na kadar page leri yaziniz
    @Test
    public void queryParamTest2() {
        //i=page olarak dusundum
        for (int i = 1; i <10 ; i++) {


            given()
                    .param("page", i)
                    .log().uri()
                    .when()
                    .get("https://gorest.co.in/public/v1/users")
                    .then()
                    .log().body()
                    .body("meta.pagination.page", equalTo(i))


            ;

        }
    }

    private ResponseSpecification responseSpecification;
    private RequestSpecification requestSpecification;
    @BeforeClass
    public void setup(){

        baseURI="http://api.zippopotam.us";// RestAssurd kendi statik degiskeni tanimli deger ataniyor

        requestSpecification =new RequestSpecBuilder()
                .log(LogDetail.URI)
                .setAccept(ContentType.JSON)
                .build();
        responseSpecification=new ResponseSpecBuilder()
                .expectStatusCode(200)
                .expectContentType(ContentType.JSON)
                .log(LogDetail.BODY)
                .build()
                ;
    }

    @Test
    public void bodyArrayHasSize_baseUriTest() {

        given()
                .when()
                .log().uri()
                .get("/us/90210")
                .then()
                .body("places", hasSize(1))
                .log().body()
                .statusCode(200)

        ;
    }



    @Test
    public void extractingJsonPath() {
        String place_name= given()

                .when()
                .get("http://api.zippopotam.us/us/90210")

                .then()
                .spec(responseSpecification)
                .extract().path("places[0].'place name'")

        ;
        System.out.println("place name:"+place_name);
    }

    @Test
        public void extractingJsonPathInt() {
        //Sitrig limit=given();
        int limit= given()
                .param("page", 1)
                .log().uri()
                .when()
                .get("https://gorest.co.in/public/v1/users")

                .then()
                //.log().body()//burda herseyi getiriyor ,biz sadece limit kismini gormek icin yoruma aldik
                .extract().path("meta.pagination.limit")//.toString() yazarak yapilabilir
        ;
        System.out.println("limit:"+limit);
    }
 @Test
        public void extractingJsonPathIntList() {
        //data[0].id  --> 1.elamanin yani indexi 0 olanin id si
        //data.id  -->tum id ler demektir list<int>

     List<Integer>idLer= given()
                .param("page", 1)
                .log().uri()
                .when()
                .get("https://gorest.co.in/public/v1/users")

                .then()
                //.log().body()
                .extract().path("data.id")
        ;
        System.out.println("idLer:"+idLer);
    }
    @Test
    public void extractingJsonPathStringList() {
        List<String>koyler= given()

                .when()
                .get("/tr/01000")

                .then()
                //.spec(responseSpecification)// butun listeyi veriyor
                .extract().path("places.'place name'")

                ;
        System.out.println("koyler:"+koyler);
        Assert.assertTrue(koyler.contains("Karakuyu Köyü"));
    }

@Test
    public void extractingJsonPOJO(){
        Location location=
        given()
                .when()
                .get("/us/90210")
                .then()
                .extract().as(Location.class);
        
        
        ;
    System.out.println("location = " + location);
    System.out.println("location.getCountry() = " + location.getCountry());
    System.out.println("location.getPlaces() = " + location.getPlaces());
    System.out.println("location.getPlaces().get(0).getPlacename() = " + location.getPlaces().get(0).getPlacename());        
}

}










