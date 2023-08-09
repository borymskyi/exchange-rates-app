[<p align="center"><img src="https://user-images.githubusercontent.com/73241214/259499746-6f93efc9-ebdd-4c95-a78d-04906a18498a.png" data-canonical-src="https://user-images.githubusercontent.com/73241214/259499746-6f93efc9-ebdd-4c95-a78d-04906a18498a.png" width="100%" height="auto" align="center"/></p>](https://google.com)

# Best currency exchange rate app [![GitHub Repo stars](https://img.shields.io/github/stars/borymskyi/exchange-rates-app?style=social&cacheSeconds=1800)](https://github.com/borymskyi/exchange-rates-app)

### Demo: https://google.com

## About

Application for providing up-to-date currency exchange rates. The app consumes data from the different 3D party systems, and store response from these systems in Redis. Also, the app allows get the best exchange rates for input currencies (for example USD/JPY) and get exchange rates history.

## Stack:
* Java 11
* Spring Boot 2.7, Spring Data JPA, Spring Retry, MapStruct, Lombok, JUnit 5, Mockito
* Redis
* Logstash, Elasticsearch, Kibana
* Maven, Docker, Nginx

## API

#### GET localhost:8080/api/v1/best-exchange-rate/latest?base=USD&target=UAH

Get exchange rates for currencies base/target.

##### Example Response:
```
{
    "meta_data": {
        "last_updated_at": "2023-08-09T18:40:05Z",
        "source_of_data": "https://www.alphavantage.co"
    },
    "exchange_rate": {
        "base_currency_code": "USD",
        "target_currency_code": "UAH",
        "rate": "36.56200000"
    }
}
```

## Details