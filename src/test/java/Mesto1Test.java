import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;
//import static org.hamcrest.Matchers.equalTo;

    public class Mesto1Test {

        String bearerToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJfaWQiOiI2MmJhZGU3ZTA0NmM5ZDAwM2QxNmY0ZDYiLCJpYXQiOjE2NjEyODc2ODcsImV4cCI6MTY2MTg5MjQ4N30.J5h__x3Lquc8yCfRADOp-RaDWh6i6muGT36WwE0fGaQ";

        @Before
        public void setUp() {
            RestAssured.baseURI = "https://qa-mesto.praktikum-services.ru";
        }

        @Test
        @DisplayName("Add a new photo")
        @Description("This test is for adding a new photo to Mesto.")
        public void addNewPhoto() {
            given()
                    .header("Content-type", "application/json")
                    .auth().oauth2(bearerToken)
                    .body("{\"name\":\"Москва\",\"link\":\"https://code.s3.yandex.net/qa-automation-engineer/java/files/paid-track/sprint1/photoSelenium.jpg\"}")
                    .post("/api/cards")
                    .then().statusCode(201);
        }

        @Test
        @DisplayName("Like the first photo")
        @Description("This test is for liking the first photo on Mesto.")
        public void likeTheFirstPhoto() {
            String photoId = getTheFirstPhotoId();

            likePhotoById(photoId);
            deleteLikePhotoById(photoId);
        }

        @Step("Take the first photo from the list")
        private String getTheFirstPhotoId() {
            return given()
                    .auth().oauth2(bearerToken)
                    .get("/api/cards")
                    .then().extract().body().path("data[0]._id");
        }

        @Step("Like a photo by id")
        private void likePhotoById(String photoId) {
            given()
                    .auth().oauth2(bearerToken)
                    .put("/api/cards/{photoId}/likes", photoId)
                    .then().assertThat().statusCode(200);
        }

        @Step("Delete like from the photo by id")
        private void deleteLikePhotoById(String photoId) {
            given()
                    .auth().oauth2(bearerToken)
                    .delete("/api/cards/{photoId}/likes", photoId)
                    .then().assertThat().statusCode(200);
        }

    }
