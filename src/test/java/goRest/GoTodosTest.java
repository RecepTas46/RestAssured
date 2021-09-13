package goRest;

import goRest.Model.Todos;
import goRest.Model.todosBody;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;


// Ayın sorusu : https://gorest.co.in/public/v1/todos  Api sinden dönen verilerdeki
// zaman olarak ilk todo nun hangi userId ye ait olduğunu bulunuz

public class GoTodosTest {


    // Task 1: https://gorest.co.in/public/v1/todos  Api sinden dönen verilerdeki
    //         en son todo nun id sini bulunuz
    @BeforeClass
    public void beforeClass() {
        baseURI = "https://gorest.co.in/public/v1";
    }

    int maxId = 0;

    @Test(enabled = false)
    public void getTodosList() {
        List<Todos> todosList =
                given()
                        .when()
                        .log().uri()
                        .get("https://gorest.co.in/public/v1/todos")
                        .then()
                        //.log().body()
                        .contentType(ContentType.JSON)
                        .statusCode(200)
                        .extract().jsonPath().getList("data", Todos.class);
        System.out.println("todosList = " + todosList);

        //en son todos nun id sini bulunuz

        for (int i = 0; i < todosList.size(); i++) {

            if (todosList.get(i).getId() > maxId) {
                maxId = todosList.get(i).getId();
            }
            System.out.println("maxId = " + maxId);
        }


    }
// Task 2: https://gorest.co.in/public/v1/todos  Api sinden dönen verilerdeki
    //         en büyük id ye sahip todo nun id sini BÜTÜN PAGE leri dikkate alarak bulunuz.


    @Test(enabled = false)
    public void getBigestIdAllOfPage() {
        int totalPage = 2;
        for (int page = 1; page <= totalPage; page++) {

            Response response =
                    given()
                            .param("page", page)
                            .when()
                            .get("/todos")
                            .then()
                            .contentType(ContentType.JSON)
                            .log().body()

                            .extract().response();

            totalPage = response.jsonPath().getInt("meta.pagination.pages");

            System.out.println("totalPage = " + totalPage);
        }

    }

    @Test(enabled = false)
    public void getBigestIdAllOfPage1() {
        int totalPage = 0, page = 1;

        do {
            Response response =
                    given()
                            .param("page", 2)
                            .when()
                            .get("/todos")
                            .then()
                            .contentType(ContentType.JSON)
                            .log().body()

                            .extract().response();
            //kac sayfa oldugunu bulduk
            if (page == 1)
                totalPage = response.jsonPath().getInt("meta.pagination.pages");
//siradaki page nin data sini aldik
            List<Todos> pageList = response.jsonPath().getList("data", Todos.class);
//elinizdeki en son maxId yi alarak bu pagedeki ID ler karsilastirip en buyuk ID yi aldik
            for (int i = 0; i < pageList.size(); i++) {
                if (maxId < pageList.get(i).getId())
                    maxId = pageList.get(i).getId();
            }
            page++;//sonraki sayfaya geciyor
        } while (page <= totalPage);
        System.out.println("maxId = " + maxId);
    }


    // Task 3 : https://gorest.co.in/public/v1/todos  Api sinden
    // dönen bütün  sayfalardaki bütün idleri tek bir Liste atınız.

    @Test(enabled = false)
    public void getTodosListAllPage() {
        int totalPage = 0, page = 1;
        List<Integer> allTodosList = new ArrayList<>();
        do {

            Response response =
                    given()
                            .param("page", page) // ?page=1
                            .when()
                            .get("/todos")

                            .then()
                            .extract().response();

            if (page == 1)
                totalPage = response.jsonPath().getInt("meta.pagination.pages");

            List<Integer> pageList = response.jsonPath().getList("data.id");

            allTodosList.addAll(pageList);

            page++;

        } while (page <= totalPage);
        System.out.println("id = " + allTodosList);
    }

