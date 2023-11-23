/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.gl.rule_engine;

import com.gl.rule_engine.rules.*;
import java.util.List;

public interface RulesClass {

    static List<Object> getItems() {
        return List.of(
                new IMEI_LENGTH(),
                new EXISTS_IN_GSMA_DETAILS_DB(),
                new EXISTS_IN_GSMA_TAC_DB(),
                new EXISTS_IN_USAGE_DB(),
                new DUPLICATE_USAGE_CHECK(),
                new EXISTS_IN_GREYLIST_DB(),
                new EXIST_IN_GSMABLACKLIST_DB(),
                new EXISTS_IN_ALL_ACTIVE_DB(), // done

                new EXIST_REGULARIZED(),
                new IMEI_LUHN_CHECK(),
                new EXIST_IN_BLACKLIST_DB(),
                new IMEI_NULL(),
                new EXIST_IN_CUSTOM_DB(),
                new EXISTS_IN_FOREIGN_DB(),
                new LBD(),
                new EXIST_IN_DISTRIBUTOR_DB(),
                new NATIONAL_WHITELISTS(),
                new EXIST_IN_END_USER_DB(),
                new EXIST_IN_END_USER_DEVICE_DB(),
                new SAME_DEVICETYPE_RECOVERY(),
                new EXISTS_IN_REGULARIZED_DB(),
                new SAME_DEVICETYPE_UNBLOCK(),
                new EXIST_IN_IMPORTER_DB(),
                new EXISTS_IN_TYPE_APPROVED_DB(),
                new SAME_OPERATOR_UNBLOCK(),
                new EXIST_IN_LAWFUL_DB(),
                new EXISTS_IN_TYPE_APPROVED_TAC(),
                new SAME_SERIAL_RECOVERY(),
                new EXIST_IN_MANUFACTURER_DB(),
                new SAME_SERIAL_UNBLOCK(),
                new EXIST_IN_OPERATOR_DB(),
                new EXISTS_IN_WHITELIST_DB(),
                new SYS_REG(),
                new EXIST_IN_RETAILER_DB(),
                new TAC_FORMAT(),
                new EXIST_IN_TAX_PAID_DB(),
                new FOREIGN_SIM(),
                new TEST_IMEI(),
                new EXIST_IN_VIP_LIST(),
                new USER_REG()
        );
    }
}
