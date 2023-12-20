/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.gl.rule_engine;

/**
 *
 * @author maverick
 */
public interface ExecutionInterface {

    String executeRule(RuleInfo ruleEngine);

    String executeAction(RuleInfo ruleEngine);
}
