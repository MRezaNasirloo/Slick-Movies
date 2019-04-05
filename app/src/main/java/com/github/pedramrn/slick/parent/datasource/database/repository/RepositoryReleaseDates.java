package com.github.pedramrn.slick.parent.datasource.database.repository;


import android.support.annotation.NonNull;

import com.github.pedramrn.slick.parent.ui.details.model.ReleaseDateModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;

@Singleton
public class RepositoryReleaseDates {

    private static final String TAG = RepositoryReleaseDates.class.getSimpleName();
    private final DatabaseReference reference;

    @Inject
    public RepositoryReleaseDates(@NonNull DatabaseReference reference) {
        this.reference = reference;
    }

    public Completable addMovieReleaseDate(@NonNull String uid, @NonNull ReleaseDateModel model) {
        return Completable.create(emitter -> {
            try {
                reference.child("user_data")
                        .child(uid)
                        .child("release_notification")
                        .child("movies")
                        .child(model.tmdb.toString())
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                ReleaseDateModel value = dataSnapshot.getValue(ReleaseDateModel.class);
                                if (value != null && value.release_dates != null) {
                                    model.release_dates.putAll(value.release_dates);
                                    Map<String, Object> releaseDates = new HashMap<>(model.release_dates);
                                    reference.child("user_data")
                                            .child(uid)
                                            .child("release_notification")
                                            .child("movies")
                                            .child(model.tmdb.toString())
                                            .child("release_dates")
                                            .updateChildren(releaseDates, (e, ref) -> emitter.onComplete());
                                } else {
                                    reference.child("user_data")
                                            .child(uid)
                                            .child("release_notification")
                                            .child("movies")
                                            .child(model.tmdb.toString())
                                            .setValue(model, (e, ref) -> emitter.onComplete());
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                emitter.onError(databaseError.toException());
                            }
                        });
            } catch (Exception e) {
                if (!emitter.isDisposed()) {
                    emitter.onError(e);
                }
            }
        });
    }

    public Completable removeMovieReleaseDate(@NonNull String uid, @NonNull ReleaseDateModel model) {
        return Completable.create(emitter -> {
            try {
                reference.child("user_data")
                        .child(uid)
                        .child("release_notification")
                        .child("movies")
                        .child(model.tmdb.toString())
                        .child("release_dates")
                        .child(model.release_dates.keySet().iterator().next())
                        .removeValue((databaseError, databaseReference) -> emitter.onComplete());
            } catch (Exception e) {
                if (!emitter.isDisposed()) {
                    emitter.onError(e);
                }
            }
        });
    }

    public Observable<ReleaseDateModel> updateStream(@NonNull String uid, @NonNull Integer tmbdId) {
        return Observable.create(emitter -> {
            try {
                DatabaseReference ref = reference.child("user_data")
                        .child(uid)
                        .child("release_notification")
                        .child("movies")
                        .child(tmbdId.toString())
                        .getRef();
                ValueEventListener valueEventListener = new ReleaseDateListValueEventListener(emitter);
                ref.addValueEventListener(valueEventListener);
            } catch (Exception e) {
                if (!emitter.isDisposed()) {
                    emitter.onError(e);
                }
            }
        });
    }

    private static class ReleaseDateListValueEventListener implements ValueEventListener {

        private final ObservableEmitter<ReleaseDateModel> emitter;

        public ReleaseDateListValueEventListener(ObservableEmitter<ReleaseDateModel> emitter) {
            this.emitter = emitter;
        }

        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            if (!emitter.isDisposed()) {
                ReleaseDateModel value = dataSnapshot.getValue(ReleaseDateModel.class);
                if (value != null) {
                    if (value.release_dates == null) {
                        value.release_dates = Collections.emptyMap();
                    }
                    emitter.onNext(value);
                }
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            if (!emitter.isDisposed()) {
                emitter.onError(databaseError.toException());
            }
        }
    }
}
