/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.gl.rule_engine;


import com.gl.rule_engine.rules.DUPLICATE_USAGE_CHECK;
import com.gl.rule_engine.rules.EXISTS_IN_ALL_ACTIVE_DB;
import com.gl.rule_engine.rules.EXISTS_IN_GREYLIST_DB;
import com.gl.rule_engine.rules.EXISTS_IN_GSMA_DETAILS_DB;
import com.gl.rule_engine.rules.EXISTS_IN_GSMA_TAC_DB;
import com.gl.rule_engine.rules.EXISTS_IN_USAGE_DB;
import com.gl.rule_engine.rules.EXIST_IN_GSMABLACKLIST_DB;
import com.gl.rule_engine.rules.IMEI_LENGTH;
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
                new EXISTS_IN_ALL_ACTIVE_DB()
        );
    }
}