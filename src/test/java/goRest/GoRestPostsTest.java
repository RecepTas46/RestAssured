package goRest;

import goRest.Model.PostsBody;
import goRest.Model.Posts;
import io.restassured.http.ContentType;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import java.util.List;

public class GoRestPostsTest {
    // Task 1 : https://gorest.co.in/public/v1/posts  API sinden dönen data bilgisini bir class yardımıyla
    // List ini alınız.

    @BeforeClass
    public void startUp() {
        baseURI = "https://gorest.co.in/public/v1";
    }


    @Test
    public void GetPostsPojo() {

        List<Posts> posts =
                given()

                        .when()
                        .get("/posts")

                        .then()
                        .statusCode(200)
                        .contentType(ContentType.JSON)
                        .log().body()
                        .extract().jsonPath().getList("data", Posts.class);

        for (Posts p : posts
        ) {
            System.out.println("p = " + p);

        }

    }
    // Task 2 : https://gorest.co.in/public/v1/posts  API sinden sadece 1 kişiye ait postları listeletiniz.
    //  https://gorest.co.in/public/v1/users/87/posts

    @Test
    public void getUserPosts() {
        List<Posts> postUserList =
                given()

                        .when()
                        .get("/users/87/posts")

                        .then()
                        .extract().jsonPath().getList("data", Posts.class);

        for (Posts p : postUserList) {
            System.out.println("p = " + p);
        }

    }

    // Task 3 : https://gorest.co.in/public/v1/posts  API sinden dönen bütün bilgileri tek bir nesneye atınız
    @Test
    public void getAllPostsAsObject() { // POJO
        PostsBody postsBody =
                given()

                        .when()
                        .get("/posts")

                        .then()
                        .extract().as(PostsBody.class);

        System.out.println("postsBody.getMeta().getPagination().getLinks().getCurrent() = " + postsBody.getMeta().getPagination().getLinks().getCurrent());
        System.out.println("postsBody.getData().get(3).getTitle() = " + postsBody.getData().get(3).getTitle());
    }

    // Task 4 : https://gorest.co.in/public/v1/posts  API sine 87 nolu usera ait bir post create ediniz.
    // gönderdiğiniz bilgilerin kayıt olduğunu kontrol ediniz.
    int postId = 0;

    @Test
    public void createPost() {
        String title = "vatan";
        String body = "bize heryer angara";

        Posts post = new Posts();
        post.setTitle(title);
        post.setBody(body);

        postId =
                given()
                        .header("Authorization", "Bearer c9d90c12b6672f8536f97d67e8a704d63b0ba74bdf7e0fa9b79b8d3e356416dd")
                        .contentType(ContentType.JSON)
                        .body(post)

                        .when()
                        .post("/users/87/posts")

                        .then()
                        .log().body()
                        .body("data.title", equalTo(title))
                        .body("data.body", equalTo(body))
                        .extract().jsonPath().getInt("data.id")
        ;

        System.out.println("postId = " + postId);
    }

    @Test(enabled = false)//enable yapmamizin sebebi deger testler calisirken bos yere calismasin diye,,calismamasini istedigimize enable yaz
    public void createPost2() {
        String postBody = "icat çıkarmak iyidir";
        String postTitle = "icat";

        Posts post = new Posts();
        post.setBody(postBody);
        post.setTitle(postTitle);
        post.setUser_id(87);

        postId =
                given()
                        .header("Authorization", "Bearer c9d90c12b6672f8536f97d67e8a704d63b0ba74bdf7e0fa9b79b8d3e356416dd")
                        .contentType(ContentType.JSON)
                        .body(post)
                        //.log().body()
                        .when()
                        .post("/posts")
                        .then()
                        .log().body()
                        .body("data.title", equalTo(postTitle))
                        .body("data.body", equalTo(postBody))
                        .extract().jsonPath().getInt("data.id");
        ;

        System.out.println("postId = " + postId);
    }

    // Task 5 : Create edilen Post ı get yaparak id sini kontrol ediniz.
    @Test(dependsOnMethods = "createPost")
    public void getPost() {
        given()
                .pathParam("postId", postId)

                .when()
                .get("posts/{postId}")

                .then()
                .statusCode(200)
                //.log().body()
                .body("data.id", equalTo(postId))
        ;
    }

    // Task 6 : Create edilen Post un body sini güncelleyerek, bilgiyi kontrol ediniz.
    @Test(dependsOnMethods = "createPost",priority = 1)
    public void postUpdate() {
        String body = "Sen bir tanesin";

        given()
                .header("Authorization", "Bearer c9d90c12b6672f8536f97d67e8a704d63b0ba74bdf7e0fa9b79b8d3e356416dd")
                .contentType(ContentType.JSON)
                .body("{\"body\":\"" + body + "\"}")
                .pathParam("postId", postId)
                .when()
                .put("posts/{postId}")

                .then()
                .log().body()
                .statusCode(200)
                .body("data.body", equalTo(body));
        ;
    }

    // Task 7 : Create edilen Post u siliniz. Status kodu kontorl ediniz 204
    @Test(dependsOnMethods = "createPost",priority = 2)
    public void postDelete() {
        given()
                .header("Authorization", "Bearer c9d90c12b6672f8536f97d67e8a704d63b0ba74bdf7e0fa9b79b8d3e356416dd")
                .pathParam("postId", postId)

                .when()
                .delete("/posts/{postId}")

                .then()
                .log().body()
                .statusCode(204)
        ;
    }
    // Task 8 : Silinen Post un negatif testini tekrar silmeye çalışarak yapınız.
    @Test(dependsOnMethods = "postDelete")
    public void PostDeleteNegative() {
        given()
                .header("Authorization", "Bearer c9d90c12b6672f8536f97d67e8a704d63b0ba74bdf7e0fa9b79b8d3e356416dd")
                .pathParam("postId", postId)

                .when()
                .delete("/posts/{postId}")

                .then()
                .log().body()

                .statusCode(404)
        ;
    }

}
