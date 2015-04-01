package com.github.abel533.controller;

/**
 * Created by liuzh on 2015/3/16.
 */
public interface Controller {
    /**
     * 视图初始化
     */
    Controller initView();

    /**
     * 事件初始化
     *
     * @return
     */
    Controller initAction();
}
