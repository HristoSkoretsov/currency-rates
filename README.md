# currency-rates
This is a small currency-rates application which uses fixer.io API.

# Populate the database 
1. Populate currencies 
Send POST request 
http://localhost:8000/currencies
2. Populate curruncy pair rates
Send POST request
http://localhost:8000/rates/historic/EUR

# If you don't populate the database in advance try locally the aplication functionalities sending GET requests which returns the desired results and populate database with it:
1. http://localhost:8000/currencies - return list with all currencies 
2. http://localhost:8000/rates/historic/{base} - return all exchange rates from 1999 with {base} *This request 
takes longer because the information is quite large.
3. http://localhost:8000/rates/historic/{base}/{date} - return all exchange rates from {date} with {base}
4. http://localhost:8000/rates/latest/{base} - return all exchange rates for current day with {base}
5. http://localhost:8000/report/{currency}/{date} - return all rates for {currency} with series per day with base = EUR
