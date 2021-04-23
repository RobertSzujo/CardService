DROP TABLE CARD;
DROP TABLE CONTACT;
DROP TABLE OWNER;
DROP TABLE CARD_TYPE;
DROP SEQUENCE OWNER_SEQ;
DROP SEQUENCE CARD_TYPE_SEQ;
DROP SEQUENCE CONTACT_SEQ;

CREATE TABLE OWNER (
  owner_id int NOT NULL PRIMARY KEY,
  owner VARCHAR2(128) );
  
CREATE TABLE CARD_TYPE (
  card_type_id int NOT NULL PRIMARY KEY,
  card_type VARCHAR2(128) );
  
CREATE TABLE CONTACT (
  contact_id int NOT NULL PRIMARY KEY,
  owner_id int REFERENCES OWNER (owner_id),
  contact_type VARCHAR(5) NOT NULL,
  contact VARCHAR2(128) );

CREATE TABLE CARD (
  card_number CHAR(16) NOT NULL PRIMARY KEY,
  valid_thru CHAR(5),
  card_hash VARCHAR2(128),
  card_type_id int REFERENCES CARD_TYPE (card_type_id),
  owner_id int REFERENCES OWNER (owner_id),
  disabled CHAR(1) DEFAULT 'N' );
  
CREATE SEQUENCE OWNER_SEQ
  MINVALUE 1
  MAXVALUE 9999999999
  START WITH 1
  INCREMENT BY 1;

CREATE SEQUENCE CONTACT_SEQ
  MINVALUE 1
  MAXVALUE 9999999999
  START WITH 1
  INCREMENT BY 1;
  
CREATE SEQUENCE CARD_TYPE_SEQ
  MINVALUE 1
  MAXVALUE 9999999999
  START WITH 1
  INCREMENT BY 1;
  
INSERT INTO CARD_TYPE (card_type_id, card_type) VALUES (CARD_TYPE_SEQ.nextval, 'MASTERCARD GEORGE BANKKÁRTYA');
INSERT INTO CARD_TYPE (card_type_id, card_type) VALUES (CARD_TYPE_SEQ.nextval, 'VISA GEORGE BANKKÁRTYA');
INSERT INTO CARD_TYPE (card_type_id, card_type) VALUES (CARD_TYPE_SEQ.nextval, 'MASTERCARD STANDARD BANKKÁRTYA');
INSERT INTO CARD_TYPE (card_type_id, card_type) VALUES (CARD_TYPE_SEQ.nextval, 'MASTERCARD GOLD BANKKÁRTYA');
INSERT INTO CARD_TYPE (card_type_id, card_type) VALUES (CARD_TYPE_SEQ.nextval, 'MASTERCARD WORLD GOLD BANKKÁRTYA');
INSERT INTO CARD_TYPE (card_type_id, card_type) VALUES (CARD_TYPE_SEQ.nextval, 'MASTERCARD STANDARD DEVIZAKÁRTYA');
INSERT INTO CARD_TYPE (card_type_id, card_type) VALUES (CARD_TYPE_SEQ.nextval, 'MASTERCARD DIÁK BANKKÁRTYA');
INSERT INTO CARD_TYPE (card_type_id, card_type) VALUES (CARD_TYPE_SEQ.nextval, 'MASTERCARD START BANKKÁRTYA');
INSERT INTO CARD_TYPE (card_type_id, card_type) VALUES (CARD_TYPE_SEQ.nextval, 'VISA CLASSIC BANKKÁRTYA');
INSERT INTO CARD_TYPE (card_type_id, card_type) VALUES (CARD_TYPE_SEQ.nextval, 'VISA VIRTUÁLIS BANKKÁRTYA');
INSERT INTO CARD_TYPE (card_type_id, card_type) VALUES (CARD_TYPE_SEQ.nextval, 'VISA ELECTRON BANKKÁRTYA');