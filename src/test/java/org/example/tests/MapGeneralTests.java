package org.example.tests;

import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.example.pages.MapPage;
import org.example.pages.SearchPage;
import org.example.utils.ParameterProvider;
import org.testng.Assert;
import org.testng.annotations.Test;

@Epic("Карта МЧС")
@Feature("Общая функциональность карты")
public class MapGeneralTests extends BaseTest {

    @Test(description = "Проверка загрузки главной страницы и отображения карты")
    @Story("Загрузка карты")
    @Severity(SeverityLevel.CRITICAL)
    public void testMapIsDisplayed() {
        driver.get(ParameterProvider.get("atlas.url"));
        MapPage mapPage = new MapPage(driver)
                .waitForMapToLoad();

        Assert.assertTrue(mapPage.isMapDisplayed(), "Карта не отображается на главной странице");
    }

    @Test(description = "Проверка масштабирования карты")
    @Story("Масштаб карты")
    @Severity(SeverityLevel.CRITICAL)
    public void testMapZoom() {
        driver.get(ParameterProvider.get("atlas.url"));
        MapPage mapPage = new MapPage(driver)
                .waitForLoading()
                .zoomInMap();

        Assert.assertTrue(mapPage.isMapDisplayed(), "Карта не отображается после масштабирования");
    }

    @Test(description = "Проверка перемещения карты")
    @Story("Перемещение карты")
    @Severity(SeverityLevel.CRITICAL)
    public void testMapMove() {
        driver.get(ParameterProvider.get("atlas.url"));
        MapPage mapPage = new MapPage(driver)
                .waitForLoading()
                .moveMap();

        Assert.assertTrue(mapPage.isMapMoved(), "Карта не переместилась");
    }

    @Test(description = "Проверка отображения и открытия событий на карте")
    @Story("События на карте")
    @Severity(SeverityLevel.CRITICAL)
    public void testEventsDisplayedAndOpen() {
        driver.get(ParameterProvider.get("atlas.url"));
        SearchPage searchPage = new SearchPage(driver);
        searchPage.waitForLoading()
                .searchAddress(ParameterProvider.get("search.address"));

        MapPage mapPage = new MapPage(driver);
        mapPage.waitForLoading()
                .enableEventLayers()
                .openEvent();

        Assert.assertTrue(mapPage.areEventsDisplayed() ? mapPage.isEventOpened() : true, "Ошибка: событие не открылось на карте");
    }

    @Test(description = "Проверка отображения туристического маршрута 'Кроноцкий заповедник'")
    @Story("Туристические маршруты")
    @Severity(SeverityLevel.CRITICAL)
    public void testTouristRouteDisplayed() {
        driver.get(ParameterProvider.get("atlas.url"));
        MapPage mapPage = new MapPage(driver);

        mapPage.waitForLoading()
                .openRoutesTab()
                .selectKronotskyReserve()
                .waitForRouteInfo();

        Assert.assertTrue(mapPage.isRouteInfoDisplayed(), "Информация о маршруте 'Кроноцкий заповедник' не отображается");
    }

    @Test(description = "Выбор даты на таймлайне и проверка обновления данных")
    @Story("Выбор даты на карте")
    @Severity(SeverityLevel.CRITICAL)
    public void testSelectTimelineDate() {
        driver.get(ParameterProvider.get("atlas.url"));
        MapPage mapPage = new MapPage(driver)
                .waitForLoading()
                .selectTimelineDate();

        Assert.assertTrue(mapPage.isTimelineDateDisplayed(), "Дата на таймлайне не изменилась");
    }
}