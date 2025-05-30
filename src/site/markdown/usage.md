## Site API Generic Edit usage

### Creating new AEM project

Using the [wcm.io Maven Archetype for AEM][wcmio-archetype-aem] you can create a new AEM project using the Generic Edit Mode (the Archetype only supports this together with wcm.io Handler and wcm.io Site API, although the Generic Edit feature itself can be used without those). To activate it, use these options (see [usage][wcmio-archetype-aem-usage] for a full list of available options):
```
optionWcmioHandler=y
optionWcmioSiteApi=y
optionWcmioSiteApiGenericEdit=y
```

### Components with Generic Edit

Basically, you need to prepare the following parts to make an AEM component editable via Generic Edit:

* Define an AEM Edit Dialog defined (as usual)
* Define a Sling Model mapped to the resource type of the component. This Sling Models needs to expose all properties that should be visible in the Generic Edit View (you need that for Sling Models Exporter to JSON anyways).
* Add a minimal HTL script which redirects the HTML view to Generic Edit (see below)

You should not add a Generic Edit HTL script to the following components:

* Core Component Container
* Core Component Navigation components (Navigation, Language Navigation, Breadcrumb)

For further components that come with a HTML view that is usable without further CSS/JS you may want to not add the Generic Edit HTL script as well, e.g.:

* Core Component Title
* Core Component Rich Text
* Core Component Image
* Core Component Separator


### Enabling Generic Edit as primary edit view

If the Generic Edit view is the only view you want to provide to the editors, the activation requires the steps described in this section.

#### Custom Header/Footer Libs

Include the Generic Edit CSS in your project's `customheaderlibs.html`:

```html
<sly
    data-sly-use.clientLib="/libs/granite/sightly/templates/clientlib.html"
    data-sly-call="${clientlib.css @ categories=['io.wcm.site-api.generic-edit']}">
/>
```

And in your project's `customfooterlibs.html`:

```html
<sly
    data-sly-use.clientLib="/libs/granite/sightly/templates/clientlib.html"
    data-sly-call="${clientlib.js @ categories=['io.wcm.site-api.generic-edit']}">
/>
```

#### HTL Scripts for Components

For each component (custom or core component), add a default HTL `<component-name>.html` script with the name of the component with the following content:
```html
<sly data-sly-include="/apps/wcm-io/site-api/generic-edit/components/component/component.html"/>
```

### Enabling Generic Edit as secondary edit view

If you want to use Generic Edit view as secondary view, and use the default HTML view of the components or an SPA Editor-based view as primary view, follow the steps described in this section.

In this case, the Generic Edit view is tied to a selector `.generic-edit` in edit mode (the name of the  selector is configurable in the OSGi configuration).


#### Custom Header/Footer Libs

Include the Generic Edit CSS in your project's `customheaderlibs.html`:

```html
<sly data-sly-use.detector="io.wcm.siteapi.genericedit.model.GenericEditDetector" data-sly-test.genericEdit="${detector.genericEdit}"
    data-sly-use.clientlib="/libs/granite/sightly/templates/clientlib.html"
    data-sly-call="${clientlib.css @ categories=['io.wcm.site-api.generic-edit']}"
/>
<sly data-sly-test="${!genericEdit}">
  <!-- insert your default view header libs -->
</sly>
```

And in your project's `customfooterlibs.html`:

```html
<sly data-sly-use.detector="io.wcm.siteapi.genericedit.model.GenericEditDetector" data-sly-test.genericEdit="${detector.genericEdit}"
    data-sly-use.clientlib="/libs/granite/sightly/templates/clientlib.html"
    data-sly-call="${clientlib.js @ categories=['io.wcm.site-api.generic-edit']}"
/>
<sly data-sly-test="${!genericEdit}">
  <!-- insert your default view footer libs -->
</sly>
```

