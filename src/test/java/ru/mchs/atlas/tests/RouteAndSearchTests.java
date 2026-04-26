package ru.mchs.atlas.tests;

import io.qameta.allure.*;
import ru.mchs.atlas.pages.RoutesPage;
import ru.mchs.atlas.pages.SearchPage;
import ru.mchs.atlas.utils.ParameterProvider;
import org.testng.Assert;
import org.testng.annotations.Test;

@Epic("Карта МЧС")
@Feature("Маршрут и поиск")
public class RouteAndSearchTests extends BaseTest {

    @Test(description = "Проверка построения маршрута и вычисление расстояния маршрута")
    @Story("Маршрут")
    @Severity(SeverityLevel.CRITICAL)
    public void testBuildRoute() {
        driver.get(ParameterProvider.get("atlas.url"));
        RoutesPage routesPage = new RoutesPage(driver)
                .waitForLoading()
                .openRoutesTab()
                .buildRoute();

        String distance = routesPage.getRouteDistance();
        Assert.assertNotNull(distance, "Расстояние маршрута не отображается");
        Assert.assertFalse(distance.isEmpty(), "Расстояние маршрута пустое");
    }

    @Test(description = "Проверка результата поиска адреса и переход на его координаты")
    @Story("Поиск адреса")
    @Severity(SeverityLevel.CRITICAL)
    public void testSearchAddress() {
        driver.get(ParameterProvider.get("atlas.url"));
        SearchPage searchPage = new SearchPage(driver)
                .waitForLoading();

        String cords = searchPage
                .searchAddress(ParameterProvider.get("search.address"))
                .getCoordinates();

        Assert.assertNotNull(cords, "Координаты не отображаются после поиска");
        Assert.assertFalse(cords.isEmpty(), "Координаты пустые после поиска");
    }
}