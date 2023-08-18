import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.chrome.ChromeDriverService
import org.openqa.selenium.chrome.ChromeOptions
import org.openqa.selenium.os.ExecutableFinder

import static org.apache.commons.lang3.SystemUtils.IS_OS_LINUX
import static org.apache.commons.lang3.SystemUtils.IS_OS_MAC
import static org.apache.commons.lang3.SystemUtils.IS_OS_WINDOWS

File findDriverExecutable() {
    def defaultExecutable = new ExecutableFinder().find("chromedriver")
    if (defaultExecutable) {
        new File(defaultExecutable)
    } else {
        new File("../drivers").listFiles().findAll {
            !it.name.endsWith(".version")
        }.find {
            if (IS_OS_LINUX) {
                it.name.contains("lin")
            } else if (IS_OS_MAC) {
                it.name.contains("mac")
            } else if (IS_OS_WINDOWS) {
                it.name.contains("win")
            }
        }
    }
}

driver = {
    ChromeDriverService.Builder serviceBuilder = new ChromeDriverService.Builder()
        .usingAnyFreePort()
        .usingDriverExecutable(findDriverExecutable())
    ChromeOptions options = new ChromeOptions();
    options.addArguments("--remote-allow-origins=*");
    new ChromeDriver(serviceBuilder.build(), options)
}