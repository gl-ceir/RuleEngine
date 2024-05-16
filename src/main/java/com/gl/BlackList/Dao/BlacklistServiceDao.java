/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gl.BlackList.Dao;

import static com.gl.BlackList.Dao.BlacklistServiceDao.logger;
import com.gl.BlackList.model.BlacklistTacDb;
import com.gl.BlackList.model.BlacklistTacDeviceHistoryDb;
import com.google.gson.Gson;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author user
 */
public class BlacklistServiceDao {

    static final Logger logger = LogManager.getLogger(BlacklistServiceDao.class);

    public String getValueFromSysParam(Connection conn, String tag) {
        String query = "  select value  from sys_param  where tag = '" + tag + "' ";
        String response = null;
        try (Statement stmt = conn.createStatement(); ResultSet rs0 = stmt.executeQuery(query);) {
            while (rs0.next()) {
                response = rs0.getString(1);
            }
            logger.debug("Value from sys_Param for tag " + tag + " is " + response);
        } catch (Exception e) {
            logger.debug("Error while getting details from sys_Param" + e);
        }
        return response;
    }

    public String getBlacklistStatus(Connection conn, String Imei) {
        String status = null;
        try {
            try (Statement stmt = conn.createStatement()) {
                String query = " select blocklist_status from blacklist_imei_details  where device_id = '" + Imei + "'   ";
                logger.debug("Query [" + query + "]");
                try (ResultSet rs = stmt.executeQuery(query)) {
                    while (rs.next()) {
                        status = rs.getString("blocklist_status");
                    }
                }
            }
            logger.debug("  Status  " + status);
        } catch (Exception e) {
            logger.error("Error " + e);
        }
        return status;
    }

    public String databaseMapper(BlacklistTacDb blacklistTacDb, Connection conn) {
        String rslt = null;
        try {
            rslt = blacklistTacDb.getBlockliststatus();
            int id = insertBlacklistTacDbAndGetId(conn, blacklistTacDb);
            List<BlacklistTacDeviceHistoryDb> blacklistTacDeviceHistoryDb = blacklistTacDb.getImeihistory();
            insertBlacklistTacDeviceHistory(conn, id, blacklistTacDeviceHistoryDb);
        } catch (Exception ex) {
            logger.debug(" .." + ex);
        }
        return rslt;
    }

    private int insertBlacklistTacDbAndGetId(Connection conn, BlacklistTacDb blacklistTacDb) {
        int generatedKey = 0;

        String query = " insert into blacklist_imei_details (ref_code, response_status, device_id, partner_id, blocklist_status, generallist_status,"
                + "manufacturer, brand_name, marketing_name, model_name , band , operating_sys , nfc , bluetooth , wlan , device_type, "
                + "created_on , modified_on)"
                + "values ('" + blacklistTacDb.getRefcode() + "', '" + blacklistTacDb.getResponsestatus() + "' , '" + blacklistTacDb.getDeviceid() + "' ,'" + blacklistTacDb.getPartnerid() + "' , '" + blacklistTacDb.getBlockliststatus() + "', '" + blacklistTacDb.getGeneralliststatus() + "' , "
                + "  '" + blacklistTacDb.getManufacturer() + "' ,  '" + blacklistTacDb.getBrandname() + "', '" + blacklistTacDb.getMarketingname() + "' , '" + blacklistTacDb.getModelname() + "', "
                + "  '" + blacklistTacDb.getBand() + "' ,  '" + blacklistTacDb.getOperatingsys() + "', '" + blacklistTacDb.getNfc() + "' , '" + blacklistTacDb.getBluetooth() + "', '" + blacklistTacDb.getWLAN() + "' , '" + blacklistTacDb.getDevicetype() + "', "
                + "  CURRENT_TIMESTAMP , CURRENT_TIMESTAMP )";
        logger.debug("query .." + query);

        try {
            PreparedStatement ps = null;
            if (conn.toString().contains("oracle")) {
                ps = conn.prepareStatement(query, new String[]{"ID"});
            } else {
                ps = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            }
            ps.execute();
            logger.debug("Going for getGenerated key  ");
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                generatedKey = rs.getInt(1);
            }
            logger.info("Inserted record's ID: " + generatedKey);
            rs.close();
        } catch (Exception e) {
            logger.error("Failed  " + e);
        }
        return generatedKey;
    }

    private void insertBlacklistTacDeviceHistory(Connection conn, int id, List<BlacklistTacDeviceHistoryDb> blacklistTacDeviceHistoryDb) {
        Statement stmt = null;
        try {
            stmt = conn.createStatement();
            for (BlacklistTacDeviceHistoryDb btr : blacklistTacDeviceHistoryDb) {
                var query = " insert  into blacklist_imei_block_history ( action, reasoncode, reasoncodedesc, action_taken_by, country ,blacklist_imei_db_id , created_on) "
                        + "values( '" + btr.getAction() + "', '" + btr.getReasoncode() + "',  '" + btr.getReasoncodedesc() + "',  '" + btr.getBy() + "',  '" + btr.getCountry() + "', '" + id + "'  , CURRENT_TIMESTAMP  )";
                logger.debug(" Query:: " + query);
                stmt.executeUpdate(query);
            }
            stmt.close();
        } catch (SQLException ex) {
            logger.error(" " + ex);
        }
    }

}

