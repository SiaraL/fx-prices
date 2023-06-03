### DESCRIPTION

This project has been created for recruitment purposes.

It allows running java spring boot application and angular app to watching updated fx prices.

### INSTRUCTION

0. You need to have:
    - node.js at version 18 (18.16.0);
    - maven (v. 3.8.2);
    - jdk 17
1. Clone this repository to your local machine.
2. Get to your path, eg. .../projects/fxprice.
3. Run a project in you IDE or use maven command:
      ```
      mvn spring-boot:run
      ```
   It will host java application at port 8080 of your localhost, so make sure, that is not currently used.
4. Get to frontend path, so:
   ```
   cd ./frontend
   ```
5. Install node modules:
   ```
   npm i
   ```
6. Run angular appliaction:
   ```
   ng serve
   ```
   It will host angular application at port 4200, so make sure, that is not currently used. Or just:
   ```
   ng serve --port=<your_port>
   ```
7. Open **localhost:4200** (or port you specified) at your browser.