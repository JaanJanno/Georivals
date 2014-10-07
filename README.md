Aardejaht
=========

### Eesti keeles
'Aardejaht' on mobiilirakendus, mis ühendab endas meelelahutuse ja sportlikkuse, pannes inimesed liikuma ja aardeid jahtima.

## Serveri käivitamise juhised
Server on konfigureeritud kasutama nii MySQL ja Postgres andmebaasi kui ka mälus olevat andmebaasi. Mõlema kasutamiseks tuleb natuke seadistada

### Vajalikud asjad
* Eclipse (soovitavalt Spring Tool Suite)
* Maven. Hea seadistamise juhis siin http://www.mkyong.com/maven/how-to-install-maven-in-windows/
* Eclipse m2e plugin (ilma selleta arvab Eclipse, et projekt on katki). Eclipse'ile lisamiseks "Install new software", lehekülg http://download.eclipse.org/technology/m2e/releases
* Valikuline: MySQL või Postgres andmebaas


### Projekti seadistamine
1. Tõmba alla giti repo
2. Mine käsurealt webapp kausta
3.  Mälus oleva andmebaasi kasutamiseks peab minema faili src/main/resources/application.properties ja võtma `#spring.profiles.active=test` eest # ära. `test` asemele võib panna `mysql`või `postgres`, mis paneb servero ühenduma kohaliku andmebaasiga. 
4. webapp kaustas käsk `mvn spring-boot:run`
5. Nüüd peaks server olema ligipääsetav aadressil `localhost:8080`. Andmete nägemiseks võid proovida `localhost:8080/user`
6. Käsk `mvn eclipse:eclipse` genereerib Eclipse jaoks vajalikud failid
7. Nüüd saab teha "import existing projects".

Serveri käivitamiseks on vaja ainult käsku `mvn spring-boot:run`


### Andmebaasi setup
`test` ehk mälus olev andmebaas kasutab andmeid, mis on kirjas `import.sql` failis. Postgres või MySQL pead ise tõmbama.  Nende paroolid ja muu info peab ühtima `application-mysql.properties` või `application-postgres.properties` kirjeldatuga. Andmebaasi tabelid tekitab server ise. Päris andmebaasi hetkel näidisandmeid ei impordita. 

## In English
'Aardejaht' is a mobile application, which combines entertainment and sports by making people move and hunt for treasures.

