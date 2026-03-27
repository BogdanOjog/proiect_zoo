# Proiect Zoo

O aplicație full-stack concepută pentru gestionarea eficientă a unei grădini zoologice. Proiectul integrează un backend robust scris în Java cu o interfață utilizator modernă, construită cu TypeScript și Vite.

## Tehnologii Utilizate

* **Backend:** Java, Maven
* **Frontend:** Node.js, TypeScript, Vite
* **Management Pachet:** npm (Node Package Manager)

## Structura Proiectului

* `src/` - Conține logica de business și codul sursă pentru backend-ul Java.
* `frontend/` - Găzduiește componentele interfeței cu utilizatorul și logica de client.
* `pom.xml` - Fișierul de configurare Maven pentru gestionarea dependințelor Java.
* `package.json` - Definește dependințele și scripturile pentru ecosistemul de frontend.
* `vite.config.ts` - Configurarea bundler-ului Vite pentru o compilare rapidă a frontend-ului.

## Instalare și Rulare

Pentru a rula acest proiect local, urmează pașii de mai jos din terminalul deschis în directorul rădăcină al proiectului:

1.  **Instalarea dependințelor de frontend:**
    ```bash
    npm install
    ```

2.  **Compilarea și rularea aplicației:**
    Folosind wrapper-ul Maven, rulează comanda pentru a porni serverul (ajustează comanda dacă folosești un alt profil sau framework specific, cum ar fi Quarkus sau Vaadin):
    ```bash
    mvn clean spring-boot:run
    ```

Aplicația va fi accesibilă în browser la adresa implicită (de obicei `http://localhost:8080`).
