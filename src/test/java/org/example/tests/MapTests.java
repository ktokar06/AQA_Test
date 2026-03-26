package org.example.tests;

import io.qameta.allure.*;
import org.example.pages.MapPage;
import org.example.pages.SearchPage;
import org.example.utils.ParameterProvider;
import org.testng.Assert;
import org.testng.annotations.Test;

@Epic("Карта МЧС")
@Feature("Функции карты")
public class MapTests extends BaseTest {

    @Test(description = "Проверка переключения подложки после включения светлой темы")
    @Story("Переключение темы карты")
    @Severity(SeverityLevel.CRITICAL)
    public void testThemeSwitch() {
        driver.get(ParameterProvider.get("base.url"));
        MapPage mapPage = new MapPage(driver);

        mapPage.switchTheme();

        Assert.assertTrue(mapPage.isLightThemeEnabled(), "Тема не переключилась на светлую");
    }

    @Test(description = "Проверка построения маршрута и вычисление расстояния")
    @Story("Маршрут")
    @Severity(SeverityLevel.CRITICAL)
    public void testBuildRoute() {
        driver.get(ParameterProvider.get("base.url"));
        MapPage mapPage = new MapPage(driver);

        mapPage.buildRoute();

        String distance = mapPage.getRouteDistance();
        Assert.assertNotNull(distance, "Расстояние маршрута не отображается");
        Assert.assertFalse(distance.isEmpty(), "Расстояние маршрута пустое");
    }

    @Test(description = "Проверка результата поиска определённого адреса и перехода на его координаты")
    @Story("Поиск адреса")
    @Severity(SeverityLevel.CRITICAL)
    public void testSearchAddress() {
        driver.get(ParameterProvider.get("base.url"));
        SearchPage searchPage = new SearchPage(driver);

        String cords = searchPage
                .searchAddress(ParameterProvider.get("search.address"))
                .getCoordinates();

        Assert.assertNotNull(cords, "Координаты не отображаются после поиска");
        Assert.assertFalse(cords.isEmpty(), "Координаты пустые после поиска");
    }

    @Test(description = "Проверка на доступность более двух картографических подложек")
    @Story("Слои карты")
    @Severity(SeverityLevel.NORMAL)
    public void testMapLayersAvailability() {
        driver.get(ParameterProvider.get("base.url"));
        MapPage mapPage = new MapPage(driver);

        Assert.assertTrue(mapPage.areMapLayersAvailable(), "Менее двух подложек доступны");
    }
}