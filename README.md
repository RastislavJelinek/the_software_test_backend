Program po skompilovaní a spustení vytvorí endpoint na 
localhost porte 8080 záložka api, teda:

http://localhost:8080/api

keďže mám implementovaný primitívny credential systém,
táto api sama o sebe s vami odmietne komunikovať.
Treba doplniť ```id=testCredential``` ako parameter

api má aj volitelný druhý parameter a to ```currencies=``` 
s vymenovanými trojpísmenovými značeniami mien odelenými čiarkami.

pre konkrétne príklady viz. [here](request.http)

zadanie mi trvalo cca 1:40, bonusové body ako credential, filtrovanie a unit testy cca ďalšie dve hodiny
