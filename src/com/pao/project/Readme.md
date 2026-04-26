# Aplicatie Bancara — PAO Proiect Etapa I

## 1.1 — 10 actiuni / interogari posibile in sistem

1. **Adauga un utilizator nou** — inregistreaza un client in baza de date a bancii
2. **Deschide un cont curent** — creeaza un cont curent cu limita de overdraft
3. **Deschide un cont de economii** — creeaza un cont de economii cu rata dobanzii si sold minim
4. **Emite un card debit** — emite un card debit asociat unui cont, cu limita zilnica
5. **Emite un card credit** — emite un card de credit cu limita de creditare
6. **Depune bani intr-un cont** — inregistreaza o depunere si actualizeaza soldul
7. **Retrage bani dintr-un cont** — retrage numerar, validand disponibilul (inclusiv overdraft / sold minim)
8. **Efectueaza un transfer intre conturi** — muta o suma de la un cont sursa la un cont destinatie
9. **Genereaza extras de cont** — produce un raport cu tranzactiile dintr-o perioada
10. **Listeaza toate conturile unui utilizator** — afiseaza conturile sortate dupa IBAN

---

## 1.2 — 8 tipuri de obiecte din domeniu

| Clasa         | Descriere                                                    |
|---------------|--------------------------------------------------------------|
| `Utilizator`  | Clientul bancii (id, nume, email, CNP, lista conturi)        |
| `Cont`        | Clasa abstracta de baza pentru conturi bancare               |
| `ContCurent`  | Cont curent cu limita de overdraft                           |
| `ContEconomii`| Cont de economii cu rata dobanzii si sold minim              |
| `Card`        | Clasa abstracta de baza pentru carduri                       |
| `CardDebit`   | Card debit cu limita zilnica                                 |
| `CardCredit`  | Card de credit cu limita de creditare                        |
| `Tranzactie`  | Inregistrare imutabila a unei operatiuni financiare          |
| `ExtrasCont`  | Extras de cont pentru o perioada data                        |

---

