package com.gl.custom.model;

public class Result {
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

    public String getImei() {return imei;}

    public void setImei(String imei) {this.imei = imei;}

    public String getSerial_number() {return serial_number;}

    public void setSerial_number(String serial_number) {this.serial_number = serial_number;}

    public String getDate_of_registration() {return date_of_registration;}

    public void setDate_of_registration(String date_of_registration) {this.date_of_registration = date_of_registration;}

    public String getDate_of_actual_import() {return date_of_actual_import;}

    public void setDate_of_actual_import(String date_of_actual_import) {this.date_of_actual_import = date_of_actual_import;}

    public String getGoods_description() {return goods_description;}

    public void setGoods_description(String goods_description) {this.goods_description = goods_description;}

    public String getCustoms_duty_tax() {return customs_duty_tax;}

    public void setCustoms_duty_tax(String customs_duty_tax) {this.customs_duty_tax = customs_duty_tax;}

    public String getDevice_type() {return device_type;}

    public void setDevice_type(String device_type) {this.device_type = device_type;}

    public String getBrand() {return brand;}

    public void setBrand(String brand) {this.brand = brand;}

    public String getModel() {return model;}

    public void setModel(String model) {this.model = model;}

    public int getSim() {return sim;}

    public void setSim(int sim) {this.sim = sim;}
    public Result(){}
    public Result(String imei, String serial_number, String date_of_registration, String date_of_actual_import, String goods_description, String customs_duty_tax, String device_type, String brand, String model, int sim) {
        this.imei = imei;
        this.serial_number = serial_number;
        this.date_of_registration = date_of_registration;
        this.date_of_actual_import = date_of_actual_import;
        this.goods_description = goods_description;
        this.customs_duty_tax = customs_duty_tax;
        this.device_type = device_type;
        this.brand = brand;
        this.model = model;
        this.sim = sim;
    }

    @Override
    public String toString() {
        return "Result{" +
                "imei='" + imei + '\'' +
                ", serial_number='" + serial_number + '\'' +
                ", date_of_registration='" + date_of_registration + '\'' +
                ", date_of_actual_import='" + date_of_actual_import + '\'' +
                ", goods_description='" + goods_description + '\'' +
                ", customs_duty_tax='" + customs_duty_tax + '\'' +
                ", device_type='" + device_type + '\'' +
                ", brand='" + brand + '\'' +
                ", model='" + model + '\'' +
                ", sim=" + sim +
                '}';
    }
}