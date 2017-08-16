package com.github.pedramrn.slick.parent.ui.search;


import io.reactivex.Observable;

/**
 * A simple View interface
 */
public interface ViewSearch {

    Observable<String> queryNexText();

    Observable<Boolean> searchOpenClose();


}
