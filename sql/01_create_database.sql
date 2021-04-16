DROP TABLE CARD;
DROP TABLE OWNER;
DROP TABLE CONTACT;
DROP TABLE CARD_TYPE;
DROP SEQUENCE OWNER_SEQ;
DROP SEQUENCE CARD_TYPE_SEQ;
DROP SEQUENCE CONTACT_SEQ;

CREATE TABLE CARD
( card_number CHAR(16) NOT NULL PRIMARY KEY,
  valid_thru CHAR(5),
  card_hash VARCHAR2(128),
  card_type_id int NOT NULL,
  owner_id int NOT NULL,
  disabled CHAR(1) DEFAULT 'N');
  
CREATE TABLE OWNER
( owner_id int NOT NULL PRIMARY KEY,
  owner VARCHAR2(128) );

CREATE TABLE CONTACT
( contact_id int NOT NULL PRIMARY KEY,
  owner_id int NOT NULL,
  contact_type VARCHAR(5) NOT NULL,
  contact VARCHAR2(128) );
  
CREATE TABLE CARD_TYPE
( card_type_id int NOT NULL PRIMARY KEY,
  card_type VARCHAR2(128) );
  
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
  
INSERT INTO CARD_TYPE (card_type_id, card_type) VALUES (CARD_TYPE_SEQ.nextval, 'Mastercard George bankk�rtya');
INSERT INTO CARD_TYPE (card_type_id, card_type) VALUES (CARD_TYPE_SEQ.nextval, 'Visa George bankk�rtya');
INSERT INTO CARD_TYPE (card_type_id, card_type) VALUES (CARD_TYPE_SEQ.nextval, 'Mastercard Standard bankk�rtya');
INSERT INTO CARD_TYPE (card_type_id, card_type) VALUES (CARD_TYPE_SEQ.nextval, 'Mastercard Gold bankk�rtya');
INSERT INTO CARD_TYPE (card_type_id, card_type) VALUES (CARD_TYPE_SEQ.nextval, 'Mastercard World Gold bankk�rtya');
INSERT INTO CARD_TYPE (card_type_id, card_type) VALUES (CARD_TYPE_SEQ.nextval, 'Mastercard Standard Devizak�rtya');
INSERT INTO CARD_TYPE (card_type_id, card_type) VALUES (CARD_TYPE_SEQ.nextval, 'Mastercard Di�k bankk�rtya');
INSERT INTO CARD_TYPE (card_type_id, card_type) VALUES (CARD_TYPE_SEQ.nextval, 'Mastercard Start bankk�rtya');
INSERT INTO CARD_TYPE (card_type_id, card_type) VALUES (CARD_TYPE_SEQ.nextval, 'Visa Classic bankk�rtya');
INSERT INTO CARD_TYPE (card_type_id, card_type) VALUES (CARD_TYPE_SEQ.nextval, 'Visa virtu�lis bankk�rtya');
INSERT INTO CARD_TYPE (card_type_id, card_type) VALUES (CARD_TYPE_SEQ.nextval, 'Visa Electron bankk�rtya');