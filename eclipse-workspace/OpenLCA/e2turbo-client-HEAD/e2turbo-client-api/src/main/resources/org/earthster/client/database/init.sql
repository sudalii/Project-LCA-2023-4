CREATE TABLE EXCHANGE (ID VARCHAR(255) NOT NULL, AMOUNT FLOAT, IS_INPUT SMALLINT DEFAULT 0, FK_FLOW VARCHAR(255), FK_ACTIVITY VARCHAR(255), PRIMARY KEY (ID));
CREATE TABLE CATEGORY (ID VARCHAR(255) NOT NULL, NAME VARCHAR(255), FK_PARENT_CATEGORY VARCHAR(255), PRIMARY KEY (ID));
CREATE TABLE ACTIVITY (ID VARCHAR(255) NOT NULL, NAME VARCHAR(255), PRIMARY KEY (ID));
CREATE TABLE FLOW (ID VARCHAR(255) NOT NULL, UNIT VARCHAR(255), CATEGORY VARCHAR(255), NAME VARCHAR(255), FK_ASSESSMENT VARCHAR(255), TYPE INTEGER, PRIMARY KEY (ID));
CREATE TABLE BEAITEM (CODE VARCHAR(255) NOT NULL, NAME VARCHAR(255), FK_BEA_COMMODITY VARCHAR(255), PRIMARY KEY (CODE));
CREATE TABLE ASSESSMENT (ID VARCHAR(255) NOT NULL, PRIMARY KEY (ID));
CREATE TABLE ASSESSMENT_RESULT (ID VARCHAR(255) NOT NULL, VALUE FLOAT, FK_LCIA_CATEGORY VARCHAR(255), FK_ASSESSMENT VARCHAR(255), PRIMARY KEY (ID));
CREATE TABLE PRODUCT (ID VARCHAR(255) NOT NULL, FK_ACTIVITY VARCHAR(255), RETRIEVALTYPE INTEGER, FK_LOCAL_ASSESSMENT VARCHAR(255), DESCRIPTION CLOB(2147483647), NAME VARCHAR(255), FK_ASSESSMENT VARCHAR(255), COMMODITYCODE VARCHAR(255), PRIMARY KEY (ID));
CREATE TABLE BEACOMMODITY (CODE VARCHAR(255) NOT NULL, NAME VARCHAR(255), PRODUCTID VARCHAR(255), FK_BEA_CATEGORY VARCHAR(255), PRIMARY KEY (CODE));
CREATE TABLE BEACATEGORY (ID VARCHAR(255) NOT NULL, NAME VARCHAR(255), PRIMARY KEY (ID));
CREATE TABLE LCIA_CATEGORY (ID VARCHAR(255) NOT NULL, UNIT VARCHAR(255), NAME VARCHAR(255), PRIMARY KEY (ID));
ALTER TABLE EXCHANGE ADD CONSTRAINT EXCHANGE_FK_FLOW FOREIGN KEY (FK_FLOW) REFERENCES FLOW (ID);
ALTER TABLE EXCHANGE ADD CONSTRAINT EXCHANGEFKACTIVITY FOREIGN KEY (FK_ACTIVITY) REFERENCES ACTIVITY (ID);
ALTER TABLE CATEGORY ADD CONSTRAINT CTGRYFKPRNTCTEGORY FOREIGN KEY (FK_PARENT_CATEGORY) REFERENCES CATEGORY (ID);
ALTER TABLE BEAITEM ADD CONSTRAINT BTEMFKBEACOMMODITY FOREIGN KEY (FK_BEA_COMMODITY) REFERENCES BEACOMMODITY (CODE);
ALTER TABLE ASSESSMENT_RESULT ADD CONSTRAINT SSSSMNTRSFKLCCTGRY FOREIGN KEY (FK_LCIA_CATEGORY) REFERENCES LCIA_CATEGORY (ID);
ALTER TABLE ASSESSMENT_RESULT ADD CONSTRAINT SSSSMNTRSFKSSSSMNT FOREIGN KEY (FK_ASSESSMENT) REFERENCES ASSESSMENT (ID);
ALTER TABLE BEACOMMODITY ADD CONSTRAINT BCMMDITYFKBCTEGORY FOREIGN KEY (FK_BEA_CATEGORY) REFERENCES BEACATEGORY (ID);