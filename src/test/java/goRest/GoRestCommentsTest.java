package goRest;
import goRest.Model.Comments;
import goRest.Model.CommentsBody;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class GoRestCommentsTest {
    // Task 1: https://gorest.co.in/public/v1/comments  Api sinden dönen verilerdeki data yı bir nesne yardımıyla
    //         List olarak alınız.

    @Test
    public void commentsTest() {
        Response response=

                given()
                        .when()
                        .get("https://gorest.co.in/public/v1/comments")
                        .then()
                        .extract().response()
                        //.extract().jsonPath().getList("data");
;
        List<Comments> commentsList =response.jsonPath().getList("data");
        List<Comments> commentsList2 =response.jsonPath().getList("data",Comments.class);

       System.out.println("commentsList = " + commentsList);
        System.out.println("commentsList2 = " + commentsList2);

//        for (Comments c:commentsList)
//        {
//            System.out.println("c = " + c);
//        }

        for (Comments c:commentsList2)
        {
            System.out.println("l2 c = " + c);
        }

    }
    // Bütün Comment lardaki emailleri bir list olarak alınız ve
    // içinde "acharya_rajinder@ankunding.biz" olduğunu doğrulayınız.
@Test
    public void getEmailList()
{
    List<String>emailList=
    given()
            .when()
            .get("https://gorest.co.in/public/v1/comments")
            .then()
            //.log().body()
            .extract().path("data.email");
            ;
    System.out.println("emailList = " + emailList);
    for (String s:emailList) {
        System.out.println("s = " + s);
    }
    Assert.assertTrue(emailList.contains("acharya_rajinder@ankunding.biz"));
}

    @Test
    public void getEmailListRespons()
    {
        Response response=
                given()
                        .when()
                        .get("https://gorest.co.in/public/v1/comments")
                        .then()
                        //.log().body()
                        .extract().response();
        ;
        List<String>emailList=response.path("data.email");
       // List<Integer>idList=response.path("data.id");
        List<String>emailList2=response.jsonPath().getList("data.email",String.class);//STRING.Class yapilmasa da olur

        System.out.println("emailList = " + emailList);

        for (String s:emailList) {
            System.out.println("s = " + s);
        }
        Assert.assertTrue(emailList.contains("acharya_rajinder@ankunding.biz"));
    }

    // Task 3 : https://gorest.co.in/public/v1/comments  Api sinden
    // dönen bütün verileri tek bir nesneye dönüştürünüz

    @Test
    public void getCommentsPojo(){
CommentsBody commentsBody=
        given()
                .when()
                .get("https://gorest.co.in/public/v1/comments")
                .then()
                //.log().body()
        .extract().as(CommentsBody.class)
                ;
//artzik elimde response yani tum datani nesne hali var
        System.out.println("commentsBody.getData().get(5).getEmail() = " + commentsBody.getData().get(5).getEmail());
        System.out.println("commentsBody.getMeta().getPagination().getLinks().getCurrent() = " + commentsBody.getMeta().getPagination().getLinks().getCurrent());
    }

// Task 4 : https://gorest.co.in/public/v1/comments  Api sine
    // 1 Comments Create ediniz.
int commentId;
    @Test
    public void CommentCreate() {
        Comments comment = new Comments();
        comment.setName("recep t");
        comment.setEmail("recep@gmail.com");
        comment.setBody("Önce manuel, sonra atumation");

        commentId =
                given()
                        .header("Authorization", "Bearer c9d90c12b6672f8536f97d67e8a704d63b0ba74bdf7e0fa9b79b8d3e356416dd")
                        .body(comment)
                        .contentType(ContentType.JSON)

                        .when()
                        .post("https://gorest.co.in/public/v1/posts/123/comments")// 68 burada konu id ye karşılık gelen 68 i kullandık

                        .then()
                        .log().body()
                        .extract().jsonPath().getInt("data.id")
        ;

        System.out.println("commentId = " + commentId);
    }
    // Task 7 : Create edilen Comment ı get yapınız.
    @Test(dependsOnMethods = "CommentCreate")
    public void getComment() {
        given()
                .pathParam("commentId", commentId)
                .when()
                .get("https://gorest.co.in/public/v1/comments/{commentId}")

                .then()
                .log().body()
                .body("data.id", equalTo(commentId))
                .statusCode(200)
        ;
    }
// Task 5 : Create edilen Comment ı n body kısmını güncelleyiniz.Sonrasında güncellemeyi kontrol ediniz.

    @Test(dependsOnMethods = "CommentCreate", priority = 1)
    public void CommentUpdate() {
        String postBody = "Önce manuel, sonra atumation";

        // String returnPostBody=
        given()
                .header("Authorization", "Bearer c9d90c12b6672f8536f97d67e8a704d63b0ba74bdf7e0fa9b79b8d3e356416dd")
                .body("{\"body\": \"" + postBody + "\"}")
                .contentType(ContentType.JSON)
                .pathParam("commentId", commentId)
                .log().body()
                .when()
                .put("https://gorest.co.in/public/v1/comments/{commentId}")

                .then()
                .log().body()
                //.extract().path("data.body")
                .body("data.body", equalTo(postBody))
        ;

        //Assert.assertTrue(returnPostBody.equalsIgnoreCase(postBody));
    }

    // Task 6 : Create edilen Comment ı siliniz. Status kodu kontorl ediniz 204
    @Test(dependsOnMethods = "CommentCreate", priority = 2)
    public void CommentDelete() {
        given()
                .header("Authorization", "Bearer c9d90c12b6672f8536f97d67e8a704d63b0ba74bdf7e0fa9b79b8d3e356416dd")
                .pathParam("commentId", commentId)

                .when()
                .delete("https://gorest.co.in/public/v1/comments/{commentId}")

                .then()
                .log().body()
                .statusCode(204)
        ;
    }

    // Task 6 : Silinen Comment ın negatif testini tekrar silmeye çalışarak yapınız.
    @Test(dependsOnMethods = "CommentDelete")
    public void CommentDeleteNegative() {
        given()
                .header("Authorization", "Bearer c9d90c12b6672f8536f97d67e8a704d63b0ba74bdf7e0fa9b79b8d3e356416dd")
                .pathParam("commentId", commentId)

                .when()
                .delete("https://gorest.co.in/public/v1/comments/{commentId}")

                .then()
                .log().body()
                .statusCode(200)
        ;
    }


}


