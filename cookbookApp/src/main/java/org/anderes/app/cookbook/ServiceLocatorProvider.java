package org.anderes.app.cookbook;

import android.support.annotation.NonNull;

public abstract class ServiceLocatorProvider {

    private static ServiceLocator serviceLocator;

    @NonNull
    public static ServiceLocator createServiceLocator(@NonNull final ServiceLocator services) {
        serviceLocator = services;
        return serviceLocator;
    }

    @NonNull
    public static ServiceLocator getInstance() {
        if(serviceLocator == null) {
            throw new IllegalStateException("Der ServiceLocator wurde nicht initialisiert!");
        }
        return serviceLocator;
    }
}
