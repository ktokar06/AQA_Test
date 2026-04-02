package org.example.tests;

import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.example.pages.MapPage;
import org.example.utils.ParameterProvider;
import org.testng.Assert;
import org.testng.annotations.Test;

@Epic("Карта МЧС")
@Feature("Подложки и темы")
public class MapLayerTests extends BaseTest {

    @Test(description = "Проверка переключения подложки после включения светлой темы")
    @Story("Переключение темы карты")
    @Severity(SeverityLevel.CRITICAL)
    public void testThemeSwitch() {
        driver.get(ParameterProvider.get("atlas.url"));
        MapPage mapPage = new MapPage(driver)
                .waitForLoading()
                .switchTheme();

        Assert.assertTrue(mapPage.isLightThemeEnabled(), "Тема не переключилась на светлую");
    }

    @Test(description = "Проверка на доступность более двух картографических подложек")
    @Story("Слои карты")
    @Severity(SeverityLevel.NORMAL)
    public void testMapLayersAvailability() {
        driver.get(ParameterProvider.get("atlas.url"));
        MapPage mapPage = new MapPage(driver)
                .waitForLoading()
                .openMapLayersList();

        Assert.assertTrue(mapPage.areMapLayersAvailable(), "Доступно менее двух подложек.");
    }
}