DROP TABLE CARD;
DROP TABLE OWNER;
DROP TABLE CONTACT;
DROP TABLE CARD_TYPE;

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
  
INSERT INTO CARD_TYPE (card_type_id, card_type) VALUES (1, 'Mastercard George bankkártya');
INSERT INTO CARD_TYPE (card_type_id, card_type) VALUES (2, 'Visa George bankkártya');
INSERT INTO CARD_TYPE (card_type_id, card_type) VALUES (3, 'Mastercard Standard bankkártya');
INSERT INTO CARD_TYPE (card_type_id, card_type) VALUES (4, 'Mastercard Gold bankkártya');
INSERT INTO CARD_TYPE (card_type_id, card_type) VALUES (5, 'Mastercard World Gold bankkártya');
INSERT INTO CARD_TYPE (card_type_id, card_type) VALUES (6, 'Mastercard Standard Devizakártya');
INSERT INTO CARD_TYPE (card_type_id, card_type) VALUES (7, 'Mastercard Diák bankkártya');
INSERT INTO CARD_TYPE (card_type_id, card_type) VALUES (8, 'Mastercard Start bankkártya');
INSERT INTO CARD_TYPE (card_type_id, card_type) VALUES (9, 'Visa Classic bankkártya');
INSERT INTO CARD_TYPE (card_type_id, card_type) VALUES (10, 'Visa virtuális bankkártya');
INSERT INTO CARD_TYPE (card_type_id, card_type) VALUES (11, 'Visa Electron bankkártya');