package com.vip.file.utils;


public class Result {

    /**
     * 接口请求返回码
     */
    private int respCode;

    /**
     * 接口请求返回描述
     */
    private String respDesc;

    /**
     * 接口请求返回实体
     */
    private Data data;

    public int getRespCode() {
        return respCode;
    }

    public void setRespCode(int respCode) {
        this.respCode = respCode;
    }

    public String getRespDesc() {
        return respDesc;
    }

    public void setRespDesc(String respDesc) {
        this.respDesc = respDesc;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public Result() {
    }

    public Result(int respCode, String respDesc) {
        this.respCode = respCode;
        this.respDesc = respDesc;
    }

    public Result(int respCode, String respDesc, Data data) {
        this.respCode = respCode;
        this.respDesc = respDesc;
        this.data = data;
    }

    public class Data<T> {

        /**
         * 业务返回码
         */
        private int resultCode;

        /**
         * 业务返回描述
         */
        private String resultMsg;

        /**
         * 业务返回具体内容
         */
        private T detail;

        /**
         * 分页对象
         */
        private Page page;

        public int getResultCode() {
            return resultCode;
        }

        public void setResultCode(int resultCode) {
            this.resultCode = resultCode;
        }

        public String getResultMsg() {
            return resultMsg;
        }

        public void setResultMsg(String resultMsg) {
            this.resultMsg = resultMsg;
        }

        public T getDetail() {
            return detail;
        }

        public void setDetail(T detail) {
            this.detail = detail;
        }

        public Page getPage() {
            return page;
        }

        public void setPage(Page page) {
            this.page = page;
        }

        public Data() {

        }

        public Data(int resultCode, String resultMsg) {
            this.resultCode = resultCode;
            this.resultMsg = resultMsg;
        }

        public Data(int resultCode, String resultMsg, T detail) {
            this.resultCode = resultCode;
            this.resultMsg = resultMsg;
            this.detail = detail;
        }

        public Data(int resultCode, String resultMsg, T detail, Page page) {
            this.resultCode = resultCode;
            this.resultMsg = resultMsg;
            this.detail = detail;
            this.page = page;
        }
    }

}
