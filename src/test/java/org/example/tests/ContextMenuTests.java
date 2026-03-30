package org.example.tests;

import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.example.pages.ContextMenuPage;
import org.example.pages.SearchPage;
import org.example.utils.FileUtils;
import org.example.utils.ParameterProvider;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.File;
import java.nio.charset.StandardCharsets;

@Epic("Карта МЧС")
@Feature("Контекстное меню")
public class ContextMenuTests extends BaseTest {

    @Test(description = "Проверка контекстного меню - импорт области")
    @Story("Импорт области")
    @Severity(SeverityLevel.NORMAL)
    public void testContextMenuImportArea() {
        driver.get(ParameterProvider.get("atlas.url"));
        ContextMenuPage contextMenuPage = new ContextMenuPage(driver)
                .waitForLoading()
                .openContextMenuOnMap();

        Assert.assertTrue(contextMenuPage.isImportAreaOptionDisplayed(), "Опция 'Импорт области' не отображается в контекстном меню");

        contextMenuPage.selectImportAreaAndUploadFile("map.geojson")
                .closeChooseLayersModal();

        String areaValue = contextMenuPage.getAreaValue();
        Assert.assertNotNull(areaValue, "Площадь не отображается");
        Assert.assertFalse(areaValue.isEmpty(), "Значение площади пустое");
    }

    @Test(description = "Проверка контекстного меню - копирование координат и сохранение в файл")
    @Story("Копирование координат")
    @Severity(SeverityLevel.CRITICAL)
    public void testContextMenuCopyCoordinates() {
        driver.get(ParameterProvider.get("atlas.url"));
        String coordinates = new ContextMenuPage(driver)
                .waitForLoading()
                .openContextMenuOnMap()
                .copyCoordinates();

        Assert.assertNotNull(coordinates, "Координаты не скопированы");
        Assert.assertFalse(coordinates.isEmpty(), "Скопированные координаты пустые");

        File file = FileUtils.createCoordinatesFile(coordinates);
        byte[] fileBytes = FileUtils.readFileBytes(file);
        String fileContent = new String(fileBytes, StandardCharsets.UTF_8);

        Assert.assertTrue(fileContent.contains(coordinates), "Координаты не сохранены в файл: " + coordinates);
    }

    @Test(description = "Проверка копирования координат, вставки в поиск и перехода в точку")
    @Story("Поиск по скопированным координатам")
    @Severity(SeverityLevel.CRITICAL)
    public void testSearchByCopiedCoordinates() {
        driver.get(ParameterProvider.get("atlas.url"));
        String copiedCoordinates = new ContextMenuPage(driver)
                .waitForLoading()
                .openContextMenuOnMap()
                .copyCoordinates();

        Assert.assertNotNull(copiedCoordinates, "Координаты не скопированы");

        String searchCoordinates = new SearchPage(driver)
                .waitForLoading()
                .searchAddress(copiedCoordinates)
                .getCoordinates();

        Assert.assertNotNull(searchCoordinates, "Не удалось вставить скопированные координаты в поиск");
    }
}