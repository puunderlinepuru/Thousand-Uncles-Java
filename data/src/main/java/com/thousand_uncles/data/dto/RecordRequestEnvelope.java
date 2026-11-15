package com.thousand_uncles.data.dto;


public class RecordRequestEnvelope {
    private String type;
    private Object data;

    public RecordRequestEnvelope(
            String type,
            Object data
    ){
        this.type = type;
        this.data = data;
    }

    public String getType() {return type;}
    public void setType(String type) {this.type = type;}

    public Object getData() {return data;}
    public void setData(Object data) {this.data = data;}

    @Override
    public String toString() {
        return super.toString();
    }
}
