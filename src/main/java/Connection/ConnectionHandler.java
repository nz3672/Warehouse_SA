package Connection;



import java.sql.*;
import java.util.ArrayList;

public class ConnectionHandler {
    public Connection connection;
    public Connection getConnection() {
        String dbName="inven_warehouse?createDatabaseIfNotExist=true";
        String userName = "root";
        String password = "admin";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            this.connection = DriverManager.getConnection("jdbc:mysql://localhost/"+dbName,userName,password);
            this.checkTable();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return this.connection;
    }

    private void checkTable() {
        try {
            String sql = "SELECT * FROM  employee";
            PreparedStatement preparedStatement = this.connection.prepareStatement(sql);
            ResultSet rec = preparedStatement.executeQuery();

            rec.next();
        } catch (SQLException err) {
            if (err.getErrorCode() == 1146) {
                try {
                    String sql = "CREATE TABLE `customer` (\n" +
                            "  `c_id` int NOT NULL AUTO_INCREMENT,\n" +
                            "  `c_name` varchar(100) DEFAULT NULL,\n" +
                            "  `c_address` text,\n" +
                            "  `c_phone` varchar(10) DEFAULT NULL,\n" +
                            "  PRIMARY KEY (`c_id`)\n" +
                            ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;\n" +
                            "@" +
                            "CREATE TABLE `employee` (\n" +
                            "  `em_id` varchar(15) NOT NULL,\n" +
                            "  `em_name` varchar(100) DEFAULT NULL,\n" +
                            "  `em_username` varchar(30) DEFAULT NULL,\n" +
                            "  `em_pwd` longtext,\n" +
                            "  `em_rank` varchar(50) DEFAULT NULL,\n" +
                            "  PRIMARY KEY (`em_id`)\n" +
                            ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;\n" +
                            "@" +
                            "CREATE TABLE `type` (\n" +
                            "  `t_id` int NOT NULL AUTO_INCREMENT,\n" +
                            "  `t_name` varchar(30) DEFAULT NULL,\n" +
                            "  PRIMARY KEY (`t_id`)\n" +
                            ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;\n" +
                            "@" +
                            "CREATE TABLE `warehouse` (\n" +
                            "  `wh_id` int NOT NULL AUTO_INCREMENT,\n" +
                            "  `wh_name` varchar(50) DEFAULT NULL,\n" +
                            "  `wh_level` varchar(10) DEFAULT NULL,\n" +
                            "  `wh_shelf` varchar(15) DEFAULT NULL,\n" +
                            "  `wh_shelf_level` varchar(15) DEFAULT NULL,\n" +
                            "  PRIMARY KEY (`wh_id`)\n" +
                            ") ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;\n" +
                            "@" +
                            "CREATE TABLE `supplier` (\n" +
                            "  `s_id` int NOT NULL AUTO_INCREMENT,\n" +
                            "  `s_phone` varchar(10) DEFAULT NULL,\n" +
                            "  `s_address` text,\n" +
                            "  `s_name` varchar(50) DEFAULT NULL,\n" +
                            "  PRIMARY KEY (`s_id`)\n" +
                            ") ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;\n" +
                            "@" +
                            "CREATE TABLE `order_customer` (\n" +
                            "  `oc_id` varchar(15) NOT NULL,\n" +
                            "  `c_id` int DEFAULT NULL,\n" +
                            "  `em_id` varchar(15) DEFAULT NULL,\n" +
                            "  `oc_date_pay` date DEFAULT NULL,\n" +
                            "  `oc_date_save` date DEFAULT NULL,\n" +
                            "  `oc_price` double DEFAULT NULL,\n" +
                            "  `oc_status` varchar(20) DEFAULT NULL,\n" +
                            "  PRIMARY KEY (`oc_id`),\n" +
                            "  KEY `employee_fk_idx` (`em_id`),\n" +
                            "  KEY `customer_fk_idx` (`c_id`),\n" +
                            "  CONSTRAINT `customer_fk` FOREIGN KEY (`c_id`) REFERENCES `customer` (`c_id`),\n" +
                            "  CONSTRAINT `employee_fk` FOREIGN KEY (`em_id`) REFERENCES `employee` (`em_id`)\n" +
                            ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;\n" +
                            "@" +
                            "CREATE TABLE `order_sup` (\n" +
                            "  `os_id` varchar(15) NOT NULL,\n" +
                            "  `s_id` int DEFAULT NULL,\n" +
                            "  `em_id` varchar(20) DEFAULT NULL,\n" +
                            "  `os_date_recieve` date DEFAULT NULL,\n" +
                            "  `os_date_save` date DEFAULT NULL,\n" +
                            "  `os_status` varchar(20) DEFAULT NULL,\n" +
                            "  PRIMARY KEY (`os_id`),\n" +
                            "  KEY `supplier_fk_idx` (`s_id`),\n" +
                            "  KEY `employee_fk_idx` (`em_id`),\n" +
                            "  CONSTRAINT `employee_os_fk` FOREIGN KEY (`em_id`) REFERENCES `employee` (`em_id`),\n" +
                            "  CONSTRAINT `supplier_fk` FOREIGN KEY (`s_id`) REFERENCES `supplier` (`s_id`)\n" +
                            ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;\n" +
                            "@" +
                            "CREATE TABLE `product` (\n" +
                            "  `pd_id` varchar(15) NOT NULL,\n" +
                            "  `pd_name` varchar(100) DEFAULT NULL,\n" +
                            "  `pd_price` double DEFAULT NULL,\n" +
                            "  `pd_qty` int DEFAULT NULL,\n" +
                            "  `pd_save_date` date DEFAULT NULL,\n" +
                            "  `t_id` int NOT NULL,\n" +
                            "  PRIMARY KEY (`pd_id`),\n" +
                            "  KEY `t_id_idx` (`t_id`),\n" +
                            "  CONSTRAINT `type id` FOREIGN KEY (`t_id`) REFERENCES `type` (`t_id`)\n" +
                            ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;\n" +
                            "@" +
                            "CREATE TABLE `purchase_list` (\n" +
                            "  `pu_id` int NOT NULL AUTO_INCREMENT,\n" +
                            "  `pu_qty` int DEFAULT NULL,\n" +
                            "  `pu_price` double DEFAULT NULL,\n" +
                            "  `os_id` varchar(15) DEFAULT NULL,\n" +
                            "  `pd_id` varchar(15) DEFAULT NULL,\n" +
                            "  PRIMARY KEY (`pu_id`),\n" +
                            "  KEY `product_pu_fk_idx` (`pd_id`),\n" +
                            "  KEY `ordersup_pu_fk_idx` (`os_id`),\n" +
                            "  CONSTRAINT `ordersup_pu_fk` FOREIGN KEY (`os_id`) REFERENCES `order_sup` (`os_id`),\n" +
                            "  CONSTRAINT `product_pu_fk` FOREIGN KEY (`pd_id`) REFERENCES `product` (`pd_id`)\n" +
                            ") ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;\n" +
                            "@" +
                            "CREATE TABLE `sale_list` (\n" +
                            "  `sale_id` int NOT NULL AUTO_INCREMENT,\n" +
                            "  `sale_qty` int DEFAULT NULL,\n" +
                            "  `pd_id` varchar(15) DEFAULT NULL,\n" +
                            "  `oc_id` varchar(15) DEFAULT NULL,\n" +
                            "  `sale_price` double DEFAULT NULL,\n" +
                            "  `sale_seller` varchar(100) DEFAULT NULL,\n" +
                            "  PRIMARY KEY (`sale_id`),\n" +
                            "  KEY `product_sale_fk_idx` (`pd_id`),\n" +
                            "  KEY `ordercustomer_sale_fk_idx` (`oc_id`),\n" +
                            "  CONSTRAINT `ordercustomer_sale_fk` FOREIGN KEY (`oc_id`) REFERENCES `order_customer` (`oc_id`),\n" +
                            "  CONSTRAINT `product_sale_fk` FOREIGN KEY (`pd_id`) REFERENCES `product` (`pd_id`)\n" +
                            ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;\n" +
                            "@" +
                            "CREATE TABLE `warehouselist` (\n" +
                            "  `wl_id` int NOT NULL AUTO_INCREMENT,\n" +
                            "  `wh_id` int DEFAULT NULL,\n" +
                            "  `pd_id` varchar(11) DEFAULT NULL,\n" +
                            "  PRIMARY KEY (`wl_id`),\n" +
                            "  KEY `product_fk_idx` (`pd_id`),\n" +
                            "  KEY `warehouse_fk_idx` (`wh_id`),\n" +
                            "  CONSTRAINT `product_fk` FOREIGN KEY (`pd_id`) REFERENCES `product` (`pd_id`),\n" +
                            "  CONSTRAINT `warehouse_fk` FOREIGN KEY (`wh_id`) REFERENCES `warehouse` (`wh_id`)\n" +
                            ") ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;\n";

                    String[] strs = sql.split("@");
                            Statement statement = connection.createStatement();
                            for(String str : strs) {
                                statement.executeUpdate(str);
                            }
                } catch (SQLException a) {
                    a.printStackTrace();
                }

            } else {
                err.printStackTrace();
            }
        }

        }
    }

