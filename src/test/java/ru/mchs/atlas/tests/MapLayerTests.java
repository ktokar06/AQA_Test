package ru.mchs.atlas.tests;

import io.qameta.allure.*;
import ru.mchs.atlas.pages.MapLayersPage;
import ru.mchs.atlas.utils.ParameterProvider;
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
        MapLayersPage mapLayersPage = new MapLayersPage(driver)
                .waitForLoading()
                .switchTheme();

        Assert.assertTrue(mapLayersPage.isLightThemeEnabled(), "Тема не переключилась на светлую");
    }

    @Test(description = "Проверка на доступность более двух картографических подложек")
    @Story("Слои карты")
    @Severity(SeverityLevel.NORMAL)
    public void testMapLayersAvailability() {
        driver.get(ParameterProvider.get("atlas.url"));
        MapLayersPage mapLayersPage = new MapLayersPage(driver)
                .waitForLoading()
                .openMapLayersList();

        Assert.assertTrue(mapLayersPage.areMapLayersAvailable(), "Доступно менее двух подложек.");
    }
}