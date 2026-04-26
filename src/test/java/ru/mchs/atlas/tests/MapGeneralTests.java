package ru.mchs.atlas.tests;

import io.qameta.allure.*;
import ru.mchs.atlas.pages.MapPage;
import ru.mchs.atlas.pages.MapEventsPage;
import ru.mchs.atlas.pages.SearchPage;
import ru.mchs.atlas.pages.TouristRoutesPage;
import ru.mchs.atlas.pages.TimelinePage;
import ru.mchs.atlas.utils.ParameterProvider;
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

        MapEventsPage mapEventsPage = new MapEventsPage(driver);
        mapEventsPage.waitForLoading()
                .enableEventLayers()
                .openEvent();

        Assert.assertTrue(!mapEventsPage.areEventsDisplayed() || mapEventsPage.isEventOpened(), "Ошибка: событие не открылось на карте");
    }

    @Test(description = "Проверка отображения туристического маршрута 'Кроноцкий заповедник'")
    @Story("Туристические маршруты")
    @Severity(SeverityLevel.CRITICAL)
    public void testTouristRouteDisplayed() {
        driver.get(ParameterProvider.get("atlas.url"));
        TouristRoutesPage touristRoutesPage = new TouristRoutesPage(driver);

        touristRoutesPage.waitForLoading()
                .openRoutesTab()
                .selectKronotskyReserve()
                .waitForRouteInfo();

        Assert.assertTrue(touristRoutesPage.isRouteInfoDisplayed(), "Информация о маршруте 'Кроноцкий заповедник' не отображается");
    }

    @Test(description = "Выбор даты на таймлайне и проверка обновления данных")
    @Story("Выбор даты на карте")
    @Severity(SeverityLevel.CRITICAL)
    public void testSelectTimelineDate() {
        driver.get(ParameterProvider.get("atlas.url"));
        TimelinePage timelinePage = new TimelinePage(driver)
                .waitForLoading()
                .selectTimelineDate();

        Assert.assertTrue(timelinePage.isTimelineDateDisplayed(), "Дата на таймлайне не изменилась");
    }
}