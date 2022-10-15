package com.pp.responde;

public class OperationStatusModel {
    private final String operationResult;
    private final String operationName;

    public static class Builder {
        private final String operationName;

        private String operationResult = RequestOperationStatus.SUCCESS.name();

        public Builder(String operationName) {
            this.operationName = operationName;
        }

        public Builder operationResult(String operationResult){
            this.operationResult = operationResult;
            return this;
        }

        public OperationStatusModel build(){
            return new OperationStatusModel(this);
        }
    }

    private OperationStatusModel(Builder builder){
        this.operationResult = builder.operationResult;
        this.operationName = builder.operationName;
    }

    public String getOperationResult() {
        return operationResult;
    }

    public String getOperationName() {
        return operationName;
    }
}
