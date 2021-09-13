import com.fasterxml.jackson.annotation.JsonProperty;
import io.restassured.http.ContentType;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.List;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class Task {
    /**
     * Task 1
     * create a request to https://httpstat.us/203
     * expect status 203
     * expect content type TEXT
     */
    @BeforeClass
    public void setup() {
        // baseURI = "https://httpstat.us/203";// iki tane baseURI kullanilamaz ayni class da,,ikincisini kabul eder
        baseURI = "https://jsonplaceholder.typicode.com/todos/";
    }

    @Test
    public void task1() {
        given()
                .when()
                .get("https://httpstat.us/203")
                .then()
                .statusCode(203)
                .contentType(ContentType.TEXT)

        ;

    }

    /**
     * Task 2
     * create a request to https://httpstat.us/203
     * expect status 203
     * expect content type TEXT
     * expect BODY to be equal to "203 Non-Authoritative Information"
     */

    @Test
    public void task2() {
        given()
                .when()
                .get("https://httpstat.us/203")
                .then()
                .log().body()
                .statusCode(203)

                .contentType(ContentType.TEXT)
                .body(equalTo("203 Non-Authoritative Information"))


        ;
    }

    /**
     * Task 3
     * create a request to https://jsonplaceholder.typicode.com/todos/2
     * expect status 200
     * expect content type JSON
     * expect title in response body to be "quis ut nam facilis et officia qui"
     */
    @Test
    public void task3() {
        given()
                .when()
                .get("https://jsonplaceholder.typicode.com/todos/2")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .log().body()
                .body("title", equalTo("quis ut nam facilis et officia qui"))
        ;

    }

    /**
     * Task 4
     * create a request to https://jsonplaceholder.typicode.com/todos/2
     * expect status 200
     * expect content type JSON
     * expect response completed status to be false
     */
    @Test
    public void task4() {
        given()
                .when()
                .get("https://jsonplaceholder.typicode.com/todos/2")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .log().body()
                .body("completed", equalTo(false))
        ;

    }

    /**
     * Task 5
     * create a request to https://jsonplaceholder.typicode.com/todos
     * expect status 200
     * expect content type JSON
     * expect third item have:
     * title = "fugiat veniam minus"
     * userId = 1
     */

    @Test
    public void task5() {
        given()
                .when()
                //.get("https://jsonplaceholder.typicode.com/todos/")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .log().body()
                .body("title[2]", equalTo("fugiat veniam minus"))
                .body("userId[2]", equalTo(1));
        ;

    }


    /**
     * Task 6
     * create a request to https://jsonplaceholder.typicode.com/todos/2
     * expect status 200
     * Converting Into POJO
     */


    @Test
    public void task6() {
        ToDo toDo =
                given()
                        .when()
                        .get("https://jsonplaceholder.typicode.com/todos/2")
                        .then()
                        .statusCode(200)
                        .log().body()
                        .extract().as(ToDo.class);
        System.out.println("toDo = " + toDo);
        System.out.println("toDo.getTitle() = " + toDo.getTitle());
        System.out.println("toDo.getId() = " + toDo.getId());

    }

    /**
     * Task 7
     * create a request to https://jsonplaceholder.typicode.com/todos
     * expect status 200
     * Converting Array Into Array of POJOs
     */
    @Test
    public void task7() {
        ToDo[] toDo =
                given()
                        .when()
                        .get("https://jsonplaceholder.typicode.com/todos")
                        .then()
                        .statusCode(200)
                        // .log().body()
                        .extract().as(ToDo[].class);
        System.out.println("Arrays.toString(toDo) = " + Arrays.toString(toDo));

    }

    /**
     * Task 8
     * create a request to https://jsonplaceholder.typicode.com/todos
     * expect status 200
     * Converting Array Into List of POJOs
     */
    @Test
    public void task8() {
        ToDo[] toDo =
                given()
                        .when()
                        .get("https://jsonplaceholder.typicode.com/todos")
                        .then()
                        .statusCode(200)
                        //.log().body()
                        .extract().as(ToDo[].class);

        List<ToDo> toDoList = Arrays.asList(toDo);
        System.out.println("toDoList = " + toDoList);
    }
}
