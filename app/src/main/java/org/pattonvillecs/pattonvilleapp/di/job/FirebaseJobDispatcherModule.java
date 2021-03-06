/*
 * Copyright (C) 2017 - 2018 Mitchell Skaggs, Keturah Gadson, Ethan Holtgrieve, Nathan Skelton, Pattonville School District
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.pattonvillecs.pattonvilleapp.di.job;

import android.app.Application;

import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;

import org.pattonvillecs.pattonvilleapp.di.AppModule;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * This module provides a singleton {@link FirebaseJobDispatcher} for the application.
 *
 * @author Mitchell Skaggs
 * @since 1.3.0
 */

@Module(includes = AppModule.class)
public class FirebaseJobDispatcherModule {
    @Provides
    @Singleton
    static FirebaseJobDispatcher provideFirebaseJobDispatcher(Application application) {
        return new FirebaseJobDispatcher(new GooglePlayDriver(application));
    }
}
