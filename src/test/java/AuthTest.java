import com.codeborne.selenide.Condition;

import static com.codeborne.selenide.Selenide.$;

import data.DataHelper;
import org.junit.jupiter.api.*;

import java.time.Duration;

import static com.codeborne.selenide.Selenide.open;

class AuthTest {

    @BeforeEach
    void setup() {
        open("http://localhost:9999");
    }

    @AfterAll
    static void tearDown() {
        DataHelper.cleanDB();
    }

    @Test
    @DisplayName("Active valid user should login successfully")
    void shouldSuccessfullyLoginActiveUser() {
        var user = DataHelper.createUserWithAuthCode("active");
        $("[data-test-id='login'] input").setValue(user.getLogin());
        $("[data-test-id='password'] input").setValue(user.getPassword());
        $("button.button").click();
        $("[data-test-id=code] input").setValue(user.getAutCode());
        $("[data-test-id=action-verify]").click();
//        $("[data-test-id=dashboard]").shouldBe(Condition.visible);
    }

    @Test
    @DisplayName("Blocked User Should Not Be Able to Login")
    void shouldNotSuccessfullyLoginBlockedUser() {
        var user = DataHelper.createUserWithAuthCode("blocked");
        $("[data-test-id=login] input").setValue(user.getLogin());
        $("[data-test-id=password] input").setValue(user.getPassword());
        $("button.button").click();
        $("[data-test-id = 'error-notification'] .notification__content")
                .shouldBe((Condition.visible))
                .shouldHave(Condition.text("Ошибка! Пользователь заблокирован"), Duration.ofSeconds(15));
    }

}