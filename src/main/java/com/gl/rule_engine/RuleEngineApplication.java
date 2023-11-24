/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.gl.rule_engine;

public class RuleEngineApplication {

    public static String startRuleEngine(RuleInfo ruleEngine) {

        return RulesList.getItems()
                .stream()
                .filter(a -> a.getClass().getName().toString().contains(ruleEngine.ruleName))
                .map(ExecutionInterface.class::cast)
                .reduce(new String(), (result, ruleNode) -> {
                    String key = ruleEngine.executeRuleAction;
                    if (key.contains("executeRule")) {
                        result = ruleNode.executeRule(ruleEngine);
                    } else {
                        result = ruleNode.executeAction(ruleEngine);
                    }
                    return result;
                }, (cumulative, intermediate) -> {
                    return intermediate;
                });
    }
}
//        stream .reduce( (Integer a, Integer b) -> a * b)
//
//                .map(     ruleClass -> )
//
//          String aasas = stream.findFirst()
//                .map( ruleClass ->   { return   ruleClass.executeRule(ruleEngine);}  )
//                .or(ruleClass ->   { return   ruleClass.executeRule(ruleEngine);} )
//                  .
//
//                .orElse(null);
//
//
//          if(true)
//
//
//
//              stream.findFirst()
//                      .reduce ( ruleClass-> ( 3  >= 4)  ?  ruleClass.executeRule(ruleEngine) : ruleClass.executeRule(ruleEngine)  )
//                      .orElse(null);
//
//
//
//                .map(ruleClass -> ruleClass.executeRule(ruleEngine))
//                .or(ruleClass -> ruleClass.executeRule(ruleEngine))
//                .orElse(null);
//        stream.forEach(ruleClass -> {
//              final  String a;
//            if ("1" == "1") {
//              a =  ruleClass.executeRule(ruleEngine);
//            } else {
//              a=  ruleClass.executeAction(ruleEngine);
//            }
//              return a;
//        }
//
//        );
//        String executioner = "";
//
//        stream
//                .anyMatch(x -> executioner.equals("1"))
//                .
//         if(stream.anyMatch(x -> executioner.equals("1")) ) {
//               return   ruleClass.executeRule(ruleEngine);
//    } else {
//        //perform function
//    }
//
//        Optional<RuleEngineInterface> a =    stream.findFirst()
//                 .filter(wx -> executioner.equals("1"))
//                .map(ruleClass -> ruleClass.executeRule(ruleEngine))
//                .or(ruleClass -> ruleClass.executeRule(ruleEngine)) ;
//
//
//
//          .filter(executioner.equals("1"))
//          .findAny()
//          .orElse(null);
//
//
//        stream.anyMatch(executioner.equals("1"))
//
//
//        System.out.println("--------##########");
//
//        new RulesCLass().getItems()
//                .stream()
//                .filter(a -> a.getClass().getName().toString().contains("EXISTS_IN_GSMA_DETAILS_DB"))
//                .forEach(some -> System.out.println(some));
//
//        System.out.println("--------########## LASTTTTT");
// System.out.println(ruleClass.executeRule(ruleEngine))
//        String a =      stream.
//                .for(ruleClass -> {
//                    if ("1" == "1") {
//                     return   ruleClass.executeRule(ruleEngine);
//                    } else {
//                     return   ruleClass.executeAction(ruleEngine);
//                    }
//                });
//        .forEach( ruleClass ->  {
//            return "1"== "2" ?  ruleClass.executeRule(ruleEngine)  : ruleClass.executeAction(ruleEngine)
//        }  );
//     stream.forEach(ruleClass -> System.out.println(ruleClass.executeRule(ruleEngine)));
//   return null;

//    static IMEI_LENGTH imeiLength;
//    static EXISTS_IN_GSMA_DETAILS_DB existInGsmaDetailsDb;
//        Stream<RuleEngineApplication> items = NewJavaClass.getItems().stream()
//                .filter(a -> "EXISTS_IN_GSMA_DETAILS_DB".equals(a.getClass().getName()))
//                .map(RuleEngineApplication.class::cast);
//
//        items.forEach(i -> i.getClass().getName());
//
//        System.out.println("--------" + items.count());
/////
//    public static void main(String[] args) {
//        System.out.println("--------" + startRuleEngine(new RuleEngine()));
//
//    }
//    Stream<RuleEngineInterface> stream = new RulesCLass().getItems()
//                .stream()
//                .filter(a -> a.getClass().getName().toString().contains("IMEI_LENGTH"))
//                .map(RuleEngineInterface.class::cast);
//
//        return stream
//                .reduce(new String(), (result, ruleNode) -> {
//                    String key = "1";
//                    if ("1".contains(key)) {
//                        result = ruleNode.executeRule(ruleEngine);
//                    } else {
//                        result = ruleNode.executeAction(ruleEngine);
//                    }
//                    System.out.println("TTTTTTTTTTTTT" + result);
//                    return result;
//                }, (cumulative, intermediate) -> {
//                    System.out.println("SSSSS" + cumulative + "AAA" + intermediate);
//                    return intermediate;
//                });
//    }
//  final  String a = "";
//        String ass = stream.findFirst()
//                .map(ruleClass -> ruleClass.executeRule(ruleEngine))
//                .orElse(null);
//        System.out.println("AAAAAAAAAAA:" + ass);
