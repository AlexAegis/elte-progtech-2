## Programming Technologies 2 - Submission Number 4

### Notes:

http://junit.org/junit5/docs/current/user-guide/

MockitoExtension


### Filmek Nyilvántartása
Készítsünk programot, amellyel otthoni adathordozón lévő filmjeinket tudjuk nyilván tartani az
alábbi funkciókkal:
- A programban megtekinthetőek a filmek egy listában, amelyben tetszőlegesen
kereshetünk cím és/vagy évszám alapján (cím esetén töredékekre is).
- Lehetőségünk van új film felvételére a cím, rendező(k), főszereplő(k), megjelenési év,
valamint az eredetiség (eredeti/kalózmásolat) megadásával. A program figyelmeztet,
ha ugyanezekkel a paraméterekkel már megadtunk egy filmet.
- A filmeket kölcsönadhatjuk barátainknak a film kiválasztásával, valamint a név, a
dátum és a lejárat megadásával. Amennyiben kalózmásolatot adunk kölcsön, a
program figyelmeztessen, és kérjen megerősítést. A program jelenítse meg a lejárt
kölcsönzéseket, és ezt a listát folyamatosan tartsa karban egy háttérszál
segítségével.
- A kölcsönzéseket listázhatjuk (név, dátum), és a listát szűkíthetjük dátum, illetve
név(töredék) megadásával.
- A kölcsönadott filmeket természetesen vissza lehet hozni. A listában külön emeljük ki
(pl. más színnel) azokat a filmeket, amelyeket kölcsönadtunk. Külön oszlopban
jelenítsük meg, összesen hányszor adtuk kölcsön az adott filmet.
- A programban legyen egy PÁNIK gomb is arra az esetre, ha jön a szoftverrendőrség.
Ez a gomb azonnal kitöröl minden olyan filmmel kapcsolatos információt, amely
kalózmásolat.
- Az adatbázis az alábbi adatokat tárolja (ezek még nem feltétlenül a fizikai adattáblák):
    - filmek (cím, rendező(k), főszereplő(k), megjelenés éve, eredetiség)
    - kölcsönzések (film, név, dátum)
    
### Közös feladat
- Készítsünk egy egyszerű kliens Cache komponenst amely ideiglenes tárolóként
szolgál az adatbázis és az implementált alkalmazás között, a betöltött adatokat tárolja
és egy háttérszálon keresztül megadott időközönként letölti az adatbázisból az utolsó
menet óta létrehozott illetve módosított rekordokat.
- Ezen változásokat jelenítsük meg a megfelelő helyen. (feltételezhetjük, hogy egy adat
szerkesztése közben soha nem módosul az adatbázisban).
- A felhasználói felületen helyezzünk el egy gombot, amely a frissítést azonnal kiváltja. 


## Megvalósítás

### 1. rész

1.) **Bevezetés**  

*   Miről szól a projekt?
*   Mit szeretnél megvalósítani?
*   Ki a célközönsége a programodnak?
*   Miért lesz hasznos a célközönségednek a programodat használni?

2.) **Feladat leírása**  
(Mivel a konkrét feladat adott, ezért jelenjen meg a leírás a dokumentációban is az egyszerűség kedvéért.)

3.) **Követelmény elemzése**  
Lehet írni a fejezet bevezetéseként egy rövid összefoglalót.  

1.  **Funkcionális követelmények**
    *   <u>Használati eset</u> (Use case) diagramot kell készíteni, amely azt mutatja meg, hogy a felhasználó hogyan tud majd együtt működni a rendszerünkkel.  
        Az ábrát jól láthatóan kell elkészíteni, akár több részre bontva, hogy áttekinthetőek legyenek a funkciók.  
        Minta: [Megtekintés](http://valdar.web.elte.hu/progtech2/data/dok1/prt2_dia2.pdf) (19\. diától)
    *   <u>Felhasználói történetek</u> (User story)  
        A használati eset diagramot érdemes lehet kiegészíteni az egyes interakciók leírásával. Ezt több féle módon is megtehetjük. Az egyik ilyen mód, hogy minden egyes interakcióhoz szöveges leírást készítünk arról, hogy adott aktorként milyen elvárásunk van az adott funkcióval.  
        Minta: [Megtekintés](http://valdar.web.elte.hu/progtech2/data/dok1/user_story_1.pdf)  
        A másik módja, hogy táblázat formájában összefoglaljuk.  
        Minta: [Megtekintés](http://valdar.web.elte.hu/progtech2/data/dok1/user_story_2.pdf)
2.  **Nem funkcionális követelmények**
    1.  Termék követelmények  
        A termék viselkedését határozzák meg.
        *   Használhatósági követelmény
        *   Hatékonysági követelmény
        *   Megbízhatósági követelmény
        *   Hordozhatósági követelmény
    2.  Szervezeti követelmények  
        A megrendelő és a fejlesztő szervezetének szabályzataiból és ügyrendjéből erednek.
        *   Telepítési követelmény
        *   Implementációs követelmény
        *   Szabványkövetelmények
    3.  Külső követelmények  
        A rendszer és annak fejlesztési folyamatán kívül eső tényezők összessége
        *   Együttműködési követelmények
        *   Etikai követelmények
        *   Törvényi követelmények
            *   Adatvédelmi követelmények
            *   Biztonsági követelmények
3.  **Szakterületi követelmények**  
    Ezek a követelmények inkább a rendszer alkalmazásának a szakterületéből származnak, nem pedig a rendszer felhasználóinak egyéni igényeiből. Jelentőssége nagy, mivel ha nem teljesülnek ezek a követelmények, a rendszert nem lehet kielégítően működtetni.

**Beadandóval kapcsolatos megjegyzések**

A beadandó szempontjából a **Funkcionális követelmény**eken belül, a user story-k esetén érdemesebb a táblázatos megoldást választani.

A beadandó szempontjából a **Nem funkcionális követelmény**eket nem szükséges ennyire részletesen kezelni. Megfelel egy minimális leírás a szükséges hardverre vonatkozóan. Lehet írni az operációs rendszerrel kapcsolatban, hogy milyen rendszereken fut a programotok. Milyen fejlesztő eszközt használtok, egy rövid leírással. Továbbá implementációs követelmények szempontjából a későbbiekben hozzátehetitek a Clean Code alkalmazását.

A beadandó szempontjából a **Szakterületi követelmények** rész elhagyható. A feladatok nem tartalmaznak ilyen problémát.
