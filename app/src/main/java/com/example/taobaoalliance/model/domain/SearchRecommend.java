package com.example.taobaoalliance.model.domain;

import java.util.List;

public class SearchRecommend {

    /**
     * success : true
     * code : 10000
     * message : 获取成功
     * data : [{"id":"1250358535347318784","keyword":"键盘","createTime":"2020-04-15 17:41"},{"id":"1250358658110402560","keyword":"面膜","createTime":"2020-04-15 17:41"},{"id":"1250358728230776832","keyword":"程序员早餐","createTime":"2020-04-15 17:42"},{"id":"1250358786997170176","keyword":"医用口罩","createTime":"2020-04-15 17:42"},{"id":"1250358963493482496","keyword":"半身裙","createTime":"2020-04-15 17:43"},{"id":"1250359035425796096","keyword":"新款男鞋","createTime":"2020-04-15 17:43"},{"id":"1250359094812946432","keyword":"四件套","createTime":"2020-04-15 17:43"},{"id":"1250359109992132608","keyword":"树莓派","createTime":"2020-04-15 17:43"},{"id":"1250359160793542656","keyword":"机械键盘","createTime":"2020-04-15 17:43"},{"id":"1250359205274136576","keyword":"安卓手机","createTime":"2020-04-15 17:43"}]
     */

    private boolean success;
    private int code;
    private String message;
    private List<DataBean> data;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * id : 1250358535347318784
         * keyword : 键盘
         * createTime : 2020-04-15 17:41
         */

        private String id;
        private String keyword;
        private String createTime;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getKeyword() {
            return keyword;
        }

        public void setKeyword(String keyword) {
            this.keyword = keyword;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }
    }
}
