
--
-- Table structure for table 'package'
--

DROP TABLE IF EXISTS 'package';

CREATE TABLE 'package' (
  'id' int(11) NOT NULL AUTO_INCREMENT,
  'name' varchar(45) DEFAULT NULL,
  'products' varchar(100) DEFAULT NULL,
  'description' blob,
  'price' float(10) DEFAULT NULL,
  PRIMARY KEY (`id`)
) 


LOCK TABLES 'package' WRITE;

INSERT INTO 'package' VALUES (1,'ProductName1','ProductA','Description for ProductA',100);

UNLOCK TABLES;