If you include your default view clientlibs via Page Properties, you have to customize `footer.html` and `headlibs.html` as well, see [this example](https://github.com/wcm-io/aem-guides-wknd-wcmio/tree/main/ui.apps/src/main/content/jcr_root/apps/wknd/components/page).

For the `xfpage` page component, provide a special script at `content/generic-edit.html`, see [this example](https://github.com/wcm-io/aem-guides-wknd-wcmio/blob/main/ui.apps/src/main/content/jcr_root/apps/wknd/components/xfpage/content/generic-edit.html).

#### HTL Scripts for Components

For each component (custom or core component), add a new HTL script named `generic-edit.html` script with the following content:
```html
<sly data-sly-include="/apps/wcm-io/site-api/generic-edit/components/component/component.html"/>
```

#### Add item to Page Information menu

To give the editors easy access to the secondary Generic Edit view, you can add a custom item to the Page Information menu.

Add a content package with an overlay page at `/apps/wcm/core/content/editor` with the content:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<jcr:root xmlns:sling="http://sling.apache.org/jcr/sling/1.0" xmlns:granite="http://www.adobe.com/jcr/granite/1.0" xmlns:cq="http://www.day.com/jcr/cq/1.0" xmlns:jcr="http://www.jcp.org/jcr/1.0" xmlns:nt="http://www.jcp.org/jcr/nt/1.0" jcr:primaryType="cq:Page">
  <jcr:content jcr:primaryType="nt:unstructured">
    <content jcr:primaryType="nt:unstructured">
      <items jcr:primaryType="sling:OrderedFolder">
        <content jcr:primaryType="sling:OrderedFolder">
          <header jcr:primaryType="nt:unstructured">
            <items jcr:primaryType="nt:unstructured">
              <headerbar jcr:primaryType="nt:unstructured">
                <items jcr:primaryType="nt:unstructured">
                  <pageinfopopover jcr:primaryType="nt:unstructured">
                    <items jcr:primaryType="nt:unstructured">
                      <list jcr:primaryType="nt:unstructured">
                        <items jcr:primaryType="nt:unstructured">
                          <wcmioGenericEditView
                            jcr:primaryType="nt:unstructured"
                            sling:resourceType="granite/ui/components/coral/foundation/button"
                            sling:orderBefore="viewinadmin"
                            granite:class="pageinfo-wcmio-genericEdit"
                            text="View in Generic Edit Mode">
                            <granite:rendercondition
                              jcr:primaryType="nt:unstructured"
                              sling:resourceType="granite/ui/components/coral/foundation/renderconditions/simple"
                              expression="${!granite:contains(requestPathInfo.suffix, '.generic-edit')}"/>
                          </wcmioGenericEditView>
                        </items>
                      </list>
                    </items>
                  </pageinfopopover>
                </items>
              </headerbar>
            </items>
          </header>
        </content>        
      </items>
    </content>
  </jcr:content>
</jcr:root>
```

Add a Client Library registered to the category `cq.authoring.editor.sites.page.hook` with a piece of JS code like this:
```js
(function ($, ns, channel, window, undefined) {
  "use strict";
  var contentFrame = Granite.author.ContentFrame;
  channel.on('click', '.pageinfo-wcmio-genericEdit', function() {
    var path = contentFrame.getContentPath();
    var url = '/editor.html' + path + '.generic-edit.html';
    window.open(url);
  });
}(jQuery, Granite.author, jQuery(document), this));
```


### Customize the Sling Model Property Data Type Detection

By default, the Generic Edit mode should detect all types of data types from the Sling Models attached to the components correctly and display them in a sensible manner without further work. It distinguishes between different types of properties:

* Simple properties
* Link properties
* Media/Image properties
* Rich Text properties
* Nested Component properties

It comes with default implementations to detect those properties, sometimes it apply heuristics for the detection.

You can add your own implementations of the `io.wcm.siteapi.genericedit.builder.ValueInspectorService` to add custom detection rules for special cases.

If you are using [wcm.io Handler][handler] in your project, you should add the [Site API Generic Edit Handler Extensions][generic-edit-handler], it comes with default implementations of the ValueInspectorService for wcm.io Handler data types.




[wcmio-archetype-aem]: https://wcm.io/tooling/maven/archetypes/aem/
[wcmio-archetype-aem-usage]: https://wcm.io/tooling/maven/archetypes/aem/usage.html
[handler]: https://wcm.io/handler/
[generic-edit-handler]: https://wcm.io/site-api/generic-edit/handler/
