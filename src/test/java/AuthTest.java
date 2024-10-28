import data.DataHelper;
import org.junit.jupiter.api.*;
import page.LoginPage;

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
        var user = DataHelper.createUser("active");

        var loginPage = new LoginPage();

        var verificationPage = loginPage.validLogin(user);

        var verificationCode = DataHelper.getVerificationCode(user.getId());

        verificationPage.validVerify(verificationCode);

    }
}