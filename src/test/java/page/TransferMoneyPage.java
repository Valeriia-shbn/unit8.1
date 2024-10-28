package page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.Keys;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class TransferMoneyPage {
    private final SelenideElement amount = $("[data-test-id=amount] input");
    private final SelenideElement fromAccount = $("[data-test-id=from] input");
    private final SelenideElement actionButton = $("[data-test-id=action-transfer]");

    private SelenideElement heading = $("h1.heading.heading_size_xl.heading_theme_alfa-on-white")
            .shouldHave(Condition.text("Пополнение карты"));

    public TransferMoneyPage() {
        heading.shouldBe(visible);
    }

    public DashboardPage transferMoney(String fromCard, int amountMoney) {

        amount.doubleClick();
        amount.sendKeys(Keys.DELETE);
        amount.sendKeys(String.valueOf(amountMoney));

        fromAccount.sendKeys(Keys.CONTROL + "a");
        fromAccount.sendKeys(Keys.DELETE);
        fromAccount.sendKeys(fromCard);
        actionButton.click();
        return new DashboardPage();
    }
}
