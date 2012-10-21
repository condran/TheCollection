package com.tekinsure.thecollection;

import org.apache.wicket.core.util.resource.locator.ResourceStreamLocator;
import org.apache.wicket.util.resource.IResourceStream;

/**
 * This class locates HTML and other resources that are in the webapp folder.
 */
public class WebResourceLocator extends ResourceStreamLocator {

    public WebResourceLocator() {
    }

    @Override
    public IResourceStream locate(Class<?> clazz, String path) {
        IResourceStream located = super.locate(clazz, trimFolders(path));
        if (located != null) {
            return located;
        }
        return super.locate(clazz, path);
    }

    private String trimFolders(String path) {
        path = path.substring(path.lastIndexOf("/") + 1);
        if (path.endsWith(".html")) {
            path = "/" + path;
        } else if (path.endsWith(".js")) {
            path = "/js/" + path;
        } else if (path.endsWith(".css")) {
            path = "/css/" + path;
        } else if (path.endsWith(".jpg") || path.endsWith(".jpeg") || path.endsWith(".gif") || path.endsWith(".png")) {
            path = "/img/" + path;
        }
        return path;
    }

}
