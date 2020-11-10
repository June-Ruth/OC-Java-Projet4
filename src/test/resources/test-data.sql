/* Setting up TEST DB */
/*create database test;*/

create table parking(
PARKING_NUMBER int PRIMARY KEY,
AVAILABLE bool NOT NULL,
TYPE varchar(10) NOT NULL
);

create table ticket(
 ID int PRIMARY KEY AUTO_INCREMENT,
 PARKING_NUMBER int NOT NULL,
 VEHICLE_REG_NUMBER varchar(10) NOT NULL,
 PRICE double,
 IN_TIME DATETIME NOT NULL,
 OUT_TIME DATETIME,
 FOREIGN KEY (PARKING_NUMBER)
 REFERENCES parking(PARKING_NUMBER)
);

insert into parking(PARKING_NUMBER,AVAILABLE,TYPE) values(1,true,'CAR');
insert into parking(PARKING_NUMBER,AVAILABLE,TYPE) values(2,true,'CAR');
insert into parking(PARKING_NUMBER,AVAILABLE,TYPE) values(3,false,'CAR');
insert into parking(PARKING_NUMBER,AVAILABLE,TYPE) values(4,true,'BIKE');
insert into parking(PARKING_NUMBER,AVAILABLE,TYPE) values(5,true,'BIKE');
commit;

insert into ticket (ID, PARKING_NUMBER, VEHICLE_REG_NUMBER, PRICE, IN_TIME, OUT_TIME) values (1, 1, 'ABC123DEF', 1.5, PARSEDATETIME('2020-01-01 12:00', 'yyyy-MM-dd HH:mm'), PARSEDATETIME('2020-11-11 13:00', 'yyyy-MM-dd HH:mm'));
insert into ticket (ID, PARKING_NUMBER, VEHICLE_REG_NUMBER, PRICE, IN_TIME, OUT_TIME) values (2, 3, 'ABC123DEF', null, PARSEDATETIME('2020-01-02 12:00', 'yyyy-MM-dd HH:mm'), null);
commit;