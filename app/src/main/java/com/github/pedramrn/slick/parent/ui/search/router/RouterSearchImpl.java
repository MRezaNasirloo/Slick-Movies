package com.github.pedramrn.slick.parent.ui.search.router;

import com.github.pedramrn.slick.parent.datasource.network.ApiTmdb;
import com.github.pedramrn.slick.parent.datasource.network.models.tmdb.MoviePageTmdb;
import com.github.pedramrn.slick.parent.datasource.network.models.tmdb.PersonPageTmdb;
import com.github.pedramrn.slick.parent.datasource.network.models.tmdb.PersonSearchTmdb;
import com.github.pedramrn.slick.parent.domain.mapper.MapperMovieSmall;
import com.github.pedramrn.slick.parent.domain.model.MovieSmallDomain;
import com.github.pedramrn.slick.parent.domain.model.PagedDomain;
import com.github.pedramrn.slick.parent.domain.model.PersonSearchDomain;
import com.github.pedramrn.slick.parent.domain.router.RouterSearch;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-08-11
 */

public class RouterSearchImpl implements RouterSearch {

    private final ApiTmdb apiTmdb;
    private final MapperMovieSmall movieSmall;

    @Inject
    public RouterSearchImpl(ApiTmdb apiTmdb, MapperMovieSmall movieSmall) {
        this.apiTmdb = apiTmdb;
        this.movieSmall = movieSmall;
    }

    @Override
    public Observable<PagedDomain<MovieSmallDomain>> movies(String query, int page) {
        return apiTmdb.searchMovie(query, page).map(new Function<MoviePageTmdb, PagedDomain<MovieSmallDomain>>() {
            @Override
            public PagedDomain<MovieSmallDomain> apply(@NonNull MoviePageTmdb moviePageTmdb) throws Exception {
                return PagedDomain.create(Observable.fromIterable(moviePageTmdb.movies()).map(movieSmall).toList().blockingGet(),
                        moviePageTmdb.page(), moviePageTmdb.totalPages(), moviePageTmdb.totalResults());
            }
        });
    }

    @Override
    public Observable<PagedDomain<PersonSearchDomain>> persons(String query, int page) {
        return apiTmdb.searchPerson(query, page).map(new Function<PersonPageTmdb, PagedDomain<PersonSearchDomain>>() {
            @Override
            public PagedDomain<PersonSearchDomain> apply(@NonNull final PersonPageTmdb personPageTmdb) throws Exception {
                List<PersonSearchDomain> personDomains =
                        Observable.fromIterable(personPageTmdb.persons()).map(new Function<PersonSearchTmdb, PersonSearchDomain>() {
                    @Override
                    public PersonSearchDomain apply(@NonNull PersonSearchTmdb personTmdb) throws Exception {
                        return PersonSearchDomain.builder()
                                .id(personTmdb.id())
                                .name(personTmdb.name())
                                .profilePath(personTmdb.profilePath())
                                .popularity(personTmdb.popularity())
                                .adult(personTmdb.adult())
                                .build()
                                ;
                    }
                }).toList().blockingGet();
                return PagedDomain.create(personDomains, personPageTmdb.page(), personPageTmdb.totalPages(), personPageTmdb.totalResults());
            }
        });
    }
}