//     public String getBlacklistUrl(Connection conn) {
//          String stats = null;
//          Statement stmt = null;
//          ResultSet rs0 = null;
//          ResultSet rs1 = null;
//          String query = null;
//
//          try {
//               query = "  select value  from system_configuration_db where tag = 'GSMA_BLACKLIST_URL' ";
//               logger.debug("Query  [" + query + "]");
//               stmt = conn.createStatement();
//               rs0 = stmt.executeQuery(query);
//               while (rs0.next()) {
//                    stats = rs0.getString(1);
//               }
//               stmt.close();
//               logger.debug("  BlacklistSTatuss url " + stats);
//
//          } catch (Exception e) {
//               logger.debug("Error at   getBlack listStatus" + e);
//          } finally {
//               try {
//                    rs0.close();
//                    rs1.close();
//                    stmt.close();
//               } catch (Exception ex) {
//                    java.util.logging.Logger.getLogger(BlacklistServiceDao.class.getName()).log(Level.SEVERE, null, ex);
//               }
//          }
//          return stats;
//     }
//     void insertInvalidTac(Connection conn, BlacklistTacDb blacklistTacDb) {
//          boolean isOracle = conn.toString().contains("oracle");
//          String dateFunction = Util.defaultDate(isOracle);
//          Statement stmt = null;
//          String query = " insert into blacklist_imei_invalid_db ( tac ,created_on , modified_on )" // gsma_blacklist_tac_invalid_db earlier
//                  + "values ( '" + blacklistTacDb.getDeviceid() + "' , " + dateFunction + "  , " + dateFunction + " )";
//          logger.debug("query .." + query);
//          try {
//               stmt = conn.createStatement();
//               stmt.executeUpdate(query);
//               logger.debug("Inserted in blck Tac Db");
//          } catch (Exception e) {
//               logger.debug("Error " + e);
//          } finally {
//               try {
//                    stmt.close();
//               } catch (Exception ex) {
//                    java.util.logging.Logger.getLogger(BlacklistServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
//               }
//          }
//
//     }
//    private void insertBlckListTacDeviceDetail(Connection conn, BlacklistTacDeviceDetailsDb btdd, int id) {
//          Statement stmt = null;
//          boolean isOracle = conn.toString().contains("oracle");
//          String dateFunction = "current_timestamp";
//          String query = " insert into  blacklist_imei_device_details_db ( bluetooth,brand_name, device_type, manufacturer,marketing_name, model_name , nfc ,operating_system , wlan , blacklist_tac_db_id ,created_on) "
//                  + " values( '" + btdd.getBluetooth() + "','" + btdd.getBrandName() + "', '" + btdd.getDeviceType() + "', '" + btdd.getManufacturer() + "','" + btdd.getMarketingName() + "','" + btdd.getModelName() + "','" + btdd.getNFC() + "',  '" + btdd.getOperatingSystem() + "', '" + btdd.getWLAN() + "', '" + id + "' ," + dateFunction + "     ) ";
//          logger.debug("uqry " + query);
//          try {
//               stmt = conn.createStatement();
//               stmt.executeUpdate(query);
//               logger.debug("Inserted in blck Tac Details Db");
//
////               stmt.close ();
//          } catch (Exception e) {
//               logger.debug(" error" + e);
//          } finally {
//               try {
//                    stmt.close();
//               } catch (SQLException ex) {
//                    java.util.logging.Logger.getLogger(BlacklistServiceDao.class.getName()).log(Level.SEVERE, null, ex);
//               }
//          }
//
//     }
