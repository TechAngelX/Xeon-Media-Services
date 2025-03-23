
//Video RestApplication.java
package com.xeon2035.video;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;

import java.util.Set;

@ApplicationScoped
@ApplicationPath("/")
public class RestApplication extends Application {
    @Override
    public Set<Class<?>> getClasses() {
        return Set.of(
                VideoResource.class
        );
    }
}
