package page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import lombok.val;

import static com.codeborne.selenide.Condition.attribute;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class DashboardPage {
    private SelenideElement heading = $("[data-test-id=dashboard]");
    private final ElementsCollection depositCardButton = $$("[data-test-id=action-deposit]");
    private ElementsCollection cards = $$(".list__item div");
    private final String balanceStart = "баланс: ";
    private final String balanceFinish = " р.";
    private final String firstCardTestId = "92df3f1c-a033-48e6-8390-206f6b1f56c0";
    private final String secondCardTestId = "0f3f5c2a-249e-4c3d-8287-09f7a039391d";


    public DashboardPage() {
        heading.shouldBe(visible);
    }

    public String getCardTextByTestId(String testId) {
        SelenideElement card = cards.filterBy(attribute("data-test-id", testId)).first();
        return card.getText();
    }

    public int getFirstCardBalance() {
        return getCardBalance(firstCardTestId);
    }

    public int getSecondCardBalance() {
        return getCardBalance(secondCardTestId);
    }

    public int getCardBalance(String id) {
        String text = getCardTextByTestId(id);
        return extractBalance(text);
    }

    private int extractBalance(String text) {
        val start = text.indexOf(balanceStart);
        val finish = text.indexOf(balanceFinish);
        val value = text.substring(start + balanceStart.length(), finish);
        return Integer.parseInt(value);
    }

    private SelenideElement getCardButtonByIndex(int index) {
        return cards.get(index).$("button");
    }

    public void clickFirstButton() {
        getCardButtonByIndex(0).click();
    }

    public void clickSecondButton() {
        getCardButtonByIndex(1).click();
    }

    public TransferMoneyPage transferToFirstCard() {
        clickFirstButton();
        return new TransferMoneyPage();
    }

    public TransferMoneyPage transferToSecondCard() {
        clickSecondButton();
        return new TransferMoneyPage();
    }

}
