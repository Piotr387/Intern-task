## Opis wykonanego zadania:
Do przechowania danych wykorzystałem bazę danych H2 (in-memory). Dane zostały zaincjalizowane w klasie SetupDataLoader 
Został zaimplementowany Spring Security aby dostęp do odpowiednich endpointów miał tylko autoryzowany użytkownik.
Logowanie pozwala nam na uwierzytelnienie użytkwonika, natomiast Role pozwalają nam na dostęp na określone endpointy. 
Użytkownik który zapisze się na pierwsze wydarzenie dostanie maila z danymi logowania i wygenerowanym hasłem przez system, oraz samo potwierdzenie wziecia udziału w prelekcji.
Endpointy dla użytkownikow niezarejestrowanych i zajrestrowanych zostały rozdzielone tak aby inni użytkownicy nie mogli zapisac innego użytkownika na daną prelekcje.  
Po zalogowaniu się należy skopiować access_token otrzymany w odpowiedzi od serwera aby otrzymać dostęp do zabiezpieczonych endpointów. Po zalogowaniu został wygenerowany również refresh_token
tak aby po wygaśnięciu access_tokena można było wysłać requesta na odpowiedni endpoint w celu wygenerowania nowego access_tokena bez konieczności ponownego logowania się.
 
### **_Uruchomienie projektu_**:  
1. Sposób I:    
   a. Sklonowanie repozytorium przy pomocy linka `https://github.com/Piotr387/Intern-task.git`  
   b. Otwarcie projektu przy pomocy środowiska programistycznego  
   c. Zbudowanie i uruchomienie projektu (poprawnośc dzialania projektu była testowana na wersji 17.0.2)  
2. Sposób II:  
   a. Pobranie zbudowanego pliku przy pomocy Maven: `https://github.com/Piotr387/Intern-task/releases/tag/v1.0.0`  
   b. Uruchomienie konsoli w katologu w kótrym umieściliśmy plik .jar  
   c. Uruchomienie przy pomocy komendy `java -jar .\Intern-task-1.0.0-RELEASE.jar`  
3. Sposób III (wymagane środowisko Docker, rekomendowany sposób jeśli w poprzednich pojawią się błędy):  
   a. Link do zadania na dockerhub: `https://hub.docker.com/repository/docker/ajres/intern-task`  
   b. Lub po prostu wpisanie komendy w konsoli: `docker run --rm --name intern-task -p 8080:8080 ajres/intern-task:v1.0.0`  
   c. Dostęp do pliku powiadomienia.txt przy użyciu komendy `docker exec -it intern-task cat powiadomienia.txt` **UWAGA**! Kontener musi być uruchomiony, najlepiej użyć drugiej konsoli

### **_Adresy url_**
#### Plik Intern_Task.postman_collection.json zawiera wyeksportowaną kolekcja zapytać z POSTMAN

1. Lista wszystkich prelekcji, metoda GET : `http://localhost:8080/lectures`
2. Zapis na prelekcje (dla użytkownika niezarejestrowanego) POST: `http://localhost:8080/users/sign-up/` -> body
`login pattern: Username pattern: Length >=3, Valid characters: a-z, A-Z, 0-9, points, dashes and underscores.`
`email pattern RFC 5322 standard`
```json
{
    "lectureName": "Lecture 1 at 12:00",
    "userDTO" : {
        "login":"test2",
        "email":"test2@gmail.com"
    }
}
```
3. Logowanie sie do aplikacji, metoda POST: `http://localhost:8080/users/login`  
Poniższe parametry należy umieścić URL-encoded (key/value) login/password (x-www-form-urlencoded)    
Dla użytkownika z rolą **user**:  
**login**: test  
**password**: test  
Dla użytkownika z rolą **organizer**:  
**login**: organizator1  
**password**: organizator1  
W przypadku chęci zalogowania na innego użytkownika należy sięgnąc do pliku powiadomienia.txt
#### Do kolejnych kroków wymagane jest skopiowanie access_token otrzymanego w odpowiedzi serwera i wpisanie do nagłówka z kluczem: Authorization i wartością "Bearer access_token" 
5. URL do wygenerowania nowego access_token, w Authorization wpisujemy "Bearer refresh_token" otrzymany przy procesie logowania `http://localhost:8080/users/token/refresh`

#### Dostęp do poniższych endpointów będą mieli użytkownicy z rolą USER + token z otrzymany przy logowaniu
6. Wyświetlenie prelekcji na które użytkownik sie zapisał, metoda GET: `http://localhost:8080/users/lectures`
7. Wyświetlenie danych konta użytkownika, metoda GET:` http://localhost:8080/users/account`
8. Rejestracja na kolejne prelekcje zarejestrowanego użytkownika, metoda POST: `http://localhost:8080/users/sign-up-register`  
+ URL-encoded **key**: lectureName, **value**: nazwa_wydarzenia, na które użytkownik chce się zapisać
9. Anulowanie wybranej rezerwacji użytkownika, metoda DELETE:` http://localhost:8080/users/cancel`
+ URL-encoded **key**: lectureName, **value**: nazwa_wydarzenia
10. Zmiana adresu email, metoda PUT: `http://localhost:8080/users/update-email`
+ URL-encoded **key**: newEmail, **value**: nowy_email
#### Dostęp do poniższych endpointów będą mieli użytkownicy z rolą ORGANIZER + token z otrzymany przy logowaniu
11. Wyświetlenie listy zarejestrowanych użytkowników (zarówno z rolą USER jak i ORGANIZER), metoda GET: `http://localhost:8080/users`
12. zestawienie wykładów wg zainteresowania, metoda GET: `http://localhost:8080/users/statistics/lectures-popularity`
13. zestawienie ścieżek tematycznych wg zainteresowania, metoda GET: `http://localhost:8080/users/statistics/thematic-path-popularity`

### Status zajętych miejsc na prelekcjach po wypełnieniu danymi startowymi
         * Lecutre 1 at 10:00 - 5/5
         * Lecture 2 at 12:00 - 4/5
         * Lecture 2 at 14:00 - 3/5

W przypadku problemów bądź pytań zapraszam do kontaktu mailowego: ptrpiw.dev@gmail.com

