package ru.frosteye.beermap.app.di.qualifier;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Qualifier;

/**
 * Created by oleg on 26.07.17.
 */

@Qualifier
@Retention(RetentionPolicy.RUNTIME)
public @interface ApiUrl {
}
