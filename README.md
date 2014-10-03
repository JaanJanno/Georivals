Aardejaht
=========

### Eesti keeles
'Aardejaht' on mobiilirakendus, mis ühendab endas meelelahutuse ja sportlikkuse, pannes inimesed liikuma ja aardeid jahtima.

## Serveri käivitamise juhised
Server on konfigureeritud kasutama nii MySQL andmebaasi kui ka mälus olevat andmebaasi. Mõlema kasutamiseks tuleb natuke seadistada

### Vajalikud asjad
* Eclipse (soovitavalt Spring Tool Suite)
* Maven
* Eclipse m2e plugin (ilma selleta arvab Eclipse, et projekt on katki). Eclipse'ile lisamiseks "Install new software", lehekülg http://download.eclipse.org/technology/m2e/releases
* OPTIONAL MySQL 5 andmebaas


### Projekti seadistamine
1. Tõmba alla giti repo
2. Mine käsurealt webapp kausta
3. Käsk `mvn eclipse:eclipse` genereerib Eclipse jaoks vajalikud failid
4. Nüüd saab teha "import existing projects".
5. Server kasutab esimesena MySQL andmebaasi. Mälus oleva andmebaasi kasutamiseks peab minema faili src/main/resources/application.properties ja võtma `#spring.profiles.active=test` eest # ära. Alternatiivselt võib käivitada, andes Application'i main meetodile argumendiks `--spring.profiles.active=test`. Mälusse loetakse `import.sql`'is olevad andmed.
6. TÄHTIS! Serveri võib käima panna Eclipse seest Run nupule vajutades, aga see ei pruugi alati töötada. Eelistatult peaks programmi kaustas tegema `mvn spring-boot:run`.
7. Nüüd peaks server olema ligipääsetav aadressil `localhost:8080`. Andmete nägemiseks võid proovida `localhost:8080/user`, `/treasure` ja `/trek`.


### MySQL setup
SIIA VAJA KIRJUTADA

## In English
'Aardejaht' is a mobile application, which combines entertainment and sports by making people move and hunt for treasures.

