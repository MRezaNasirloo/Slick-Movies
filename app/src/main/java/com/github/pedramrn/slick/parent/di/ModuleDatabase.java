package com.github.pedramrn.slick.parent.di;

import com.github.pedramrn.slick.parent.ui.main.di.ComponentMain;

import javax.inject.Singleton;

import dagger.Module;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-04-22
 */
@Singleton
@Module(subcomponents = ComponentMain.class)
public class ModuleDatabase {

    /*@Provides
    public ReactiveEntityStore<Persistable> entityStore(Context context) {
        final DatabaseSource source = new DatabaseSource(context, Models.DEFAULT, 1);
        if (BuildConfig.DEBUG) {
            // use this in development mode to drop and recreate the tables on every upgrade
            source.setTableCreationMode(TableCreationMode.DROP_CREATE);
            source.setLoggingEnabled(true);
        }
        final Configuration configuration = source.getConfiguration();
        final ReactiveEntityStore<Persistable> entityStore =
                ReactiveSupport.toReactiveStore(new EntityDataStore<Persistable>(configuration));

        if (entityStore.select(UserEntity.class).get().firstOrNull() == null) {
            final UserEntity userEntity = new UserEntity();
            userEntity.setBio("You're awesome");
            userEntity.setAwesome(true);
            userEntity.setName("joe");
            entityStore.insert(userEntity).subscribe();
        }
        return entityStore;
    }*/

    /*@Provides
    public FirebaseDatabase firebaseDatabase() {
        return FirebaseDatabase.getInstance()
    }*/
}
