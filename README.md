Aardejaht
=========

## Eesti keeles
'Aardejaht' on mobiilirakendus, mis ühendab endas meelelahutuse ja sportlikkuse, pannes inimesed liikuma ja aardeid jahtima.

### Serveri käivitamise juhised
Server on võimeline kasutama nii MySQL ja Postgres andmebaasi kui ka mälus olevat andmebaasi. 

#### Vajalikud asjad
* Maven. Hea seadistamise juhis siin http://www.mkyong.com/maven/how-to-install-maven-in-windows/

#### Soovitatud asjad
* Eclipse (soovitavalt Spring Tool Suite)
* Eclipse m2e plugin (ilma selleta arvab Eclipse, et projekt on katki). Eclipse'ile lisamiseks "Install new software", lehekülg http://download.eclipse.org/technology/m2e/releases
* MySQL või Postgres andmebaas

#### Projekti seadistamine
1. Tõmba alla giti repo
2. Mine käsurealt webapp kausta
3.  Mälus oleva andmebaasi kasutamiseks peab minema faili src/main/resources/application.properties ja muutma rida `spring.profiles.active=default`. `default` asemele kirjuta `test`. Kohaliku andmebaasi kasutamiseks kirjuta `mysql`või `postgres`. 
4. webapp kaustas käsk `mvn spring-boot:run`
5. Nüüd peaks server olema ligipääsetav aadressil `localhost:8080`. Andmete nägemiseks võid proovida `localhost:8080/user`
6. Käsk `mvn eclipse:eclipse` genereerib Eclipse jaoks vajalikud failid
7. Nüüd saab teha `import existing projects`.

Serveri käivitamiseks on vaja ainult käsku `mvn spring-boot:run`


#### Andmebaasi setup
`test` ehk mälus olev andmebaas kasutab andmeid, mis on kirjas `import.sql` failis. Postgres või MySQL pead ise tõmbama.  Nende paroolid ja muu info peab ühtima `application-mysql.properties` või `application-postgres.properties` kirjeldatuga. Andmebaasi tabelid tekitab server ise. 

Päris andmebaasi  näidisandmeid hetkel ei impordita.

Herokus töötab andmebaas `DATABASE_URL` kaudu, nii et soovi kõrval võib Heroku stiilis Postgres ühendamise stringi süsteemi keskonnamuutujatesse panna. Sellisel juhul ei pea `application.properties` failis midagi muutma. 

Selline string on näiteks `postgres://root:root@localhost:3306/test`.

### Androidi seadistus
Projekti kompileerimiseks on kaks varianti: Eclipse ADT ja Android Studio. Järgnev juhis on projekti seadistamisest ja kompileerimisest Eclipse'i kasutades.
#### Vajalikud asjad
* Eclipse, Eclipse ADT, Android SDK: http://developer.android.com/sdk/index.html

Lisaks läheb vaja Android SDK Managerist:
* Android 4.4.2 (API 19)
* Google Play services

#### Soovitatud asjad
* Nutitelefon Androidiga 4.0 või kõrgem.

#### Projekti seadistamine
1. Lae alla Aardejahi giti repositoorium.
2. Impordi mobileapp alamkausta projektina toimetades Eclipse'i keskkonnas järgnevalt: Git Repositories > Add an existing local Git Repository to this view > Directory: ...\Aardejaht > Finish > Parem klõps mobileapp kaustal > Import projects... > Import existing project > Next > Finish.
3. Impordi Google Play services library projektina. (http://developer.android.com/google/play-services/setup.html, http://developer.android.com/tools/projects/projects-eclipse.html#SettingUpLibraryProject)
4. Lisa Google Play services Aardejahi projekti Android library'ks. (http://developer.android.com/tools/projects/projects-eclipse.html#ReferencingLibraryProject)
5. Kui soovid oma telefoni kasutada:
	1. Ühenda telefon arvutiga.
	2. Lülita USB debugging telefonis sisse. (http://www.phonearena.com/news/How-to-enable-USB-debugging-on-Android_id53909)
6. Kui soovid Android emulaatorit kasutada:
	1. Vali Android Virtual Device Manager.
	2. Loo uus seade, valides Target'iks 'Google APIs - API Level 19' ja RAM'iks 256. Muu võid vabal valikul valida.
7. Vajuta Run > Run as Android application.
8. Vali omale sobiv seade (oma telefon või emulaator) ja vajuta OK.
9. Kui oled kõiki samme õigesti järginud, peaks programm nüüd käivituma.

## In English
'Aardejaht' is a mobile application, which combines entertainment and sports by making people move and hunt for treasures.

