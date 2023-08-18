package steps

import static io.cucumber.groovy.RU.*

Когда(~/^открываем в браузере страницу сайта по её адресу$/) { ->
    go "https://javajunior.ru/tat/"
}

Тогда(~/^проверяем, что открытая страница имеет название "UI Test Automation Sandbox - Главная"$/) { ->
    assert page.title == "UI Test Automation Sandbox - Главная"
}