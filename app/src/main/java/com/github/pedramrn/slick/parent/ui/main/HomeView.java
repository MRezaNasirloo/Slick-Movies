package com.github.pedramrn.slick.parent.ui.main;

import com.github.pedramrn.slick.parent.datasource.network.models.BoxOfficeItem;

import java.util.List;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-02-28
 */

public interface HomeView {
    void showData(List<BoxOfficeItem> items);
}
