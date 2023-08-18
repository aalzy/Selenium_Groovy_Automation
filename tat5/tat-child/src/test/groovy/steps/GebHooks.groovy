package steps

import geb.Browser
import geb.binding.BindingUpdater
import io.cucumber.groovy.Hooks
import static io.cucumber.groovy.Hooks.*

// Add functions to register hooks to this script.
this.metaClass.mixin(Hooks)

def bindingUpdater

// This closure gets run before each scenario
// and has direct access to the new world object
// but can also make use of script variables.
Before() { scenario ->
    // Then normal Geb commands and objects are available in your Cucumber steps
    bindingUpdater = new BindingUpdater(binding, new Browser())
    bindingUpdater.initialize()
}

After() { scenario ->
    bindingUpdater.remove()
}