    // Task 4 : https://gorest.co.in/public/v1/todos  Api sine
    // 1 todos Create ediniz.
    public String date() {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_DATE_TIME;
        LocalDateTime date = LocalDateTime.now();
        return date.format(dateTimeFormatter);
    }

    int todosID;

    @Test
    public void createTodos() {

        Todos todos = new Todos();

        todos.setTitle("Rest Assured");
        todos.setUser_id(87);
        todos.setDue_on(date());
        todos.setStatus("completed");

        todosID =
                given()
                        .header("Authorization",
                                "Bearer c9d90c12b6672f8536f97d67e8a704d63b0ba74bdf7e0fa9b79b8d3e356416dd")
                        .contentType(ContentType.JSON)
                        .body(todos)

                        .when()
                        .post("/todos")

                        .then()
                        .log().body()
                        .extract().jsonPath().getInt("data.id")
        ;

        System.out.println("todosID = " + todosID);
    }
    //ikinci cozum..
    @Test
    public void createTodo()
    {
        Todos todo=new Todos();
        todo.setStatus("pending");
        todo.setTitle("Bizim asistanımız bi tane");
        todo.setDue_on("2021-09-25");   //  todo.setDue_on(getNowDateToString());
        todo.setUser_id(7);

        todosID=
                given()
                        .header("Authorization", "Bearer 36e95c8fd3e7eb89a65bad6edf4c0a62ddb758f9ed1e15bb98421fb0f1f3e57f")
                        .contentType(ContentType.JSON)
                        .body(todo)
                        .when()
                        .post("/todos")

                        .then()
                        .log().body()
                        .statusCode(201)
                        .extract().jsonPath().getInt("data.id")
        ;

        System.out.println("todoId = " + todosID);
    }

    // Task 5 : Create edilen ToDo yu get yaparak id sini kontrol ediniz.

    @Test(dependsOnMethods = "createTodos")
    public void getTodosByID() {

        given()
                .pathParam("todosID", todosID)
                .log().uri()
                .when()
                .get("/todos/{todosID}")

                .then()
                //.log().body()
                .statusCode(200)
                .body("data.id", equalTo(todosID))
        ;

    }

    // Task 6 : Create edilen ToDo u un status kısmını ("complated") güncelleyiniz.
    //  Sonrasında güncellemeyi kontrol ediniz.

    @Test(dependsOnMethods = "createTodos",priority = 1)
    public void getUpdateTodosById() {
        String title = "SONA YAKLASTIK...";
        given()
                .header("Authorization", "Bearer c9d90c12b6672f8536f97d67e8a704d63b0ba74bdf7e0fa9b79b8d3e356416dd")
                .contentType(ContentType.JSON)
                .body("{\"title\":\"" + title + "\"}")
                .pathParam("todosID", todosID)
                .log().uri()
                .when()
                .put("/todos/{todosID}")
                .then()
                .log().body()
                .statusCode(200)
                .body("data.title", equalTo(title))


        ;


    }

    // Task 7 : Create edilen ToDo yu siliniz. Status kodu kontorl ediniz 204
    @Test(dependsOnMethods = "createTodos",priority = 2)
    public void deleteTodosById() {

        given()
                .header("Authorization", "Bearer c9d90c12b6672f8536f97d67e8a704d63b0ba74bdf7e0fa9b79b8d3e356416dd")

                .pathParam("todosID", todosID)
                .log().uri()

                .when()
                .delete("/todos/{todosID}")

                .then()
                .log().body()
                .statusCode(204)

        ;
    }@Test(dependsOnMethods = "deleteTodosById")
    public void deleteTodosNegative() {

        given()
                .header("Authorization", "Bearer c9d90c12b6672f8536f97d67e8a704d63b0ba74bdf7e0fa9b79b8d3e356416dd")

                .pathParam("todosID", todosID)
                .log().uri()

                .when()
                .delete("/todos/{todosID}")

                .then()
                .log().body()
                .statusCode(404)

        ;
    }
}