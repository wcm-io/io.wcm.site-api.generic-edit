## About Site API Generic Edit

Generic Edit Mode for Headless AEM projects based on AEM Sites.

[![Maven Central](https://img.shields.io/maven-central/v/io.wcm/io.wcm.site-api.generic-edit)](https://repo1.maven.org/maven2/io/wcm/io.wcm.site-api.generic-edit/)


### Documentation

* [Usage][usage]
* [API documentation][apidocs]
* [Changelog][changelog]


### Overview

When building an AEM Headless projects based on AEM pages and components and JSON-Generation via Sling Models Exporter (and not using Content Fragments), there is no built-in edit view for your components. If you are not using the SPA Editor, you have to provide your own HTL representation of each component to allow the editors to build the content. wcm.io Site API Generic Edit helps here to automatically generate a generic "edit view" of a component, based on the Sling Models properties attached with the component. So you only have to build the Sling Model, and the AEM component edit dialog, the HTL view is generated automatically.

Example view:

<p><img src="images/generic-edit-mode.jpg" style="width:100%;max-width:500px;border:1px solid #ccc;"/></p>

This generic edit view works well together with the other modules from [wcm.io Site API][site-api], but can also be used standalone.

If you are using [wcm.io Handler][handler] in your project, consider adding the [Site API Generic Edit Handler Extensions][generic-edit-handler].

### AEM Version Support Matrix

|Site API Generic Edit version | AEM Sites Core Component version |AEM version supported
|------------------------------|----------------------------------|-----------------------
|1.1.2 or higher               |2.25.4 and up                     |AEM 6.5.24+, AEM 6.6.2+, AEMaaCS
|1.1.0                         |2.25.4 and up                     |AEM 6.5.17+, AEM 6.6.0+, AEMaaCS


### Dependencies

To use this module you have to deploy also:

|---|---|---|
| [wcm.io Sling Commons](https://repo1.maven.org/maven2/io/wcm/io.wcm.sling.commons/) | [![Maven Central](https://img.shields.io/maven-central/v/io.wcm/io.wcm.sling.commons)](https://repo1.maven.org/maven2/io/wcm/io.wcm.sling.commons/) |


### GitHub Repository

Sources: https://github.com/wcm-io/io.wcm.site-api.generic-edit


[usage]: usage.html
[apidocs]: apidocs/
[changelog]: changes.html
[site-api]: https://wcm.io/site-api/
[handler]: https://wcm.io/handler/
[generic-edit-handler]: https://wcm.io/site-api/generic-edit/handler/
