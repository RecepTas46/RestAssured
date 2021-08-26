import io.restassured.http.ContentType;
import org.testng.annotations.Test;

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
    public void bodyJsonPathTest422() {
        given()

                .when()
                .get("http://api.zippopotam.us/us/90210")

                .then()
                .log().body()
                .body("places[0].'place name'", equalTo("Beverly Hills"))
                // arasında bosluk olan key lerde keyin başına ve sonuna tek tırnak konur ('place name')
                .statusCode(200)
        ;
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

    @Test
    public void combiningTest22() {
        given()

                .when()
                .get("http://api.zippopotam.us/us/90210")

                .then()
                .body("places", hasSize(1))
                .body("places.state", hasItem("California"))
                .body("places[0].'place name'", equalTo("Beverly Hills"))
                // 1 den fazla assertion yapılabilir.
                .statusCode(200)
        ;
    }

    @Test
    public void pathParamTest() {
        String country = "us";
        String zipKod = "90210";

        given()
                .pathParam("country", country)
                .pathParam("zipKod", zipKod)
                .log().uri() //request linki

                .when()
                .get("http://api.zippopotam.us/{country}/{zipKod}")

                .then()
                .log().body()
                .body("places", hasSize(1))
        ;
    }

    @Test
    public void pathParamTest22() {
        String country = "us";

        for (int i = 90210; i < 90214; i++) {
            //String zipKod = i.toString();

            given()
                    .pathParam("country", country)
                    .pathParam("zipKod", i)
                    .log().uri() //request linki

                    .when()
                    .get("http://api.zippopotam.us/{country}/{zipKod}")

                    .then()
                    .log().body()
                    .body("places", hasSize(1))
            ;
        }

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
}










