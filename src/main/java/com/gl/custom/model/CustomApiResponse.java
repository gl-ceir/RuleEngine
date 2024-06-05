package com.gl.custom.model;

public class CustomApiResponse {
    String status;
    String message;
    Result result;
}


class Result {
    private String imei;
    private String serial_number;
    private String date_of_registration;
    private String date_of_actual_import;
    private String goods_description;
    private String customs_duty_tax;
    private String device_type;
    private String brand;
    private String model;
    private int sim;

}