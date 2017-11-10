package com.github.pedramrn.slick.parent.datasource.database.repository;


import android.support.annotation.NonNull;
import android.util.Log;

import com.github.pedramrn.slick.parent.datasource.database.model.ItemFavoriteModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Logger;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-11-09
 */

@Singleton
public class RepositoryFavoriteImpl {

    private static DatabaseReference reference;

    @Inject
    public RepositoryFavoriteImpl() {

    }

    protected synchronized DatabaseReference init() {
        synchronized (this) {
            if (reference == null) {
                FirebaseDatabase instance = FirebaseDatabase.getInstance();
                instance.setPersistenceEnabled(true);
                instance.setLogLevel(Logger.Level.INFO);
                return instance.getReference();
            }
            return reference;
        }
    }

    public Observable<Object> add(@NonNull String uid, @NonNull ItemFavoriteModel model) {
        return Observable.create(emitter -> {
            try {
                if (reference == null) reference = init();
                reference.child("user_data")
                        .child(uid)
                        .child("favorite_list")
                        .child("movies")
                        .child(model.tmdb.toString())
                        .setValue(model, (databaseError, databaseReference) -> {
                            Log.e(TAG, "added ");
                            emitter.onNext(1);
                            emitter.onComplete();
                        });
            } catch (Exception e) {
                if (!emitter.isDisposed()) {
                    emitter.onError(e);
                }
            }
        });
    }

    private static final String TAG = RepositoryFavoriteImpl.class.getSimpleName();

    public Observable<Object> remove(@NonNull String uid, @NonNull ItemFavoriteModel model) {
        return Observable.create(emitter -> {
            try {
                if (reference == null) reference = init();
                reference.child("user_data")
                        .child(uid)
                        .child("favorite_list")
                        .child("movies")
                        .child(model.tmdb.toString())
                        .removeValue((databaseError, databaseReference) -> {
                            emitter.onNext(1);
                            emitter.onComplete();
                        });
            } catch (Exception e) {
                if (!emitter.isDisposed()) {
                    emitter.onError(e);
                }
            }
        });
    }

    public Observable<Boolean> updateStream(@NonNull String uid, @NonNull Integer tmbdId) {
        return Observable.create(emitter -> {
            try {
                if (reference == null) reference = init();
                DatabaseReference ref = reference.child("user_data")
                        .child(uid)
                        .child("favorite_list")
                        .child("movies")
                        .child(tmbdId.toString()).getRef();
                ExistenceValueListener valueEventListener = new ExistenceValueListener(emitter);
                ref.addValueEventListener(valueEventListener);
            } catch (Exception e) {
                if (!emitter.isDisposed()) {
                    emitter.onError(e);
                }
            }
        });
    }

    public Observable<List<ItemFavoriteModel>> updateStream(@NonNull String uid) {
        return Observable.create(emitter -> {
            try {
                if (reference == null) reference = init();
                DatabaseReference ref = reference.child("user_data")
                        .child(uid)
                        .child("favorite_list")
                        .child("movies")
                        .orderByChild("dateAdded")
                        .getRef();
                ValueEventListener valueEventListener = new MovieFavoriteListValueEventListener(emitter);
                ref.addValueEventListener(valueEventListener);
            } catch (Exception e) {
                if (!emitter.isDisposed()) {
                    emitter.onError(e);
                }
            }
        });
    }

    private static class ExistenceValueListener implements ValueEventListener {
        private final ObservableEmitter<Boolean> emitter;

        public ExistenceValueListener(ObservableEmitter<Boolean> emitter) {
            this.emitter = emitter;
        }

        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            if (!emitter.isDisposed()) {
                emitter.onNext(dataSnapshot.exists());
            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            if (!emitter.isDisposed()) {
                emitter.onError(databaseError.toException());
            }
        }
    }

    private static class MovieFavoriteListValueEventListener implements ValueEventListener {

        private final ObservableEmitter<List<ItemFavoriteModel>> emitter;

        public MovieFavoriteListValueEventListener(ObservableEmitter<List<ItemFavoriteModel>> emitter) {
            this.emitter = emitter;
        }

        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            if (!emitter.isDisposed()) {
                ArrayList<ItemFavoriteModel> list = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    list.add(snapshot.getValue(ItemFavoriteModel.class));
                }
                emitter.onNext(list);
            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            if (!emitter.isDisposed()) {
                emitter.onError(databaseError.toException());
            }
        }
    }
}
