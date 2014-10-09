Aardejaht
=========

### Eesti keeles
'Aardejaht' on mobiilirakendus, mis ühendab endas meelelahutuse ja sportlikkuse, pannes inimesed liikuma ja aardeid jahtima.

## Serveri käivitamise juhised
Server on võimeline kasutama nii MySQL ja Postgres andmebaasi kui ka mälus olevat andmebaasi. 

### Vajalikud asjad
* Maven. Hea seadistamise juhis siin http://www.mkyong.com/maven/how-to-install-maven-in-windows/

### Soovitavad asjad
* Eclipse (soovitavalt Spring Tool Suite)
* Eclipse m2e plugin (ilma selleta arvab Eclipse, et projekt on katki). Eclipse'ile lisamiseks "Install new software", lehekülg http://download.eclipse.org/technology/m2e/releases
* MySQL või Postgres andmebaas

### Projekti seadistamine
1. Tõmba alla giti repo
2. Mine käsurealt webapp kausta
3.  Mälus oleva andmebaasi kasutamiseks peab minema faili src/main/resources/application.properties ja muutma rida `spring.profiles.active=default`. `default` asemele kirjuta `test`. Kohaliku andmebaasi kasutamiseks kirjuta `mysql`või `postgres`. 
4. webapp kaustas käsk `mvn spring-boot:run`
5. Nüüd peaks server olema ligipääsetav aadressil `localhost:8080`. Andmete nägemiseks võid proovida `localhost:8080/user`
6. Käsk `mvn eclipse:eclipse` genereerib Eclipse jaoks vajalikud failid
7. Nüüd saab teha `import existing projects`.

Serveri käivitamiseks on vaja ainult käsku `mvn spring-boot:run`


### Andmebaasi setup
`test` ehk mälus olev andmebaas kasutab andmeid, mis on kirjas `import.sql` failis. Postgres või MySQL pead ise tõmbama.  Nende paroolid ja muu info peab ühtima `application-mysql.properties` või `application-postgres.properties` kirjeldatuga. Andmebaasi tabelid tekitab server ise. 

Päris andmebaasi  näidisandmeid hetkel ei impordita.

Herokus töötab andmebaas `DATABASE_URL` kaudu, nii et soovi kõrval võib Heroku stiilis Postgres ühendamise stringi süsteemi keskonnamuutujatesse panna. Sellisel juhul ei pea `application.properties` failis midagi muutma. 

Selline string on näiteks `postgres://root:root@localhost:3306/test`.

### Androidi seadistus

# Androidi eeldused

Projekti kompileerimiseks on esmalt vaja alla laadida ADT:
	http://developer.android.com/sdk/index.html

Android SDK Manageri (ADT's "Window" -> "Android SDK Manager") alt installida API 19 ja Google Play services. On tarvis Google Play services ka projektina ADT-sse laadida, et seda kompileerides library'na kasutada võimalik oleks. Abiks järgmise lingi alt alampeatükk "Add Google Play Services to Your Project" ja "Setting up a Library Project".
	http://developer.android.com/google/play-services/setup.html
	http://developer.android.com/tools/projects/projects-eclipse.html#SettingUpLibraryProject

Androidi telefon, mille OS versioon on vähemalt 4.0

# Androidi projekti importimine ja jooksutamine.

Laadida alla giti repositoorium.

Importida ADT'ga "mobileapp" alamkaust.
Minna "Project"->"Properties"->"Android", et ADT genereeriks conf failid ning avada vajadusel ADT uuesti.

Lisada eeldustes mainitud viisil "Google play services" projekti library'ks. (http://developer.android.com/tools/projects/projects-eclipse.html#SettingUpLibraryProject)

Ühendada USB'ga telefon arvuti külge.

Vajutada run ning valida enda telefon. Kui kõik on tehtud õigesti, siis rakendus installeerub automaatselt telefonil ja alustab tööd. (Veenduda, et telefonil on USB debugging sisse lülitatud! http://www.phonearena.com/news/How-to-enable-USB-debugging-on-Android_id53909)


## In English
'Aardejaht' is a mobile application, which combines entertainment and sports by making people move and hunt for treasures.

